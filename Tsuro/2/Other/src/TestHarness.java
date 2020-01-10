import java.util.Scanner;

public class TestHarness {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            try {
                TestInput input = TestInput.from(scanner.nextLine());
                ITile tile = TestTileAdapter.adaptTile(input);
                ITile.Port startPort = TestTileAdapter.adaptStartPort(input);
                TestOutput output = TestOutput.from(startPort, tile.connectedPort(startPort));
                System.out.println(output.toString());
            } catch (Exception ignored) {
            }
        }
    }
}
