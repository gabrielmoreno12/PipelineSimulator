import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
/*
Classe responsável pela leitura do arquivo das
instruções e pelo direcionamento dos dados para
uma instância de instruction
 */

public class FlReader {

    private Instruction instruction;
    private List<Instruction> instructions;

    public FlReader() {
        this.instruction = new Instruction();
    }

    public void readFile() {
        try {
            // Localizando o arquivo no diretório de origem do projeto
            File file = new File("src\\instructions.txt");

            // Criando um BufferedReader para ler o arquivo
            BufferedReader reader = new BufferedReader(new FileReader(file));

            // Variável para armazenar cada linha do arquivo
            String line;

            // Lendo e imprimindo cada linha do arquivo
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

                // Dividindo a string em substrings com base no espaço em branco
                String[] arrayElem = line.split("\\s+");

                // A primeira parte contém a operação
                String operation = arrayElem[0];
                instruction.setOperation(operation);

                // As partes restantes contêm os operandos
                int[] ops = new int[arrayElem.length - 1];
                for (int i = 1; i < arrayElem.length; i++) {
                    ops[i - 1] = Integer.parseInt(arrayElem[i]);

                    // Atualização dos atributos da intrução
                    switch (i) {
                        case 1:
                            instruction.setOp1(ops[i - 1]);
                            break;
                        case 2:
                            instruction.setOp2(ops[i - 1]);
                            break;
                        case 3:
                            instruction.setOp3(ops[i - 1]);
                            break;
                    }
                }

                // Imprimindo os resultados para debug (temporário)
                System.out.println("Operação: " + operation);
                System.out.print("Operandos: ");
                for (int operando : ops) {
                    System.out.print(operando + " ");
                }
                System.out.println();
                System.out.println(instruction.getOperation());
                System.out.println(instruction.getOp1());
                System.out.println(instruction.getOp2());
                System.out.println(instruction.getOp3());

            }

            // Fechando o BufferedReader após a leitura do arquivo
            reader.close();
        } catch (IOException e) {
            // Lidando com possíveis exceções de IO
            e.printStackTrace();
        }
    }

    // Getters e Setters
    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public Instruction getInstruction() {
        return instruction;
    }
}
