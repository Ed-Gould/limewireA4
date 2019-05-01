package com.rear_admirals.york_pirates;

import com.rear_admirals.york_pirates.screen.combat.attacks.Attack;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DepartmentTest extends GameTest {

    @Mock
    PirateGame pirateGame;

    // allows @Mock mocks to be used
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * Tests for purchase() method
     * renamed from buyUpgrade() in assessment 3
     */
    @Test
    public void purchaseNoGoldTest() {
        // Remove Player and Ship classes dependencies by mocking them
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));
        when(pirateGame.getPlayer().getPlayerShip().getDefence()).thenReturn(5); // default defence value for Ship

        // changed constructor from A3
        Department defenceDepartment = new Department("Derwent", "defence", pirateGame);

        // Assume player doesn't have enough gold
        when(pirateGame.getPlayer().payGold(anyInt())).thenReturn(false);

        // Should then return false
        assertFalse(defenceDepartment.purchase());
    }


    @Test
    public void purchaseDefenceTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));
        when(pirateGame.getPlayer().getPlayerShip().getDefence()).thenReturn(5);

        // From now, assume player has enough gold
        when(pirateGame.getPlayer().payGold(anyInt())).thenReturn(true);

        Department defenceDepartment = new Department("Derwent", "defence", pirateGame);
        assertTrue(defenceDepartment.purchase());

        // Verify that only addDefence should called as the department's upgrade is defence
        verify(pirateGame.getPlayer().getPlayerShip()).addDefence(anyInt());
        verify(pirateGame.getPlayer().getPlayerShip(), never()).addAttack(anyInt());
        verify(pirateGame.getPlayer().getPlayerShip(), never()).addAccuracy(anyInt());
    }


    @Test
    public void purchaseAttackTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));

        when(pirateGame.getPlayer().payGold(anyInt())).thenReturn(true);

        Department attackDepartment = new Department("Vanbrugh", "attack", pirateGame);
        assertTrue(attackDepartment.purchase());

        // Only addAttack should have been called
        verify(pirateGame.getPlayer().getPlayerShip(), never()).addDefence(anyInt());
        verify(pirateGame.getPlayer().getPlayerShip()).addAttack(anyInt());
        verify(pirateGame.getPlayer().getPlayerShip(), never()).addAccuracy(anyInt());
    }

    @Test
    public void purchaseAccuracyTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));

        when(pirateGame.getPlayer().payGold(anyInt())).thenReturn(true);

        Department accuracyDepartment = new Department("James", "accuracy", pirateGame);
        assertTrue(accuracyDepartment.purchase());

        // Only addAccuracy should have been called
        verify(pirateGame.getPlayer().getPlayerShip(), never()).addDefence(anyInt());
        verify(pirateGame.getPlayer().getPlayerShip(), never()).addAttack(anyInt());
        verify(pirateGame.getPlayer().getPlayerShip()).addAccuracy(anyInt());
    }

    /**
     * Tests for getUpgrade() method
     */
    @Test
    public void getUpgradeCostNoUpgradeTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));

        Department noneDepartment = new Department("Wentworth", "", pirateGame);
        // Should return 0 if the department has no upgrade to offer
        assertEquals(0, noneDepartment.getUpgradeCost());
    }

    @Test
    public void getUpgradeCostDefenceTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));
        when(pirateGame.getPlayer().getPlayerShip().getDefence()).thenReturn(5);

        Department defenceDepartment = new Department("Derwent", "defence", pirateGame);
        defenceDepartment.getUpgradeCost();

        verify(pirateGame.getPlayer().getPlayerShip()).getDefence();
        verify(pirateGame.getPlayer().getPlayerShip(), never()).getAttack();
        verify(pirateGame.getPlayer().getPlayerShip(), never()).getAccuracy();
    }

    @Test
    public void getUpgradeCostAttackTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));
        when(pirateGame.getPlayer().getPlayerShip().getAttack()).thenReturn(1); // default atkMultiplier value

        Department attackDepartment = new Department("Vanbrugh", "attack", pirateGame);
        attackDepartment.getUpgradeCost();

        verify(pirateGame.getPlayer().getPlayerShip(), never()).getDefence();
        verify(pirateGame.getPlayer().getPlayerShip()).getAttack();
        verify(pirateGame.getPlayer().getPlayerShip(), never()).getAccuracy();
    }

    @Test
    public void getUpgradeCostAccuracyTest() {
        when(pirateGame.getPlayer()).thenReturn(mock(Player.class));
        when(pirateGame.getPlayer().getPlayerShip()).thenReturn(mock(Ship.class));
        when(pirateGame.getPlayer().getPlayerShip().getAccuracy()).thenReturn(1); // default accMultiplier

        Department accuracyDepartment = new Department("James", "accuracy", pirateGame);
        accuracyDepartment.getUpgradeCost();

        verify(pirateGame.getPlayer().getPlayerShip(), never()).getDefence();
        verify(pirateGame.getPlayer().getPlayerShip(), never()).getAttack();
        verify(pirateGame.getPlayer().getPlayerShip()).getAccuracy();
    }
}