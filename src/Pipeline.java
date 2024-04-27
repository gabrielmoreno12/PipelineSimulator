
/*
Classe responsável pela declaração dos métodos da
pipeline:
1 - Busca instrução
2 - Lê instrução
3 - Executa instrução (ULA)
4 - Acessa memória
5 - Registra resposta da ULA nos registradores
 */

public class Pipeline {
    private int[] registers = new int[32];
    private ULA ula;
//    private PC pc;
    private Pipeline next;
    private Pipeline prev;
    private InstructionsReader instructionsReader;

    public Pipeline (ULA ula) {
        this.ula = ula;
    }

    public int stage3_ULA(Instruction instruction, Pipeline pipeline) {
        return ula.operationDecider(instruction, pipeline);
    }

    public void test(InstructionsReader instructionsReader, Pipeline pipeline) {
        for (Instruction instruction : instructionsReader.getInstructions()) {
            System.out.println(stage3_ULA(instruction, pipeline));
        }
    }

    public void stage4_WriteBack(Instruction instruction, Pipeline pipeline) {

        registers[instruction.getOp1()] += ula.operationDecider(instruction, pipeline);
    }

    public void test2(InstructionsReader instructionsReader, Pipeline pipeline) {
        for (Instruction instruction : instructionsReader.getInstructions()) {
            stage4_WriteBack(instruction, pipeline);
            System.out.println(registers[instruction.getOp1()]);
        }
    }


    public int[] getRegisters() {
        System.out.print("Registers: ");
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


