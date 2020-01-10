package com.company.client.tcp;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class LabTCPClientTest {
    private StringWriter stringWriter = null;
    private PrintWriter mockOut = null;
    private StringReader stringReader = null;
    private BufferedReader mockIn = null;
    @Before
    public void setUp() throws Exception {
        this.stringWriter = new StringWriter();
        this.mockOut = new PrintWriter(stringWriter);
        this.stringReader = new StringReader("TestResponse");
        this.mockIn = new BufferedReader(stringReader);
    }

    @Test
    public void startShouldSendName() {
        ILabTCPClient client = new LabTCPClient(mockIn, mockOut);
        //assertEquals("TestResponse", client.start("Test Name"));
        assertEquals("\"Test Name\"", this.stringWriter.toString());
    }

    @Test(expected = RuntimeException.class)
    public void startShouldThrowRuntimeExceptionOnEmptyName() {
        ILabTCPClient client = new LabTCPClient(mockIn, mockOut);
        //client.start("");
    }

    @Test(expected = RuntimeException.class)
    public void startShouldThrowRuntimeExceptionOnNullName() {
        ILabTCPClient client = new LabTCPClient(mockIn, mockOut);
        //client.start(null);
    }
}