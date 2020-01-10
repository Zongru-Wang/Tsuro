package com.company.client;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.Closeable;
import java.io.EOFException;
import java.io.StreamCorruptedException;

public interface IUserRequestScanner extends Closeable {
    boolean hasNextUserRequest();
    IUserRequest nextUserRequest() throws EOFException, StreamCorruptedException;
}
