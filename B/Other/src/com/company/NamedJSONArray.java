package com.company;

import org.json.simple.JSONArray;



/**
 * Constructs this NamedJSONArray.  Throws an IllegalArgumentException if the size
 * of the array is 0, or if the first element is not a String.
 *
 * @param obj       a JSONArray to construct this NamedJSONArray out of
 */
public class NamedJSONArray implements INamedJsonObject {
    private JSONArray obj;
    public NamedJSONArray(JSONArray obj) {
        if (obj.size() == 0 || !(obj.get(0) instanceof String)) {
            throw new IllegalArgumentException();
        }
        this.obj = obj;
    }
    @Override
    public String getName() {
        return (String)obj.get(0);
    }
    @Override
    public String toString() {
        return obj.toString();
    }
}
