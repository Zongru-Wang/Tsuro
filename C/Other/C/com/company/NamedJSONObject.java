package com.company;

import org.json.simple.JSONObject;

/**
 * Constructs this NamedJSONObject.  Throws an IllegalArgumentException if it
 * the given JSONObject is missing the key "this" or if the value associated
 * with the key "this" isn't a String.
 *
 * @param obj       a JSONObject to construct this NamedJSONObject out of
 */
public class NamedJSONObject implements INamedJsonObject {
    private JSONObject obj;
    private String helperString;
    public NamedJSONObject(JSONObject obj, String helperString) {
        if (!(obj.containsKey("this") && obj.get("this") instanceof String)) {
            throw new IllegalArgumentException();
        }
        this.obj = obj;
        this.helperString = helperString;
    }
    @Override
    public String getName() {
        return (String)obj.get("this");
    }
    @Override
    public String toString() {
        return helperString;
    }
}
