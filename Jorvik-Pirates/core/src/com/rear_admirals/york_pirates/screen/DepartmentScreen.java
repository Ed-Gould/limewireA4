package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.*;
import com.rear_admirals.york_pirates.base.BaseScreen;

public class DepartmentScreen extends BaseScreen {

    private Player player;

    private Label pointsValueLabel, pointsTextLabel;
    private Label goldValueLabel, goldTextLabel;
    private Label healthTextLabel, healthValueLabel;
    private int toHeal;
    private int healthFromMax;

    public DepartmentScreen(final PirateGame main, final Department department) {
        super(main);
        player = main.getPlayer();
        // A4: Added variable to get the amount of health required to heal to maximum
        this.healthFromMax = player.getPlayerShip().getHealthFromMax();
        // End of A4 change

        // A4: Cleaned up code for UI table, added display for health
        Table uiTable = new Table();

        /* Creates labels for the health, gold, and points display.
        These displays are separated into two labels each:
        A "TextLabel": These labels are composed of a text element (either the world "Points" or "Gold")
        A "ValueLabel": These labels are the integer value associated to the Text Labels (e.g. 40 for gold)
        */

        healthTextLabel = new Label("Health: ", main.getSkin());
        healthValueLabel = new Label(Integer.toString(main.getPlayer().getPlayerShip().getHealth()), main.getSkin());
        healthValueLabel.setAlignment(Align.left);

        goldTextLabel = new Label("Gold: ", main.getSkin());
        goldValueLabel = new Label(Integer.toString(main.getPlayer().getGold()), main.getSkin());
        goldValueLabel.setAlignment(Align.left);

        pointsTextLabel = new Label("Points: ", main.getSkin());
        pointsValueLabel = new Label(Integer.toString(main.getPlayer().getPoints()), main.getSkin());
        pointsValueLabel.setAlignment(Align.left);

        uiTable.add(healthTextLabel).fill();
        uiTable.add(healthValueLabel).fill();
        uiTable.row();
        uiTable.add(goldTextLabel).fill();
        uiTable.add(goldValueLabel).fill();
        uiTable.row();
        uiTable.add(pointsTextLabel);
        uiTable.add(pointsValueLabel).width(pointsTextLabel.getWidth());
        // End of A4 change

        uiTable.align(Align.topRight);
        uiTable.setFillParent(true);

        uiStage.addActor(uiTable);

        Table optionsTable = new Table();
        optionsTable.setFillParent(true);
        Label title = new Label(department.getName(), main.getSkin());

        final TextButton upgrade = new TextButton("Upgrade Ship " + department.getProduct() + " for " + department.getUpgradeCost() + " gold", main.getSkin());
        final Label message = new Label("", main.getSkin());
        final TextButton heal = new TextButton("Repair Ship for "+ Integer.toString(getHealCost()) +" gold", main.getSkin());

        upgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                department.purchase();
                upgrade.setText("Upgrade Ship " + department.getProduct() + " for " + department.getUpgradeCost() + " gold");
            }
        });

        if (healthFromMax == 0) { heal.setText("Your ship is already fully repaired!"); }

        heal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (toHeal == 0) {
                    heal.setText("Your ship is already fully repaired!");
                } else {
                    if (player.payGold(getHealCost())) {
                        System.out.println("charged");
                        player.getPlayerShip().setHealth(player.getPlayerShip().getHealthMax());
                        message.setText("Successful repair");
                    } else {
                        message.setText("You don't have the funds to repair your ship");
                    }
                }
            }
        });

        final TextButton playerMinigame = new TextButton("Play Minigame", main.getSkin());
        playerMinigame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pirateGame.setScreen(new MinigameScreen(pirateGame, department));
                dispose();
            }
        });

        //A4: Changed table to be more visually appealing
        optionsTable.add(title).padBottom(viewheight / 40);
        optionsTable.row();
        optionsTable.add(upgrade).padBottom(viewheight / 40);
        optionsTable.row();
        optionsTable.add(heal).padBottom(viewheight / 40);
        optionsTable.row();
        optionsTable.add(playerMinigame);
        // End of A4 change

        mainStage.addActor(optionsTable);
        Gdx.input.setInputProcessor(mainStage);
    }

    // A4: Added function to clearly define healing costt
    public int getHealCost(){ // Function to get the cost to heal to full:
        // if statement ensures player pays at least 1 gold to heal
        if (healthFromMax / 10 == 0){
            return 1;
        }
        // Formula for cost: Every 10 health costs 1 gold to heal
        return healthFromMax / 10;
    }
    // End of A4 change

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESCAPE");
            pirateGame.setScreen(pirateGame.getSailingScene());
        }

        goldValueLabel.setText(Integer.toString(pirateGame.getPlayer().getGold()));
        pointsValueLabel.setText(Integer.toString(pirateGame.getPlayer().getPoints()));
        healthFromMax = player.getPlayerShip().getHealthFromMax();

    }
}