package game.board;

import game.common.Position;
import game.tile.Port;
import game.tile.Tile;

import static game.board.BoardUtil.atPeriphery;
import static game.board.BoardUtil.withinBoardBoundary;

/**
 * The class represent the Initial placements.
 * Initial placements must place the tile against the wall, and the
 * Token position also must against the wall, the placements must within the board as well.
 */
public class InitialPlacement {
    private final Tile tile;
    private final Port port;
    private final Token token;
    private final Position position;

    private static final String INVALID_INPUTS = "Invalid Inputs";
    private static final String NOT_WITHIN_BOARD = "Position is not within Board";
    private static final String NOT_AT_PERIPHERY = "Port is not at Periphery";


    /**
     * Make an initial placement from a token, a tile, the port and a position.
     * Throw exceptions if the placements are invalid
     * @param token     The token for the placement
     * @param tile      The tile we want to place
     * @param port      The port that the token is on
     * @param position  The position that we want to place the tile on the board.
     */
    private InitialPlacement(Token token, Tile tile, Port port, Position position) {
        if (token == null || tile == null || port == null || position == null)
            throw new IllegalArgumentException(INVALID_INPUTS);
        if (!withinBoardBoundary(position))
            throw new IllegalArgumentException(NOT_WITHIN_BOARD);
        if (!atPeriphery(position, port))
            throw new IllegalArgumentException(NOT_AT_PERIPHERY);
        this.token = token;
        this.tile = tile;
        this.port = port;
        this.position = position;
    }

    public Tile getTile() {
        return this.tile;
    }

    public Port getPort() {
        return this.port;
    }

    public Token getToken() {
        return this.token;
    }

    public Position getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InitialPlacement) {
            InitialPlacement other = (InitialPlacement) obj;
            return this.token == other.getToken() &&
                    this.tile.equals(other.getTile()) &&
                    this.position.equals(other.getPosition()) &&
                    this.port.equals(other.getPort());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.token.hashCode() * this.tile.hashCode() * this.port.hashCode() * this.position.hashCode();
    }

    /**
     * Instantiation method to make a new instance of initial placements.
     */
    public static InitialPlacement of(Token token, Tile tile, Port port, Position position) {
        return new InitialPlacement(token, tile, port, position);
    }
}
