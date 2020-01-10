package com.company.client;

import com.company.util.IOrderedPair;
import com.company.util.OrderedPair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.*;

public class UserRequestScanner implements IUserRequestScanner {
    private InputStream in;
    private Scanner scanner;
    private JSONParser parser;
    private String rest;
    UserRequestScanner(InputStream in) {
        this.in = in;
        this.scanner = new Scanner(in);
        this.parser = new JSONParser();
        this.rest = "";
    }

    @Override
    public void close() throws IOException {
        this.in.close();
    }

    @Override
    public boolean hasNextUserRequest() {
        return !this.rest.matches("^(\\r|\\r\\n|\\n| )*$") || this.scanner.hasNextLine();
    }

    private String nextLine() {
        if (this.rest.matches("^(\\r|\\r\\n|\\n| )*$")) {
            return this.scanner.nextLine();
        } else {
            return this.rest;
        }
    }

    @Override
    public IUserRequest nextUserRequest() throws EOFException, StreamCorruptedException {
        if (this.hasNextUserRequest()) {
            String line = this.nextLine();
            for (int i = 0; i < line.length(); i++) {
                try {
                    String considered = line.substring(0, i + 1);
                    Object obj = parser.parse(considered);
                    this.rest = line.substring(i + 1);
                    if (obj instanceof JSONArray) {
                        JSONArray array = (JSONArray) obj;
                        if (array.size() > 0) {
                            try {
                                return this.createUserRequest(array);
                            } catch (IllegalArgumentException e) {
                                throw new StreamCorruptedException(considered);
                            }
                        } else {
                            throw new StreamCorruptedException(considered);
                        }
                    } else {
                        throw new StreamCorruptedException(considered);
                    }
                } catch (ParseException ignored) {
                }
            }
            this.rest = "";
            throw new StreamCorruptedException(line);
        }
        throw new EOFException("Unexpected End of File");
    }

    private ICreateLabyrinthRequest createCreateLabyrinthToken(JSONArray array) throws IllegalArgumentException {
        List<IOrderedPair<String>> edges = new ArrayList<>();
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i) instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject)array.get(i);
                if (jsonObject.containsKey("from") && jsonObject.containsKey("to")) {
                    Object from = jsonObject.get("from");
                    Object to = jsonObject.get("to");
                    if (from instanceof String && to instanceof String) {
                        String fromStr= (String)from;
                        String toStr = (String)to;
                        IOrderedPair<String> edge = new OrderedPair<>(fromStr, toStr);
                        edges.add(edge);
                    }
                } else {
                    throw new IllegalArgumentException("Lab command's json object must contain key from and to");
                }
            } else {
                throw new IllegalArgumentException("Lab command must be sequence of Json objects");
            }
        }
        return new CreateLabyrinthRequest(edges);
    }

    private IAddTokenRequest createAddTokenRequest(JSONArray array) throws IllegalArgumentException {
        if (array.size() == 3) {
            Object color = array.get(1);
            Object name = array.get(2);
            if (color instanceof String && name instanceof String) {
                String colorStr = (String)color;
                String nameStr = (String)name;
                return new AddTokenRequest(colorStr, nameStr);
            } else {
                throw new IllegalArgumentException("add command must be array of strings");
            }
        } else {
            throw new IllegalArgumentException("add command array must have size of 3");
        }
    }

    private IReachableQueryRequest createReachableQueryRequest(JSONArray array) throws IllegalArgumentException {
        if (array.size() == 3) {
            Object color = array.get(1);
            Object name = array.get(2);
            if (color instanceof String && name instanceof String) {
                String colorStr = (String)color;
                String nameStr = (String)name;
                return new ReachableQueryRequest(colorStr, nameStr);
            } else {
                throw new IllegalArgumentException("move command must be array of strings");
            }
        } else {
            throw new IllegalArgumentException("move command array must have size of 3");
        }
    }

    private IUserRequest createUserRequest(JSONArray array) throws IllegalArgumentException {
        if(array.get(0).equals("lab")) {
            return this.createCreateLabyrinthToken(array);
        } else if (array.get(0).equals("add")) {
            return this.createAddTokenRequest(array);
        } else if (array.get(0).equals("move")) {
            return this.createReachableQueryRequest(array);
        } else {
            throw new IllegalArgumentException("Not a valid command name");
        }
    }
}
