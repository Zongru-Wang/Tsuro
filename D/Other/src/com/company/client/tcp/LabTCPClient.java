package com.company.client.tcp;

import com.company.client.IAddTokenRequest;
import com.company.client.ICreateLabyrinthRequest;
import com.company.client.IReachableQueryRequest;
import com.company.util.IOrderedPair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class LabTCPClient implements ILabTCPClient {
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public LabTCPClient(String IPAddress, int port) throws IOException {
        this.socket = new Socket(IPAddress, port);
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    LabTCPClient(BufferedReader mockIn, PrintWriter mockOut) {
        this.socket = new Socket();
        this.out = mockOut;
        this.in = mockIn;
    }

    @Override
    public String start(String name) throws IOException {
        if (name.isEmpty()) {
            throw new RuntimeException("Name value passed on to LabTCPClient must be non empty");
        }
        return this.send(String.format("\"%s\"", name));
    }

    @Override
    public void createLabyrinth(ICreateLabyrinthRequest createLabyrinthRequest) throws IOException {
        List<String> nodes = createLabyrinthRequest.getNodes();
        String nodeJson = String.format("[%s]", String.join(", ", nodes));
        List<IOrderedPair<String>> edges = createLabyrinthRequest.getEdges();
        List<String> edgeJsons = new ArrayList<String>();
        for (IOrderedPair<String> pair: edges) {
            edgeJsons.add(String.format("[%s, %s]", pair.first(), pair.second()));
        }
        String edgesJson = String.format("[%s]", String.join(", ", edgeJsons));
        this.sendWithoutResponse(String.format("[\"lab\", %s, %s]", nodeJson, edgesJson));
    }

    @Override
    public String sendBatch(List<IAddTokenRequest> addTokenRequests, IReachableQueryRequest reachableQueryRequest)
    throws IOException {
        List<String> requestJsons = new ArrayList<String>();
        for(IAddTokenRequest addTokenRequest: addTokenRequests) {
            requestJsons.add(addTokenRequest.asJSON());
        }
        requestJsons.add(reachableQueryRequest.asJSON());
        String batch = String.format("[%s]", String.join(", ", requestJsons));
        return this.send(batch);
    }

    @Override
    public boolean isClosed() {
        return this.socket.isClosed();
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        this.in.close();
        this.out.close();
        this.socket.close();
    }

    private String send(String message) throws IOException {
        this.out.println(message);
        return this.in.readLine();
    }

    private void sendWithoutResponse(String message) {
        this.out.println(message);
    }
}
