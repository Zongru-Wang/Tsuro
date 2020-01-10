package property;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.json.deserializer.*;
import game.json.serializer.*;
import game.remote.EndOfGame;
import game.remote.InitialRequest;
import game.remote.IntermediateRequest;
import game.remote.IntermediateRequestOrEndOfGame;
import game.tile.Port;
import game.tile.Tile;
import org.junit.runner.RunWith;
import property.generator.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnitQuickcheck.class)
public class TestSerializationProperty {
    @Property public void portSerialization(@From(PortGenerator.class) Port port) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Port.class, new PortDeserializer());
        builder.registerTypeAdapter(Port.class, new PortSerializer());
        Gson gson = builder.create();
        assertEquals(port, gson.fromJson(gson.toJsonTree(port, Port.class), Port.class));
    }

    @Property public void tileSerialization(@From(TileGenerator.class) Tile tile) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        Gson gson = builder.create();
        assertEquals(tile, gson.fromJson(gson.toJsonTree(tile, Tile.class), Tile.class));
    }

    @Property
    public void boardSerialization(@From(BoardGenerator.class) Board board) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Board.class, new BoardDeserializer());
        builder.registerTypeAdapter(Board.class, new BoardSerializer());
        Gson gson = builder.create();
        assertEquals(board, gson.fromJson(gson.toJsonTree(board, Board.class), Board.class));
    }

    @Property public void tileOptSerialization(@From(TileGenerator.class) Tile tile) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new OptionalTileDeserializer());
        builder.registerTypeAdapter(Tile.class, new OptionalTileSerializer());
        Gson gson = builder.create();
        assertEquals(tile, gson.fromJson(gson.toJsonTree(tile, Tile.class), Tile.class));
        assertNull(gson.fromJson(gson.toJsonTree(null, Tile.class), Tile.class));
    }

    @Property public void endOfGameSerialization(@From(EndOfGameGenerator.class)EndOfGame endOfGame) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameDeserializer());
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameSerializer());
        Gson gson = builder.create();
        assertEquals(endOfGame, gson.fromJson(gson.toJsonTree(endOfGame, EndOfGame.class), EndOfGame.class));
    }

    @Property public void initialRequestSerialization(@From(InitialRequestGenerator.class)InitialRequest initialRequest) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(InitialRequest.class, new InitialRequestDeserializer());
        builder.registerTypeAdapter(InitialRequest.class, new InitialRequestSerializer());
        Gson gson = builder.create();
        assertEquals(initialRequest, gson.fromJson(gson.toJsonTree(initialRequest, InitialRequest.class), InitialRequest.class));
    }

    @Property public void intermediateRequestSerialization(@From(IntermediateRequestGenerator.class)IntermediateRequest intermediateRequest) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestDeserializer());
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestSerializer());
        Gson gson = builder.create();
        assertEquals(intermediateRequest, gson.fromJson(gson.toJsonTree(intermediateRequest, IntermediateRequest.class), IntermediateRequest.class));
    }

    @Property public void tokenSerialization(Token token) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Token.class, new TokenDeserializer());
        builder.registerTypeAdapter(Token.class, new TokenSerializer());
        Gson gson = builder.create();
        assertEquals(token, gson.fromJson(gson.toJsonTree(token, Token.class), Token.class));
    }

    @Property
    public void initialPlacementSerialization(@From(InitialPlacementGenerator.class) InitialPlacement initialPlacement) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(InitialPlacement.class, new InitialPlacementDeserializer());
        builder.registerTypeAdapter(InitialPlacement.class, new InitialPlacementSerializer());
        Gson gson = builder.create();
        assertEquals(initialPlacement, gson.fromJson(gson.toJsonTree(initialPlacement, InitialPlacement.class), InitialPlacement.class));
    }

    @Property
    public void intermediatePlacementSerialization(@From(IntermediatePlacementGenerator.class) IntermediatePlacement intermediatePlacement) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediatePlacement.class, new IntermediatePlacementDeserializer());
        builder.registerTypeAdapter(IntermediatePlacement.class, new IntermediatePlacementSerializer());
        Gson gson = builder.create();
        assertEquals(intermediatePlacement, gson.fromJson(gson.toJsonTree(intermediatePlacement, IntermediatePlacement.class), IntermediatePlacement.class));
    }

    @Property
    public void intermediatePlacementOrEndOfGameSerialization(@From(IntermediateRequestOrEndOfGameGenerator.class) IntermediateRequestOrEndOfGame iore) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IntermediateRequestOrEndOfGame.class, new IntermediateRequestOrEndOfGameDeserializer());
        builder.registerTypeAdapter(IntermediateRequestOrEndOfGame.class, new IntermediateRequestOrEndOfGameSerializer());
        Gson gson = builder.create();
        assertEquals(iore, gson.fromJson(gson.toJsonTree(iore, IntermediateRequestOrEndOfGame.class), IntermediateRequestOrEndOfGame.class));
    }
}
