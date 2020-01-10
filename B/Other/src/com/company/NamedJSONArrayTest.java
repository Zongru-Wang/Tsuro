package com.company;

import static org.junit.Assert.*;;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

public class NamedJSONArrayTest {
    private JSONArray emptyJSONArray = null;
    private JSONArray invalidJSONArray = null;
    private JSONArray singleElementJSONArray = null;
    private JSONArray multipleElementJSONArray = null;
    private NamedJSONArray singleElementNamedJSONArray = null;
    private NamedJSONArray multipleElementNamedJSONArray = null;
    private JSONParser parser = new JSONParser();

    @Before
    public void setUp() {
        try {
            emptyJSONArray = (JSONArray)parser.parse("[]");
            invalidJSONArray = (JSONArray)parser.parse("[1, 2, 3]");
            singleElementJSONArray = (JSONArray)parser.parse("[\"name\"]");
            multipleElementJSONArray = (JSONArray)parser.parse("[\"name2\", 1, {\"this\": 3}, [1, {\"bar\" : \"foo\"}]]");
        } catch ( Exception e ) {
            e.printStackTrace(System.err);
        }
        singleElementNamedJSONArray = new NamedJSONArray(singleElementJSONArray);
        multipleElementNamedJSONArray = new NamedJSONArray(multipleElementJSONArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyNamedArrayConstructionThrowIllegalArgumentException() {
        new NamedJSONArray(emptyJSONArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidNamedArrayConstructionThrowIllegalArgumentException() {
        new NamedJSONArray(invalidJSONArray);
    }

    @Test
    public void validNamedArrayConstructionDoesNotThrow() {
        new NamedJSONArray(singleElementJSONArray);
        new NamedJSONArray(multipleElementJSONArray);
        assertTrue(true);
    }

    @Test
    public void getNameReturnsProperName() {
        assertEquals("name", singleElementNamedJSONArray.getName());
        assertEquals("name2", multipleElementNamedJSONArray.getName());
    }

    @Test
    public void toStringReturnsProperString() {
        assertEquals("[\"name\"]", singleElementNamedJSONArray.toString());
        assertEquals("[\"name2\",1,{\"this\":3},[1,{\"bar\":\"foo\"}]]", multipleElementNamedJSONArray.toString());
    }
}