package com.company.client;

/**
 * Represents a user request"
 */
public interface IUserRequest {


    /**
     * @return whether this request is create labyrinth request
     */
    boolean isCreateLabyrinth();

    /**
     * @return whether this request is add token request
     */
    boolean isAddToken();


    /**
     * @return whether this request is reachable query request
     */
    boolean isReachableQuery();

    /**
     * @return JSON string that reconstructed to simulate the actual user input in accordance to
     * https://vesely.io/teaching/CS4500f19/C/C.html, Task3, Diagram 1, 2, 3.
     */
    String asJSON();
}
