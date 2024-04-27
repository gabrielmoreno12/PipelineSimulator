public class Main {
    public static void main(String[] args) {

        InstructionsReader ir = new InstructionsReader();
        ir.readInstruction();
        System.out.println(ir.instructionsToString());



    }
}