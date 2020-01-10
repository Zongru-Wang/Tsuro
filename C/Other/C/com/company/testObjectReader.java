package com.company;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class testObjectReader {


    String input = "[\"b\", 1, 2, 3]\n" +
            "\n" +
            "          \"a\"   \n" +
            "        \n" +
            "        {\"this\" : \"c\",\n" +
            "\n" +
            "             \"other\" : 0}";

   
    INamedJsonObjectReader reader = new NamedJsonObjectReader();
    List<INamedJsonObject> objs = null;

    @Test(expected = IllegalArgumentException.class)
    // Test for empty JsonArray input
    public void testJsonReaderEmptyJsonArray() {
        String input3 = "[]";
        InputStream inputStream1 = new ByteArrayInputStream(input3.getBytes());
        Scanner scanner1 = new Scanner(inputStream1).useDelimiter("");
        objs = reader.readAll(scanner1);
    }


    @Test(expected = IllegalArgumentException.class)
    // Test for empty Json input
    public void testEmptyJsonString() {
        String input4 = "";
        InputStream inputStream2 = new ByteArrayInputStream(input4.getBytes());
        Scanner scanner2 = new Scanner(inputStream2).useDelimiter("");
        objs = reader.readAll(scanner2);
    }

    @Test(expected = IllegalArgumentException.class)
    // Test for empty Json input
    public void testEmptyJsonObject() {
        String input4 = "{}";
        InputStream inputStream2 = new ByteArrayInputStream(input4.getBytes());
        Scanner scanner2 = new Scanner(inputStream2).useDelimiter("");
        objs = reader.readAll(scanner2);
    }

    @Test
    public void testJasonString() {

        InputStream inputStream4 = new ByteArrayInputStream(input.getBytes());
        Scanner scanner4 = new Scanner(inputStream4).useDelimiter("");
        objs = reader.readAll(scanner4);
        assertEquals("[[\"b\",1,2,3], \"a\", {\"this\":\"c\",\"other\":0}]", objs.toString());
    }

    @Test
    public void testJasonString1() {
        String input = "[\"b\"]\n" +
                "\n" +
                "          \"a\"   \n" +
                "        \n" +
                "        {\"this\" : \"c\",\n" +
                "\n" +
                "             \"other\" : 0}";
        InputStream inputStream4 = new ByteArrayInputStream(input.getBytes());
        Scanner scanner4 = new Scanner(inputStream4).useDelimiter("");
        objs = reader.readAll(scanner4);
        assertEquals("[[\"b\"], \"a\", {\"this\":\"c\",\"other\":0}]", objs.toString());
    }
}
