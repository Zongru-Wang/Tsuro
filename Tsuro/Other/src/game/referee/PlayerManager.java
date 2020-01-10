package game.referee;

import game.board.Board;
import game.board.Token;
import game.board.TokenStatus;
import game.player.Player;

import java.util.*;

/**
 * Represents an entity managing a set of Tsuro players.
 * Determines wins, losses, ejections, and assigns tokens.
 */
public class PlayerManager {
    private static final int MAX_PLAYERS = 5;

    private List<Player> players;
    private List<Player> playingPlayers;
    private List<List<Player>> losersByRound;
    private List<Player> ejected;
    private TokenManager tokenManager;

    PlayerManager() {
        this.players = new ArrayList<>();
        this.playingPlayers = new ArrayList<>();
        this.losersByRound = new ArrayList<>();
        this.ejected = new ArrayList<>();
        this.tokenManager = new TokenManager(Token.WHITE, Token.BLACK, Token.RED, Token.GREEN, Token.BLUE);
    }

    /**
     * Adds the given Player to the list of Player's managed by this PlayerManager.
     *
     * @param player                        the new Player to be managed by this PlayerManager
     * @throws IllegalStateException        if there are already MAX_PLAYERS handled by this PlayerManager
     * @throws IllegalArgumentException     if the given Player has already been assigned a token
     */
    public void addPlayer(Player player) throws IllegalStateException, IllegalArgumentException {
        if (this.players.size() >= MAX_PLAYERS) {
            throw new IllegalStateException("There cannot be more than " + MAX_PLAYERS + " players playing a game");
        } else if (player.getToken().isPresent()) {
            throw new IllegalArgumentException("Cannot add Player that already has a Token");
        }

        player.giveToken(this.tokenManager.getNextAvailableToken());
        this.players.add(player);
        this.playingPlayers.add(player);
        this.playingPlayers.sort((p1, p2) -> (int) (p2.getAge() - p1.getAge()));
    }

    /**
     * The Player's being managed by this PlayerManager.
     * @return      a list of Players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(this.players);
    }

    /**
     * A copy of the list of players currently playing this game.
     * @return
     */
    public ArrayList<Player> getPlayingPlayers() {
        return new ArrayList<>(this.playingPlayers);
    }

    /**
     * Are there enough Players to start playing a Tsuro game?
     * @return      true if there is at least 1 Player, false otherwise
     */
    public boolean hasEnoughPlayersToStartGame() {
        return this.players.size() > 0;
    }

    /**
     * Have a winner been determined?
     * @return      true if there are fewer than 2 Players playing, false otherwise
     */
    public boolean hasDeterminedWinner() {
        return this.playingPlayers.size() < 2;
    }

    /**
     * A list of the Players who weren't ejected over the course of a game.
     * @return      all the Players who weren't ejected from the game in lists reflecting the order of rounds in which
     *              the contained Players lost
     */
    public List<List<Player>> getWinners() {
        List<List<Player>> winners = new ArrayList<>(this.losersByRound);
        if (this.playingPlayers.size() == 1) {
            winners.add(Arrays.asList(this.playingPlayers.get(0)));
        }
        Collections.reverse(winners);

        return winners;
    }

    /**
     * A list of all the player's who have been ejected from the PlayerManager.
     * @return      all the Players who have been ejected
     */
    public List<Player> getEjected() {
        return new ArrayList<>(this.ejected);
    }

    /**
     * Ejects the given Player from the PlayerManager.
     * @param player                        the Player to be ejected
     * @throws IllegalArgumentException     if the given Player is not playing the game
     */
    public void ejectPlayer(Player player) throws IllegalArgumentException {
        if (!this.playingPlayers.contains(player)) {
            throw new IllegalArgumentException("Cannot eject a Player who is not playing the game");
        }
        this.ejected.add(player);
    }

    /**
     * Removes the players who lost or were ejected during this round and does any other per-round cleanup.
     * @param board     the board the Players are playing on
     */
    public void endRound(Board board) {
        this.removeEjected(board);
        this.removeLosers(board);
    }

    /**
     * Notifies all the player's that the game of Tsuro has begun.
     */
    public void startGame() {
        for (Player player : this.players) {
            player.notifyStarted();
        }
    }

    /**
     * Notifies all players that the game has ended, notifies winners, and revokes tokens.
     * @param board     the board the Players are playing on
     */
    public void endGame(Board board) {
        // this.losersByRound is empty if and only if:
        //  - no rounds have been played
        //      <=> there was only ever 1 player
        //      <=> this.playingPlayers still has 1 player in it...
        if (this.playingPlayers.isEmpty()) {
            // ...therefore, I don't need the condition that !this.losersByRound.isEmpty() for this line.
            List<Player> lastPlayersStanding = this.losersByRound.get(this.losersByRound.size() - 1);
            for (Player player : lastPlayersStanding) {
                player.notifyWin(board);
            }
        } else {
            this.playingPlayers.get(0).notifyWin(board);
        }
        this.revokeAllTokens();
    }

    private void revokeAllTokens() {
        for (Player player : this.players) {
            player.revokeToken();
        }
    }

    private void removeEjected(Board board) {
        for (Player player : this.ejected) {
            player.notifyLoss(board);
            this.playingPlayers.remove(player);
        }
    }

    private void removeLosers(Board board) {
        List<Player> losersThisRound = this.determineLosersThisRound(board);
        if (!losersThisRound.isEmpty()) {
            this.losersByRound.add(losersThisRound);
            this.playingPlayers.removeAll(losersThisRound);
        }
    }

    private List<Player> determineLosersThisRound(Board board) {
        List<Player> losersThisRound = new ArrayList<>();
        for (Player player : this.playingPlayers) {
            Optional<TokenStatus> status = board.getPortPosition(player.getToken().get());
            if (status.get().didExit()) {
                player.notifyLoss(board);
                losersThisRound.add(player);
            }
        }
        return losersThisRound;
    }
}
