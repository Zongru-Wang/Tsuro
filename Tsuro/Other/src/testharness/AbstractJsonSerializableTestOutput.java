package testharness;

import com.google.gson.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;

public abstract class AbstractJsonSerializableTestOutput<T extends TestOutput> implements TestOutput, JsonSerializer<T> {
    @Override
    public void print(OutputStream out) {
        new PrintStream(out).println(toJson());
    }

    protected abstract JsonElement toJson();
}
