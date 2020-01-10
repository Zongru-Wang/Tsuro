package game.remote;

import com.google.gson.*;
import game.board.Token;
import game.json.deserializer.EndOfGameDeserializer;
import game.json.deserializer.InitialRequestDeserializer;
import game.json.deserializer.IntermediateRequestDeserializer;
import game.json.deserializer.TokenDeserializer;
import game.remote.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

/**
 * represent the ClientSocket which extends Socket
 */
public class ClientSocket extends Socket {
    private static String UNEXPECTED_EOF = "Unexpected end of message from server";
    private static String UNEXPECTED_MESSAGE = "Unexpected message from server";
    private final Gson gson;

    public ClientSocket(InetAddress host, int port) throws IOException {
        super(host, port);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Token.class, new TokenDeserializer());
        builder.registerTypeAdapter(InitialRequest.class, new InitialRequestDeserializer());
        builder.registerTypeAdapter(IntermediateRequest.class, new IntermediateRequestDeserializer());
        builder.registerTypeAdapter(EndOfGame.class, new EndOfGameDeserializer());
        this.gson = builder.create();
    }

    /**
     * @param message the message to send
     * @throws IOException if there is IO error
     */
    void sendMessage(Message message) throws IOException {
        message.sendTo(this.getOutputStream());
    }

    /**
     * @return Token receiver from server
     * @throws IOException if there is IO error
     */
    Token expectTokenMessage() throws IOException {
        JsonElement jsonElement = this.expectJson();
        return gson.fromJson(jsonElement, Token.class);
    }

    /**
     * @throws IOException if it is not JsonPrimitive or there is no "started" in the jsonElement
     */
    public void expectStartedMessage() throws IOException {
        JsonElement jsonElement = this.expectJson();
        if (!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().getAsString().equals("started")) {
            throw new JsonParseException(UNEXPECTED_MESSAGE);
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private JsonElement expectJson() throws IOException {
        Scanner scanner = new Scanner(this.getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
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

    public InitialRequest expectInitialRequest(Token token) throws IOException {
        JsonElement jsonElement = this.expectJson();
        return this.gson.fromJson(jsonElement, InitialRequest.class);
    }

    public IntermediateRequestOrEndOfGame expectIntermediateRequestOrEndOfGame() throws IOException {
        JsonElement jsonElement = this.expectJson();
        try {
            IntermediateRequest intermediateRequest = this.gson.fromJson(jsonElement, IntermediateRequest.class);
            return new IntermediateRequestOrEndOfGame(Optional.of(intermediateRequest), Optional.empty());
        } catch (JsonParseException ignored) {
        }
        EndOfGame endOfGame = this.gson.fromJson(jsonElement, EndOfGame.class);
        return new IntermediateRequestOrEndOfGame(Optional.empty(), Optional.of(endOfGame));
    }
}
