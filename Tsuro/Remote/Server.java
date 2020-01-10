package game.remote;

import com.google.gson.JsonParseException;
import game.player.Player;
import game.player.ProxyPlayerImpl;
import game.referee.GameManager;
import game.referee.GameManagerImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final String ILLEGAL_PARAMETER = "Illegal Parameter must be: xserver <host:optional> <port>";
    private static final int MIN_PLAYERS = 3;
    private static final int MAX_PLAYERS = 5;

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
        serverSocket = new ServerSocket(port, MAX_PLAYERS, host);
        this.logger = logger;
    }

    public void run() throws IOException {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < MIN_PLAYERS; i++) {
            players.add(new ProxyPlayerImpl(serverSocket, 0, 5 - i, logger));
        }
        int timeout = 30000;
        for (int i = MIN_PLAYERS; i < MAX_PLAYERS; i++) {
            try {
                long start = System.currentTimeMillis();
                players.add(new ProxyPlayerImpl(serverSocket, timeout, 5 - i, logger));
                long end = System.currentTimeMillis();
                timeout -= end - start;
            } catch (SocketTimeoutException ignored) {
                break;
            }
        }
        GameManager gm = new GameManagerImpl();
        for (Player player : players) {
            gm.addPlayer(player);
        }
        gm.startGame();
        logger.outputTo(new FileOutputStream("./xserver.log"));
    }
}

