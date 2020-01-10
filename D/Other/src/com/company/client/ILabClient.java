package com.company.client;

import java.io.Closeable;
import java.io.IOException;

public interface ILabClient extends Closeable {
    void start(String IPAddress, int port, String name) throws IOException;
}
