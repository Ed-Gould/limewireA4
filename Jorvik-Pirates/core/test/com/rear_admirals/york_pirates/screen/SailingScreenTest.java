package com.rear_admirals.york_pirates.screen;

import com.rear_admirals.york_pirates.GameTest;
import org.junit.Test;

import static org.junit.Assert.*;


public class SailingScreenTest extends GameTest {

    @Test
    public void capitaliseFirstLetterTest() {
        SailingScreen screen = new SailingScreen();

        // should return original if passed an empty string
        assertEquals("", screen.capitalizeFirstLetter(""));

        assertEquals("A", screen.capitalizeFirstLetter("a"));
        assertEquals("Lorem ipsum", screen.capitalizeFirstLetter("lorem ipsum"));

        assertEquals("Hello", screen.capitalizeFirstLetter("hello"));
    }
}