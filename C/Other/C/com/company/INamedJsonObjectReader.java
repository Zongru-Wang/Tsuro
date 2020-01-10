package com.company;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a Json reader.
 */
public interface INamedJsonObjectReader {

    /**
     * Reads an input stream and converts it into a list of INamedJsonObjects if it is
     * correctly formed.
     *
     * A Json string is correctly formed if and only if
     *  - the string specified valid Json.
     *  - each object within the Json has a key "this" with a String value.
     *  - each array within the Json has a String as its first element.
     *
     * An IllegalArgumentException is thrown if the Json is not correctly formed.
     *
     * @param in    an InputStream containing a Json object as a String
     * @return      a list of INamedJsonObjects
     */

    List<INamedJsonObject> readAll(Scanner scanner);
}
