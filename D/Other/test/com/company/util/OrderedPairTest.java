package com.company.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderedPairTest {
    private String str1 = "a";
    private String str2 = "b";
    private IOrderedPair<String> pair1;
    private IOrderedPair<String> pair2;
    private IOrderedPair<String> pair3;
    private IOrderedPair<String> pair4;
    @Before
    public void setUp() throws Exception {
        this.pair1 = new OrderedPair<String>(str1, str2);
        this.pair2 = new OrderedPair<String>(str1, null);
        this.pair3 = new OrderedPair<String>(null, str2);
        this.pair4 = new OrderedPair<String>(null, null);
    }

    @Test
    public void ValuesOrderedPairConstructedWithMustEqualWhenRead() {
        assertEquals(str1, this.pair1.first());
        assertEquals(str2, this.pair1.second());
        assertEquals(str1, this.pair2.first());
        assertNull(this.pair2.second());
        assertNull(this.pair3.first());
        assertEquals(str2, this.pair3.second());
        assertNull(this.pair4.first());
        assertNull(this.pair4.second());
    }
}