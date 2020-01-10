package testharness;

import java.io.*;

public interface TestHarness<T, U extends TestOutput> {
    public void executeTest(InputStream in, PrintStream out);
    public void executeTest(InputStream in, PrintStream out, FileOutputStream file);
    public U test (T input);
}
