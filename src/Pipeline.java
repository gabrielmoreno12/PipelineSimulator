import java.util.ArrayList;
import java.util.List;

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

    private final FlReader flReader;
    private final int[] reg = new int[32];
    private int pc;
    private List<Instruction> instructions = new ArrayList<Instruction>();


    public Pipeline() {
        flReader = new FlReader();
        flReader.readFile();
        instructions.add(flReader.getInstruction());

    }

    public FlReader getFlReader() {
        return flReader;
    }

    public int[] getReg() {
        return reg;
    }
}
