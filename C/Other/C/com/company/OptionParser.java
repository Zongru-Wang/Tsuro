package com.company;

import java.util.HashSet;
import java.util.Set;

public class OptionParser implements IOptionParser {
    @Override
    public Set<String> parse(String[] args) {
        if (args.length == 1) {
            HashSet<String> result = new HashSet<String>();
            if (args[0].equals("-up")) {
                result.add("u");
            } else if (args[0].equals(("-down"))) {
                result.add("d");
            } else {
                throw new IllegalArgumentException();
            }
            return result;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
