package testharness;

import java.io.OutputStream;
import java.io.PrintStream;

public interface TestOutput {
    void print(OutputStream out);
}
