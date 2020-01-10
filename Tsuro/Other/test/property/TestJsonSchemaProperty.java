package property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;
import property.generator.JsonElementOrArrayGenerator;
import property.generator.JsonSchemaGenerator;
import testharness.schema.JsonSchema;

import java.util.Arrays;

import static org.junit.Assert.*;
import static testharness.schema.JsonSchema.*;

@RunWith(JUnitQuickcheck.class)
public class TestJsonSchemaProperty {
    @Property public void x(int i) {
        JsonElement xJson = new JsonPrimitive(Math.abs(i) % 10);
        assertTrue(x.validate(xJson));
    }
    @Property public void y(int i) {
        JsonElement yJson = new JsonPrimitive(Math.abs(i) % 10);
        assertTrue(y.validate(yJson));
    }
    @Property public void color(int i) {
        String colorValue = Arrays.asList("white", "black", "red", "green", "black").get(Math.abs(i) % 5);
        assertTrue(color.validate(new JsonPrimitive(colorValue)));
    }
    @Property public void tileIndex(int i) {
        JsonElement tileIndexValue = new JsonPrimitive(Math.abs(i) % 35);
        assertTrue(tileIndex.validate(tileIndexValue));
    }
    @Property public void rotation(int i) {
        JsonElement rotationValue = new JsonPrimitive((Math.abs(i) / 90) * 90 % 360);
        assertTrue(rotation.validate(rotationValue));
    }
    @Property public void port(int i) {
        String portValue = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H").get(Math.abs(i) % 8);
        assertTrue(port.validate(new JsonPrimitive(portValue)));
    }
    @Property public void schema1OrSchema2(
            @From(JsonSchemaGenerator.class) JsonSchema schema1,
            @From(JsonSchemaGenerator.class) JsonSchema schema2,
            @From(JsonElementOrArrayGenerator.class) JsonElement jsonElement) {
        if (schema1.validate(jsonElement) || schema2.validate(jsonElement)) {
            assertTrue(or(schema1, schema2).validate(jsonElement));
        }
    }
    @Property public void boundedSchemas(
            @From(JsonSchemaGenerator.class) JsonSchema schema1,
            @From(JsonSchemaGenerator.class) JsonSchema schema2,
            @From(JsonElementOrArrayGenerator.class) JsonElement jsonElement1,
            @From(JsonElementOrArrayGenerator.class) JsonElement jsonElement2) {
        if (schema1.validate(jsonElement1) && schema2.validate(jsonElement2)) {
            JsonArray array = new JsonArray();
            array.add(jsonElement1);
            array.add(jsonElement2);
            assertTrue(bounded(schema1, schema2).validate(array));
        }
    }
    @Property public void unboundedSchemas(
            @From(JsonSchemaGenerator.class) JsonSchema schema,
            @From(JsonElementOrArrayGenerator.class) JsonElement jsonElement,
            int i) {
        if (schema.validate(jsonElement)) {
            JsonArray array = new JsonArray();
            for (int j = 0; j < i; j++) {
                array.add(jsonElement);
            }
            assertTrue(unbounded(schema).validate(array));
        }
    }
}
