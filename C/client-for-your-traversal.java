package com.company;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Entry point for the application
public class Main {

    public static void main(String[] args) {
        ILabyrinthJsonReader reader = new LabyrinthJsonReader();
        ILabyrinth labyrinth = new MockLabyrinth();
        List<ILabyrinthJson> labyrinthJsons = null;
        ILabyrinthCreationJson creationJson = null;
        Set<String> nodes = new HashSet<String>();
        Set<String> tokensAdded = new HashSet<String>();
        try {
            labyrinthJsons = reader.readAll(System.in);
        } catch (IllegalArgumentException e) {
            return;
        }
        for (int i = 0; i < labyrinthJsons.size(); i++) {
            if (i == 0 && !labyrinthJsons.get(i).isCreation()) {
                return;
            } else if (labyrinthJsons.get(i).isCreation()) {
                creationJson = (ILabyrinthCreationJson) labyrinthJsons.get(i);
                if (creationJson.validate()) {
                    for (INodePair nodePair : creationJson.getNodePairs()) {
                        nodes.add(nodePair.getFrom());
                        nodes.add(nodePair.getTo());
                    }
                    for (String node : nodes) {
                        labyrinth.addNode(node);
                    }
                    for (INodePair nodePair : creationJson.getNodePairs()) {
                        labyrinth.connectNode(nodePair.getFrom(), nodePair.getTo());
                    }
                } else {
                    return;
                }
            } else if (labyrinthJsons.get(i).isAdd()) {
                ILabyrinthAddJson addJson = (ILabyrinthAddJson) labyrinthJsons.get(i);
                IColorNodePair colorNodePair = addJson.getColorNodePair();
                if (addJson.validate() && nodes.contains(colorNodePair.getNode())) {
                    labyrinth.addColoredToken(colorNodePair.getNode(), colorNodePair.getColor());
                    tokensAdded.add(colorNodePair.getColor());
                }
            } else if (labyrinthJsons.get(i).isMove()) {
                ILabyrinthMoveJson moveJson = (ILabyrinthMoveJson) labyrinthJsons.get(i);
                IColorNodePair colorNodePair = moveJson.getColorNodePair();
                if (moveJson.validate() && nodes.contains(colorNodePair.getNode()) && tokensAdded.contains(colorNodePair.getColor())) {
                    if (labyrinth.canColoredTokenReachNamedNode(colorNodePair.getColor(), colorNodePair.getNode())) {
                        System.out.println("success!");
                    } else {
                        System.out.println("failed.");
                    }
                }
            }
        }
    }
}

// the interface with function to check the command type
public interface ILabyrinthJson {
    boolean isCreation();

    boolean isAdd();

    boolean isMove();

    boolean validate();
}

// the abstract class with available colors with functions to check if the given color is valid
public class ALabyrinthJson {
    protected boolean validateColor(String color) {
        return color.equals("white") ||
                color.equals("black") ||
                color.equals("red") ||
                color.equals("green") ||
                color.equals("blue");
    }
}

// the interface extent ILabyrinthJson sepcially for: creation of a plain labyrinth with named nodes
public interface ILabyrinthCreationJson extends ILabyrinthJson {
    List<INodePair> getNodePairs();
}


public class LabyrinthCreationJson implements ILabyrinthCreationJson {
    private List<INodePair> nodePairs;


    public LabyrinthCreationJson(List<INodePair> nodePairs) {
        this.nodePairs = nodePairs;
    }

    @Override
    public List<INodePair> getNodePairs() {
        return new ArrayList<INodePair>(this.nodePairs);
    }

    @Override
    public boolean isCreation() {
        return true;
    }

    @Override
    public boolean isAdd() {
        return false;
    }

    @Override
    public boolean isMove() {
        return false;
    }

    @Override
    public boolean validate() {
        boolean validate = false;
        for (INodePair node : this.nodePairs) {
            if (node.getFrom().equals(node.getTo())) {
                return false;
            }
        }
        for (int i = 0; i < this.nodePairs.size(); i++) {
            for (int j = i + 1; j < this.nodePairs.size(); j++) {
                INodePair firstPair = this.nodePairs.get(i);
                INodePair secondPair = this.nodePairs.get(j);
                if ((firstPair.getFrom().equals(secondPair.getFrom()) && firstPair.getTo().equals(secondPair.getTo())) ||
                        (firstPair.getFrom().equals(secondPair.getTo()) && firstPair.getTo().equals(secondPair.getFrom()))) {
                    return false;
                }
            }
        }
        return true;
    }
}

// the interface extent ILabyrinthJson sepcially for: addition of a colored token to a node
public interface ILabyrinthAddJson extends ILabyrinthJson {
    IColorNodePair getColorNodePair();
}

public class LabyrinthAddJson extends ALabyrinthJson implements ILabyrinthAddJson {

    private IColorNodePair colorNodePair;

    public LabyrinthAddJson(String color, String node) {
        this.colorNodePair = new ColorNodePair(color, node);
    }

    @Override
    public IColorNodePair getColorNodePair() {
        return this.colorNodePair;
    }

    @Override
    public boolean isCreation() {
        return false;
    }

    @Override
    public boolean isAdd() {
        return true;
    }

    @Override
    public boolean isMove() {
        return false;
    }

    @Override
    public boolean validate() {
        return this.validateColor(this.colorNodePair.getColor());
    }
}

// the interface extent ILabyrinthJson sepcially for query whether some colored token can reach some named graph node
public interface ILabyrinthMoveJson extends ILabyrinthJson {
    IColorNodePair getColorNodePair();
}

public class LabyrinthMoveJson extends ALabyrinthJson implements ILabyrinthMoveJson {
    private IColorNodePair colorNodePair;

    public LabyrinthMoveJson(String color, String node) {
        this.colorNodePair = new ColorNodePair(color, node);
    }

    @Override
    public IColorNodePair getColorNodePair() {
        return this.colorNodePair;
    }

    @Override
    public boolean isCreation() {
        return false;
    }

    @Override
    public boolean isAdd() {
        return false;
    }

    @Override
    public boolean isMove() {
        return true;
    }

    @Override
    public boolean validate() {
        return this.validateColor(this.colorNodePair.getColor());
    }
}

// the interface represent Node pairs, with function which can get from node name and to node name
public interface INodePair {
    String getFrom();

    String getTo();
}

public class NodePair implements INodePair {
    private String from;
    private String to;

    public NodePair(String from, String to) {
        if (from == null || from.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.from = from;
        this.to = to;
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    @Override
    public String getTo() {
        return this.to;
    }
}

// the interface represent a Node pair with color, with function can get the color and the name of node
public interface IColorNodePair {
    String getColor();

    String getNode();
}

public class ColorNodePair implements IColorNodePair {
    private String color;
    private String node;

    public ColorNodePair(String color, String node) {
        if (color == null || color.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (node == null || node.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.color = color;
        this.node = node;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public String getNode() {
        return this.node;
    }
}

// the interface for building Labryinth
public interface ILabyrinthJsonBuilder {
    public ILabyrinthJson build(Object obj);
}

public class LabyrinthJsonBuilder implements ILabyrinthJsonBuilder {
    @Override
    public ILabyrinthJson build(Object obj) {
        if (!(obj instanceof JSONArray)) {
            throw new IllegalArgumentException();
        }
        JSONArray jsonArray = (JSONArray) obj;
        if (!(jsonArray.get(0) instanceof String)) {
            throw new IllegalArgumentException();
        }
        String name = (String) jsonArray.get(0);
        if (name.equals("lab")) {
            List<INodePair> nodePairs = new ArrayList<>();
            for (int i = 1; i < jsonArray.size(); i++) {
                if (!(jsonArray.get(i) instanceof JSONObject)) {
                    throw new IllegalArgumentException();
                }
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (!(jsonObject.size() == 2 && jsonObject.containsKey("from") && jsonObject.containsKey("to"))) {
                    throw new IllegalArgumentException();
                }
                if (!(jsonObject.get("from") instanceof String && jsonObject.get("to") instanceof String)) {
                    throw new IllegalArgumentException();
                }
                String from = (String) jsonObject.get("from");
                String to = (String) jsonObject.get("to");
                NodePair nodePair = new NodePair(from, to);
                NodePair reverse = new NodePair(to, from);
                if (nodePairs.contains(nodePair) || nodePairs.contains(reverse)) {
                    throw new IllegalArgumentException();
                } else {
                    nodePairs.add(nodePair);
                }
            }
            return new LabyrinthCreationJson(nodePairs);
        } else if (name.equals("add") || name.equals("move")) {
            if (!(jsonArray.size() == 3 &&
                    jsonArray.get(1) instanceof String &&
                    jsonArray.get(2) instanceof String)) {
                throw new IllegalArgumentException();
            }
            String color = (String) jsonArray.get(1);
            String node = (String) jsonArray.get(2);
            if (name.equals("add")) {
                return new LabyrinthAddJson(color, node);
            } else {
                return new LabyrinthMoveJson(color, node);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}

// the interface to reads JSON values from STDIN
public interface ILabyrinthJsonReader {
    List<ILabyrinthJson> readAll(InputStream in);
}

public class LabyrinthJsonReader implements ILabyrinthJsonReader {
    @Override
    public List<ILabyrinthJson> readAll(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("");
        StringBuilder builder = new StringBuilder();
        JSONParser parser = new JSONParser();
        ILabyrinthJsonBuilder labyrinthJsonBuilder = new LabyrinthJsonBuilder();
        List<ILabyrinthJson> results = new ArrayList<ILabyrinthJson>();
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.equals(" ") || next.equals(System.lineSeparator())) {
                continue;
            }
            builder.append(next);
            String test = builder.toString();
            try {
                Object obj = parser.parse(test);
                ILabyrinthJson labyrinthJson = labyrinthJsonBuilder.build(obj);
                results.add(labyrinthJson);
                builder.setLength(0);
            } catch (ParseException ex) {
                continue;
            }
        }
        scanner.close();
        if (results.size() == 0) {
            throw new IllegalArgumentException();
        }
        if (builder.length() != 0) {
            throw new IllegalArgumentException();
        }
        return results;
    }
}

// mock module, a fake implementation which implements the interface
public interface ILabyrinth {
    List<String> getAllNodeNames();

    List<String> getConnectedNodeNames(String name);

    void addNode(String name);

    void connectNode(String nodeName1, String nodeName2);

    void addColoredToken(String name, String color);

    boolean canColoredTokenReachNamedNode(String color, String name);
}

public class MockLabyrinth implements ILabyrinth {
    @Override
    public List<String> getAllNodeNames() {
        return null;
    }

    @Override
    public List<String> getConnectedNodeNames(String name) {
        return null;
    }

    @Override
    public void addNode(String name) {

    }

    @Override
    public void connectNode(String nodeName1, String nodeName2) {

    }

    @Override
    public void addColoredToken(String name, String color) {

    }

    @Override
    public boolean canColoredTokenReachNamedNode(String color, String name) {
        return false;
    }
}