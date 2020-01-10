package testharness;

import java.io.InputStream;

public interface TestInputExtractor<T> {
    public T extract(InputStream input);
}
