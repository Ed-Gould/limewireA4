package com.rear_admirals.york_pirates.screen.combat.attacks;

import com.rear_admirals.york_pirates.Ship;

/**
 * Engineer's repair ability
 */
public class Repair extends Attack {

    protected Repair() {
        this.name = "REPAIR";
        this.desc = "Repair your ship for 50hp with 10 wood pieces.";
    }

    @Override
    public int doAttack(Ship attacker, Ship defender) {
        return 1;
    }

    public static final Repair attackRepair = new Repair();
}