package com.rear_admirals.york_pirates;

import com.rear_admirals.york_pirates.screen.combat.attacks.*;
import com.rear_admirals.york_pirates.screen.combat.attacks.GrapeShot;

import java.util.ArrayList;
import java.util.List;

import static com.rear_admirals.york_pirates.College.*;
import static com.rear_admirals.york_pirates.ShipType.*;

public class Player {
    private Ship playerShip;
    private int gold;
    private int points;
    //A4: parameters for new crew
    private int woods;
    private boolean engineer;
    public static List<Attack> attacks = new ArrayList<Attack>();

    public Player() {
        //Altered For Assessment 3
        this.playerShip = new Ship(Player, "Your Ship", Derwent);
        //End Altered
        this.gold = 1000;
        this.points = 0;
        this.woods = 0;
        this.engineer = false;

        attacks.add(Ram.attackRam);
        attacks.add(GrapeShot.attackSwivel);
        attacks.add(Attack.attackBoard);
    }

    public Player(Ship ship) {
        this.playerShip = ship;
        this.gold = 0;
        this.points = 0;

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

    public boolean isEngineer() {return engineer;}

    public void setPoints(int points) {
        this.points = points;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setWoods(int woods) { this.woods = woods;}

    public void setEngineer(boolean engineer){this.engineer = engineer;}

    public boolean payGold(int value) {
        if (value > gold) {
            return false;
        } else {
            addGold(-value);
            return true;
        }
    }

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
}