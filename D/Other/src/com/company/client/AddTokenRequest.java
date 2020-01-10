package com.company.client;

import com.company.util.IOrderedPair;
import com.company.util.OrderedPair;

import java.util.List;

/**
 *  Represents user request that is in form of "["add" , token:color-string, name:string]
 */
public class AddTokenRequest implements IAddTokenRequest {
    private String color;
    private String name;
    /**
     * Construct a AddTokenRequest that those parameters repesents
     * @param color token:color-string this request represents.
     * @param name name:string this request represents.
     * @throws IllegalArgumentException must be thrown when validation fails.
     */
    AddTokenRequest(String color, String name) throws IllegalArgumentException {
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
        return true;
    }

    /**
     * @return whether this request is reachable query request
     */
    @Override
    public boolean isReachableQuery() {
        return false;
    }

    /**
     * @return JSON string that reconstructed to simulate the actual user input in accordance to
     * https://vesely.io/teaching/CS4500f19/C/C.html, Task3, Diagram 1, 2, 3.
     */
    @Override
    public String asJSON() {
        return String.format("[\"add\", \"%s\",\"%s\"]", this.color, this.name);
    }
}
