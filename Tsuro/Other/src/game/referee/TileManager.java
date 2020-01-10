package game.referee;

import game.tile.Tile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an entity that distributes Tiles during a game of Tsuro.
 */
public class TileManager {
    private List<Tile> availableTiles;
    private int nextAvailableTileIndex;

    public TileManager() {
        this.nextAvailableTileIndex = 0;
        this.availableTiles = new ArrayList<>();
        try {
            this.availableTiles.addAll(Tile.loadAll("/tile.json"));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to fetch list of tiles");
        }
    }

    /**
     * Determines the next available tiles to give away and returns it.  May or may not be different upon each call.
     * @param nTiles    the number of tiles to get
     * @return          the next available tile
     */
    public List<Tile> getNextAvailableTiles(int nTiles) {
        List<Tile> tiles = new ArrayList<>(nTiles);
        for (int i = 0; i < nTiles; i++) {
            tiles.add(this.availableTiles.get(this.nextAvailableTileIndex));
            // cycle as necessary
            this.nextAvailableTileIndex = (this.nextAvailableTileIndex + 1) % this.availableTiles.size();
        }
        return tiles;
    }
}
