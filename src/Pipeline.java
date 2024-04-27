
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

    public int stage3_ULA(Instruction instruction) {
        return ula.operationDecider(instruction);
    }

    public void test(InstructionsReader instructionsReader) {
        for (Instruction instruction : instructionsReader.getInstructions()) {
            System.out.println(stage3_ULA(instruction));
        }
    }

    public void stage4_WriteBack(Instruction instruction) {
        registers[instruction.getOp1()] = ula.operationDecider(instruction);
    }

    public void test2(InstructionsReader instructionsReader) {
        for (Instruction instruction : instructionsReader.getInstructions()) {
            stage4_WriteBack(instruction);
            System.out.println(registers[instruction.getOp1()]);
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


