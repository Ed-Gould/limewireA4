package com.rear_admirals.york_pirates;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShipTest extends GameTest{

    @Test
    public void healTest() {
        // use basic constructor to avoid having to define all the LibGDX objects
        Ship ship = new Ship();

        assertEquals(ship.getHealthMax(), 100); // assume healthMax = 100 for the test

        // ship health = 100 by default
        // test that healing by 0 doesn't change anything
        ship.heal(0);
        assertEquals(ship.getHealth(), 100);

        //
        ship.setHealth(50);
        ship.heal(20);
        assertEquals(ship.getHealth(), 70);

        ship.heal(50);
        assertEquals(ship.getHealth(), 100);
    }
}