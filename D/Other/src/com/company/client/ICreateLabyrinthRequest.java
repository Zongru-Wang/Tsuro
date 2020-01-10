package com.company.client;

import com.company.util.IOrderedPair;

import java.util.List;

public interface ICreateLabyrinthRequest extends IUserRequest {
    List<String> getNodes();
    List<IOrderedPair<String>> getEdges();
}
