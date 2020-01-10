package com.company.util;

/**
 * Represents a ordered pair
 * @param <T> type in which this pair stores values
 */
public class OrderedPair<T> implements IOrderedPair<T> {
    private T first;
    private T second;
    public OrderedPair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return Represents a first element in this pair.
     */
    @Override
    public T first() {
        return this.first;
    }

    /**
     * @return Represents a second element in this pair.
     */
    @Override
    public T second() {
        return this.second;
    }
}
