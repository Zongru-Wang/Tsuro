package game.referee;

import game.board.*;
import game.observer.Observer;
import game.player.Player;
import game.rulechecker.RuleChecker;
import game.tile.Tile;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
    private static final int TILES_PER_INITIAL_TURN = 3;
    private static final int TILES_PER_INTERMEDIATE_TURN = 2;

    private TileManager tileManager;
    private PlayerManager playerManager;

    private boolean isRunning;
    private Board board;
    private RuleChecker ruleChecker;

    private List<Observer> observers;

    public GameManager() {
        this.tileManager = new TileManager();
        this.playerManager = new PlayerManager();

        this.isRunning = false;
        this.board = Board.empty();
        this.ruleChecker = new RuleChecker();

        this.observers = new ArrayList<>();
    }

    /**
     * A 2d list of the game's winners in descending order, with a list of the first place winners as the first element,
     * the second place winners as the second element, etc.
     * @return      a 2d list of the game's winners
     */
    public List<List<String>> getWinnerIdentifiers() {
        return this.playerManager.getWinners().stream()
                .map(playerList -> playerList.stream()
                        .map(player -> player.getIdentifier())
                        .sorted()
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    /**
     * A list of all the player's who requested illegal moves during the game.
     * @return      the player's who requests illegal moves during the game
     */
    public List<String> getEjectedIdentifiers() {
        return this.playerManager.getEjected().stream()
                .map(player -> player.getIdentifier())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Adds a new Player to the game managed by this GameManager.
     * @param player    the Player to be added to this GameManager's Tsuro game
     * @throws IllegalStateException    if too many players have already been added to this game
     * @throws IllegalArgumentException if the added player already belongs to another game (if the player's token has
     *                                  been set)
     */
    public void addPlayer(Player player) throws IllegalStateException, IllegalArgumentException {
        this.playerManager.addPlayer(player);
    }

    /**
     * Adds a new Observer to the game managed by this GameManager.
     * @param observer      the new Observer of this game
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Every Player playing the game of Tsuro managed by this GameManager.
     * @return      a List of Players playing the game of Tsuro managed by this GameManager
     */
    public List<Player> getPlayers() {
        return this.playerManager.getPlayers();
    }

    /**
     * Starts the game managed by this GameManager.
     * @throws IllegalStateException    if not enough players have joined the game
     */
    public void startGame() throws IllegalStateException {
        if (!this.playerManager.hasEnoughPlayersToStartGame() || this.isRunning) {
            throw new IllegalStateException();
        }

        this.isRunning = true;
        this.playerManager.startGame();
        this.playGame();
    }

    private void playGame() {
        this.playInitialTurns();
        while (this.isRunning) {
            this.playRound();
        }
        this.endGame();
    }

    private void endGame() {
        this.playerManager.endGame(this.board);
        // notify all users that the game has ended
        List<Player> allPlayers = playerManager.getPlayers();
        for (Player player : allPlayers) {
            // should we also be notifying observers?
            player.notifyEndOfGame(this.getWinnerIdentifiers(), this.getEjectedIdentifiers(), this.board);
        }
    }

    private void playInitialTurns() {
        List<Player> playingPlayers = this.playerManager.getPlayingPlayers();
        for (Player player : playingPlayers) {
            this.playInitialTurn(player);
            this.updateObservers();
        }
    }

    private void playInitialTurn(Player player) {
        List<Tile> availableTiles = this.tileManager.getNextAvailableTiles(TILES_PER_INITIAL_TURN);
        InitialPlacement placement = player.notifyInitialTurn(this.board, availableTiles, this.ruleChecker);

        if (!this.ruleChecker.checkInitialPlacement(this.board, placement, availableTiles)) {
            // player messed up -- player loses for now
            this.playerManager.ejectPlayer(player);
        } else {
            this.board = this.board.placeInitial(placement);
        }
    }

    private void playRound() {
        this.playIntermediateTurns();
        this.playerManager.endRound(this.board);
        this.isRunning = !this.gameIsOver();
    }

    private void playIntermediateTurns() {
        List<Player> playingPlayers = this.playerManager.getPlayingPlayers();
        for (Player player : playingPlayers) {
            this.playIntermediateTurn(player);
            this.updateObservers();
        }
    }

    private void playIntermediateTurn(Player player) {
        // Able to call getPortPosition(...).get() because playIntermediateTurn(player) is only called if player has
        // already taken an initialTurn.
        if (this.board.getPortPosition(player.getToken().get()).get().didExit()) {
            // If another player sent this player off the board.
            return;
        }

        List<Tile> availableTiles = this.tileManager.getNextAvailableTiles(TILES_PER_INTERMEDIATE_TURN);
        IntermediatePlacement placement = player.notifyIntermediateTurn(this.board, availableTiles, this.ruleChecker);

        if (!this.ruleChecker.checkIntermediatePlacement(this.board, player.getToken().get(), placement, availableTiles)) {
            this.playerManager.ejectPlayer(player);
        } else {
            this.board = this.board.place(placement);
        }
    }

    private boolean gameIsOver() {
        return this.playerManager.hasDeterminedWinner();
    }

    private void updateObservers() {
        for (Observer observer : this.observers) {
            observer.update(this.board);
        }
    }
}