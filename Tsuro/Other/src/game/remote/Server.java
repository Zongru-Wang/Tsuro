package game.remote;

import com.google.gson.JsonParseException;
import game.player.ProxyPlayerImpl;
import game.referee.GameManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server implements Runnable {
    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 5;

    public static void main(String[] args) {
        try {
            InetAddress host = TCPUtil.getHost("localhost");
            int port = 8000;
            if (args.length == 1) {
                port = TCPUtil.getPort(args[0]);
            } else if (args.length == 2) {
                host = TCPUtil.getHost(args[0]);
                port = TCPUtil.getPort(args[1]);
            }

            Server server = new Server(host, port, new Logger());
            server.run();
        }  catch (IllegalArgumentException | IOException | JsonParseException e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    private final ServerSocket serverSocket;
    private final Logger logger;

    public Server(InetAddress host, int port, Logger logger) throws IOException {
        this.serverSocket = new ServerSocket(port, MAX_PLAYERS, host);
        this.logger = logger;
    }

    public void run() {
        try {
//            List<Player> players = new ArrayList<>();
            GameManager gm = new GameManager();
            for (int i = 0; i < MIN_PLAYERS; i++) {
                Socket socket = serverSocket.accept();
//                players.add(new ProxyPlayerImpl(new TsuroSocket(socket), 5 - i, logger));
                gm.addPlayer(new ProxyPlayerImpl(new TsuroSocket(socket), 5 - i, logger));
            }
            int timeout = 30 * 1000;
            for (int i = MIN_PLAYERS; i < MAX_PLAYERS; i++) {
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
            gm.startGame();
            logger.outputTo(new FileOutputStream("./xserver.log"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

