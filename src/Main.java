import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        Pipeline pipeline = new Pipeline();
        pipeline.runPipeline();

        System.out.println("Registers: " + Arrays.toString(pipeline.getRegisters()));

    }
}