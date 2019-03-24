package com.rear_admirals.york_pirates;

import static java.lang.Math.max;
import static java.lang.Math.pow;

public class Department {

    private final String name;
    private String product;
    private int baseUpgradeCost, costPerLevel;
    private PirateGame pirateGame;
    private Ship playerShip;

    public Department(String name, String product, PirateGame pirateGame) {
        this.name = name;
        this.product = product;
        this.baseUpgradeCost = 40;
        this.costPerLevel = 10;
        this.pirateGame = pirateGame;
        this.playerShip = pirateGame.getPlayer().getPlayerShip();
    }


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
    // A4: Rework and rebalanced formula to calculate cost of upgrades
    public int getUpgradeCost() {
        if (product.equals("defence")) {
            return calculateCost(playerShip.getDefence());
        } else if (product.equals("attack")) {
            return calculateCost(playerShip.getAttack());
        } else if (product.equals("accuracy")) {
            return calculateCost(playerShip.getAccuracy());
        }
        return 0;
    }

    public int calculateCost(int upgradeStat){
        // 5 is the starting value for players stats
        return baseUpgradeCost + costPerLevel * (upgradeStat - 5);
    }
    // End of A4 change

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
    }
}