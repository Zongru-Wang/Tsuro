package game.remote.messages;

import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.remote.EndOfGame;
import game.remote.InitialRequest;
import game.remote.IntermediateRequest;

import java.io.OutputStream;

/**
 * represent the messages between client and server
 */
public interface Message {

    /**
     * @param outputStream the destination of where the message send to
     */
    void sendTo(OutputStream outputStream);

    /**
     * instantiate method to get the JoinMessageImpl
     * (client-i=>server)
     * @param name the name of player
     * @return new JoinMessageImpl
     */
    static Message join(String name) {
        return new JoinMessageImpl(name);
    }

    /**
     * instantiate method to get the InitialPlacementMessageImpl
     * @param initialPlacement (Token, Port, x, y) values of initial token position
     * @return new InitialPlacementMessageImpl
     */
    static Message initialPlacement(InitialPlacement initialPlacement) {
        return new InitialPlacementMessageImpl(initialPlacement);
    }

    /**
     * instantiate method to get the IntermediatePlacementMessageImpl
     * @param intermediatePlacement (Token, x, y) values of intermediate tile placement
     * @return new IntermediatePlacementMessageImpl
     */
    static Message intermediatePlacement(IntermediatePlacement intermediatePlacement) {
        return new IntermediatePlacementMessageImpl(intermediatePlacement);
    }

    static Message initialRequest(InitialRequest initialRequest) {
        return new InitialRequestMessageImpl(initialRequest);
    }

    static Message intermediateRequest(IntermediateRequest intermediateRequest) {
        return new IntermediateRequestMessageImpl(intermediateRequest);
    }

    static Message started() {
        return new StartedMessageImpl();
    }

    static Message token(Token token) {
        return new TokenJsonMessageImpl(token);
    }

    static Message ended(EndOfGame endOfGame) {
        return new EndedMessageImpl(endOfGame);
    }
}
