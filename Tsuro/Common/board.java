import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPlayerBoard {
    Optional<ITile> get(int x, int y);
    Board.TokenLocation getLocationOf(Token token);
}

public interface IRefereeBoard extends IPlayerBoard {
    void place(ITile tile, int x, int y) throws IllegalArgumentException, IndexOutOfBoundsException;
    void placeToken(Token token, ITile.Port port, int x, int y) throws IllegalArgumentException, IndexOutOfBoundsException;
    void advanceToken(Token token) throws IllegalArgumentException;
    void placeFirsts(List<IInitialPlacement> initialPlacements) throws IllegalArgumentException, IndexOutOfBoundsException;
}

public class Board implements IRefereeBoard {
    //entry in this 2d array may be null;
    private ITile[][] tiles;
    private Map<Token, TokenLocation> tokenLocationMap;

    public Board() {
        this.tiles = new ITile[10][10];
        this.tokenLocationMap = new HashMap<>();
    }

    // For testing
    public Board(ITile[][] tiles, Map<Token, TokenLocation> tokenLocationMap) {
        this.tiles = tiles;
        this.tokenLocationMap = tokenLocationMap;
    }

    @Override
    // reminder: UPDATE Board.java TO REFLECT UPDATED INTERFACE
    public Optional<ITile> get(int x, int y) throws IndexOutOfBoundsException {
        if (!this.withinBoard(x, y)) {
            throw new IndexOutOfBoundsException();
        }

        // I just realized we agreed to use a 2d array but didn't agree on how elements are accessed
        // I'm assuming we're accessing it by [y][x], since that's standard
        ITile tile = this.tiles[y][x];
        if (tile == null) {
            return Optional.empty();
        }

        return Optional.of(tile);
    }

    @Override
    public TokenLocation getLocationOf(Token token) throws IllegalArgumentException {
        if(!tokenLocationMap.containsKey(token))
            throw new IllegalArgumentException();
        return this.tokenLocationMap.get(token);
    }


    @Override
    public void place(ITile tile, int x, int y) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (!this.withinBoard(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        if (this.get(x, y).isPresent()) {
            throw new IllegalArgumentException();
        }
        if (tile == null) {
            throw new IllegalArgumentException();
        }
        this.tiles[y][x] = tile;
    }

    public void advanceToken(Token token) throws IllegalArgumentException {
        if (!this.tokenLocationMap.containsKey(token)) {
            throw new IllegalArgumentException();
        }
        TokenLocation location = this.tokenLocationMap.get(token);
        TokenLocation newLocation = location;
        while (hasNextLocation(newLocation)) {
            newLocation = getNextLocation(newLocation);
        }
        TokenLocation mostRecentLegalLocation = newLocation;
        newLocation = getNextLocation(newLocation);
        if (newLocation == null) {
            this.tokenLocationMap.put(token, mostRecentLegalLocation);
        } else if (isPortPeriphery(newLocation.getX(), newLocation.getY(), newLocation.getPort())) {
            this.tokenLocationMap.remove(token);
        } else if (!withinBoard(newLocation.getX(), newLocation.getY())) {
            this.tokenLocationMap.remove(token);
        } else {
            throw new RuntimeException();
        }
    }

    private boolean hasNextLocation(TokenLocation location) {
        TokenLocation newLocation = getNextLocation(location);
        return newLocation != null && withinBoard(newLocation.getX(), newLocation.getY());
    }

    private TokenLocation getNextLocation(TokenLocation location) {
        ITile tile = this.get(location.getX(), location.getY()).get();
        ITile.Port port = tile.connectedPort(location.getPort());
        int nextX = location.getX();
        int nextY = location.getY();
        if (port.getDirection().equals(ITile.Port.Direction.SOUTH)) {
            nextY++;
        } else if (port.getDirection().equals(ITile.Port.Direction.NORTH)) {
            nextY--;
        } else if (port.getDirection().equals(ITile.Port.Direction.EAST)) {
            nextX++;
        } else if (port.getDirection().equals(ITile.Port.Direction.WEST)) {
            nextX--;
        } else {
            throw new RuntimeException();
        }
        if (!withinBoard(nextX, nextY))
            return new TokenLocation(nextPort(tile.connectedPort(port)), nextX, nextY);
        if (!this.get(nextX, nextY).isPresent())
            return null;
        return new TokenLocation(nextPort(port), nextX, nextY);
    }

    private ITile.Port nextPort(ITile.Port port) {
        ITile.Port.Direction newDirection = null;
        ITile.Port.Index newIndex = null;
        if (port.getDirection().equals(ITile.Port.Direction.SOUTH)) {
            newDirection = ITile.Port.Direction.NORTH;
        } else if (port.getDirection().equals(ITile.Port.Direction.NORTH)) {
            newDirection = ITile.Port.Direction.SOUTH;
        } else if (port.getDirection().equals(ITile.Port.Direction.EAST)) {
            newDirection = ITile.Port.Direction.WEST;
        } else if (port.getDirection().equals(ITile.Port.Direction.WEST)) {
            newDirection = ITile.Port.Direction.EAST;
        } else {
            throw new RuntimeException();
        }
        if (port.getIndex().equals(ITile.Port.Index.ONE)) {
            newIndex = ITile.Port.Index.TWO;
        } else if (port.getIndex().equals(ITile.Port.Index.TWO)) {
            newIndex = ITile.Port.Index.ONE;
        } else {
            throw new RuntimeException();
        }
        return new ITile.Port(newDirection, newIndex);
    }

    private boolean withinBoard(int x, int y) {
        return 0 <= x && x < 10 && 0 <= y && y < 10;
    }

    @Override
    public void placeToken(Token token, ITile.Port port, int x, int y) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (this.tokenLocationMap.containsKey(token)) {
            throw new IllegalArgumentException();
        }
        if (!this.withinBoard(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.isPortPeriphery(x, y, port)) {
            throw new IllegalArgumentException();
        }
        if (!this.get(x,y).isPresent()) {
            throw new IllegalStateException();
        }
        TokenLocation location = new TokenLocation(port, x, y);
        this.tokenLocationMap.put(token, location);
    }

    @Override
    public void placeFirsts(List<IInitialPlacement> initialPlacements) throws IllegalArgumentException, IndexOutOfBoundsException {
        Board testBoard = this.copy();
        for (IInitialPlacement initialPlacement: initialPlacements) {
            ITile.Port port = initialPlacement.getPort();
            ITile tile = initialPlacement.getTile();
            Token token = initialPlacement.getToken();
            int x = initialPlacement.getX();
            int y = initialPlacement.getY();
            if (this.tokenLocationMap.containsKey(token)) {
                throw new IllegalArgumentException();
            }
            if (!testBoard.withinBoard(x, y))
                throw new IndexOutOfBoundsException();
            if (port == null || tile == null || token == null)
                throw new IllegalArgumentException();
            if (!testBoard.isPeriphery(x, y))
                throw new IllegalArgumentException();
            if (testBoard.isNeighborOccupied(x, y))
                throw new IllegalArgumentException();
            if (!testBoard.isPortPeriphery(x, y, port))
                throw new IllegalArgumentException();
            testBoard.place(tile, x, y);
            testBoard.placeToken(token, port, x, y);
        }

        for (IInitialPlacement initialPlacement: initialPlacements) {
            ITile.Port port = initialPlacement.getPort();
            ITile tile = initialPlacement.getTile();
            Token token = initialPlacement.getToken();
            int x = initialPlacement.getX();
            int y = initialPlacement.getY();
            place(tile, x, y);
            placeToken(token, port, x, y);
        }
    }

    private Board copy() {
        ITile[][] copyTiles = new ITile[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (this.get(x, y).isPresent()) {
                    copyTiles[y][x] = this.get(x, y).get();
                } else {
                    copyTiles[y][x] = null;
                }
            }
        }
        return new Board(copyTiles, new HashMap<>(this.tokenLocationMap));
    }

    private boolean isPeriphery(int x, int y) {
        return x == 0 || x == 9 || y == 0 || y == 9;
    }

    private boolean isNeighborOccupied(int x, int y) {
        if (withinBoard(x + 1, y))
            if (get(x + 1, y).isPresent())
                return true;
        if (withinBoard(x - 1, y))
            if (get(x - 1, y).isPresent())
                return true;
        if (withinBoard(x, y + 1))
            if (get(x, y + 1).isPresent())
                return true;
        if (withinBoard(x, y - 1))
            if (get(x, y - 1).isPresent())
                return true;
        return false;
    }

    // Must be called after isPeriphery
    private boolean isPortPeriphery(int x, int y, ITile.Port port) {
        if (!isPeriphery(x, y)) {
            throw new RuntimeException();
        }
        return (x == 0 && port.getDirection().equals(ITile.Port.Direction.WEST)) ||
                (x == 9 && port.getDirection().equals(ITile.Port.Direction.EAST)) ||
                (y == 0 && port.getDirection().equals(ITile.Port.Direction.NORTH)) ||
                (y == 9 && port.getDirection().equals(ITile.Port.Direction.SOUTH));
    }

    public static class TokenLocation {
        private ITile.Port port;
        private int x;
        private int y;

        public TokenLocation(ITile.Port port, int x, int y) {
            this.port = port;
            this.x = x;
            this.y = y;
        }

        public ITile.Port getPort() {
            return port;
        }

        public int getX() {
            return x;
        }

        public  int getY() {
            return y;
        }
    }
}

/*
Represents an initial placement of a player specified by the location of tile,
token type, tile type, port that the token occupies.
 */
public interface IInitialPlacement {
    /**
     * get the tile type of the initial placement.
     * @return      the initial tile
     */
    ITile getTile();

    /**
     * get the port in which the token is placed.
     * @return      the initial port
     */
    ITile.Port getPort();

    /**
     * get the token type.
     * @return      the initial token
     */
    Token getToken();

    /**
     * get the x component of the location of tile placement..
     * @return      the initial x component
     */
    int getX();

    /**
     * get the y component of the location of tile placement..
     * @return      the initial y component
     */
    int getY();
}

public class InitialPlacement implements IInitialPlacement {
    private ITile tile;
    private ITile.Port port;
    private Token token;
    private int x;
    private int y;

    InitialPlacement(ITile tile, ITile.Port port, Token token, int x, int y) {
        this.tile = tile;
        this.port = port;
        this.token = token;
        if (!(x == 0||x == 9||y == 0||y ==9)) {
            throw new IllegalArgumentException();
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public ITile getTile() {
        return this.tile;
    }

    @Override
    public ITile.Port getPort() {
        return this.port;
    }

    @Override
    public Token getToken() {
        return this.token;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}

public enum Token {
    WHITE, BLACK, RED, GREEN, BLUE
}

