package game;

import java.util.List;

/**
 * Represents an entity that manages a game of Tsuro.
 */
public interface GameManager extends TurnHandler {
    /**
     * Adds a new Player to the game managed by this GameManager.
     * @param player    the Player to be added to this GameManager's Tsuro game
     * @throws IllegalStateException    if too many players have already been added to this game
     * @throws IllegalArgumentException if the added player already belongs to another game (if the player's token has
     *                                  been set)
     */
    void addPlayer(Player player) throws IllegalStateException, IllegalArgumentException;

    /**
     * Every Player playing the game of Tsuro managed by this GameManager.
     * @return      a List of Players playing the game of Tsuro managed by this GameManager
     */
    List<Player> getPlayers();

    /**
     * Starts the game managed by this GameManager.
     * @throws IllegalStateException    if not enough players have joined the game
     */
    void startGame() throws IllegalStateException;
}

package game;

import game.board.*;
import game.rulechecker.IRuleChecker;
import game.rulechecker.RuleChecker;
import game.tile.Tile;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class GameManagerImpl implements GameManager {
    private static final String TILE_LOCATION = "/tile.json";
    private static final int MAX_PLAYERS = 5;
    private static final List<Token> TOKENS_TO_GIVE_AWAY = Arrays.asList(Token.BLUE, Token.GREEN, Token.RED, Token.BLACK, Token.WHITE);
    private static final int TILES_PER_INITIAL_TURN = 3;
    private static final int TILES_PER_INTERMEDIATE_TURN = 2;

    private List<Token> tokensToGiveAway;
    private List<Player> players;
    private List<Player> playingPlayers;
    private List<Tile> allTiles;
    private int nextTileIndex;
    private IRefereeBoard board;
    private RuleChecker ruleChecker;

    private List<Player> previousLosers;
    private List<Player> losers;
    private Optional<Player> winner;
    private boolean isRunning;

    public GameManagerImpl() {
        this.tokensToGiveAway = new ArrayList<>(TOKENS_TO_GIVE_AWAY);
        this.playingPlayers = new ArrayList<>();
        this.players = new ArrayList<>();
        this.allTiles = this.loadTiles();
        this.nextTileIndex = 0;
        this.board = new Board();
        this.ruleChecker = new RuleChecker();

        this.previousLosers = new ArrayList<>();
        this.losers = new ArrayList<>();
        this.winner = Optional.empty();
        this.isRunning = false;
    }

    private List<Tile> loadTiles() {
        try {
            return new ArrayList<>(Tile.loadAll(TILE_LOCATION));
        } catch (FileNotFoundException e) {
            return new ArrayList<>(Tile.computeAll());
        }
    }

    @Override
    public void addPlayer(Player player) throws IllegalStateException, IllegalArgumentException {
        if (this.players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("There cannot be more than five players playing a game");
        } else if (player.getToken().isPresent()) {
            throw new IllegalArgumentException("Cannot add Player that already has a Token");
        }

        player.giveToken(this.tokensToGiveAway.remove(this.tokensToGiveAway.size() - 1));
        this.players.add(player);
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public void startGame() throws IllegalStateException {
        if (this.players.isEmpty() || this.isRunning) {
            throw new IllegalStateException();
        }

        this.isRunning = true;
        this.playingPlayers = new ArrayList<>(this.players);
        this.playingPlayers.sort((p1, p2) -> (int) (p2.getAge() - p1.getAge()));
        this.playGame();
    }

    private void playGame() {
        // Assumes there are enough/not too many players to play
        this.playInitialTurns();
        while (this.isRunning) {
            // WARNING -- might cause infinite loop if checkWinnersAndLosers is not handled correctly
            this.playIntermediateTurns();
            this.checkWinnersAndLosers();
            this.isRunning = !(this.foundAWinner() || this.everyoneLost());
        }
        this.endGame();
    }

    private boolean foundAWinner() {
        return this.losers.size() == this.players.size() - 1;
    }

    private boolean everyoneLost() {
        return this.losers.size() >= this.players.size();
    }

    private void checkWinnersAndLosers() {
        if (this.losers.size() != this.previousLosers.size()) {
            // then more people have lost and we need to notify them
            // calculating differences
            List<Player> difference = this.losers.stream()
                    .filter(player -> !this.previousLosers.contains(player))
                    .collect(Collectors.toList());
            for (Player newLoser : difference) {
                this.board.removeToken(newLoser.getToken().get());
                this.playingPlayers.remove(newLoser);
                newLoser.notifyLoss(this.board);
            }
            // previousLosers becomes copy of losers
            this.previousLosers = new ArrayList<>();
        }
    }

    private void endGame() {
        if (this.foundAWinner()) {
            // then there is one player left and that player has won!
            // let's find them...
            this.winner = Optional.of(this.players.stream()
                    .filter(player -> !this.losers.contains(player))
                    .collect(Collectors.toList()).get(0));
            // ...and congratulate them!
            this.winner.get().notifyWin(this.board);
        }

        // notify all users that the game has ended
        for (Player player : this.players) {
            player.notifyEndOfGame(this.board);
        }
    }

    private void playInitialTurns() {
        // For now, if a player messes up their turn they can't take it again
        for (Player player : this.playingPlayers) {
            List<Tile> availableTiles = this.generateAvailableTiles(TILES_PER_INITIAL_TURN);
            IInitialPlacement placement = player.notifyInitialTurn(this.board, availableTiles, this);

            if (!this.ruleChecker.checkInitialPlacement(this.board, placement, availableTiles)) {
                // player messed up -- player loses for now
                this.losers.add(player);
                continue;
            }

            this.makeInitialPlacement(placement);
        }
//        this.advanceAllTokens();
        // advanceAllTokens won't do anything here
    }

    private void makeInitialPlacement(IInitialPlacement placement) {
        this.board.place(placement.getTile(), placement.getX(), placement.getY());
        this.board.placeToken(placement.getToken(), placement.getPort(), placement.getX(), placement.getY());
    }

    private void playIntermediateTurns() {
        for (Player player : this.playingPlayers) {
            List<Tile> availableTiles = this.generateAvailableTiles(TILES_PER_INTERMEDIATE_TURN);
            IIntermediatePlacement placement = player.notifyIntermediateTurn(this.board, availableTiles, this);

            // why didn't we create an IIntermediateTile class originally?
            boolean successfulPlacement = this.ruleChecker.checkIntermediatePlacement(
                    this.board, placement.getToken(),
                    placement.getTile(),
                    placement.getX(),
                    placement.getY(),
                    availableTiles);
            if (!successfulPlacement) {
                // player messed up -- player loses for now
                this.losers.add(player);
                continue;
            }

            this.board.place(placement.getTile(), placement.getX(), placement.getY());
            // this should be handled in board
            this.advanceAllTokens();
        }
    }

    private void advanceAllTokens() {
        // HACK -- advancing all tokens for every placement, which should be done in board itself
        for (Player player : this.playingPlayers) {
            Board.AdvancementResult result = this.board.advanceToken(player.getToken().get());
            if (result.didExit()) {
                if (!this.losers.contains(player)) {
                    // then they haven't lost on this round yet (no double counting!!!)
                    this.losers.add(player);
                }
            }
            // TODO -- might need to handle collision case
        }
    }

    private List<Tile> generateAvailableTiles(int nTiles) {
        List<Tile> tiles = new ArrayList<>(nTiles);
        for (int i = 0; i < nTiles; i++) {
            tiles.add(this.allTiles.get(this.nextTileIndex));
            // cycle as necessary
            this.nextTileIndex = (this.nextTileIndex + 1) % this.allTiles.size();
        }
        return tiles;
    }

    @Override
    public boolean takeInitialTurn(Player player, IInitialPlacement placement) {
//        if (this.ruleChecker.checkInitialPlacement(this.board, placement, this.currentAvailableTiles)) {
//
//        }
        return false;
    }

    @Override
    public boolean takeIntermediateTurn(Player player, IIntermediatePlacement placement) {
        return false;
    }

    @Override
    public IRuleChecker getRuleChecker() {
        return this.ruleChecker;
    }
}
