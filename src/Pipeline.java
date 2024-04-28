
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

    public void stage4_WriteBack(Instruction instruction, Pipeline pipeline) {
        registers[instruction.getOp1()] += stage3_ULA(instruction, pipeline);
    }

    public void test(InstructionsReader instructionsReader, Pipeline pipeline) {
        for (Instruction instruction : instructionsReader.getInstructions()) {
            // Caso instruction seja add, addi, sub ou subi
            if (stage3_ULA(instruction, pipeline) != 0) {
                stage3_ULA(instruction, pipeline);
            } else { // Caso instruction seja j
                stage3_ULA(new Instruction(
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOperation(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOp1(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOp2(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOp3(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getValida()
                ), pipeline);
            }
        }
    }


    public void test2(InstructionsReader instructionsReader, Pipeline pipeline) {
        for (Instruction instruction : instructionsReader.getInstructions()) {
            // Caso instruction seja add, addi, sub ou subi
            if (stage3_ULA(instruction, pipeline) != 0) {
                stage4_WriteBack(instruction, pipeline);
            } else { // Caso instruction seja j
                stage4_WriteBack(new Instruction(
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOperation(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOp1(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOp2(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getOp3(),
                        instructionsReader.getInstructions().get(instruction.getOp1()).getValida()
                ), pipeline);
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


