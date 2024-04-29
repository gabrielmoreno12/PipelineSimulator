import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // Debugs
        InstructionsReader ir = new InstructionsReader();
        ir.readInstruction();
        System.out.println(ir.instructionsToString());

        ULA ula = new ULA();
        Pipeline p = new Pipeline(ir, ula);

        System.out.println();

        p.stage4_forAll(ir, p);
        System.out.println(ir.getInstructions().getLast());
        p.stage3_forAll(ir, p);
        System.out.println(ir.getInstructions().getLast());
        p.stage4_forAll(ir, p);






        System.out.print("Registers: ");
        System.out.println(Arrays.toString(p.getRegisters()));

    }
}