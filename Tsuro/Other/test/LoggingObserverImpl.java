import game.board.Board;
import game.board.InitialPlacement;
import game.board.IntermediatePlacement;
import game.board.Token;
import game.observer.ObserverImpl;
import game.observer.image.VectorImage;
import game.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class LoggingObserverImpl extends ObserverImpl implements LoggingObserver<VectorImage> {
    private final List<VectorImage> log;

    LoggingObserverImpl() {
        this.log = new ArrayList<>();
    }

    @Override
    public void update(Board board) {
        super.update(board);
        this.log.add(this.render());
    }

    @Override
    public void update(Board board, InitialPlacement requested, List<Tile> choices, Token turn) {
        super.update(board, requested, choices, turn);
        this.log.add(this.render());
    }

    @Override
    public void update(Board board, IntermediatePlacement requested, List<Tile> choices, Token turn) {
        super.update(board, requested, choices, turn);
        this.log.add(this.render());
    }

    @Override
    public VectorImage getLogAt(int index) {
        return this.log.get(index);
    }

    @Override
    public List<VectorImage> getLogs() {
        return new ArrayList<>(log);
    }
}
