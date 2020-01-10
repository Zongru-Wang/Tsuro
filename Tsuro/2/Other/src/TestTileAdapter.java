import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

public class TestTileAdapter {
    public static ITile adaptTile(TestInput input) {

        // TODO
        // Convert tile representation of input to ITile
        String[] tiles = {"[0,[[\"A\",\"E\"],[\"B\",\"F\"],[\"C\",\"H\"],[\"D\",\"G\"]]]",
                "[1,[[\"A\",\"E\"],[\"B\",\"F\"],[\"C\",\"G\"],[\"D\",\"H\"]]]",
                "[2,[[\"A\",\"F\"],[\"B\",\"E\"],[\"C\",\"H\"],[\"D\",\"G\"]]]",
                "[3,[[\"A\",\"E\"],[\"B\",\"D\"],[\"C\",\"G\"],[\"F\",\"H\"]]]",
                "[4,[[\"A\",\"H\"],[\"B\",\"C\"],[\"D\",\"E\"],[\"F\",\"G\"]]]",
                "[5,[[\"A\",\"E\"],[\"B\",\"C\"],[\"D\",\"H\"],[\"F\",\"G\"]]]",
                "[6,[[\"A\",\"E\"],[\"B\",\"C\"],[\"D\",\"G\"],[\"F\",\"H\"]]]",
                "[7,[[\"A\",\"D\"],[\"B\",\"G\"],[\"C\",\"F\"],[\"E\",\"H\"]]]",
                "[8,[[\"A\",\"D\"],[\"B\",\"F\"],[\"C\",\"G\"],[\"E\",\"H\"]]]",
                "[9,[[\"A\",\"D\"],[\"B\",\"E\"],[\"C\",\"H\"],[\"F\",\"G\"]]]",
                "[10,[[\"A\",\"D\"],[\"B\",\"E\"],[\"C\",\"G\"],[\"F\",\"H\"]]]",
                "[11,[[\"A\",\"D\"],[\"B\",\"C\"],[\"E\",\"H\"],[\"F\",\"G\"]]]",
                "[12,[[\"A\",\"C\"],[\"B\",\"H\"],[\"D\",\"F\"],[\"E\",\"G\"]]]",
                "[13,[[\"A\",\"C\"],[\"B\",\"H\"],[\"D\",\"E\"],[\"F\",\"G\"]]]",
                "[14,[[\"A\",\"C\"],[\"B\",\"G\"],[\"D\",\"F\"],[\"E\",\"H\"]]]",
                "[15,[[\"A\",\"C\"],[\"B\",\"G\"],[\"D\",\"E\"],[\"F\",\"H\"]]]",
                "[16,[[\"A\",\"C\"],[\"B\",\"F\"],[\"D\",\"H\"],[\"E\",\"G\"]]]",
                "[17,[[\"A\",\"C\"],[\"B\",\"F\"],[\"D\",\"G\"],[\"E\",\"H\"]]]",
                "[18,[[\"A\",\"C\"],[\"B\",\"E\"],[\"D\",\"H\"],[\"F\",\"G\"]]]",
                "[19,[[\"A\",\"C\"],[\"B\",\"E\"],[\"D\",\"G\"],[\"F\",\"H\"]]]",
                "[20,[[\"A\",\"C\"],[\"B\",\"D\"],[\"E\",\"H\"],[\"F\",\"G\"]]]",
                "[21,[[\"A\",\"C\"],[\"B\",\"D\"],[\"E\",\"G\"],[\"F\",\"H\"]]]",
                "[22,[[\"A\",\"B\"],[\"C\",\"H\"],[\"D\",\"G\"],[\"E\",\"F\"]]]",
                "[23,[[\"A\",\"B\"],[\"C\",\"H\"],[\"D\",\"F\"],[\"E\",\"G\"]]]",
                "[24,[[\"A\",\"B\"],[\"C\",\"H\"],[\"D\",\"E\"],[\"F\",\"G\"]]]",
                "[25,[[\"A\",\"B\"],[\"C\",\"G\"],[\"D\",\"H\"],[\"E\",\"F\"]]]",
                "[26,[[\"A\",\"B\"],[\"C\",\"G\"],[\"D\",\"F\"],[\"E\",\"H\"]]]",
                "[27,[[\"A\",\"B\"],[\"C\",\"G\"],[\"D\",\"E\"],[\"F\",\"H\"]]]",
                "[28,[[\"A\",\"B\"],[\"C\",\"F\"],[\"D\",\"H\"],[\"E\",\"G\"]]]",
                "[29,[[\"A\",\"B\"],[\"C\",\"F\"],[\"D\",\"G\"],[\"E\",\"H\"]]]",
                "[30,[[\"A\",\"B\"],[\"C\",\"E\"],[\"D\",\"H\"],[\"F\",\"G\"]]]",
                "[31,[[\"A\",\"B\"],[\"C\",\"E\"],[\"D\",\"G\"],[\"F\",\"H\"]]]",
                "[32,[[\"A\",\"B\"],[\"C\",\"D\"],[\"E\",\"H\"],[\"F\",\"G\"]]]",
                "[33,[[\"A\",\"B\"],[\"C\",\"D\"],[\"E\",\"G\"],[\"F\",\"H\"]]]",
                "[34,[[\"A\",\"B\"],[\"C\",\"D\"],[\"E\",\"F\"],[\"G\",\"H\"]]]"};

        int index = input.getIndex();
        JsonArray jsonArray = new Gson().fromJson(tiles[index], JsonArray.class);
        JsonArray jsonArrayTile = (JsonArray) jsonArray.get(1);
        JsonArray firstConnection = (JsonArray) jsonArrayTile.get(0);
        JsonArray secondConnection = (JsonArray) jsonArrayTile.get(1);
        JsonArray thirdConnection = (JsonArray) jsonArrayTile.get(2);
        JsonArray fourthConnection = (JsonArray) jsonArrayTile.get(3);
        JsonArray[] arrays = {firstConnection, secondConnection, thirdConnection, fourthConnection};
        List<String> atile = new ArrayList<>();
        for(int i =  0; i < arrays.length; i++){
            atile.add(arrays[i].get(0).getAsString());
            atile.add(arrays[i].get(1).getAsString());
        }

        int degree = input.getDegree();
        ITile tile = build(transferType(atile));
        for (int i = 0; i < degree/90; i++) {
            tile = tile.rotate();
        }
        return tile;
    }

    private static ITile build(List<ITile.Port> ports) {
        BaseTile.Builder builder = new BaseTile.Builder();
        builder.addConnection(ports.get(0), ports.get(1));
        builder.addConnection(ports.get(2), ports.get(3));
        builder.addConnection(ports.get(4), ports.get(5));
        builder.addConnection(ports.get(6), ports.get(7));
        return builder.build();
    }

    private static List<ITile.Port> transferType(List<String> atile){
        List<ITile.Port> ports = new ArrayList<>();
        for(int i =  0; i < atile.size(); i++){
            ports.add(adaptStartPortHelper(atile.get(i)));
        }
        return ports;
    }

    public static ITile.Port adaptStartPort(TestInput input) {
        String port = input.getPort();
        return adaptStartPortHelper(port);
    }

    public static ITile.Port adaptStartPortHelper (String port) {
        // TODO
        // Convert port of output json to ITile.Port
        ITile.Port A = new ITile.Port(ITile.Port.Direction.NORTH, ITile.Port.Index.ONE);
        ITile.Port B = new ITile.Port(ITile.Port.Direction.NORTH, ITile.Port.Index.TWO);
        ITile.Port C = new ITile.Port(ITile.Port.Direction.EAST, ITile.Port.Index.ONE);
        ITile.Port D = new ITile.Port(ITile.Port.Direction.EAST, ITile.Port.Index.TWO);
        ITile.Port E = new ITile.Port(ITile.Port.Direction.SOUTH, ITile.Port.Index.ONE);
        ITile.Port F = new ITile.Port(ITile.Port.Direction.SOUTH, ITile.Port.Index.TWO);
        ITile.Port G = new ITile.Port(ITile.Port.Direction.WEST, ITile.Port.Index.ONE);
        ITile.Port H = new ITile.Port(ITile.Port.Direction.WEST, ITile.Port.Index.TWO);
        ITile.Port result = null;

        if (port.equals("A")) {
            result = A;
        } else if (port.equals("B")) {
            result = B;
        } else if (port.equals("C")) {
            result = C;
        } else if (port.equals("D")) {
            result = D;
        } else if (port.equals("E")) {
            result = E;
        } else if (port.equals("F")) {
            result = F;
        } else if (port.equals("G")) {
            result = G;
        } else if (port.equals("H")) {
            result = H;
        } else {
            throw new IllegalArgumentException("Illegal port");
        }
        return result;
    }
}
