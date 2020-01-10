package com.company;

/**
 * Represents a JSON object that is associated with a name.
 */
public interface INamedJsonObject {

    /**
     * Getter for the name of this object.
     * @return      the name of this INamedJsonObject
     */
    String getName();

    /**
     * Turns this INamedJsonObject into a String.
     * @return      this INamedJsonObject as a String
     */
    String toString();
}
