package property;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.remote.*;
import game.tile.Port;
import game.tile.Tile;
import org.junit.runner.RunWith;
import property.generator.*;
import property.mock.MockTestSocket;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(JUnitQuickcheck.class)
public class TestTsuroSocketProperty {
    @Property
    public void portClientSocketCommunication(@From(PortGenerator.class) Port port) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Port.class, port);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Port.class), port);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void tileClientSocketCommunication(@From(TileGenerator.class) Tile tile) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Tile.class, tile);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Tile.class), tile);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void boardClientSocketCommunication(@From(BoardGenerator.class) Board board) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Board.class, board);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Board.class), board);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void tileOptClientSocketCommunication() {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Tile.class, null);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertNull(receivingSocket.expect(Tile.class));
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void endOfGameClientSocketCommunication(@From(EndOfGameGenerator.class) EndOfGame endOfGame) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(EndOfGame.class, endOfGame);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(EndOfGame.class), endOfGame);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void initialRequestClientSocketCommunication(@From(InitialRequestGenerator.class) InitialRequest initialRequest) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(InitialRequest.class, initialRequest);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(InitialRequest.class), initialRequest);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void intermediateRequestClientSocketCommunication(@From(IntermediateRequestGenerator.class) IntermediateRequest intermediateRequest) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(IntermediateRequest.class, intermediateRequest);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(IntermediateRequest.class), intermediateRequest);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void tokenClientSocketCommunication(Token token) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Token.class, token);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Token.class), token);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void initialPlacementClientSocketCommunication(@From(InitialPlacementGenerator.class) InitialPlacement initialPlacement) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(InitialPlacement.class, initialPlacement);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(InitialPlacement.class), initialPlacement);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void intermediatePlacementClientSocketCommunication(@From(IntermediatePlacementGenerator.class) IntermediatePlacement intermediatePlacement) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(IntermediatePlacement.class, intermediatePlacement);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(IntermediatePlacement.class), intermediatePlacement);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void intermediatePlacementOrEndOfGameClientSocketCommunication(@From(IntermediateRequestOrEndOfGameGenerator.class) IntermediateRequestOrEndOfGame iore) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(IntermediateRequestOrEndOfGame.class, iore);
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(IntermediateRequestOrEndOfGame.class), iore);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void stringGameSocketCommunication() {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send("teststr");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertTrue(receivingSocket.expect().contains("teststr"));
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void portClientSocketCommunicationWithLogger(@From(PortGenerator.class) Port port) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Port.class, port, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Port.class, new Logger(), "test"), port);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void tileClientSocketCommunicationWithLogger(@From(TileGenerator.class) Tile tile) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Tile.class, tile, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Tile.class, new Logger(), "test"), tile);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void boardClientSocketCommunicationWithLogger(@From(BoardGenerator.class) Board board) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Board.class, board, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Board.class, new Logger(), "test"), board);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void tileOptClientSocketCommunicationWithLogger() {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Tile.class, null, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertNull(receivingSocket.expect(Tile.class, new Logger(), "test"));
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void endOfGameClientSocketCommunicationWithLogger(@From(EndOfGameGenerator.class) EndOfGame endOfGame) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(EndOfGame.class, endOfGame, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(EndOfGame.class, new Logger(), "test"), endOfGame);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void initialRequestClientSocketCommunicationWithLogger(@From(InitialRequestGenerator.class) InitialRequest initialRequest) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(InitialRequest.class, initialRequest, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(InitialRequest.class, new Logger(), "test"), initialRequest);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void intermediateRequestClientSocketCommunicationWithLogger(@From(IntermediateRequestGenerator.class) IntermediateRequest intermediateRequest) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(IntermediateRequest.class, intermediateRequest, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(IntermediateRequest.class, new Logger(), "test"), intermediateRequest);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void tokenClientSocketCommunicationWithLogger(Token token) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(Token.class, token, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(Token.class, new Logger(), "test"), token);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void initialPlacementClientSocketCommunicationWithLogger(@From(InitialPlacementGenerator.class) InitialPlacement initialPlacement) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(InitialPlacement.class, initialPlacement, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(InitialPlacement.class, new Logger(), "test"), initialPlacement);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void intermediatePlacementClientSocketCommunicationWithLogger(@From(IntermediatePlacementGenerator.class) IntermediatePlacement intermediatePlacement) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(IntermediatePlacement.class, intermediatePlacement, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(IntermediatePlacement.class, new Logger(), "test"), intermediatePlacement);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void intermediatePlacementOrEndOfGameClientSocketCommunicationWithLogger(@From(IntermediateRequestOrEndOfGameGenerator.class) IntermediateRequestOrEndOfGame iore) {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send(IntermediateRequestOrEndOfGame.class, iore, new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertEquals(receivingSocket.expect(IntermediateRequestOrEndOfGame.class, new Logger(), "test"), iore);
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void stringGameSocketCommunicationWithLogger() {
        try {
            MockTestSocket sendingMockTestSocket = new MockTestSocket();
            TsuroSocket sendingSocket = new TsuroSocket(sendingMockTestSocket);
            sendingSocket.send("teststr", new Logger(), "test");
            MockTestSocket receivingMockTestSocket = new MockTestSocket(sendingMockTestSocket.getSentMessage());
            TsuroSocket receivingSocket = new TsuroSocket(receivingMockTestSocket);
            assertTrue(receivingSocket.expect(new Logger(), "test").contains("teststr"));
        } catch (IOException e) {
            fail();
        }
    }

    @Property
    public void socketClosure() {
        try (TsuroSocket socket = new TsuroSocket(new MockTestSocket())) {
        } catch (Exception e) {
            fail();
        }
    }
}
