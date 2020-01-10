package game.remote;

import com.google.gson.*;
import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.json.deserializer.*;
import game.json.serializer.*;
import game.tile.Port;
import game.tile.Tile;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * represent the ClientSocket which composes Socket
 */
public class TsuroSocket implements AutoCloseable {
    private static String UNEXPECTED_EOF = "Unexpected end of message from server";
    private static String UNEXPECTED_MESSAGE = "Unexpected message from server";
    private final Gson gson;
    private final PrintWriter printWriter;
    private final Scanner scanner;
    private final Socket socket;

    public TsuroSocket(Socket socket) throws IOException {
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(socket.getInputStream());
        this.socket = socket;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Port.class, new PortDeserializer());
        builder.registerTypeAdapter(Port.class, new PortSerializer());
        builder.registerTypeAdapter(Board.class, new BoardDeserializer());
        builder.registerTypeAdapter(Board.class, new BoardSerializer());
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameDeserializer());
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameSerializer());
        builder.registerTypeAdapter(InitialRequest.class, new InitialRequestDeserializer());
        builder.registerTypeAdapter(InitialRequest.class, new InitialRequestSerializer());
        builder.registerTypeAdapter(Tile.class, new TileDeserializer());
        builder.registerTypeAdapter(Tile.class, new TileSerializer());
        builder.registerTypeAdapter(IntermediatePlacement.class, new IntermediatePlacementDeserializer());
        builder.registerTypeAdapter(IntermediatePlacement.class, new IntermediatePlacementSerializer());
        builder.registerTypeAdapter(Token.class, new TokenSerializer());
        builder.registerTypeAdapter(Token.class, new TokenDeserializer());
        builder.registerTypeAdapter(InitialPlacement.class, new InitialPlacementDeserializer());
        builder.registerTypeAdapter(InitialPlacement.class, new InitialPlacementSerializer());
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestDeserializer());
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestSerializer());
        builder.registerTypeAdapter(IntermediateRequestOrEndOfGame.class, new IntermediateRequestOrEndOfGameDeserializer());
        builder.registerTypeAdapter(IntermediateRequestOrEndOfGame.class, new IntermediateRequestOrEndOfGameSerializer());
        this.gson = builder.create();
    }

    /**
     * send json message through socket.
     *
     * @param tClass the Class of obj
     * @param obj    object to serialize as json and send
     * @param <T>    type parameter for custom serialization selection.
     */
    public <T> void send(Class<T> tClass, T obj) {
        this.printWriter.println(this.gson.toJsonTree(obj, tClass));
    }

    /**
     * send json message through socket while logging it.
     *
     * @param tClass the Class of obj
     * @param obj    object to serialize as json and send
     * @param <T>    type parameter for custom serialization selection.
     */
    public <T> void send(Class<T> tClass, T obj, Logger logger, String identifier) {
        this.printWriter.println(this.gson.toJsonTree(obj, tClass));
        logger.logSentMessage(this.gson.toJsonTree(obj, tClass).toString(), identifier);
    }

    /**
     * send string message as json message
     *
     * @param str message
     */
    public void send(String str) {
        this.printWriter.println(String.format("\"%s\"", str));
    }

    /**
     * send string message as json message
     *
     * @param str message
     */
    public void send(String str, Logger logger, String identifier) {
        this.printWriter.println(String.format("\"%s\"", str));
        logger.logSentMessage(String.format("\"%s\"", str), identifier);
    }

    /**
     * expect a json message from a socket and deserialize it.
     *
     * @param tClass the Class of object to expect.
     * @param <T>    Object type
     * @return received message from Socket as T
     */
    public <T> T expect(Class<T> tClass) throws EOFException {
        JsonElement jsonElement = this.expectJson();
        return this.gson.fromJson(jsonElement, tClass);
    }

    /**
     * expect a json message from a socket and deserialize it.
     *
     * @param tClass the Class of object to expect.
     * @param <T>    Object type
     * @return received message from Socket as T
     */
    public <T> T expect(Class<T> tClass, Logger logger, String identifier) throws EOFException {
        JsonElement jsonElement = this.expectJson();
        logger.logReceivedMessage(jsonElement.toString(), identifier);
        return this.gson.fromJson(jsonElement, tClass);
    }

    /**
     * expect a string message from a socket and validate it
     *
     * @return whether string message received was as expected.
     */
    public String expect() throws EOFException {
        JsonElement jsonElement = this.expectJson();
        if (!jsonElement.isJsonPrimitive()) {
            throw new JsonParseException(UNEXPECTED_MESSAGE);
        }
        return jsonElement.getAsJsonPrimitive().getAsString();
    }


    /**
     * expect a string message from a socket and validate it
     *
     * @return whether string message received was as expected.
     */
    public String expect(Logger logger, String identifier) throws EOFException {
        JsonElement jsonElement = this.expectJson();
        if (!jsonElement.isJsonPrimitive()) {
            throw new JsonParseException(UNEXPECTED_MESSAGE);
        }
        logger.logReceivedMessage(jsonElement.toString(), identifier);
        return jsonElement.getAsJsonPrimitive().getAsString();
    }

    /**
     * read json from a socket connection and return it as jsonElement
     *
     * @return jsonElement sent from the socket
     * @throws EOFException when json with no clear end was sent
     */
    private JsonElement expectJson() throws EOFException {
        StringBuilder stringBuilder = new StringBuilder();
        while (this.scanner.hasNextLine()) {
            stringBuilder.append(this.scanner.nextLine());
            try {
                if (stringBuilder.toString().isEmpty()) {
                    continue;
                }
                return JsonParser.parseString(stringBuilder.toString());
            } catch (JsonParseException ignored) {
            }
        }
        throw new EOFException(UNEXPECTED_EOF);
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        this.socket.close();
        this.printWriter.close();
        this.scanner.close();
    }
}
