package com.rear_admirals.york_pirates;

import java.util.ArrayList;

/**
 * College class
 * Creates and handles colleges in the game
 */
public class College {

    private final String name;
    private ArrayList<College> ally;
    private boolean bossDead;

    /**
     * College Constructor
     *
     * @param name - name of the college
     */
    public College(String name) {
        this.name = name;
        this.ally = new ArrayList<College>();
        this.ally.add(this);
        this.bossDead = false;
    }

    public String getName() {
        return name;
    }

    public ArrayList<College> getAlly() {
        return ally;
    }

    public void addAlly(College newAlly) {
        ally.add(newAlly);
    }

    public boolean isBossDead() {
        return bossDead;
    }

    public void setBossDead(boolean bossDead) {
        this.bossDead = bossDead;
    }

    //Altered For Assessment 3
    //In-game Colleges
    public static College Derwent = new College("Derwent");
    public static College Vanbrugh = new College("Vanbrugh");
    public static College James = new College("James");
    public static College Goodricke = new College("Goodricke");
    public static College Langwith = new College("Langwith");
    //End Altered
}