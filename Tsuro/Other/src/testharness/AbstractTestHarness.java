package testharness;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public abstract class AbstractTestHarness<T, U extends TestOutput> implements TestHarness<T, U> {
    private final TestInputExtractor<T> extractor;
    protected AbstractTestHarness(TestInputExtractor<T> extractor) {
        this.extractor = extractor;
    }
    @Override
    public void executeTest(InputStream in, PrintStream out) {
        try {
            T input = this.extractor.extract(in);
            U output = this.test(input);
            output.print(out);
        } catch (IllegalArgumentException e) {
            out.println("invalid input");
        }
    }

    @Override
    public void executeTest(InputStream in, PrintStream out, FileOutputStream file) {
        try {
            T input = this.extractor.extract(in);
            U output = this.test(input);
            output.print(file);
        } catch (IllegalArgumentException e) {
            out.println("invalid input");
        }
    }
}
