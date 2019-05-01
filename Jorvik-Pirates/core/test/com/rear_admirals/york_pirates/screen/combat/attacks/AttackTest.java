package com.rear_admirals.york_pirates.screen.combat.attacks;

import com.rear_admirals.york_pirates.GameTest;
import com.rear_admirals.york_pirates.Ship;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AttackTest extends GameTest {

    @Test
    public void doAttackTest() {
        // Use constructor with parameters to set accPercent = 100 so damage is always caused when required
        Attack attack = new Attack("testAttack", "Attack for unit tests", 3, 1, false, 100);
        Ship attacker = mock(Ship.class);
        Ship defender = mock(Ship.class);
        when(attacker.getHealth()).thenReturn(150);
        when(attacker.getAttack()).thenReturn(50); // high attack value to ensure damage occurs when required

        // set to -50 so doesHit always returns false to test behaviour when an attack misses
        when(attacker.getAccuracy()).thenReturn(-50);

        // if attack does not hit, no damage is caused
        assertEquals(0, attack.doAttack(attacker, defender));

        // Assume attack never misses from now
        // accuracy = 100 to ensure the attack causes damage from now
        when(attacker.getAccuracy()).thenReturn(100);

        assertNotEquals(0, attack.doAttack(attacker, defender));

        // Ensure damage is only ever applied to defending ship
        verify(attacker, never()).damage(anyInt());
        verify(defender).damage(anyInt());
    }
}