package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NamedJsonObjectReader implements INamedJsonObjectReader {
    @Override
    public List<INamedJsonObject> readAll(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("");
        StringBuilder builder = new StringBuilder();
        JSONParser parser = new JSONParser();
        List<INamedJsonObject> results = new ArrayList<INamedJsonObject>();
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.equals(" ") || next.equals(System.lineSeparator())) {
                continue;
            }
            builder.append(next);
            String test = builder.toString();
            try {
                Object obj = parser.parse(test);
                INamedJsonObject namedObj = null;
                if (obj instanceof String) {
                    namedObj = new NamedJSONString((String)obj);
                } else if (obj instanceof JSONArray) {
                    namedObj = new NamedJSONArray((JSONArray) obj);
                } else if (obj instanceof JSONObject) {
                    namedObj = new NamedJSONObject((JSONObject) obj, builder.toString());
                } else {
                    throw new IllegalArgumentException();
                }
                results.add(namedObj);
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
