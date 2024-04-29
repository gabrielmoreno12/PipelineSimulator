import java.util.HashMap;
import java.util.Map;

public class ULA {
    private Map<String, Integer> operationsMap = new HashMap<>();

    // Alimenta o operationMap com as chaves
    // String das operações e atribui um ID para elas
    public ULA () {
        operationsMap.put("add", 0);
        operationsMap.put("addi", 1);
        operationsMap.put("sub", 2);
        operationsMap.put("subi", 3);
        operationsMap.put("j", 4);
        operationsMap.put("beq", 5);
    }

    // Decide qual operação realizar de acordo com a instrução
    // utilizando o ID da operação
    public int operationDecider(Instruction instruction, Pipeline pipeline) {

        int opCode = operationsMap.get(instruction.getOperation());
        int[] registers = pipeline.getRegisters();

        return switch (opCode) {
            // add
            case 0 -> add(registers[instruction.getOp2()],
                    registers[instruction.getOp3()]);
            // addi
            case 1 -> add(instruction.getOp3(),
                    registers[instruction.getOp2()]);
            // sub
            case 2 -> sub(registers[instruction.getOp3()],
                    registers[instruction.getOp2()]);
            // subi
            case 3 -> sub(instruction.getOp3(),
                    registers[instruction.getOp2()]);
            default -> -1;
        };
    }

    public boolean beqComparison(int a, int b) {
        return a == b;
    }

    public int add(int a, int b) {
        return (a + b);
    }


    public int sub(int a, int b) {
        return (a - b);
    }

    public void setOperationsMap(Map<String, Integer> operationsMap) {
        this.operationsMap = operationsMap;
    }

    public Map<String, Integer> getOperationsMap() {
        return operationsMap;
    }

}
