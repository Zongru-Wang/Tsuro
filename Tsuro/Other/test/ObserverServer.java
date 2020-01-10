import game.observer.image.VectorImage;
import game.player.ProxyPlayerImpl;
import game.referee.GameManager;
import game.remote.Logger;
import game.remote.Server;
import game.remote.TsuroSocket;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ObserverServer implements Runnable {
    private final ServerSocket serverSocket;
    private final Logger logger;
    private final LoggingObserver<VectorImage> loggingObserver;

    public ObserverServer(InetAddress host, int port, Logger logger, LoggingObserver<VectorImage> loggingObserver) throws IOException {
        this.serverSocket = new ServerSocket(port, Server.MAX_PLAYERS, host);
        this.logger = logger;
        this.loggingObserver = loggingObserver;
    }


    public void run() {
        try {
//            List<Player> players = new ArrayList<>();
            GameManager gm = new GameManager();
            for (int i = 0; i < Server.MIN_PLAYERS; i++) {
                Socket socket = serverSocket.accept();
//                players.add(new ProxyPlayerImpl(new TsuroSocket(socket), 5 - i, logger));
                gm.addPlayer(new ProxyPlayerImpl(new TsuroSocket(socket), 5 - i, logger));
            }
            int timeout = 30 * 1000;
            for (int i = Server.MIN_PLAYERS; i < Server.MAX_PLAYERS; i++) {
                try {
                    serverSocket.setSoTimeout(timeout);
                    long start = System.currentTimeMillis();
                    Socket socket = serverSocket.accept();
                    long end = System.currentTimeMillis();
                    timeout -= end - start;
//                    players.add(new ProxyPlayerImpl(new TsuroSocket(socket), 5 - i, logger));
                    gm.addPlayer(new ProxyPlayerImpl(new TsuroSocket(socket), 5 - i, logger));
                } catch (SocketTimeoutException ignored) {
                    break;
                }
            }
//            GameManager gm = new GameManager();
//            for (Player player : players) {
//                gm.addPlayer(player);
//            }
            gm.addObserver(this.loggingObserver);
            gm.startGame();
            logger.outputTo(new FileOutputStream("./xserver.log"));
            JFrame frame = new JFrame("TSURO");
            frame.add(new LoggingObserver.VectorImagePanel(loggingObserver.getLogs()));
            frame.pack();
            frame.setVisible(true);
            frame.setSize(640, 480);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
