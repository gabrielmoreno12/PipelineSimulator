/*
Classe responsável pela declaração dos métodos da
pipeline:
1 - Busca instrução
2 - Lê instrução
3 - Executa instrução (ULA)
4 - Acessa memória (Ignorado)
5 (4) - Registra resposta da ULA nos registradores
 */

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pipeline {
    private ULA ula;
    private InstructionsReader instructionsReader;
    private int PC;
    private int clockCicles;
    private int[] registers = new int[32];
    private final List<Instruction>
            stage1Instruction = new ArrayList<>(),
            stage2Instruction = new ArrayList<>(),
            stage3Instruction = new ArrayList<>(),
            stage4Instruction = new ArrayList<>();

    /*
    Como estamos  utilizando uma arquitetura pipeline de parelelismo, cada
    lista de estágios terá, por vez, no MÁXIMO 1 INSTRUÇÃO
     */

    public Pipeline() {
        this.ula = new ULA();
        this.instructionsReader = new InstructionsReader();
        this.PC = 0;
        this.clockCicles = 0;
    }

    public void runPipeline() {

        /*
        Funcionamento:
        1 - Adiciona a primeira instrução em stage1Instruction, executa a busca,
        remove da lista atual e adiciona na próxima (stage2Instruction);
        2 - Adiciona a segunda instrução em stage2Instruction, executa a decodificação,
        remove da lista atual e adiciona na próxima (stage3Instruction);
        3 - Adiciona a terceira instrução em stage3Instruction, executa a ULA,
        remove da lista atual e adiciona na próxima (stage4Instruction);
        4 - Executa a escrita nos regsitradores e remove da lista atual (stage4Instruction).
         */

        List<Instruction> instructions = instructionsReader.getInstructions();

        for (int i = 0; i < instructions.size(); i++) {
            Instruction currentInstruction = instructions.get(i);

            executeStage1(currentInstruction);
            clockCicles++;

            executeStage2();
            clockCicles++;
            if (i + 1 < instructions.size()) {
                executeStage1(instructions.get(i + 1));
            }

            executeStage3();
            clockCicles++;
            if (i + 2 < instructions.size()) {
                executeStage2();
                executeStage1(instructions.get(i + 2));
            }

            executeStage4();
            clockCicles++;
            if (i + 3 < instructions.size()) {
                executeStage3();
                executeStage2();
                executeStage1(instructions.get(i + 3));
            }
            PC++;
        }

        // Executar ciclos restantes sem instruções
        while (!stage1Instruction.isEmpty() || !stage2Instruction.isEmpty() || !stage3Instruction.isEmpty() || !stage4Instruction.isEmpty()) {
            if (!stage4Instruction.isEmpty()) {
                executeStage4();
            }
            if (!stage3Instruction.isEmpty()) {
                executeStage3();
            }
            if (!stage2Instruction.isEmpty()) {
                executeStage2();
            }
            if (!stage1Instruction.isEmpty()) {
                executeStage1(stage1Instruction.get(0)); // Assumindo que a primeira instrução deve ser processada
            }
        }


        System.out.println(instructionsReader.instructionsToString());
        System.out.println("Clock cicles: " + clockCicles);
        System.out.println("PC usages: " + PC);

    }

    // Executa o estágio 1 para a instrução presente na lista do estágio 1
    public void executeStage1(Instruction instruction) {
        execute_stage1_Fetch(instruction);
        moveStage1ToStage2();
    }

    // Executa o estágio 2 para a instrução presente na lista do estágio 2
    public void executeStage2() {
        Instruction instruction = stage2Instruction.getFirst();
        execute_stage2_Decode(instruction);
        moveStage2ToStage3();
    }

    // Executa o estágio 3 para a instrução presente na lista do estágio 3
    public void executeStage3() {
        Instruction instruction = stage3Instruction.getFirst();
        execute_stage3_ULA(instruction, instructionsReader, this);
        moveStage3ToStage4();
    }

    // Executa o estágio 4 para a instrução presente na lista do estágio 4
    public void executeStage4() {
        Instruction instruction = stage4Instruction.getFirst();
        execute_stage4_WriteBack(instruction, instructionsReader, this);
        stage4Instruction.remove(instruction);
        // Instrução passou por todas as etapas
    }

    // Realiza o estágio de busca de instrução
    public void execute_stage1_Fetch(Instruction instruction) {
        // Processo igual para todas instruções (com ou sem desvio - j ou beq)
        addToStage1(instruction);
    }

    private void execute_stage2_Decode(Instruction instruction) {
    }

    // Realiza o estágio de acesso à ULA
    public void execute_stage3_ULA(Instruction instruction,
                                   InstructionsReader instructionReader,
                                   Pipeline pipeline) {
        // Caso instruction seja add, addi, sub ou subi
        if (stage3_ULA(instruction, pipeline) != -1) {
            stage3_ULA(instruction, pipeline);
        } else { // Caso instruction seja j
            if (instruction.getOperation().equals("j")) {
                stage3_ULA_J(instruction, instructionsReader, pipeline);
            } else {
                // Se a comparação do beq for falsa, então a instrução não é valida
                if (!instruction.beqExecution(ula, this)) {
                    beqTillTrue(instruction, instructionsReader, this, ula);
                }

            }
        }

    }

    // Realiza o estágio de write back
    public void execute_stage4_WriteBack(Instruction instrucion,
                                         InstructionsReader instructionReader,
                                         Pipeline pipeline) {
        // Verifica se é uma operação J
        if (instrucion.getOperation().equals("j")) {
            stage4_WriteBack_J(instrucion, instructionReader, pipeline);
        } else {
            // Caso instruction seja add, addi, sub ou subi
            int ULAresult = stage3_ULA(instrucion, pipeline);
            if (ULAresult != -1) {
                stage4_WriteBack(instrucion, pipeline);
            }
        }

    }

    // Remove instrução do estágio 1 e adiciona ao estágio 2
    public void moveStage1ToStage2() {
        if (!stage1Instruction.isEmpty()) {
            for (Instruction instrucion : new ArrayList<>(stage1Instruction)) {
                stage1Instruction.remove(instrucion);
                addToStage2(instrucion);
            }
        }
    }

    // Remove instrução do estágio 2 e adiciona ao estágio 3
    public void moveStage2ToStage3() {
        if (!stage2Instruction.isEmpty()) {
            for (Instruction instrucion : new ArrayList<>(stage2Instruction)) {
                stage2Instruction.remove(instrucion);
                addToStage3(instrucion);
            }
        }
    }

    // Remove instrução do estágio 3 e adiciona ao estágio 4
    public void moveStage3ToStage4() {
        if (!stage3Instruction.isEmpty()) {
            for (Instruction instrucion : new ArrayList<>(stage3Instruction)) {
                stage3Instruction.remove(instrucion);
                addToStage4(instrucion);
            }
        }
    }

    // Adiciona uma instrução ao estágio 1
    private void addToStage1(Instruction instruction) {
        stage1Instruction.add(instruction);
    }

    // Adiciona uma instrução ao estágio 2
    private void addToStage2(Instruction instruction) {
        stage2Instruction.add(instruction);
    }

    // Adiciona uma instrução ao estágio 3
    private void addToStage3(Instruction instruction) {
        stage3Instruction.add(instruction);
    }

    // Adiciona uma instrução ao estágio 4
    private void addToStage4(Instruction instruction) {
        stage4Instruction.add(instruction);
    }


    // Retorna resultado da ULA
    public int stage3_ULA(Instruction instruction, Pipeline pipeline) {
        return (ula.operationDecider(instruction, pipeline));
    }

    // Escreve no registrador o resultado da ULA
    public void stage4_WriteBack(Instruction instruction, Pipeline pipeline) {
        registers[instruction.getOp1()] = stage3_ULA(instruction, pipeline);
    }

    // Realiza o estágio de ULA de instruções J
    public void stage3_ULA_J(Instruction instruction,
                             InstructionsReader instructionsReader,
                             Pipeline pipeline) {
        Instruction jumpReference = instructionsReader.getInstructions().get(instruction.getOp1());
        stage3_ULA(jumpReference, pipeline);
    }

    // Realiza o estágio de write back de instruções J
    public void stage4_WriteBack_J(Instruction instruction, InstructionsReader instructionsReader, Pipeline pipeline) {
        Instruction jumpReference = instructionsReader.getInstructions().get(instruction.getOp1());
        stage4_WriteBack(jumpReference, pipeline);
    }

    // Realiza o beq até que beqComparison = true <<CUIDADO COM LOOP INFINITO, REVISAR INSTRUÇÃO!>>
    public void beqTillTrue(Instruction instruction,
                            InstructionsReader instructionsReader,
                            Pipeline pipeline,
                            ULA ula) {
        // Instrução nesse caso é beq
        Instruction beqReference = instructionsReader.getInstructions().get(instruction.getOp1());

        System.out.println("teste: " + instructionsReader.getInstructions().get(instruction.getOp1()));

        int cont = 0;
        boolean beqComparison = instruction.beqExecution(ula, this);
        System.out.println(instruction);

        while (!beqComparison) {
            if (cont < 10) {
                execute_stage4_WriteBack(beqReference, instructionsReader, pipeline);
                execute_stage3_ULA(beqReference, instructionsReader, pipeline);

                instruction.setValida(instruction.beqExecution(ula, this));
                beqComparison = instruction.beqExecution(ula, this);

                System.out.println("Passou aqui: " + Arrays.toString(pipeline.getRegisters()));
                cont++;
                System.out.println(cont);
            }
            if (beqComparison) {
                break;
            }
        }

        if (beqComparison) {
            System.out.println("TEEEEEEEESTE");
        }
        System.out.println(instruction);
    }

    public int[] getRegisters() {
        return registers;
    }

}


