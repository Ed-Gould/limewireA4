package com.rear_admirals.york_pirates;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerTest extends GameTest {

    @Mock
    Ship playerShip;

    // allows @Mock mocks to be used
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * Tests for the payGold() method
     */
    @Test
    public void payGoldNoGoldTest() {
        Player player = new Player(playerShip);

        // Player's gold = 0 by default
        assertFalse(player.payGold(1));
        assertFalse(player.payGold(10));
        assertFalse(player.payGold(50));
        assertTrue(player.payGold(0));
        assertEquals(0, player.getGold());
    }

    @Test
    public void payGoldFailedTest() {
        Player player = new Player(playerShip);

        player.setGold(50);

        assertFalse(player.payGold(51));
        assertFalse(player.payGold(70));
        assertFalse(player.payGold(100));
    }

    @Test
    public void payGoldSuccessfulTest() {
        Player player = new Player(playerShip);

        player.setGold(50);

        assertTrue(player.payGold(0));
        assertEquals(50, player.getGold());

        assertTrue(player.payGold(10));
        assertEquals(40, player.getGold());

        assertTrue(player.payGold(40));
        assertEquals(0, player.getGold());

        assertFalse(player.payGold(1));
    }

    /**
     * Test for the useWoods() method
     * Player needs at least 10 wood for
     * a successful useWoods() call
     */
    @Test
    public void useWoodsTest() {
        doNothing().when(playerShip).heal(anyInt());

        Player player = new Player(playerShip);

        // player has 0 wood by default
        assertFalse(player.useWoods());

        // test that any amount of wood under 10 is a fail
        for (int w = 1; w < 10; w++) {
            player.setWoods(w);
            assertFalse(player.useWoods());
        }

        // Now assume player has enough wood
        player.setWoods(10);
        assertTrue(player.useWoods());

        // player's wood should be 0 after the last useWoods call
        assertFalse(player.useWoods());

        // should only have enough wood for one repair
        player.setWoods(19);
        assertTrue(player.useWoods());
        assertFalse(player.useWoods());

        // so should now be able to repair 4 times before having too little wood
        player.setWoods(45);
        for (int i = 0; i < 4; i++) {
            assertTrue(player.useWoods());
        }
        assertFalse(player.useWoods());
    }
}