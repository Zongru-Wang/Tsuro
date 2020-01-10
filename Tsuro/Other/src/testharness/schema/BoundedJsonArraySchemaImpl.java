package testharness.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Arrays;
import java.util.List;

public class BoundedJsonArraySchemaImpl implements JsonSchema {
    private final List<JsonSchema> schemas;
    BoundedJsonArraySchemaImpl(JsonSchema... schemas) {
        this.schemas = Arrays.asList(schemas);
    }
    @Override
    public boolean validate(JsonElement jsonElement) {
        if (!jsonElement.isJsonArray())
            return false;
        if (jsonElement.getAsJsonArray().size() != this.schemas.size())
            return false;
        JsonArray array = jsonElement.getAsJsonArray();
        for (int i = 0; i < this.schemas.size(); i++) {
            if (!this.schemas.get(i).validate(array.get(i)))
                return false;
        }
        return true;
    }
}
