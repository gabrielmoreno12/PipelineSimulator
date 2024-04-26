import java.util.ArrayList;
import java.util.List;

public class Pipeline {

    private final FlReader flReader;
    private final int[] reg = new int[32];
    private int pc;
    private List<Instruction> instructions = new ArrayList<Instruction>();


    public Pipeline() {
        flReader = new FlReader();
        flReader.readFile();
    }

    public FlReader getFlReader() {
        return flReader;
    }

    public int[] getReg() {
        return reg;
    }
}
