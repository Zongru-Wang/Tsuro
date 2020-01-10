import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        String[] strings = {"\"A\"", "\"B\"", "\"C\"", "\"D\"", "\"E\"", "\"F\"", "\"G\"", "\"H\""};
        for(int i = 0; i < 35; i++){
            for (int j = 0; j < 360; j+=90){
                for (String string : strings){
                    String str = "[" + i + "," + j + "," + string + "]";
                    try {
                        TestInput input = TestInput.from(str);
                        ITile tile = TestTileAdapter.adaptTile(input);
                        ITile.Port startPort = TestTileAdapter.adaptStartPort(input);
                        TestOutput output = TestOutput.from(startPort, tile.connectedPort(startPort));
                        System.out.println(str);
                        System.out.println(output.toString());
                    } catch (Exception ignored) {
                    }
                }
            }
        }

    }
}