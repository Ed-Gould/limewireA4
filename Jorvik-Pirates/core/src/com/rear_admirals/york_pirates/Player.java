package com.rear_admirals.york_pirates;

import com.rear_admirals.york_pirates.screen.combat.attacks.*;
import com.rear_admirals.york_pirates.screen.combat.attacks.GrapeShot;

import java.util.ArrayList;
import java.util.List;

import static com.rear_admirals.york_pirates.College.*;
import static com.rear_admirals.york_pirates.ShipType.*;

/**
 * Player class handles the user's character in the game
 */
public class Player {
    private Ship playerShip;
    private int gold;
    private int points;
    //A4: parameters for new crew
    private int woods;
    private boolean engineer;
    private int collegeDefeated;
    public static List<Attack> attacks = new ArrayList<Attack>();

    /**
     * Basic Constructor
     */
    public Player() {
        //Altered For Assessment 3
        this.playerShip = new Ship(Player, "Your Ship", Derwent);
        //End Altered
        this.gold = 0;
        this.points = 0;
        this.woods = 0;
        this.engineer = false;
        this.collegeDefeated = 0;
        //A4: testing number for college defeated.
        //this.collegeDefeated = 1;

        attacks.add(Ram.attackRam);
        attacks.add(GrapeShot.attackSwivel);
        attacks.add(Attack.attackBoard);
    }

    /**
     * Constructor with ship
     * @param ship - the ship object to be the player's ship
     */
    public Player(Ship ship) {
        this.playerShip = ship;
        this.gold = 0;
        this.points = 0;
        this.woods = 0;
        this.engineer = false;
        this.collegeDefeated = 0;

        attacks.add(Ram.attackRam);
        attacks.add(Attack.attackSwivel);
        attacks.add(Attack.attackBoard);
    }

    public List<Attack> getAttacks(){
        return  attacks;
    }

    public Ship getPlayerShip() {
        return this.playerShip;
    }

    public int getPoints() {
        return points;
    }

    public int getGold() {
        return gold;
    }

    public int getWoods() { return  woods;}

    public int getCollegeDefeated() { return collegeDefeated;}

    public boolean isEngineer() {return engineer;}

    public void setPoints(int points) {
        this.points = points;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setWoods(int woods) { this.woods = woods;}

    public void setEngineer(boolean engineer){this.engineer = engineer;}

    /**
     * Used whenever the player makes a transaction
     * e.g buying an upgrade or wood for repairs
     *
     * @param value - the amount of gold to pay
     * @return boolean saying whether or not the transaction
     *          was successful
     */
    public boolean payGold(int value) {
        if (value > gold) {
            return false;
        } else {
            addGold(-value);
            return true;
        }
    }

    /**
     * Called when the user tries to make repairs
     * Repairs the player's ship if they have
     * at least 10 wood
     *
     * @return boolean - whether or not the repair was successful
     */
    public boolean useWoods() {
        if (10 > woods) {
            return false;
        } else {
            addWood(-10);
            playerShip.heal(50);
            return true;
        }
    }

    public void addPoints(int value) {
        points += value;
    }

    public void addGold(int value) {
        gold = gold + value;
    }

    public void addWood(int value) { woods = woods + value;}

    public void addCollegeDefeated() {collegeDefeated = collegeDefeated + 1;}
}