import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class DerServer {

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("The capitalization server is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Capitalizer(listener.accept()));
            }
        }
    }

    private static class Capitalizer implements Runnable {
        private Socket socket;
        TicTacToe spiel = new TicTacToe();
        Capitalizer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {

                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);

                while (in.hasNextLine()) {
                    String[] xysymb = in.nextLine().split(" ");
                    int y = Integer.parseInt(xysymb[0]);
                    int x = Integer.parseInt(xysymb[1]);
                    String symb = xysymb[2];
                    spiel.putCross(y,x,symb);
                }

            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }
    }
}