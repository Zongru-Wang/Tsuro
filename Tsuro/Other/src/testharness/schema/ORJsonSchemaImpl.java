package testharness.schema;

import com.google.gson.JsonElement;

public class ORJsonSchemaImpl implements JsonSchema {
    private final JsonSchema schema1;
    private final JsonSchema schema2;
    ORJsonSchemaImpl(JsonSchema schema1, JsonSchema schema2) {
        this.schema1 = schema1;
        this.schema2 = schema2;
    }
    @Override
    public boolean validate(JsonElement jsonElement) {
        return this.schema1.validate(jsonElement) || this.schema2.validate(jsonElement);
    }
}
