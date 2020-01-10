package testharness;

import com.google.gson.*;

import java.io.InputStream;
import java.util.Scanner;

public abstract class AbstractJsonDeserializableTestInputExtractor<T> implements TestInputExtractor<T>, JsonDeserializer<T> {

    @Override
    public T extract(InputStream input) {
        Scanner scanner = new Scanner(input);
        StringBuilder builder = new StringBuilder();
        JsonArray jsonList = new JsonArray();
        Gson gson = new Gson();
        while (scanner.hasNext()) {
            builder.append(scanner.next());
            try {
                JsonElement json = gson.fromJson(builder.toString(), JsonElement.class);
                jsonList.add(json);
                builder.setLength(0);
            } catch (JsonParseException ignored) {
            }
        }
        return fromJsonList(jsonList);
    }
    public abstract T fromJsonList(JsonArray jsonList);
}
