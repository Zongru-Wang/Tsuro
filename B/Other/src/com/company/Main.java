package com.company;

import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * Entry point for the application
 * @param args      A list of arguments containing the single string "-up" or "down".
 *                  Throws an exception if more than one string is present, or if empty.
 */
public class Main {

    public static void main(String[] args) {
	    IOptionParser parser = new OptionParser();
	    INamedJsonObjectReader reader = new NamedJsonObjectReader();
        List<INamedJsonObject> objs = null;
        Set<String> options = null;
        try {
            options = parser.parse(args);
        } catch(IllegalArgumentException e) {
            System.out.println("Illegal Option was supplied");
        }

	    try {
            objs = reader.readAll(System.in);
        } catch (IllegalArgumentException e) {
	        System.out.println("Json provided was not valid");
        }
        Comparator<INamedJsonObject> INamedJsonObjectComparator = Comparator.comparing(INamedJsonObject::getName);
        if (options.contains("u")) {
            objs.sort(INamedJsonObjectComparator);
        } else if (options.contains("d")) {
            objs.sort(INamedJsonObjectComparator.reversed());
        } else {
            throw new IllegalStateException();
        }
	    for (INamedJsonObject obj : objs) {
	        System.out.println(obj.toString());
        }
    }
}
