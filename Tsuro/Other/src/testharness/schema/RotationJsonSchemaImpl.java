package testharness.schema;

import com.google.gson.JsonElement;

import java.util.Arrays;

public class RotationJsonSchemaImpl implements JsonSchema {
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive())
            return false;
        try {
            int rotation = jsonElement.getAsInt();
            return Arrays.asList(0, 90, 180, 270).contains(rotation);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
