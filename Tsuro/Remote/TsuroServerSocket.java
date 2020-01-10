package game.remote;

import com.google.gson.*;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.json.deserializer.InitialPlacementDeserializer;
import game.json.deserializer.IntermediatePlacementDeserializer;
import game.remote.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TsuroServerSocket {
    private final Socket socket;
    private final Gson gson;

    private static String UNEXPECTED_MESSAGE = "Unexpected message from client";
    private static String UNEXPECTED_EOF = "Unexpected end of message from client";

    public TsuroServerSocket(Socket socket) {
        this.socket = socket;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(InitialPlacement.class, new InitialPlacementDeserializer());
        builder.registerTypeAdapter(IntermediatePlacement.class, new IntermediatePlacementDeserializer());
        this.gson = builder.create();
    }

    /**
     * @param message the message to send
     * @throws IOException if there is IO error
     */
    public void sendMessage(Message message, String identifier, Logger logger) throws IOException {
        message.sendTo(this.socket.getOutputStream());
        logger.logSentMessage(message, identifier);
    }

    /**
     * @return Token receiver from server
     * @throws IOException if there is IO error
     */
    public String expectIdentifierMessage(String identifier, Logger logger) throws IOException {
        JsonElement jsonElement = this.expectJson(identifier, logger);
        if (!jsonElement.isJsonPrimitive()) {
            throw new JsonParseException(UNEXPECTED_MESSAGE);
        }
        return jsonElement.getAsString();
    }

    /**
     * @return
     * @throws IOException
     */
    private JsonElement expectJson(String identifier, Logger logger) throws IOException {
        Scanner scanner = new Scanner(this.socket.getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
            try {
                if (stringBuilder.toString().isEmpty()) {
                    continue;
                }
                JsonElement jsonElement = JsonParser.parseString(stringBuilder.toString());
                logger.logReceivedMessage(jsonElement.toString(), identifier);
                return jsonElement;
            } catch (JsonParseException ignored) {
            }
        }
        throw new EOFException(UNEXPECTED_EOF);
    }

    public InitialPlacement expectInitialPlacement(String identifier, Logger logger) throws IOException {
        JsonElement jsonElement = this.expectJson(identifier, logger);
        return this.gson.fromJson(jsonElement, InitialPlacement.class);
    }

    public IntermediatePlacement expectIntermediatePlacement(String identifier, Logger logger) throws IOException {
        JsonElement jsonElement = this.expectJson(identifier, logger);
        return this.gson.fromJson(jsonElement, IntermediatePlacement.class);
    }
}
