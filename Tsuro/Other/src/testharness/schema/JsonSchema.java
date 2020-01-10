package testharness.schema;

import com.google.gson.JsonElement;

public interface JsonSchema {
    public boolean validate(JsonElement jsonElement);
    public static final JsonSchema x = new BoardCoordinateSchemaImpl();
    public static final JsonSchema y = new BoardCoordinateSchemaImpl();
    public static final JsonSchema color = new ColorJsonSchemaImpl();
    public static final JsonSchema tileIndex = new TileIndexJsonSchemaImpl();
    public static final JsonSchema rotation = new RotationJsonSchemaImpl();
    public static final JsonSchema port = new PortJsonSchemaImpl();
    public static JsonSchema bounded(JsonSchema... schemas) {
        return new BoundedJsonArraySchemaImpl(schemas);
    }
    public static JsonSchema unbounded(JsonSchema schema) {
        return new UnboundedJsonArraySchemaImpl(schema);
    }
    public static JsonSchema or(JsonSchema schema1, JsonSchema schema2) {
        return new ORJsonSchemaImpl(schema1, schema2);
    }
}
