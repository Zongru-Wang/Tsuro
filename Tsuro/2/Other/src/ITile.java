import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ITile {
    ITile rotate();
    Port connectedPort(Port port);
    public static class Port {
        private Direction direction;
        private Index index;
        public Port(Direction direction, Index index) {
            if (direction == null || index == null) {
                throw new RuntimeException();
            }
            this.direction = direction;
            this.index = index;
        }
        public Direction getDirection() {
            return this.direction;
        }
        public Index getIndex() {
            return this.index;
        }
        public Port rotate() {
            return new Port(getRotated(), this.index);
        }
        private Direction getRotated() {
            if(this.direction == Direction.NORTH) {
                return Direction.EAST;
            } else if (this.direction == Direction.EAST) {
                return Direction.SOUTH;
            } else if (this.direction == Direction.SOUTH) {
                return Direction.WEST;
            } else if (this.direction == Direction.WEST) {
                return Direction.NORTH;
            } else {
                throw new RuntimeException();
            }
        }
        public boolean equals(Object obj) {
            if (obj instanceof Port) {
                return ((Port)obj).hashCode() == this.hashCode();
            }
            return false;
        }
        public int hashCode() {
            return this.direction.getValue() * 2 + this.index.getValue();
        }
        protected static List<List<Port>> permutations() {
            return generatePermutations(allPort());
        }

        private static List<List<Port>> generatePermutations(List<Port> current) {
            List<List<Port>> result = new ArrayList<List<Port>>();
            if (current.size() == 1) {
                result.add(current);
                return result;
            }
            for (int i = 0; i < current.size(); i++) {
                List<Port> withoutFirst = new ArrayList<Port>(current);
                Port firstPort = withoutFirst.remove(i);
                List<List<Port>> permutationsWithoutFirs = generatePermutations(withoutFirst);
                for (List<Port> permutation : permutationsWithoutFirs) {
                    List<Port> temp = new ArrayList<Port>(permutation);
                    temp.add(0, firstPort);
                    result.add(temp);
                }
            }
            return result;
        }
        static List<Port> allPort() {
            return new ArrayList<>(Arrays.asList(new Port(Direction.NORTH, Index.ONE),
                    new Port(Direction.NORTH, Index.TWO),
                    new Port(Direction.EAST, Index.ONE),
                    new Port(Direction.EAST, Index.TWO),
                    new Port(Direction.SOUTH, Index.ONE),
                    new Port(Direction.SOUTH, Index.TWO),
                    new Port(Direction.WEST, Index.ONE),
                    new Port(Direction.WEST, Index.TWO)));
        }
        public static enum Direction {
            NORTH(0), EAST(1), SOUTH(2), WEST(3);
            private final int value;
            private Direction(int value) {
                this.value = value;
            }
            public int getValue() {
                return value;
            }
        }
        public static enum Index {
            ONE(0), TWO(1);
            private final int value;
            private Index(int value) {
                this.value = value;
            }
            public int getValue() {
                return value;
            }
        }
    }
}
