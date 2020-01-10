package testharness.schema;

import com.google.gson.JsonElement;

public class UnboundedJsonArraySchemaImpl implements JsonSchema {
    private final JsonSchema schema;
    UnboundedJsonArraySchemaImpl(JsonSchema schema) {
        this.schema = schema;
    }
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonArray())
            return false;
        for (JsonElement element: jsonElement.getAsJsonArray()) {
            if (!schema.validate(element))
                return false;
        }
        return true;
    }
}
