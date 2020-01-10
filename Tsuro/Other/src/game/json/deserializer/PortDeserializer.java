package game.json.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import game.tile.Port;

import java.lang.reflect.Type;

/**
 * PortDeserializer that deserialize port json as one of "A", "B", "C", "D", "E", "F", "G".
 */
public class PortDeserializer implements JsonDeserializer<Port> {
    private static final String PORT_JSON_SHOULD_BE_PRIMITIVE = "Port json must be json primitive";
    private static final String PORT_JSON_IS_NOT_ALPHABETIC = "Port json is not alphabetic index";

    /**
     * @param jsonElement jsonElement to deserialize
     * @param type Port.class
     * @param jsonDeserializationContext default jsonDeserializationContext
     * @return Port object jsonElement represents
     * @throws JsonParseException when jsonElement is not one of "A", "B", "C", "D", "E", "F", "G".
     */
    @Override
    public Port deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonPrimitive())
            throw new JsonParseException(PORT_JSON_SHOULD_BE_PRIMITIVE);
        String alphabeticIndex = jsonElement.getAsString();
        for (Port port : Port.all()) {
            if (String.valueOf(port.asAlphabeticIndex()).equals(alphabeticIndex)) {
                return port;
            }
        }
        throw new JsonParseException(PORT_JSON_IS_NOT_ALPHABETIC);
    }
}
