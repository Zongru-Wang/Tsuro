package com.company.client;

import com.company.util.IOrderedPair;
import com.company.util.OrderedPair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ReachableQueryRequestTest {

    IOrderedPair<String> nodepair1;
    IOrderedPair<String> nodepair2;

    IOrderedPair<String> nodepair3;
    IOrderedPair<String> nodepair4;

    IOrderedPair<String> nodepair5;
    IOrderedPair<String> nodepair6;

    IOrderedPair<String> nodepair7;
    IOrderedPair<String> nodepair8;



    List<IOrderedPair<String>> nodeContext = new ArrayList<>();

    ReachableQueryRequest request;

    Set<String> colorContext= new HashSet<>();



    @Before
    public void setup() {

        this.nodepair1  = new OrderedPair<String>("1", "2");
        this.nodepair2 = new OrderedPair<String>("2", "1");
        this.nodepair3  = new OrderedPair<String>("1", "3");
        this.nodepair4 = new OrderedPair<String>("3", "1");
        this.nodepair5 = new OrderedPair<String>("2", "4");
        this.nodepair6 = new OrderedPair<String>("4", "2");
        this.nodepair7 = new OrderedPair<String>("3", "4");
        this.nodepair8 = new OrderedPair<String>("4", "3");

        this.nodeContext.add(this.nodepair1);
        this.nodeContext.add(nodepair2);
        this.nodeContext.add(nodepair3);
        this.nodeContext.add(nodepair4);
        this.nodeContext.add(nodepair5);
        this.nodeContext.add(nodepair6);
        this.nodeContext.add(nodepair7);
        this.nodeContext.add(nodepair8);

        this.colorContext.add("white");
        this.colorContext.add("red");
        this.colorContext.add("black");

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGivenColor() {
        new ReachableQueryRequest("blue", "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGivenColor1() {
        new ReachableQueryRequest("818727", "1");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGivenName() {
        new ReachableQueryRequest("red", "10");
    }

    @Test
    public void testGetJson() {
        System.out.println(this.nodeContext);
        System.out.println(this.colorContext);
       IReachableQueryRequest request = new ReachableQueryRequest("red", "1");
       assertEquals("[\"move\", token:red, name:1]", request.asJSON());

    }



}
