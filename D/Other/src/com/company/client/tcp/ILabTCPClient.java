package com.company.client.tcp;

import com.company.client.IAddTokenRequest;
import com.company.client.ICreateLabyrinthRequest;
import com.company.client.IReachableQueryRequest;

import java.util.List;
import java.io.Closeable;
import java.io.IOException;

public interface ILabTCPClient extends Closeable {
    String start(String name) throws IOException;
    void createLabyrinth(ICreateLabyrinthRequest createLabyrinthRequest) throws IOException;
    String sendBatch(List<IAddTokenRequest> addTokenRequests, IReachableQueryRequest reachableQueryRequest) throws IOException;
    boolean isClosed();
}
