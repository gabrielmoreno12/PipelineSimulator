import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/*
Classe responsável por:
1 - Leitura do arquivo instructions.txt
2 - Separa instruções individualmente
3 - Alimenta lista com todas as instruções
 */

public class InstructionsReader {
    private List<Instruction> instructions = new ArrayList<Instruction>();

    public InstructionsReader() {
    }

    public void readInstruction() {
        try {
            // Localizando o arquivo no diretório de origem do projeto
            File file = new File("src\\instructions.txt");

            // Criando um BufferedReader para ler o arquivo
            BufferedReader reader = new BufferedReader(new FileReader(file));
            splitInstructions(reader);
            reader.close();

        } catch (IOException e) {
            // Lidando com possíveis exceções de IO
            e.printStackTrace();
        }
    }

    public void splitInstructions(BufferedReader reader) throws IOException {
        String line;

        // Lendo e imprimindo cada linha do arquivo
        while ((line = reader.readLine()) != null) {
            System.out.println(line);

            // Dividindo a string em substrings com base no espaço em branco
            String[] arrayElem = line.split("\\s+");

            // Verifica se a linha contém a quantidade correta de elementos
            if (arrayElem.length < 2) {
                continue; // Pula para a próxima iteração se a linha for inválida
            }

            // Cria uma nova instrução
            Instruction instruction = new Instruction();

            // Define a operação da instrução
            instruction.setOperation(arrayElem[0]);

            // Define os operandos da instrução
            for (int i = 1; i < arrayElem.length; i++) {
                switch (i) {
                    case 1:
                        instruction.setOp1(Integer.parseInt(arrayElem[i]));
                        break;
                    case 2:
                        instruction.setOp2(Integer.parseInt(arrayElem[i]));
                        break;
                    case 3:
                        instruction.setOp3(Integer.parseInt(arrayElem[i]));
                        break;
                    default:
                        break;
                }
            }


            // Alimentando a lista com instruções
            instructions.add(instruction);
        }

    }


    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public String instructionsToString() {
        StringBuilder sb = new StringBuilder();
        for (Instruction instruction : instructions) {
            sb.append(instruction.toString()).append("\n");
        }
        return sb.toString();
    }

}