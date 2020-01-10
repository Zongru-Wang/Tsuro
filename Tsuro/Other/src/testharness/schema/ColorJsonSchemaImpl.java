package testharness.schema;

import com.google.gson.JsonElement;

import java.util.Arrays;

public class ColorJsonSchemaImpl implements JsonSchema {
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive())
            return false;
        String color = jsonElement.getAsString().replaceAll("\"", "");
        return Arrays.asList("white", "black", "red", "green", "blue").contains(color);
    }
}
