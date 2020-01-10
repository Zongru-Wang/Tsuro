package com.company;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectivityOptionTest {
    private String DEFAULT_IP = "127.0.0.1";
    private int DEFAULT_PORT = 8000;
    private String DEFAULT_NAME = "John Doe";

    private String[] noArgument;
    private IConnectivityOption noArgumentConnectivityOption;

    private String[] oneValidArgument1;
    private String[] oneValidArgument2;
    private String[] oneValidArgument3;
    private IConnectivityOption oneValidArgument1ConnectivityOption;
    private IConnectivityOption oneValidArgument2ConnectivityOption;
    private IConnectivityOption oneValidArgument3ConnectivityOption;
    private String[] oneInvalidArgument1;
    private String[] oneInvalidArgument2;
    private String[] oneInvalidArgument3;
    private String[] oneInvalidArgument4;
    private String[] oneInvalidArgument5;
    private String[] oneInvalidArgument6;
    private String[] oneInvalidArgument7;

    private String[] twoValidArgument1;
    private String[] twoValidArgument2;
    private String[] twoValidArgument3;
    private String[] twoValidArgument4;
    private String[] twoValidArgument5;
    private IConnectivityOption twoValidArgument1ConnectivityOption;
    private IConnectivityOption twoValidArgument2ConnectivityOption;
    private IConnectivityOption twoValidArgument3ConnectivityOption;
    private IConnectivityOption twoValidArgument4ConnectivityOption;
    private IConnectivityOption twoValidArgument5ConnectivityOption;
    private String[] twoInvalidArgument1;
    private String[] twoInvalidArgument2;
    private String[] twoInvalidArgument3;
    private String[] twoInvalidArgument4;
    private String[] twoInvalidArgument5;
    private String[] twoInvalidArgument6;
    private String[] twoInvalidArgument7;
    private String[] twoInvalidArgument8;
    private String[] twoInvalidArgument9;
    private String[] twoInvalidArgument10;
    private String[] twoInvalidArgument11;
    private String[] twoInvalidArgument12;

    private String[] threeValidArgument1;
    private String[] threeValidArgument2;
    private String[] threeValidArgument3;
    private String[] threeValidArgument4;
    private String[] threeValidArgument5;
    private String[] threeValidArgument6;
    private String[] threeValidArgument7;
    private String[] threeValidArgument8;
    private IConnectivityOption threeValidArgument1ConnectivityOption;
    private IConnectivityOption threeValidArgument2ConnectivityOption;
    private IConnectivityOption threeValidArgument3ConnectivityOption;
    private IConnectivityOption threeValidArgument4ConnectivityOption;
    private IConnectivityOption threeValidArgument5ConnectivityOption;
    private IConnectivityOption threeValidArgument6ConnectivityOption;
    private IConnectivityOption threeValidArgument7ConnectivityOption;
    private IConnectivityOption threeValidArgument8ConnectivityOption;
    private String[] threeInvalidArgument1;
    private String[] threeInvalidArgument2;
    private String[] threeInvalidArgument3;
    private String[] threeInvalidArgument4;
    private String[] threeInvalidArgument5;
    private String[] threeInvalidArgument6;
    private String[] threeInvalidArgument7;
    private String[] threeInvalidArgument8;
    private String[] threeInvalidArgument9;
    private String[] threeInvalidArgument10;
    private String[] threeInvalidArgument11;
    private String[] threeInvalidArgument12;
    private String[] threeInvalidArgument13;
    private String[] threeInvalidArgument14;
    private String[] threeInvalidArgument15;
    private String[] threeInvalidArgument16;
    private String[] threeInvalidArgument17;
    private String[] threeInvalidArgument18;

    private String[] multipleValidArgument;
    private IConnectivityOption multipleValidArgumentConnectivityOption;
    @Before
    public void setUp() {
        this.noArgument = new String[0];
        this.noArgumentConnectivityOption = new ConnectivityOption(this.noArgument);
        this.oneValidArgument1 = new String[] {"167.234.0.4"};
        this.oneValidArgument2 = new String[] {"255.255.255.255"};
        this.oneValidArgument3 = new String[] {"0.0.0.0"};
        this.oneValidArgument1ConnectivityOption = new ConnectivityOption(this.oneValidArgument1);
        this.oneValidArgument2ConnectivityOption = new ConnectivityOption(this.oneValidArgument2);
        this.oneValidArgument3ConnectivityOption = new ConnectivityOption(this.oneValidArgument3);
        this.oneInvalidArgument1 = new String[] {"invalid"};
        this.oneInvalidArgument2 = new String[] {"256.256.256.256"};
        this.oneInvalidArgument3 = new String[] {"-1.-1.-1.-1"};
        this.oneInvalidArgument4 = new String[] {"0.0.0.0.0"};
        this.oneInvalidArgument5 = new String[] {"127.0.0 .1"};
        this.oneInvalidArgument6 = new String[] {"127.0.0.1 "};
        this.oneInvalidArgument7 = new String[] {""};
        this.twoValidArgument1 = new String[] {"167.237.0.4", "3500"};
        this.twoValidArgument2 = new String[] {"0.0.0.0", "1"};
        this.twoValidArgument3 = new String[] {"0.0.0.0", "65535"};
        this.twoValidArgument4 = new String[] {"255.255.255.255", "1"};
        this.twoValidArgument5 = new String[] {"255.255.255.255", "65535"};
        this.twoValidArgument1ConnectivityOption = new ConnectivityOption(this.twoValidArgument1);
        this.twoValidArgument2ConnectivityOption = new ConnectivityOption(this.twoValidArgument2);
        this.twoValidArgument3ConnectivityOption = new ConnectivityOption(this.twoValidArgument3);
        this.twoValidArgument4ConnectivityOption = new ConnectivityOption(this.twoValidArgument4);
        this.twoValidArgument5ConnectivityOption = new ConnectivityOption(this.twoValidArgument5);
        this.twoInvalidArgument1 = new String[] {"invalid", "invalid"};
        this.twoInvalidArgument2 = new String[] {"167.237.0.4", "0"};
        this.twoInvalidArgument3 = new String[] {"167.237.0.4", "65536"};
        this.twoInvalidArgument4 = new String[] {"167.237.0.4", ""};
        this.twoInvalidArgument5 = new String[] {"167.237.0.4", "invalid"};
        this.twoInvalidArgument6 = new String[] {"167.237.0.4", "0.2"};
        this.twoInvalidArgument7 = new String[] {"167.237.0.4", "FF"};
        this.twoInvalidArgument8 = new String[] {"256.256.256.256", "0"};
        this.twoInvalidArgument9 = new String[] {"-1.-1.-1.-1", "65536"};
        this.twoInvalidArgument10 = new String[] {"0.0.0.0.0", ""};
        this.twoInvalidArgument11 = new String[] {"127.0.0 .1", "invalid"};
        this.twoInvalidArgument12 = new String[] {"127.0.0.1 ", "0.2"};
        this.threeValidArgument1 = new String[] {"167.237.0.4", "3500", "John"};
        this.threeValidArgument2 = new String[] {"0.0.0.0", "3500", "John"};
        this.threeValidArgument3 = new String[] {"255.255.255.255", "3500", "John"};
        this.threeValidArgument4 = new String[] {"167.237.0.4", "1", "John"};
        this.threeValidArgument5 = new String[] {"167.237.0.4", "65535", "John"};
        this.threeValidArgument6 = new String[] {"255.255.255.255", "1", "J"};
        this.threeValidArgument7 = new String[] {"0.0.0.0", "65535", "J"};
        this.threeValidArgument8 = new String[] {"255.255.255.255", "65535", "John"};
        this.threeValidArgument1ConnectivityOption = new ConnectivityOption(this.threeValidArgument1);
        this.threeValidArgument2ConnectivityOption = new ConnectivityOption(this.threeValidArgument2);
        this.threeValidArgument3ConnectivityOption = new ConnectivityOption(this.threeValidArgument3);
        this.threeValidArgument4ConnectivityOption = new ConnectivityOption(this.threeValidArgument4);
        this.threeValidArgument5ConnectivityOption = new ConnectivityOption(this.threeValidArgument5);
        this.threeValidArgument6ConnectivityOption = new ConnectivityOption(this.threeValidArgument6);
        this.threeValidArgument7ConnectivityOption = new ConnectivityOption(this.threeValidArgument7);
        this.threeValidArgument8ConnectivityOption = new ConnectivityOption(this.threeValidArgument8);
        this.threeInvalidArgument1 = new String [] {"invalid", "invalid", ""};
        this.threeInvalidArgument2 = new String [] {"167.237.0.4", "3500", ""};
        this.threeInvalidArgument3 = new String[] {"0.0.0.0", "3500", ""};
        this.threeInvalidArgument4 = new String[] {"255.255.255.255", "3500", ""};
        this.threeInvalidArgument5 = new String[] {"167.237.0.4", "1", ""};
        this.threeInvalidArgument6 = new String[] {"167.237.0.4", "65535", ""};
        this.threeInvalidArgument7 = new String[] {"255.255.255.255", "1", ""};
        this.threeInvalidArgument8 = new String[] {"0.0.0.0", "65535", ""};
        this.threeInvalidArgument9 = new String[] {"256.256.256.256", "65536", "John"};
        this.threeInvalidArgument10 = new String[] {"", "3500", "John"};
        this.threeInvalidArgument11 = new String[] {"255.1.255.ff", "1" ,"John"};
        this.threeInvalidArgument12 = new String[] {"0 .0.0.0", "3500", "John"};
        this.threeInvalidArgument13 = new String[] {"invalid", "3500", "John"};
        this.threeInvalidArgument14 = new String[] {"167.237.0.4", "0", "John"};
        this.threeInvalidArgument15 = new String[] {"167.237.0.4", "655356", "John"};
        this.threeInvalidArgument16 = new String[] {"255.255. 255.255", "0", "J"};
        this.threeInvalidArgument17 = new String[] {"0.0.0.0 ", "65535", "J"};
        this.threeInvalidArgument18 = new String[] {"255.255.355.255", "65536", "John"};
        this.multipleValidArgument = new String[] {"167.237.0.4", "3500", "Jane", "Doe"};
        this.multipleValidArgumentConnectivityOption = new ConnectivityOption(this.multipleValidArgument);
    }

    @Test
    public void NoArgumentOptionShouldReturnDefaultValues() {
        assertEquals(this.DEFAULT_IP, this.noArgumentConnectivityOption.getIP());
        assertEquals(this.DEFAULT_PORT, this.noArgumentConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.noArgumentConnectivityOption.getName());
    }

    @Test
    public void OneValidArgument1OptionShouldReturnCorrectIP() {
        assertEquals("167.234.0.4", this.oneValidArgument1ConnectivityOption.getIP());
        assertEquals(this.DEFAULT_PORT, this.oneValidArgument1ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.oneValidArgument1ConnectivityOption.getName());
    }

    @Test
    public void OneValidArgument2OptionShouldReturnCorrectIP() {
        assertEquals("255.255.255.255", this.oneValidArgument2ConnectivityOption.getIP());
        assertEquals(this.DEFAULT_PORT, this.oneValidArgument2ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.oneValidArgument2ConnectivityOption.getName());
    }

    @Test
    public void OneValidArgument3OptionShouldReturnCorrectIP() {
        assertEquals("0.0.0.0", this.oneValidArgument3ConnectivityOption.getIP());
        assertEquals(this.DEFAULT_PORT, this.oneValidArgument3ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.oneValidArgument3ConnectivityOption.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument1OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument2OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument2);
    }


    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument3OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument3);
    }


    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument4OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument4);
    }


    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument5OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument5);
    }


    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument6OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void OneInvalidArgument7OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.oneInvalidArgument7);
    }

    @Test
    public void TwoValidArgument1ShouldReturnCorrectIPAndPort () {
        assertEquals("167.237.0.4", this.twoValidArgument1ConnectivityOption.getIP());
        assertEquals(3500, this.twoValidArgument1ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.twoValidArgument1ConnectivityOption.getName());
    }

    @Test
    public void TwoValidArgument2ShouldReturnCorrectIPAndPort () {
        assertEquals("0.0.0.0", this.twoValidArgument2ConnectivityOption.getIP());
        assertEquals(1, this.twoValidArgument2ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.twoValidArgument2ConnectivityOption.getName());
    }

    @Test
    public void TwoValidArgument3ShouldReturnCorrectIPAndPort () {
        assertEquals("0.0.0.0", this.twoValidArgument3ConnectivityOption.getIP());
        assertEquals(65535, this.twoValidArgument3ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.twoValidArgument3ConnectivityOption.getName());
    }

    @Test
    public void TwoValidArgument4ShouldReturnCorrectIPAndPort () {
        assertEquals("255.255.255.255", this.twoValidArgument4ConnectivityOption.getIP());
        assertEquals(1, this.twoValidArgument4ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.twoValidArgument4ConnectivityOption.getName());
    }

    @Test
    public void TwoValidArgument5ShouldReturnCorrectIPAndPort () {
        assertEquals("255.255.255.255", this.twoValidArgument5ConnectivityOption.getIP());
        assertEquals(65535, this.twoValidArgument5ConnectivityOption.getPort());
        assertEquals(this.DEFAULT_NAME, this.twoValidArgument5ConnectivityOption.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument1OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument2OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument3OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument4OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument5OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument6OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument7OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument8OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument9OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument10OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument11OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TwoInvalidArgument12OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.twoInvalidArgument12);
    }

    @Test
    public void ThreeValidArgument1ShouldReturnCorrectIPPortAndName () {
        assertEquals("167.237.0.4", this.threeValidArgument1ConnectivityOption.getIP());
        assertEquals(3500, this.threeValidArgument1ConnectivityOption.getPort());
        assertEquals("John", this.threeValidArgument1ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument2ShouldReturnCorrectIPPortAndName () {
        assertEquals("0.0.0.0", this.threeValidArgument2ConnectivityOption.getIP());
        assertEquals(3500, this.threeValidArgument2ConnectivityOption.getPort());
        assertEquals("John", this.threeValidArgument2ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument3ShouldReturnCorrectIPPortAndName () {
        assertEquals("255.255.255.255", this.threeValidArgument3ConnectivityOption.getIP());
        assertEquals(3500, this.threeValidArgument3ConnectivityOption.getPort());
        assertEquals("John", this.threeValidArgument3ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument4ShouldReturnCorrectIPPortAndName () {
        assertEquals("167.237.0.4", this.threeValidArgument4ConnectivityOption.getIP());
        assertEquals(1, this.threeValidArgument4ConnectivityOption.getPort());
        assertEquals("John", this.threeValidArgument4ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument5ShouldReturnCorrectIPPortAndName () {
        assertEquals("167.237.0.4", this.threeValidArgument5ConnectivityOption.getIP());
        assertEquals(65535, this.threeValidArgument5ConnectivityOption.getPort());
        assertEquals("John", this.threeValidArgument5ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument6ShouldReturnCorrectIPPortAndName () {
        assertEquals("255.255.255.255", this.threeValidArgument6ConnectivityOption.getIP());
        assertEquals(1, this.threeValidArgument6ConnectivityOption.getPort());
        assertEquals("J", this.threeValidArgument6ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument7ShouldReturnCorrectIPPortAndName () {
        assertEquals("0.0.0.0", this.threeValidArgument7ConnectivityOption.getIP());
        assertEquals(65535, this.threeValidArgument7ConnectivityOption.getPort());
        assertEquals("J", this.threeValidArgument7ConnectivityOption.getName());
    }

    @Test
    public void ThreeValidArgument8ShouldReturnCorrectIPPortAndName () {
        assertEquals("255.255.255.255", this.threeValidArgument8ConnectivityOption.getIP());
        assertEquals(65535, this.threeValidArgument8ConnectivityOption.getPort());
        assertEquals("John", this.threeValidArgument8ConnectivityOption.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument1OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument2OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument3OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument4OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument5OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument6OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument7OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument8OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument9OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument10OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument11OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument12OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument13OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument13);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument14OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument15OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument16OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument16);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument17OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument17);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ThreeInvalidArgument18OptionShouldThrowOnConstruction() {
        new ConnectivityOption(this.threeInvalidArgument18);
    }

    @Test
    public void MultipleValidArgumentShouldReturnCorrectIPPortAndName () {
        assertEquals("167.237.0.4", this.multipleValidArgumentConnectivityOption.getIP());
        assertEquals(3500, this.multipleValidArgumentConnectivityOption.getPort());
        assertEquals("Jane Doe", this.multipleValidArgumentConnectivityOption.getName());
    }
}