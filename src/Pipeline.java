
/*
Classe responsável pela declaração dos métodos da
pipeline:
1 - Busca instrução
2 - Lê instrução
3 - Executa instrução (ULA)
4 - Acessa memória
5 - Registra resposta da ULA nos registradores
 */

import java.util.Arrays;
import java.util.List;

public class Pipeline {
    private int[] registers = new int[32];
    private ULA ula;
    private Pipeline next;
    private Pipeline prev;
    private InstructionsReader instructionsReader;
//    private PC pc;

    public Pipeline (InstructionsReader instructionsReader, ULA ula) {
        this.ula = ula;
        this.instructionsReader = instructionsReader;
    }

    // Retorna resultado da ULA
    public int stage3_ULA(Instruction instruction, Pipeline pipeline) {
        return (ula.operationDecider(instruction, pipeline));
    }

    // Escreve no registrador o resultado da ULA
    public void stage4_WriteBack(Instruction instruction, Pipeline pipeline) {
        registers[instruction.getOp1()] = stage3_ULA(instruction, pipeline);
    }




    // Realiza o estágio de write back de instruções J
    public void stage4_WriteBack_J(Instruction instruction, InstructionsReader instructionsReader, Pipeline pipeline) {
        Instruction jumpReference = instructionsReader.getInstructions().get(instruction.getOp1());
        stage4_WriteBack(jumpReference, pipeline);
    }

    // Realiza o estágio de ULA de instruções J
    public void stage3_ULA_J(Instruction instruction,
                             InstructionsReader instructionsReader,
                             Pipeline pipeline) {
        Instruction jumpReference = instructionsReader.getInstructions().get(instruction.getOp1());
        stage3_ULA(jumpReference, pipeline);
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



    // Realiza o estágio de acesso à ULA para todas as instruções
    public void stage3_forAll(InstructionsReader instructionsReader,
                              Pipeline pipeline) {
        List<Instruction> instructions = instructionsReader.getInstructions();
        for (Instruction instruction : instructions) {
            /*
            Não há verificação de validade da instrução porque
            todas instruções a princípio são válidas, somente
            no estágio da ULA é que, no caso do nosso projeto,
            uma instrução recebe validade falsa se for um beq
            com comparação falsa
             */
            execute_stage3_ULA(instruction, instructionsReader, pipeline);

        }
    }

    // Realiza o estágio de acesso à ULA
    public void execute_stage3_ULA(Instruction instrucion,
                                   InstructionsReader instructionReader,
                                   Pipeline pipeline) {
        // Caso instruction seja add, addi, sub ou subi
        if (stage3_ULA(instrucion, pipeline) != -1) {
            stage3_ULA(instrucion, pipeline);
        } else { // Caso instruction seja j
            if (instrucion.getOperation().equals("j")) {
                stage3_ULA_J(instrucion, instructionsReader, pipeline);
            } else {
                // Se a comparação do beq for falsa, então a instrução não é valida
                if (!(instrucion.beqExecution(ula, pipeline))) {
                    instrucion.setValida(false);
                }
            }
        }

    }

    // Executa o writeback para todas instruções
    public void stage4_forAll(InstructionsReader instructionsReader,
                              Pipeline pipeline) {
        List<Instruction> instructions = instructionsReader.getInstructions();
        for (Instruction instruction : instructions) {
            // Caso valida, prossegue normalmente
            if (instruction.getValida()) {
                execute_stage4_WriteBack(instruction, instructionsReader, pipeline);
            } else { // Caso não seja válida indica que é beq com comparação falsa
                beqTillTrue(instruction, instructionsReader, pipeline, ula);
            }
        }
    }

    // Realiza o beq até que beqComparison = true <<CUIDADO COM LOOP INFINITO, REVISAR INSTRUÇÃO!>>
    public void beqTillTrue(Instruction instruction,
                            InstructionsReader instructionsReader,
                            Pipeline pipeline,
                            ULA ula) {
        int cont=0;
        // Instrução nesse caso é beq
        Instruction beqReference = instructionsReader.getInstructions().get(instruction.getOp1());

        System.out.println("teste: "+ instructionsReader.getInstructions().get(instruction.getOp1()));

        while (!instruction.beqExecution(ula, pipeline)) {

            if (cont<5) {
                execute_stage4_WriteBack(beqReference, instructionsReader, pipeline);
                execute_stage3_ULA(beqReference, instructionsReader, pipeline);

                System.out.println("PAssou aqui: " + Arrays.toString(pipeline.getRegisters()));
                cont++;
                System.out.println(cont);
            } else {
                break;
            }

        }
    }



    public int[] getRegisters() {
        return registers;
    }


    public void setRegisters(int[] registers) {
        this.registers = registers;
    }

    public ULA getUla() {
        return ula;
    }

    public void setUla(ULA ula) {
        this.ula = ula;
    }

    public InstructionsReader getInstructionsReader() {
        return instructionsReader;
    }

    public void setInstructionsReader(InstructionsReader instructionsReader) {
        this.instructionsReader = instructionsReader;
    }
}


