import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // Debugs
        InstructionsReader ir = new InstructionsReader();
        ir.readInstruction();
        System.out.println(ir.instructionsToString());

        Pipeline p = new Pipeline(new ULA());
        System.out.println(p.getUla().getOperationsMap());

        p.test(ir);
        System.out.println();
        p.test2(ir);
        System.out.println();
        System.out.println(Arrays.toString(p.getRegisters()));

    }
}