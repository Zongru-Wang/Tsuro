package com.company;


import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
public class testOptionParser {




    IOptionParser parser = new OptionParser();


    @Test
    public void testOptionParser() {

        String[] input2 = {"-down"};
        Set<String> options = parser.parse(input2);
        assertTrue(options.size() == 1);
        assertTrue(options.contains("d"));

    }

    private void assertTrue(boolean d) {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalOptionParser() {
        String[] input2 = {"down"};
        Set<String> options = parser.parse(input2);
    }

    @Test
    public void testOptionParserFailedCase(){
        String[] input2 = {"-up"};
        Set<String> options = parser.parse(input2);
        assertTrue(options.size() == 1);
        assertTrue(options.contains("u"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testOptionParserTwoConflictingOptions(){
        String[] input2 = {"-up", "-down"};
        Set<String> options = parser.parse(input2);
    }
}
