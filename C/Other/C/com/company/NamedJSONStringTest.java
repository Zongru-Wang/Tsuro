package com.company;

import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamedJSONStringTest {
    private String emptyString = "";
    private String nullString = null;
    private String namedString = "name";
    private NamedJSONString namedJSONString = null;
    @Before
    public void setUp() throws Exception {
        namedJSONString = new NamedJSONString(namedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyNamedJSONStringConstructionThrowsIllegalArgumentException() {
        new NamedJSONString(emptyString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullNamedJSONStringConstructionThrowsIllegalArgumentException() {
        new NamedJSONString(nullString);
    }

    @Test
    public void validNamedJSONStringConstructionDoesNotThrow() {
        new NamedJSONString(namedString);
        assertTrue(true);
    }

    @Test
    public void getNameReturnsProperValue() {
        assertEquals("name", namedJSONString.getName());
    }

    @Test
    public void toStringReturnsProperValue() {
        assertEquals("\"name\"", namedJSONString.toString());
    }
}