package com.company.client;


import com.company.util.IOrderedPair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateLabyrinthRequest implements ICreateLabyrinthRequest {
    private List<IOrderedPair<String>> edges;
    CreateLabyrinthRequest(List<IOrderedPair<String>> edges) throws IllegalArgumentException {
        this.edges = edges;
    }

    @Override
    public boolean isCreateLabyrinth() {
        return true;
    }

    @Override
    public boolean isAddToken() {
        return false;
    }

    @Override
    public boolean isReachableQuery() {
        return false;
    }

    @Override
    public String asJSON() {
        List<String> edgeJsons = new ArrayList<String>();
        for (IOrderedPair<String> pair: this.edges) {
            edgeJsons.add(String.format("{\"from\": \"%s\", \"to\":\"%s\"}", pair.first(), pair.second()));
        }
        String edgesJson = String.join(", ", edgeJsons);
        return String.format("[\"lab\", %s]", edgesJson);
    }

    @Override
    public List<String> getNodes() {
        Set<String> nodes = new HashSet<String>();
        for (IOrderedPair<String> pair: this.edges) {
            nodes.add(pair.first());
            nodes.add(pair.second());
        }
        return new ArrayList<String>(nodes);
    }

    @Override
    public List<IOrderedPair<String>> getEdges() {
        return new ArrayList<IOrderedPair<String>>(this.edges);
    }
}
