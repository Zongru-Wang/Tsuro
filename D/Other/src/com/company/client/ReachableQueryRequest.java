package com.company.client;

import com.company.util.IOrderedPair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Represents user request that is in form of "["move", token:color-string, name:string]"
 */
public class ReachableQueryRequest implements IReachableQueryRequest{

    private String color;
    private String name;

    private Set<String> defaultColorSet = new HashSet<String>();

    /**
     * Construct a AddTokenRequest that those parameters repesents
     * @param color token:color-string this request represents.
     * @param name name:string this request represents.
     * @throws IllegalArgumentException must be thrown when validation fails.
     */
    ReachableQueryRequest(String color, String name) {
        if (!(color.equals("white") ||
                color.equals("black") ||
                color.equals("red") ||
                color.equals("green") ||
                color.equals("blue"))) {
            throw new IllegalArgumentException();
        }
        this.color = color;
        this.name = name;
    }

    /**
     * @return whether this request is create labyrinth request
     */
    @Override
    public boolean isCreateLabyrinth() {
        return false;
    }

    /**
     * @return whether this request is add token request
     */
    @Override
    public boolean isAddToken() {
        return false;
    }

    /**
     * @return whether this request is reachable query request
     */
    @Override
    public boolean isReachableQuery() {
        return true;
    }

    /**
     * @return JSON string that reconstructed to simulate the actual user input in accordance to
     * https://vesely.io/teaching/CS4500f19/C/C.html, Task3, Diagram 1, 2, 3.
     */
    @Override
    public String asJSON() {
        return String.format("[\"move\", \"%s\", \"%s\"]", this.color, this.name);
    }
}
