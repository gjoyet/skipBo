package skipbo.server;

public interface NWPListener extends Runnable {

    void run();

    void analyze(String[] input);

    void stopRunning();
}
