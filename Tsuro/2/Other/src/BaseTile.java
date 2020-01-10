import java.util.*;

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

}
