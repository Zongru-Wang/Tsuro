package game.remote;

import com.google.gson.JsonParseException;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.RefereeBoard;
import game.board.Token;
import game.player.Strategy;
import game.remote.messages.Message;
import game.rulechecker.RuleChecker;
import game.rulechecker.RuleCheckerImpl;
import game.tile.Tile;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * represent the client
 */
public class Client {
    private static final String ILLEGAL_PARAMETER =
            "Illegal Parameter must be: xclient <host> <port> <name> <strategy>";

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
            ClientSocket clientSocket = new ClientSocket(host, port);
            Client client = new Client(clientSocket, strategy, name);
            client.run();
        } catch (IllegalArgumentException | IOException | JsonParseException e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    private final ClientSocket socket;
    private final Strategy strategy;
    private final String name;

    public Client(ClientSocket socket, Strategy strategy, String name) {
        this.socket = socket;
        this.strategy = strategy;
        this.name = name;
    }

    /**
     * start communicating with server using given socket.
     *
     * @throws IOException
     */
    public void run() throws IOException {
        RuleChecker ruleChecker = new RuleCheckerImpl();
        socket.sendMessage(Message.join(name));
        Token token = socket.expectTokenMessage();
        socket.expectStartedMessage();
        InitialRequest initialRequest = socket.expectInitialRequest(token);
        InitialPlacement initialPlacement = strategy.generateInitialPlacement(
                initialRequest.getBoard(),
                initialRequest.getChoices(),
                token,
                ruleChecker);
        socket.sendMessage(Message.initialPlacement(initialPlacement));
        while (true) {
            IntermediateRequestOrEndOfGame intermediateRequestOrEndOfGame
                    = socket.expectIntermediateRequestOrEndOfGame();
            if (intermediateRequestOrEndOfGame.getIntermediateRequest().isPresent()) {
                IntermediateRequest intermediateRequest = intermediateRequestOrEndOfGame.getIntermediateRequest().get();
                RefereeBoard board = intermediateRequest.getBoard();
                List<Tile> choices = intermediateRequest.getChoices();
                IntermediatePlacement intermediatePlacement = strategy.generateIntermediatePlacement(board, choices, token, ruleChecker);
                socket.sendMessage(Message.intermediatePlacement(intermediatePlacement));
            } else {
                assert intermediateRequestOrEndOfGame.getEndOfGame().isPresent();
                EndOfGame end = intermediateRequestOrEndOfGame.getEndOfGame().get();
                return;
            }
        }
    }
}