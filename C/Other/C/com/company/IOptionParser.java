package com.company;

import java.util.Set;

/**
 * Represents an object that parses a set of command line arguments.
 */
public interface IOptionParser {

    /**
     * Reads an array of command line arguments, throwing an IllegalArgumentException for
     * missing or superfluous arguments, as defined by the concrete implementation.
     *
     * @param args      a list of command line arguments
     * @return          a set of command line arguments
     */
    Set<String> parse(String[] args);
}
