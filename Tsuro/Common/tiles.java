import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

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

public class BaseTile implements ITile {
    private Map<Port, Port> connections;
    private BaseTile(Map<Port, Port> connections) {
        if (connections == null || connections.size() != 8) {
            throw new RuntimeException();
        }
        this.connections = new HashMap<>(connections);
    }
    @Override
    public ITile rotate() {
        Map<Port, Port> newConnections = new HashMap<Port, Port>();
        for (Map.Entry<Port, Port> entry: this.connections.entrySet()) {
            Port newFrom = entry.getKey().rotate();
            Port newTo = entry.getValue().rotate();
            newConnections.put(newFrom, newTo);
        }
        return new BaseTile(newConnections);
    }

    @Override
    public Port connectedPort(Port port) {
        return this.connections.get(port);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ITile) {
            return ((ITile)obj).hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        ITile current = this;
        List<Integer> hashes = new ArrayList<>();
        List<Port> allPort = Port.allPort();
        for (int i = 0; i < 4; i++) {
            int currentHash = 0;
            for (int j = 0; j < 8; j++) {
                Port currentConnectedPort = current.connectedPort(allPort.get(j));
                currentHash += Math.pow(8, j) * currentConnectedPort.hashCode();
            }
            hashes.add(currentHash);
            current = current.rotate();
        }
        return Collections.min(hashes);
    }

    public String toASCIIArt() {
        return new BoardRenderer(10, this).render();
    }

    public static class Builder {
        private Map<Port, Port> connections;
        public Builder() {
            this.connections = new HashMap<Port, Port>();
        }
        public void addConnection(Port from, Port to) throws IllegalArgumentException, IllegalStateException {
            if (from == null || to == null) {
                throw new IllegalArgumentException();
            }
            if (this.connections.containsKey(from) || this.connections.containsValue(to)) {
                throw new IllegalStateException();
            }
            this.connections.put(from, to);
            this.connections.put(to, from);
        }
        public BaseTile build() {
            if (this.connections.size() != 8) {
                throw new IllegalStateException();
            }
            return new BaseTile(new HashMap<Port, Port>(this.connections));
        }
    }

    private static List<BaseTile> allPossibleConfiguration = null;
    private static List<BaseTile> computeAllConfiguration () {
        Set<BaseTile> result = new HashSet<BaseTile>();
        for (List<Port> permutation: Port.permutations()) {
            Builder builder = new BaseTile.Builder();
            builder.addConnection(permutation.get(0), permutation.get(1));
            builder.addConnection(permutation.get(2), permutation.get(3));
            builder.addConnection(permutation.get(4), permutation.get(5));
            builder.addConnection(permutation.get(6), permutation.get(7));
            BaseTile tile = builder.build();
            result.add(tile);
        }
        return new ArrayList<>(result);
    }
    public static BaseTile fromInt(int i) {
        if (i < 0 || i > 34) {
            throw new IllegalArgumentException();
        }
        if (allPossibleConfiguration == null) {
            allPossibleConfiguration = computeAllConfiguration();
        }
        return allPossibleConfiguration.get(i);
    }

    public String asJson() {
        String json = new Gson().toJson(this);
        return json;
    }

}


public class BoardRenderer {
    private TraversableBoard board;
    public BoardRenderer(int size, ITile tile) {
        this.board = new TraversableBoard(4 + 3 * size, 4 + 3 * size);
        Position leftTop = Position.from(0, 0);
        Position rightTop = Position.from(3 * size + 3, 0);
        Position leftBottom = Position.from(0, 3 * size + 3);
        Position rightBottom = Position.from(3 * size + 3, 3 * size + 3);
        List<Position> top = this.board.traverse(leftTop, rightTop);
        List<Position> right = this.board.traverse(rightTop, rightBottom);
        List<Position> bottom = this.board.traverse(rightBottom, leftBottom);
        List<Position> left = this.board.traverse(leftBottom, leftTop);
        this.board.override(top, '─');
        this.board.override(right, '│');
        this.board.override(bottom, '─');
        this.board.override(left, '│');
        this.board.override(leftTop, '┌');
        this.board.override(rightTop, '┐');
        this.board.override(leftBottom, '└');
        this.board.override(rightBottom, '┘');
        Position northOne = Position.from(size + 1, 0);
        Position northTwo = Position.from(2 * size + 2, 0);
        Position eastOne = Position.from(3 * size + 3, size + 1);
        Position eastTwo = Position.from(3 * size + 3, 2 * size + 2);
        Position southOne = Position.from(size + 1, 3 * size + 3);
        Position southTwo = Position.from(2 * size + 2, 3 * size + 3);
        Position westOne = Position.from(0, size + 1);
        Position westTwo = Position.from(0, 2 * size + 2);
        this.board.override(northOne, 'o');
        this.board.override(northTwo, 'o');
        this.board.override(eastOne, 'o');
        this.board.override(eastTwo, 'o');
        this.board.override(southOne, 'o');
        this.board.override(southTwo, 'o');
        this.board.override(westOne, 'o');
        this.board.override(westTwo, 'o');

        Set<ITile.Port> processed = new HashSet<>();
        Set<Position> illegals = new HashSet<>();
        Set<Position> unpreferred = new HashSet<>();
        illegals.addAll(top);
        illegals.addAll(right);
        illegals.addAll(left);
        illegals.addAll(bottom);
        for (ITile.Port port: ITile.Port.allPort()) {
            illegals.add(this.getPosition(port, size));
        }
        int i = 1;
        for (ITile.Port port: ITile.Port.allPort()) {
            if (processed.contains(port)) {
                continue;
            }
            ITile.Port toPort = tile.connectedPort(port);
            processed.add(port);
            processed.add(toPort);
            Position from = this.getPosition(port, size);
            Position to = this.getPosition(toPort, size);
            illegals.remove(this.getPosition(toPort, size));
            List<Position> path = this.board.traverse(from, to, illegals, new Comparator<List<Position>>() {
                @Override
                public int compare(List<Position> o1, List<Position> o2) {
                    int o1_cost = 0;
                    int o2_cost = 0;
                    Position prevPos = null;
                    for (Position pos: o1) {
                        if (unpreferred.contains(pos)) {
                            o1_cost += 100;
                        }
                        if (prevPos != null) {
                            int manhattan = Math.abs(prevPos.x - pos.x) + Math.abs(prevPos.y - pos.y);
                            if (manhattan == 1) {
                                o1_cost += 5;
                            } else {
                                o1_cost += 7;
                            }
                        }
                        prevPos = pos;
                    }
                    prevPos = null;
                    for (Position pos: o2) {
                        if (unpreferred.contains(pos)) {
                            o2_cost += 100;
                        }
                        if (prevPos != null) {
                            int manhattan = Math.abs(prevPos.x - pos.x) + Math.abs(prevPos.y - pos.y);
                            if (manhattan == 1) {
                                o2_cost += 5;
                            } else {
                                o2_cost += 7;
                            }
                        }
                        prevPos = pos;
                    }
                    return o1_cost - o2_cost;
                }
            });
            illegals.add(this.getPosition(toPort, size));
            unpreferred.addAll(path);
            this.board.override(path, String.valueOf(i).charAt(0));
            i++;
        }
        this.board.render();
    }

    private Position getPosition(ITile.Port port, int size) {
        if (port.getDirection() == ITile.Port.Direction.NORTH && port.getIndex() == ITile.Port.Index.ONE) {
            return Position.from(size + 1, 1);
        } else if (port.getDirection() == ITile.Port.Direction.NORTH && port.getIndex() == ITile.Port.Index.TWO) {
            return Position.from(2 * size + 2, 1);
        } else if (port.getDirection() == ITile.Port.Direction.EAST && port.getIndex() == ITile.Port.Index.ONE) {
            return Position.from(3 * size + 2, size + 1);
        } else if (port.getDirection() == ITile.Port.Direction.EAST && port.getIndex() == ITile.Port.Index.TWO) {
            return Position.from(3 * size + 2, 2 * size + 2);
        } else if (port.getDirection() == ITile.Port.Direction.SOUTH && port.getIndex() == ITile.Port.Index.ONE) {
            return Position.from(2 * size + 2, 3 * size + 2);
        } else if (port.getDirection() == ITile.Port.Direction.SOUTH && port.getIndex() == ITile.Port.Index.TWO) {
            return Position.from(size + 1, 3 * size + 2);
        } else if (port.getDirection() == ITile.Port.Direction.WEST && port.getIndex() == ITile.Port.Index.ONE) {
            return Position.from(1, 2 * size + 2);
        } else if (port.getDirection() == ITile.Port.Direction.WEST && port.getIndex() == ITile.Port.Index.TWO) {
            return Position.from(1, size + 1);
        } else {
            throw new RuntimeException();
        }
    }

    public String render() {
        return this.board.render();
    }
    private static class TraversableBoard {
        private char[][] board;
        private TraversableBoard(int xSize, int ySize) {
            this.board = new char[xSize][ySize];
            for (int i = 0; i<xSize; i++){
                for (int j = 0; j<ySize; j++) {
                    this.board[i][j] = ' ';
                }
            }
        }

        public void override(Position position, char substitute) {
            this.board[position.y][position.x] = substitute;
        }

        public void override(List<Position> positions, char substitute) {
            for (Position position: positions) {
                this.override(position, substitute);
            }
        }
        public List<Position> traverse(Position from, Position to) {
            return this.traverse(from, to, new HashSet<>());
        }
        public List<Position> traverse(Position from, Position to, Comparator<List<Position>> pathPriority) {
            return this.traverse(from, to, new HashSet<>(), pathPriority);
        }
        public List<Position> traverse(Position from, Position to, Set<Position> illegals) {
            return this.traverse(from, to, illegals, new Comparator<List<Position>>() {
                @Override
                public int compare(List<Position> o1, List<Position> o2) {
                    int o1_cost = 0;
                    int o2_cost = 0;
                    Position prevPos = null;
                    for (Position pos: o1) {
                        if (prevPos != null) {
                            int manhattan = Math.abs(prevPos.x - pos.x) + Math.abs(prevPos.y - pos.y);
                            if (manhattan == 1) {
                                o1_cost += 5;
                            } else {
                                o1_cost += 7;
                            }
                        }
                        prevPos = pos;
                    }
                    prevPos = null;
                    for (Position pos: o2) {
                        if (prevPos != null) {
                            int manhattan = Math.abs(prevPos.x - pos.x) + Math.abs(prevPos.y - pos.y);
                            if (manhattan == 1) {
                                o2_cost += 5;
                            } else {
                                o2_cost += 7;
                            }
                        }
                        prevPos = pos;
                    }
                    return o1_cost - o2_cost;
                }
            });
        }
        public List<Position> traverse(Position from, Position to, Set<Position> illegals,
                                       Comparator<List<Position>> pathPriority) {
            Set<Position> closed = new HashSet<>();
            Queue<List<Position>> fringe = new PriorityQueue<List<Position>>(pathPriority);
            List<Position> first = new ArrayList<Position>();
            first.add(from);
            fringe.add(first);
            while (true) {
                if (fringe.isEmpty()) {
                    throw new RuntimeException();
                }
                List<Position> path = fringe.poll();
                Position node = path.get(path.size() - 1);
                if (to.equals(node)) {
                    return path;
                }
                if (!closed.contains(node)) {
                    closed.add(node);
                    for(Position successor: node.neighbors()) {
                        if (!illegals.contains(successor)
                                && successor.x >= 0 && successor.x < this.board.length
                                && successor.y >= 0 && successor.y < this.board[successor.x].length) {
                            List<Position> newPath = new ArrayList<Position>(path);
                            newPath.add(successor);
                            fringe.add(newPath);
                        }
                    }
                }
            }
        }

        public String render() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    builder.append(this.board[i][j]);
                    builder.append(' ');
                }
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }
    }

    private static class Position extends Object {
        private int x;
        private int y;
        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public List<Position> neighbors() {
            List<Position> result = new ArrayList<Position>();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (!(i == 0 && j == 0)) {
                        result.add(Position.from(this.x + i, this.y + j));
                    }
                }
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Position) {
                Position pos = (Position)obj;
                return pos.x == this.x && pos.y == this.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return x * y;
        }

        private static Position from(int x, int y) {
            return new Position(x, y);
        }
    }
}
