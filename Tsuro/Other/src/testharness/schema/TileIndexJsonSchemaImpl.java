package testharness.schema;

import com.google.gson.JsonElement;

public class TileIndexJsonSchemaImpl implements JsonSchema {
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive())
            return false;
        try {
            int index = jsonElement.getAsInt();
            return 0 <= index && index < 35;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
