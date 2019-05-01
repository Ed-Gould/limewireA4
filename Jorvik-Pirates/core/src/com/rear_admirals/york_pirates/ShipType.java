package com.rear_admirals.york_pirates;

import com.badlogic.gdx.graphics.Texture;

public class ShipType {
    private int attack;
    private int defence;
    private int accuracy;
    //Added For Assessment 3
    private int goldValue;
    private int pointValue;
    //End Added
    private String name;
    private Texture texture;

    /**
     * ShipType Constructor
     *
     * @param name - name of ship type e.g Brig, Frigate
     * @param attack - strength of ship type's attacks
     * @param defence - strength of ship type's defence
     * @param accuracy - accuracy of ship type's weapons
     * @param health - default starting health of ship type
     * @param goldValue - the amount of gold the ship type is worth,
     *                  player will receive this amount if they defeat it
     * @param pointValue - points the player gains by defeating this ship type
     */
    //Altered For Assessment 3
    public ShipType(String name, int attack, int defence, int accuracy, int health, int goldValue, int pointValue) {
        this.name = name;
        this.attack = attack;
        this.defence = defence;
        this.accuracy = accuracy;
        this.goldValue = goldValue;
        this.pointValue = pointValue;
        this.texture = new Texture("ship4.png"); //TESTING (without assets created)
    } // There is currently no way to give ships a custom texture. Do we need this?
    //End Altered

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getAccuracy() {
        return accuracy;
    }

    //Added For Assessment 3
    public int getGoldValue() {
        return goldValue;
    }

    public int getPointValue() {
        return pointValue;
    }
    //End Added

    public Texture getTexture() {
        return texture;
    }

    // Static Ship Types go here
    //Altered For Assessment 3
    //Player and encountered enemy ships
    public static ShipType Enemy = new ShipType("Schooner", 4, 4, 5, 80, 40, 40);
    public static ShipType Player = new ShipType("Brig", 5, 5, 5, 100, 60, 60);

    //Testing player ship
    //public static ShipType Player = new ShipType("Brig", 25, 25, 25, 100, 60, 60);

    //College Bosses
    public static ShipType James = new ShipType("Galleon", 6, 6, 5, 110, 80, 80);
    public static ShipType Van = new ShipType("Frigate", 9, 6, 5, 120, 100, 100);
    public static ShipType Good = new ShipType("Man o' War", 6, 9, 5, 130, 120, 120);
    public static ShipType Lan = new ShipType("Leviathan", 10, 10, 5, 150, 140, 140);
    //End Altered
}