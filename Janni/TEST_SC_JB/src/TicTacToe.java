import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TicTacToe {

    String[][] matrize;

    public TicTacToe() {
        this.matrize = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrize[i][j] = "O";
            }
        }
    }

    public void putCross(int y, int x, String symb){

        this.matrize[x-1][y-1] = symb;

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                System.out.print(" " + matrize[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        TicTacToe spiel = new TicTacToe();

        while(true){

            String[] xysymb = br.readLine().split(" ");
            int y = Integer.parseInt(xysymb[0]);
            int x = Integer.parseInt(xysymb[1]);
            String symb = xysymb[2];
            spiel.putCross(y,x,symb);
        }

    }

}
