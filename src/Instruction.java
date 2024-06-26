public class Instruction {
    /*
    Uma instrução (linha) conterá os seguintes atributos:
    Ex:
    add 1 2 3
    operation = add
    op1 = 1, op2 = 2, op = 3
    Execução:
    soma do valor do registrador 2 + valor do registrador 3
    é armazenada no registrador 1 ($1 = $2 + $3)
     */
    private String operation;
    private int op1, op2, op3;
    private boolean valida=true; // Será utilizada nas etapas de pipeline
                            // em caso de instrução antecedente ser
                            // BEQ ou J (estudar comportamento)

    public Instruction(String operation,
                       int op1,
                       int op2,
                       int op3,
                       boolean valida) {
        this.operation = operation;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.valida = valida;
    }

    // Caso da instrução j
    public Instruction(String operation,
                       int op1) {
        this.operation = operation;
        this.op1 = op1;
    }

    public Instruction() { }

    // Método a ser definido ainda
    public boolean beqExecution(ULA ula, Pipeline pipeline) {
        return (ula.beqComparison(pipeline.getRegisters()[getOp2()],
                                pipeline.getRegisters()[getOp3()]));
    }

    // Getters e Setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getOp1() {
        return op1;
    }

    public void setOp1(int op1) {
        this.op1 = op1;
    }

    public int getOp2() {
        return op2;
    }

    public void setOp2(int op2) {
        this.op2 = op2;
    }

    public int getOp3() {
        return op3;
    }

    public void setOp3(int op3) {
        this.op3 = op3;
    }

    public void setValida(boolean valida) {
        this.valida = valida;
    }

    public boolean getValida() {
        return valida;
    }

    @Override
    public String toString() {
        return "[Operation: "+ operation
                + ", op1: " + op1
                + ", op2: " + op2
                + ", op3: " + op3
                + ", Válida: " + valida+"]";
    }
}
