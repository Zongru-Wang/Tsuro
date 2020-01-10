package property.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import testharness.schema.JsonSchema;

import java.util.ArrayList;
import java.util.List;

import static testharness.schema.JsonSchema.*;
import static testharness.schema.JsonSchema.unbounded;

public class JsonSchemaGenerator extends Generator<JsonSchema> {
    private final int DEPTH = 1;
    public JsonSchemaGenerator() {
        super(JsonSchema.class);
    }

    @Override
    public JsonSchema generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        return this.generateHelper(sourceOfRandomness.nextInt(0, this.DEPTH), sourceOfRandomness);
    }

    private JsonSchema generateHelper(int depth, SourceOfRandomness r) {
        JsonSchema[] schemas = new JsonSchema[] { tileIndex, rotation, color, x, y, port};
        if (depth == 0) {
            return r.choose(schemas);
        }
        if (r.nextBoolean()) {
            List<JsonSchema> boundedSchemaList = new ArrayList<>();
            for (int i = 0; i < r.nextInt(1, 5); i++) {
                boundedSchemaList.add(this.generateHelper(depth - 1, r));
            }
            JsonSchema[] boundedSchemas = new JsonSchema[boundedSchemaList.size()];
            boundedSchemaList.toArray(boundedSchemas);
            return bounded(boundedSchemas);
        } else {
            return unbounded(generateHelper(depth - 1, r));
        }
    }
}
