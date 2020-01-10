package game.player;

import game.board.*;
import game.common.Direction;
import game.common.PositionUtil;
import game.rulechecker.RuleChecker;
import game.tile.Port;
import game.tile.Tile;
import javafx.util.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Second implements Strategy {
    /**
     * Generates an IInitialPlacement using the given availableTiles and board.
     *
     * @param board          the board on which to make the IInitialPlacement
     * @param availableTiles the Tiles this Strategy can use to generate their placement
     * @param token          the Token to be placed in the returned IInitialPlacement
     * @param checker
     * @return some IInitialPlacement
     */
    @Override
    public InitialPlacement generateInitialPlacement(PlayerBoard board, List<Tile> availableTiles, Token token, RuleChecker checker) {
        Iterator<Pair<Port, Pair<Integer, Integer>>> iterator = this.counterClockwisePortPosition();
        while (iterator.hasNext()) {
            Pair<Port, Pair<Integer, Integer>> portPosition = iterator.next();
            InitialPlacement initialPlacement = InitialPlacement.of(token, availableTiles.get(2), portPosition.getKey(), portPosition.getValue());
            if (checker.checkInitialPlacement((RefereeBoard) board, initialPlacement, availableTiles)) {
                return initialPlacement;
            }
        }
        throw new RuntimeException();
    }

    /**
     * Generates an IIntermediatePlacement using the given availableTiles and board.
     *
     * @param board          the board on which to make the IIntermediatePlacement
     * @param availableTiles the Tiles this Strategy can use to generate their placement
     * @param token          the Token to be placed in the returned IIntermediatePlacement
     * @param checker
     * @return some IIntermediatePlacement
     */
    @Override
    public IntermediatePlacement generateIntermediatePlacement(PlayerBoard board, List<Tile> availableTiles, Token token, RuleChecker checker) {
        Optional<TokenStatus> tokenStatus = board.getPortPosition(token);
        if (!tokenStatus.isPresent() || tokenStatus.get().didExit()) {
            throw new IllegalArgumentException();
        }
        Pair<Port, Pair<Integer, Integer>> portPosition = tokenStatus.get().tokenPosition();
        Pair<Integer, Integer> newPosition = PositionUtil.neighborAt(portPosition.getValue(), portPosition.getKey().getDirection());
        for (Tile tile: availableTiles) {
            for (int degree = 0; degree < 360; degree += 90) {
                IntermediatePlacement intermediatePlacement = IntermediatePlacement.of(tile.rotate(degree), newPosition);
                if (checker.checkIntermediatePlacement((RefereeBoard)board, token, intermediatePlacement, availableTiles)) {
                    return intermediatePlacement;
                }
            }
        }
        return IntermediatePlacement.of(availableTiles.get(1), newPosition);
    }

    private Iterator<Pair<Port, Pair<Integer, Integer>>> counterClockwisePortPosition() {
        return new Iterator<Pair<Port, Pair<Integer, Integer>>>() {
            private Direction direction = Direction.WEST;
            private int sideIndex = 1;
            private int sideProgress = 0;
            @Override
            public boolean hasNext() {
                return !(direction == Direction.NORTH && sideIndex == 0 && sideProgress == 9);
            }

            @Override
            public Pair<Port, Pair<Integer, Integer>> next() {
                Port resultPort = Port.of(direction, sideIndex);
                Pair<Integer, Integer> resultPosition = null;
                sideIndex = (sideIndex + 1) % 2;
                switch (direction) {
                    case WEST:
                        resultPosition = new Pair<>(0, sideProgress);
                        if (sideProgress == 9) {
                            direction = Direction.SOUTH;
                        }
                        break;
                    case SOUTH:
                        resultPosition = new Pair<>(sideProgress, 9);
                        if (sideProgress == 9) {
                            direction = Direction.EAST;
                        }
                        break;
                    case EAST:
                        resultPosition = new Pair<>(9, 9 - sideProgress);
                        if (sideProgress == 9) {
                            direction = Direction.EAST;
                        }
                        break;
                    case NORTH:
                        resultPosition = new Pair<>(9 - sideProgress, 0);
                        if (sideProgress == 9) {
                            throw new IllegalStateException();
                        }
                        break;
                    default:
                        throw new RuntimeException();
                }
                sideProgress = (sideProgress + 1) % 10;
                return new Pair<>(resultPort, resultPosition);
            }
        };
    }
}
