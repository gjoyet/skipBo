import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class TCTServer {
    public static void main(String[]args) throws Exception{
        int port = 59090;
        ServerSocket listener = new ServerSocket(58901);
        try(listener){
            System.out.println("Game Server is running now! - ");
            var pool = Executors.newFixedThreadPool(200);
            while(true){
                Game game = new Game();
                pool.execute(game. new Player(listener.accept(), 'X'));
                pool.execute(game. new Player(listener.accept(), 'O'));
            }
        }
    }
}

class Game {
    private Player[] board = new Player[9];
    Player currentPlayer;

    public boolean hasWinner() {
        return (board[0] != null && board[0] == board[1] && board[0] == board[2])
                || (board[3] != null && board[3] == board[4] && board[3] == board[5])
                || (board[6] != null && board[6] == board[7] && board[6] == board[8])
                || (board[0] != null && board[0] == board[3] && board[0] == board[6])
                || (board[1] != null && board[1] == board[4] && board[1] == board[7])
                || (board[2] != null && board[2] == board[5] && board[2] == board[8])
                || (board[0] != null && board[0] == board[4] && board[0] == board[8])
                || (board[2] != null && board[2] == board[4] && board[2] == board[6]);
    }

    public boolean boardFilledUp() {
        return Arrays.stream(board).allMatch(p -> p != null);
    }

    public synchronized void move(int location, Player player) {
        if (player != currentPlayer) {
            throw new IllegalStateException("Not your turn");
        } else if (player.opponent == null) {
            throw new IllegalStateException("You don't have an opponent yet");
        } else if (board[location] != null) {
            throw new IllegalStateException("Cell already occupied");
        }
        board[location] = currentPlayer;
        currentPlayer = currentPlayer.opponent;
    }

    class Player implements Runnable {
        char mark;
        Player opponent;
        Player currentPlayer;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
        }

        public void run(){
            try {
                setup();
                processCommands();
            } catch (Exception e){
                e.printStackTrace();
            } finally{
                if(opponent != null && opponent.output != null)
                    opponent.output.println("Player Left");
                try{
                    socket.close();
                }catch(IOException ie){
                }
            }
        }

    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream());
        output.println("Welcome" + mark);
        if (mark == 'X') {
            currentPlayer = this;
            output.println("Waiting for second player: ");
        } else {
            opponent = currentPlayer;
            opponent.opponent = this;
            opponent.output.println("What is Your Move?");
        }
    }
        private void processCommands(){
           while(input.hasNextLine()){
               var cmd = input.nextLine();
               if(cmd.startsWith("QUIT")) {
                   return;
               }else if(cmd.startsWith("MOVE")){
                   //moveCommand(Integer.parseInt(cmd.substring(5)));
               }
           }
    }

        private void moveCommand(int loc){
            try{
                move(loc,this);
                output.println("VALID MOVE");
                opponent.output.println("OPPONENT MOVED: " + loc);
                if (hasWinner()) {
                    output.println("VICTORY");
                    opponent.output.println("DEFEAT");
                } else if (boardFilledUp()) {
                    output.println("TIE");
                    opponent.output.println("TIE");
                }
            } catch (IllegalStateException e) {
                output.println("MESSAGE " + e.getMessage());
            }
            }
        }
    }
