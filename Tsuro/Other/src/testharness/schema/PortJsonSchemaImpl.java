package testharness.schema;

import com.google.gson.JsonElement;

import java.util.Arrays;

public class PortJsonSchemaImpl implements JsonSchema {
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive())
            return false;
        String port = jsonElement.getAsString().replaceAll("\"", "");
        return Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H").contains(port);
    }
}
