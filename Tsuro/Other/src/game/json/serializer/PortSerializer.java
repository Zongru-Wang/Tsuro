package game.json.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import game.tile.Port;

import java.lang.reflect.Type;

/**
 * PortSerializer that serialize one of port to "A", "B", "C", "D", "E", "F", "G" in json string
 */
public class PortSerializer implements JsonSerializer<Port> {

    /**
     * @param port the port that we want to transfer to json string
     * @param type Port.class
     * @param jsonSerializationContext default jsonSerializationContext
     * @return One of "A", "B", "C", "D", "E", "F", "G" in json
     */
    @Override
    public JsonElement serialize(Port port, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(port.asAlphabeticIndex());
    }
}
