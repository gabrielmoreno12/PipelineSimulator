import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        // Debugs
        InstructionsReader ir = new InstructionsReader();
        ULA ula = new ULA();
        Pipeline p = new Pipeline(ir, ula);

        ir.readInstruction();
        System.out.println(ir.instructionsToString());

        System.out.println();

        p.stage4_forAll(ir, p);

        p.stage3_forAll(ir, p);

        p.stage4_forAll(ir, p);


        System.out.println(p.getInstructionsReader().getInstructions().get(4));

        System.out.print("Registers: ");
        System.out.println(Arrays.toString(p.getRegisters()));

    }
}