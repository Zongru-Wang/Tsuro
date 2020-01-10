package com.company.util;

/**
 * Represents a ordered pair
 * @param <T> type in which this pair stores values
 */
public interface IOrderedPair<T> {

    /**
     * @return Represents a first element in this pair.
     */
    T first();

    /**
     * @return Represents a second element in this pair.
     */
    T second();
}
