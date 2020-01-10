package testharness.schema;

import com.google.gson.JsonElement;

public class BoardCoordinateSchemaImpl implements JsonSchema {
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonPrimitive())
            return false;
        try {
            int coordinate = jsonElement.getAsInt();
            return 0 <= coordinate && coordinate < 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
