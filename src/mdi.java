
import interpreter.command.Command;
import lexical.Lexema;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import syntatic.SyntaticAnalysis;

public class mdi {

    public static void main(String[] args) {
        /*if (args.length != 1) {
            System.out.println("Usage: java mdi [miniDart file]");
            return;
        }*/

        LexicalAnalysis l = new LexicalAnalysis("C:/Users/WazPC/OneDrive/Documentos/Dartf3/MiniDart/src/dices.txt");
            // O código a seguir é dado para testar o interpretador.
            // TODO: descomentar depois que o analisador léxico estiver OK.
            SyntaticAnalysis s = new SyntaticAnalysis(l);
            Command c = s.start();
            c.execute();

            // // O código a seguir é usado apenas para testar o analisador léxico.
            // // TODO: depois de pronto, comentar o código abaixo.


    }

}