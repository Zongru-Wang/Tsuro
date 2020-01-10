import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class TestInput {
    private int index;
    private int degree;
    private String port;
    private TestInput(int index, int degree, String port) {
        if (index < 0 || index > 34) {
            throw new IllegalArgumentException();
        }
        this.index = index;
        if (!(degree == 0 || degree == 90 || degree == 180 || degree == 270)) {
            throw new IllegalArgumentException();
        }
        this.degree = degree;
        this.port = port;
    }

    public static TestInput from(String json) {
        // TODO
        // parse json line and create Test Input
        JsonArray jsonArray = new Gson().fromJson(json, JsonArray.class);
        if (jsonArray.size()!=3){
            throw new IllegalArgumentException();
        }
        int ind = Integer.valueOf(jsonArray.get(0).toString());
        int deg = Integer.valueOf(jsonArray.get(1).toString());
        String por = jsonArray.get(2).getAsString();
        return new TestInput(ind, deg, por);
    }

    public int getIndex() {
        return this.index;
    }

    public int getDegree() {
        return this.degree;
    }

    public String getPort() {
        return this.port;
    }
}
