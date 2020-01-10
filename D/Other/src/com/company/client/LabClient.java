package com.company.client;

import com.company.client.tcp.ILabTCPClient;
import com.company.client.tcp.LabTCPClient;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LabClient implements ILabClient {
    private IUserRequestScanner scanner;
    private PrintStream out;
    private InputStream in;
    private ILabTCPClient tcpClient;
    public LabClient(InputStream in, PrintStream out) {
        this.in = in;
        this.scanner = new UserRequestScanner(in);
        this.out = out;
        this.tcpClient = null;
    }

    @Override
    public void start(String IPAddress, int port, String name) throws IOException {
        if (tcpClient != null) {
            throw new RuntimeException();
        }
        this.tcpClient = new LabTCPClient(IPAddress, port);
        String session = this.tcpClient.start(name);
        this.out.println(String.format("[\"the server will call me\", \"%s\"]", session));
        List<IAddTokenRequest> batch = new ArrayList<IAddTokenRequest>();
        while (scanner.hasNextUserRequest()) {
            if (this.tcpClient.isClosed()) {
                return;
            }
            try {
                IUserRequest nextRequest = scanner.nextUserRequest();
                if(nextRequest.isCreateLabyrinth()) {
                    this.tcpClient.createLabyrinth((ICreateLabyrinthRequest)nextRequest);
                } else if(nextRequest.isAddToken()) {
                    batch.add((IAddTokenRequest)nextRequest);
                } else if(nextRequest.isReachableQuery()) {
                    String response = this.tcpClient.sendBatch(batch, (IReachableQueryRequest)nextRequest);
                    batch.clear();
                    try {
                        JSONArray array = (JSONArray)new JSONParser().parse(response);
                        for (int i = 0; i < array.size() - 1; i++) {
                            this.out.println(String.format("[\"invalid\", %s]", array.get(i).toString()));
                        }
                        this.out.println(String.format("[\"the response to\", %s, \"is\", %s]",
                                nextRequest.asJSON(),
                                (Boolean)array.get(array.size() - 1)));
                    } catch (ParseException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    throw new RuntimeException();
                }
            } catch (EOFException e) {
                break;
            } catch (StreamCorruptedException e) {
                this.out.println(String.format("[\"not a request\", \"%s\"]", e.getMessage()));
            } catch (IOException e) {
                this.out.println("Server disconnected");
                break;
            }
         }
        try {
            this.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        this.in.close();
        this.scanner.close();
        this.out.close();
        this.tcpClient.close();
    }
}
