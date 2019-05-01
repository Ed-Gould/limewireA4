package com.rear_admirals.york_pirates.screen.combat.attacks;

import com.rear_admirals.york_pirates.GameTest;
import com.rear_admirals.york_pirates.Ship;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class GrapeShotTest extends GameTest {

    @Test
    public void doAttackTest() {
        GrapeShot grapeShot = new GrapeShot("Grape Shot","Fire four very weak cannonballs. ",3 ,5,false, 100);
        Ship attacker = mock(Ship.class);
        Ship defender = mock(Ship.class);
        when(attacker.getHealth()).thenReturn(150);
        when(attacker.getAttack()).thenReturn(50);

        // set to -50 so doesHit always returns false to test behaviour when an attack misses
        when(attacker.getAccuracy()).thenReturn(-50);

        // if attack does not hit, no damage is caused
        assertEquals(0, grapeShot.doAttack(attacker, defender));

        // Assume attack never misses from now
        // accuracy = 100 to ensure the attack causes damage from now
        when(attacker.getAccuracy()).thenReturn(100);

        assertNotEquals(0, grapeShot.doAttack(attacker, defender));

        // Ensure damage is only applied to defending ship
        verify(attacker, never()).damage(anyInt());
        verify(defender, times(2)).damage(anyInt()); //second time damage is called in this test method
    }
}