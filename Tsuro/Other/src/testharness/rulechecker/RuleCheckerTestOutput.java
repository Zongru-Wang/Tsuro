package testharness.rulechecker;

import com.google.gson.*;
import testharness.AbstractJsonSerializableTestOutput;
import testharness.TestOutput;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;

public class RuleCheckerTestOutput implements TestOutput {
    public final String output;

    public RuleCheckerTestOutput(String output) {
        if (!output.equals("invalid") && !output.equals("valid")) {
            throw new IllegalArgumentException();
        }
        this.output = output;
    }

    @Override
    public void print(OutputStream out) {
        new PrintStream(out).println(String.format("\"%s\"", this.output));
    }
}
