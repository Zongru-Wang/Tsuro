package game.remote;

import com.google.gson.JsonParseException;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.player.Strategy;
import game.rulechecker.RuleChecker;
import game.tile.Tile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * represent the client
 */
public class Client implements Runnable {
    private static final String ILLEGAL_PARAMETER =
            "Illegal Parameter must be: xclient <host> <port> <name> <strategy>";
    private static final String UNEXPECTED_MESSAGE = "Server sent unexpected message";
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println(ILLEGAL_PARAMETER);
            return;
        }
        try {
            InetAddress host = TCPUtil.getHost(args[0]);
            int port = TCPUtil.getPort(args[1]);
            String name = args[2];
            Strategy strategy = Strategy.fromString(args[3]);
            TsuroSocket tsuroSocket = new TsuroSocket(new Socket(host, port));
            Client client = new Client(tsuroSocket, strategy, name);
            client.run();
        } catch (IllegalArgumentException | IOException | JsonParseException e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    private final TsuroSocket socket;
    private final Strategy strategy;
    private final String name;

    public Client(TsuroSocket socket, Strategy strategy, String name) {
        this.socket = socket;
        this.strategy = strategy;
        this.name = name;
    }

    /**
     * start communicating with server using given socket.
     *
     * @throws IOException
     */
    public void run() {
        try {
            RuleChecker ruleChecker = new RuleChecker();
            socket.send(name);
            Token token = socket.expect(Token.class);
            String started = socket.expect();
            if (!started.contains("started")) {
                throw new IOException(UNEXPECTED_MESSAGE);
            }
            InitialRequest initialRequest = socket.expect(InitialRequest.class);
            InitialPlacement initialPlacement = strategy.generateInitialPlacement(
                    initialRequest.getBoard(),
                    initialRequest.getChoices(),
                    token,
                    ruleChecker);
            socket.send(InitialPlacement.class, initialPlacement);
            while (true) {
                IntermediateRequestOrEndOfGame intermediateRequestOrEndOfGame
                        = socket.expect(IntermediateRequestOrEndOfGame.class);
                if (intermediateRequestOrEndOfGame.getIntermediateRequest().isPresent()) {
                    IntermediateRequest intermediateRequest = intermediateRequestOrEndOfGame.getIntermediateRequest().get();
                    Board board = intermediateRequest.getBoard();
                    List<Tile> choices = intermediateRequest.getChoices();
                    IntermediatePlacement intermediatePlacement = strategy.generateIntermediatePlacement(board, choices, token, ruleChecker);
                    socket.send(IntermediatePlacement.class, intermediatePlacement);
                } else {
//                    assert intermediateRequestOrEndOfGame.getEndOfGame().isPresent();
//                    EndOfGame end = intermediateRequestOrEndOfGame.getEndOfGame().get();
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}