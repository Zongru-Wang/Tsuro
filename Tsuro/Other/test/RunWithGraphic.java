import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import game.observer.image.VectorImage;
import game.player.Strategy;
import game.remote.Client;
import game.remote.Logger;
import game.remote.Server;
import game.remote.TsuroSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunWithGraphic {
    private static final String ILLEGAL_JSON_FORMAT = "Json provided was ill-formed";
    private static final String NOT_ENOUGH_CLIENT = "Client count must be 3 to 5";

    public static void main(String[] args) {
        try {
            Logger logger = new Logger();
            LoggingObserver<VectorImage> loggingObserver = new LoggingObserverImpl();
            ObserverServer server = new ObserverServer(InetAddress.getLocalHost(), 8000, logger, loggingObserver);
            List<Client> clients = getClientSpecifications();
            if (clients.size() < Server.MIN_PLAYERS || clients.size() > Server.MAX_PLAYERS) {
                System.err.println(NOT_ENOUGH_CLIENT);
                return;
            }
            new Thread(server).start();
            for (Client client : clients) {
                new Thread(client).start();
            }
        } catch (IllegalArgumentException | IOException | JsonParseException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Get all the valid clients.
     *
     * @return A List of valid clients
     * @throws IOException if the input Json is not legal.
     */
    private static List<Client> getClientSpecifications() throws IOException {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        List<Client> result = new ArrayList<>();
        while (scanner.hasNext()) {
            builder.append(scanner.next());
        }
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(builder.toString(), JsonElement.class);
        if (!jsonElement.isJsonArray()) {
            throw new JsonParseException(ILLEGAL_JSON_FORMAT);
        }
        for (JsonElement player : jsonElement.getAsJsonArray()) {
            if (!player.isJsonObject()) {
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            }
            if (!player.getAsJsonObject().has("name") || !player.getAsJsonObject().has("strategy")) {
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            }
            if (player.getAsJsonObject().size() != 2) {
                throw new JsonParseException(ILLEGAL_JSON_FORMAT);
            }
            String name = player.getAsJsonObject().get("name").getAsString();
            String strategyName = player.getAsJsonObject().get("strategy").getAsString();
            Strategy strategy = Strategy.fromString(strategyName);
            TsuroSocket socket = new TsuroSocket(new Socket(InetAddress.getLocalHost(), 8000));
            Client client = new Client(socket, strategy, name);
            result.add(client);
        }
        return result;
    }
}
