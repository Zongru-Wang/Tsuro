package game.observer;

import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.common.Position;
import game.observer.image.VectorImage;
import game.tile.Tile;
import javafx.util.Pair;

import java.util.List;


public class ObserverImpl implements Observer<VectorImage>{
    private Board board;
    private Pair<Tile, Position> requested;
    private List<Tile> choices;
    private Token turn;

    @Override
    public void update(Board board, IntermediatePlacement requested, List<Tile> choices, Token turn) {
        this.board = board;
        this.requested = new Pair<>(requested.getTile(), requested.getPosition());
        this.choices = choices;
        this.turn = turn;
    }

    @Override
    public void update(Board board, InitialPlacement requested, List<Tile> choices, Token turn) {
        this.board = board;
        this.requested = new Pair<>(requested.getTile(), requested.getPosition());
        this.choices = choices;
        this.turn = turn;
    }

    @Override
    public void update(Board board) {
        this.board = board;
        this.requested = null;
        this.choices = null;
        this.turn = null;
    }

    @Override
    public VectorImage render() {
        int boardLength = 1000;
        VectorImage result = VectorImage.empty((int)(boardLength * 1.5), (int)(boardLength * 1.2));
        if (board != null && requested != null) {
            result = result.embed(VectorImage.board(board, boardLength, requested.getValue()), boardLength * 4 / 10, boardLength / 10);
        } else if (board != null) {
            result = result.embed(VectorImage.board(board, boardLength), boardLength * 4 / 10, boardLength / 10);
        }
        int y = boardLength / 10;
        if (choices != null) {
            for (Tile choice: choices) {
                result = result.embed(VectorImage.tile(choice, boardLength / 10), boardLength / 10, y);
                y += boardLength / 5;
            }
        }
        if (turn != null) {
            result = result.embed(VectorImage.circle(boardLength /20, TokenColorUtil.toColor(this.turn)), boardLength / 10, y);
        }
        return result;
    }

}
