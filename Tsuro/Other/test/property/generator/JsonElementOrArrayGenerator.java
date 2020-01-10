package property.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class JsonElementOrArrayGenerator extends Generator<JsonElement> {
    private final int DEPTH = 1;
    public JsonElementOrArrayGenerator() {
        super(JsonElement.class);
    }

    @Override
    public JsonElement generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        return this.generateHelper(sourceOfRandomness.nextInt(0, this.DEPTH), sourceOfRandomness);
    }

    private JsonElement generateHelper(int depth, SourceOfRandomness r) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < r.nextInt(0, 10); i++) {
            builder.append(r.nextChar('A', 'z'));
        }
        JsonElement[] elements = new JsonElement[] {
                new JsonPrimitive(r.nextInt()),
                new JsonPrimitive(r.nextBoolean()),
                new JsonPrimitive(builder.toString())
        };
        if (depth == 0) {
            return r.choose(elements);
        }
        JsonArray array = new JsonArray();
        for (int i = 0; i < r.nextInt(1, 5); i++) {
            array.add(this.generateHelper(depth - 1, r));
        }
        return array;
    }
}
