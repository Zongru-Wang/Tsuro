package game;

import game.board.IInitialPlacement;
import game.board.IIntermediatePlacement;
import game.board.IPlayerBoard;
import game.board.Token;
import game.tile.Tile;

import java.util.List;
import java.util.Optional;

public class PlayerImpl implements Player {
    private String identifier;
    private Optional<Token> token;
    private long age;
    private Strategy strategy;

    public PlayerImpl(String identifier, long age, Strategy strategy) {
        if (age < 0) {
            throw new IllegalStateException("No player can have a negative age.");
        }

        this.identifier = identifier;
        this.token = Optional.empty();
        this.age = age;
        this.strategy = strategy;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public Optional<Token> getToken() {
        return this.token;
    }

    @Override
    public long getAge() {
        return this.age;
    }

    @Override
    public void giveToken(Token token) throws IllegalStateException {
        if (this.token.isPresent()) {
            throw new IllegalStateException("This Player already has a Token assigned to it");
        }
        this.token = Optional.of(token);
    }

    @Override
    public void revokeToken() throws IllegalStateException {

    }

    @Override
    public void notifyOfOtherPlayers(List<Token> tokens) {
        // not sure what this should do for now -- fill in later
    }

    @Override
    public IInitialPlacement notifyInitialTurn(IPlayerBoard board, List<Tile> availableTiles, TurnHandler turnHandler) {
        // Assuming token has been assigned, since this should only be called during a game.
        return this.strategy.generateInitialPlacement(board, availableTiles, this.token.get(), turnHandler.getRuleChecker());
    }

    @Override
    public IIntermediatePlacement notifyIntermediateTurn(IPlayerBoard board, List<Tile> availableTiles, TurnHandler turnHandler) {
        // Assuming token has been assigned, since this should only be called during a game.
        return this.strategy.generateIntermediatePlacement(board, availableTiles, this.token.get(), turnHandler.getRuleChecker());
    }

    @Override
    public void notifyLoss(IPlayerBoard board) {
        // not sure what this should do for now -- fill in later
    }

    @Override
    public void notifyWin(IPlayerBoard board) {
        // not sure what this should do for now -- fill in later
    }

    @Override
    public void notifyEndOfGame(IPlayerBoard board) {
        // not sure what this should do for now -- fill in later
    }
}

package game;

import game.board.IInitialPlacement;
import game.board.IIntermediatePlacement;
import game.board.IPlayerBoard;
import game.board.Token;
import game.rulechecker.IRuleChecker;
import game.tile.Tile;

import java.util.List;

public interface Strategy {
    /**
     * Generates an IInitialPlacement using the given availableTiles and board.
     * @param board             the board on which to make the IInitialPlacement
     * @param availableTiles    the Tiles this Strategy can use to generate their placement
     * @param token             the Token to be placed in the returned IInitialPlacement
     * @return                  some IInitialPlacement
     */
    IInitialPlacement generateInitialPlacement(IPlayerBoard board, List<Tile> availableTiles, Token token, IRuleChecker checker);

    /**
     * Generates an IIntermediatePlacement using the given availableTiles and board.
     * @param board             the board on which to make the IIntermediatePlacement
     * @param availableTiles    the Tiles this Strategy can use to generate their placement
     * @param token             the Token to be placed in the returned IIntermediatePlacement
     * @return                  some IIntermediatePlacement
     */
    IIntermediatePlacement generateIntermediatePlacement(IPlayerBoard board, List<Tile> availableTiles, Token token, IRuleChecker checker);
}



package game;

import game.board.*;
import game.common.Direction;
import game.rulechecker.IRuleChecker;
import game.tile.Port;
import game.tile.PortImpl;
import game.tile.Tile;
import javafx.util.Pair;

import java.util.List;

public class Dumb implements Strategy {
    @Override
    public IInitialPlacement generateInitialPlacement(IPlayerBoard board, List<Tile> availableTiles, Token token, IRuleChecker checker) throws RuntimeException {
        Pair<Integer, Integer> startLocation = new Pair<>(0, 0);
        Pair<Integer, Integer> bounds = new Pair<>(10, 10);
        Pair<Integer, Integer> location = new Pair<>(0, 0);

        do {
            IInitialPlacement placement = new InitialPlacement(
                    availableTiles.get(availableTiles.size() - 1),
                    getFirstPortForBorderLocation(location),
                    token,
                    location.getKey(), location.getValue());
            // BAD CAST -- I need to discuss a change to the interface with my group, as the rule checker implementation
            // currently relies on an IRefereeBoard
            if (checker.checkInitialPlacement((IRefereeBoard) board, placement, availableTiles)) {
                return placement;
            }
            location = this.getNextClockwiseLocationForBounds(location, bounds);
        } while (location.getKey() != startLocation.getKey() || location.getValue() != startLocation.getValue());

        throw new RuntimeException("No valid locations to place initial Tile :(");
    }

    @Override
    public IIntermediatePlacement generateIntermediatePlacement(IPlayerBoard board, List<Tile> availableTiles, Token token, IRuleChecker checker) {
        Board.TokenLocation tokenLocation = board.getLocationOf(token);

        int currentX = tokenLocation.getX();
        int currentY = tokenLocation.getY();

        // these next three lines are only necessary because advanceToken is working under an incorrect assumption.
        Tile currentTile = board.get(currentX, currentY).get();
        Port currentPort = tokenLocation.getPort();
        tokenLocation = new Board.TokenLocation(currentTile.connectedPort(currentPort), currentX, currentY);
        // TODO -- remove the above 3 lines after advanceToken is fixed

        Tile tile = availableTiles.get(0);
        Pair<Integer, Integer> location = getAdjacentLocationFromTokenLocation(tokenLocation);

        // So this sucks.
        // Board's advanceToken method is implemented wrong, so that after an initial placement is made, a token stays
        // at the port's periphery, which isn't supposed to happen.  This causes 'getAdjacentLocationFromTokenLocation'
        // to return an invalid location in my original implementation of the method.

        return new IntermediatePlacement(tile, token, location.getKey(), location.getValue());
    }

    /**
     * The x, y location directly next to the given TokenLocation.  Assumes tokenLocation is facing inward.
     * @param tokenLocation     the given TokenLocation
     * @return      the x, y, location directly next to the given TokenLocation
     */
    private Pair<Integer, Integer> getAdjacentLocationFromTokenLocation(Board.TokenLocation tokenLocation) {
        Direction direction = tokenLocation.getPort().getDirection();
        int x = tokenLocation.getX();
        int y = tokenLocation.getY();

        // If the location returned is invalid, the token should already have exited the board,
        // UNLESS I encode whether or not the previous placement for the given token was the initial placement based on
        // whether or not the port in tokenLocation is on the board's periphery.
        // This is a dirty, dirty hack that shouldn't be here, but advanceToken must be fixed before this method is
        // implemented correctly and Lucas has been working on that in his refactoring.
        // It's also late.
        // I'm also tired.

        if      (direction == Direction.NORTH)  y--;
        else if (direction == Direction.EAST)   x++;
        else if (direction == Direction.SOUTH)  y++;
        else if (direction == Direction.WEST)   x--;

        return new Pair<>(x, y);
    }

    /**
     * Calculates the next location counterclockwise to location based on the given bounds (exclusively).
     * @param current   the current location
     * @param bounds    the bounds of some grid
     * @return          the next location counterclockwise to the given location
     */
    private Pair<Integer, Integer> getNextClockwiseLocationForBounds(Pair<Integer, Integer> current, Pair<Integer, Integer> bounds) {
        int x = current.getKey();
        int y = current.getValue();
        int maxX = bounds.getKey() - 1;
        int maxY = bounds.getValue() - 1;

        // 4 cases, one for each side current could be on
        if (x >= 0 && x < maxX && y == 0)           x++;    // top
        else if (x == maxX && y >= 0 && y < maxY)   y++;    // right
        else if (x <= maxX && x > 0 && y == maxY)   x--;    // bottom
        else if (x == 0 && y <= maxY && y > 0)      y--;    // left
        else throw new IllegalArgumentException("Invalid current location given");

        return new Pair<>(x, y);
    }

    /**
     * Is the given location empty on the given board without any neighbors?
     * @param board     the board we are checking the location of
     * @param location  the location we are checking if we can place at the given location
     * @return
     */
    private boolean canPlaceInitialTileAtLocation(IPlayerBoard board, Pair<Integer, Integer> location, IRuleChecker checker) {
        // keeping code around because it might be helpful, but not calling it because I forgot we could just use the
        // rule checker here (yay!)
        final int x = location.getKey();
        final int y = location.getValue();
        final int maxX = 9;
        final int maxY = 9;

        boolean currentCheck    =                   board.get(x, y).isPresent();
        boolean xMinusOneCheck  = (x != 0)      &&  board.get(x - 1, y).isPresent();
        boolean xPlusOneCheck   = (x != maxX)   &&  board.get(x + 1, y).isPresent();
        boolean yMinusOneCheck  = (y != 0)      &&  board.get(x, y - 1).isPresent();
        boolean yPlusOneCheck   = (y != maxY)   &&  board.get(x, y + 1).isPresent();

        return !(currentCheck || xMinusOneCheck || xPlusOneCheck || yMinusOneCheck || yPlusOneCheck);
    }

    /**
     * Returns the "first" port at the given border location
     * @param location      the location we are checking
     * @return              the clockwise port (???)
     */
    private Port getFirstPortForBorderLocation(Pair<Integer, Integer> location) {
        final int x = location.getKey();
        final int y = location.getValue();
        final int maxX = 9;
        final int maxY = 9;

        Direction direction;
        if      (x >= 0 && x < maxX && y == 0)      direction = Direction.NORTH;    // top
        else if (x == maxX && y >= 0 && y < maxY)   direction = Direction.EAST;     // right
        else if (x <= maxX && x > 0 && y == maxY)   direction = Direction.SOUTH;    // bottom
        else if (x == 0 && y <= maxY && y > 0)      direction = Direction.WEST;     // left
        else throw new IllegalArgumentException("Location must be on border of board.");

        return new PortImpl(direction, 0);
    }
}
