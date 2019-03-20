package com.rear_admirals.york_pirates;

import static java.lang.Math.max;
import static java.lang.Math.pow;

public class Department {

    private final String name;
    private String product;
    private int baseUpgradeCost;
    private PirateGame pirateGame;

    public Department(String name, String product, PirateGame pirateGame) {
        this.name = name;
        this.product = product;
        this.baseUpgradeCost = 10;
        this.pirateGame = pirateGame;
    }

    //Altered For Assessment 3

    /**
     * Upgrade ship by spending gold.
     * @return - Boolean, purchase successful
     */
    public boolean purchase() {
        if (pirateGame.getPlayer().payGold(getUpgradeCost())) {
            Ship playerShip = pirateGame.getPlayer().getPlayerShip();
            if (product.equals("defence")) {
                playerShip.addDefence(1);
                return true;
            } else if (product.equals("attack")) {
                playerShip.addAttack(1);
                return true;
            } else if (product.equals("accuracy")) {
                playerShip.addAccuracy(1);
                return true;
            }
        }return false;}

    /**
     * Get the price of an upgrade from this department, taking into account current upgrade level
     * @return price of upgrade
     */
    public int getPrice() {
        if (product.equals("defence")) {
            return (int) (baseUpgradeCost * pow(2, max(0, pirateGame.getPlayer().getPlayerShip().getDefence() - 3)));
        } else if (product.equals("attack")) {
            return (int) (baseUpgradeCost * pow(2, max(0, pirateGame.getPlayer().getPlayerShip().getAttack() - 3)));
        } else {
            throw new IllegalArgumentException("Invalid Department Product");
        }
    }

    public int getUpgradeCost() {
        if (product.equals("defence")) {
            return (int) (baseUpgradeCost * pow(2, max(0, pirateGame.getPlayer().getPlayerShip().getDefence() - 3)));
        } else if (product.equals("attack")) {
            return (int) (baseUpgradeCost * pow(2, max(0, pirateGame.getPlayer().getPlayerShip().getAttack() - 3)));
        } else if (product.equals("accuracy")) {
            return (int) (baseUpgradeCost * pow(2, max(0, pirateGame.getPlayer().getPlayerShip().getAccuracy() - 3)));
        }
        return 0;
    }
    //End Altered

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
    }
}