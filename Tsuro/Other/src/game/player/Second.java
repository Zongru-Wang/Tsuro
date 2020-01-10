package game.player;

import game.board.*;
import game.common.Direction;
import game.common.PortPosition;
import game.common.Position;
import game.common.PositionUtil;
import game.rulechecker.RuleChecker;
import game.tile.Port;
import game.tile.Tile;

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
    public InitialPlacement generateInitialPlacement(Board board, List<Tile> availableTiles, Token token, RuleChecker checker) {
        Iterator<PortPosition> iterator = this.counterClockwisePortPosition();
        while (iterator.hasNext()) {
            PortPosition portPosition = iterator.next();
            InitialPlacement initialPlacement = InitialPlacement.of(token, availableTiles.get(2), portPosition.getPort(), portPosition.getPosition());
            if (checker.checkInitialPlacement((Board) board, initialPlacement, availableTiles)) {
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
    public IntermediatePlacement generateIntermediatePlacement(Board board, List<Tile> availableTiles, Token token, RuleChecker checker) {
        Optional<TokenStatus> tokenStatus = board.getPortPosition(token);
        if (!tokenStatus.isPresent() || tokenStatus.get().didExit()) {
            throw new IllegalArgumentException();
        }
        PortPosition portPosition = tokenStatus.get().tokenPosition();
        Position newPosition = PositionUtil.neighborAt(portPosition.getPosition(), portPosition.getPort().getDirection());
        for (Tile tile: availableTiles) {
            for (int degree = 0; degree < 360; degree += 90) {
                IntermediatePlacement intermediatePlacement = IntermediatePlacement.of(tile.rotate(degree), newPosition);
                if (checker.checkIntermediatePlacement((Board) board, token, intermediatePlacement, availableTiles)) {
                    return intermediatePlacement;
                }
            }
        }
        return IntermediatePlacement.of(availableTiles.get(1), newPosition);
    }

    private Iterator<PortPosition> counterClockwisePortPosition() {
        return new Iterator<PortPosition>() {
            private Direction direction = Direction.WEST;
            private int sideIndex = 1;
            private int sideProgress = 0;
            @Override
            public boolean hasNext() {
                return !(direction == Direction.NORTH && sideIndex == 0 && sideProgress == 9);
            }

            @Override
            public PortPosition next() {
                Port resultPort = Port.of(direction, sideIndex);
                Position resultPosition = null;
                sideIndex = (sideIndex + 1) % 2;
                switch (direction) {
                    case WEST:
                        resultPosition = Position.of(0, sideProgress);
                        if (sideProgress == 9) {
                            direction = Direction.SOUTH;
                        }
                        break;
                    case SOUTH:
                        resultPosition = Position.of(sideProgress, 9);
                        if (sideProgress == 9) {
                            direction = Direction.EAST;
                        }
                        break;
                    case EAST:
                        resultPosition = Position.of(9, 9 - sideProgress);
                        if (sideProgress == 9) {
                            direction = Direction.EAST;
                        }
                        break;
                    case NORTH:
                        resultPosition = Position.of(9 - sideProgress, 0);
                        if (sideProgress == 9) {
                            throw new IllegalStateException();
                        }
                        break;
                    default:
                        throw new RuntimeException();
                }
                sideProgress = (sideProgress + 1) % 10;
                return PortPosition.of(resultPort, resultPosition);
            }
        };
    }
}
