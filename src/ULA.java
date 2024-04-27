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
        operationsMap.put("beq", 4);
        operationsMap.put("j", 5);
    }

    // Decide qual operação realizar de acordo com a instrução
    // utilizando o ID da operação
    public int operationDecider(Instruction instruction) {
        int opCode = operationsMap.get(instruction.getOperation());

        return switch (opCode) {
            case 0, 1 -> add(instruction.getOp2(), instruction.getOp3());
            case 2, 3 -> sub(instruction.getOp2(), instruction.getOp3());
            default -> 0;
        };
    }

    public int add (int a, int b) {
        return (a + b);
    }

    public int sub (int a, int b) {
        return (a - b);
    }

    public void setOperationsMap(Map<String, Integer> operationsMap) {
        this.operationsMap = operationsMap;
    }

    public Map<String, Integer> getOperationsMap() {
        return operationsMap;
    }

}
