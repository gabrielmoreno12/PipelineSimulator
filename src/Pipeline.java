
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
    private int[] registers = new int[32];
    private ULA ula;
    private PC pc;
    private Pipeline next;
    private Pipeline prev;
    


}


