import java.util.List;

public class TestOutput {
    private String fromPort;
    private String toPort;

    private TestOutput(String fromPort, String toPort) {
        this.fromPort = fromPort;
        this.toPort = toPort;
    }

    public static TestOutput from(ITile.Port fromPort, ITile.Port toPort) {
        return new TestOutput(help(fromPort), help(toPort));
    }

    private static String help(ITile.Port port){
        ITile.Port A = new ITile.Port(ITile.Port.Direction.NORTH, ITile.Port.Index.ONE);
        ITile.Port B = new ITile.Port(ITile.Port.Direction.NORTH, ITile.Port.Index.TWO);
        ITile.Port C = new ITile.Port(ITile.Port.Direction.EAST, ITile.Port.Index.ONE);
        ITile.Port D = new ITile.Port(ITile.Port.Direction.EAST, ITile.Port.Index.TWO);
        ITile.Port E = new ITile.Port(ITile.Port.Direction.SOUTH, ITile.Port.Index.ONE);
        ITile.Port F = new ITile.Port(ITile.Port.Direction.SOUTH, ITile.Port.Index.TWO);
        ITile.Port G = new ITile.Port(ITile.Port.Direction.WEST, ITile.Port.Index.ONE);
        ITile.Port H = new ITile.Port(ITile.Port.Direction.WEST, ITile.Port.Index.TWO);
        String result = "";
        if (port.equals(A)){
            result = "A";
        } else if (port.equals(B)){
            result = "B";
        } else if (port.equals(C)){
            result = "C";
        } else if (port.equals(D)){
            result = "D";
        } else if (port.equals(E)){
            result = "E";
        } else if (port.equals(F)){
            result = "F";
        } else if (port.equals(G)){
            result = "G";
        } else if (port.equals(H)) {
            result = "H";
        } else {
            throw new IllegalArgumentException("Illegal port");
        }
        return result;
    }



    @Override
    public String toString() {
        String result = String.format("[\"if \", \"%s\", \" is the entrance, \", \"%s\", \" is the exit.\"]", this.fromPort, this.toPort);
        return result;
    }
}
