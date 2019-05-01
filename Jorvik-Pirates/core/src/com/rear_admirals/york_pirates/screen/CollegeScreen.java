package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.College;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.Player;
import com.rear_admirals.york_pirates.base.BaseScreen;

/**
 * Class for the GUI that appears when you interact with a college
 */
public class CollegeScreen extends BaseScreen {
    private Player player;

    private Label pointsValueLabel, pointsTextLabel;
    private Label goldValueLabel, goldTextLabel;
    private Label healthTextLabel, healthValueLabel;
    private Label woodsTextLabel, woodsValueLabel;

    private int healthFromMax;

    /**
     * CollegeScreen Constructor
     * Creates the GUI elements of the screen
     *
     * @param main - the game which this CollegeScreen is part of
     * @param college - the college this screen is for
     */
    public CollegeScreen(PirateGame main, College college) {
        super(main);
        this.player = main.getPlayer();

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

        //A4: wood's label
        woodsTextLabel = new Label("Woods: ", main.getSkin());
        woodsValueLabel = new Label(Integer.toString(main.getPlayer().getWoods()), main.getSkin());
        woodsValueLabel.setAlignment(Align.left);
        //End of A4 change

        uiTable.add(healthTextLabel).fill();
        uiTable.add(healthValueLabel).fill();
        uiTable.row();
        uiTable.add(goldTextLabel).fill();
        uiTable.add(goldValueLabel).fill();
        uiTable.row();
        uiTable.add(woodsTextLabel).fill();
        uiTable.add(woodsValueLabel).fill();
        uiTable.row();
        uiTable.add(pointsTextLabel);
        uiTable.add(pointsValueLabel).width(pointsTextLabel.getWidth());
        // End of A4 change

        uiTable.align(Align.topRight);
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);

        // A4: Restructured implementation for LibGDX tables and buttons for the UI
        // Create and align department screen title text
        Label titleText = new Label(college.getName() + " College", main.getSkin(), "title");
        titleText.setAlignment(Align.top);
        titleText.setFillParent(true);

        // Create and align text and buttons for healing options
        Table healTable = new Table();
        healTable.align(Align.center);
        healTable.setFillParent(true);

        final Label healText = new Label("Heal", main.getSkin(), "title");
        final TextButton healFullBtn = new TextButton("Fully heal ship for "+ Integer.toString(getHealCost(healthFromMax)) +" gold", main.getSkin());
        final TextButton healTenBtn = new TextButton("Heal 10 health for 1 gold", main.getSkin());
        final Label healMessage = new Label("health status", main.getSkin());
        if (healthFromMax == 0) { healMessage.setText("Your ship is fully repaired."); }

        healTable.add(healText).padBottom(viewHeight/40);
        healTable.row();
        healTable.add(healFullBtn).padBottom(viewHeight/40);
        healTable.row();
        healTable.add(healTenBtn).padBottom(viewHeight/40);
        healTable.row();
        healTable.add(healMessage);
        // End of A4 change

        //A4: Create buttons used to but wood pieces
        Table woodTable = new Table();
        woodTable.setX(viewWidth * 0.3f, Align.center);
        woodTable.setFillParent(true);

        final Label woodText = new Label("Wood Pieces", main.getSkin(), "title");
        final TextButton getWoodBtn = new TextButton("Get 10 wood pieces for 10 gold", main.getSkin());
        final Label woodMessage = new Label("", main.getSkin());

        woodTable.add(woodText).padBottom(viewHeight/40);
        woodTable.row();
        woodTable.add(getWoodBtn).padBottom(viewHeight/40);
        woodTable.row();
        woodTable.add(woodMessage);
        // End of A4 change

        // A4: Added new buttons to allow for more healing options
        healFullBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (healthFromMax == 0) {
                    healMessage.setText("Your ship is already fully repaired!");
                } else {
                    if (player.payGold(getHealCost(healthFromMax))) {
                        System.out.println("Charged to fully heal");
                        player.getPlayerShip().setHealth(player.getPlayerShip().getHealthMax());
                        healMessage.setText("Health fully restored");
                    } else {
                        healMessage.setText("Not enough money to repair ship");
                    }
                }
            }
        });

        healTenBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (healthFromMax == 0) {
                    healMessage.setText("Your ship is already fully repaired!");
                } else {
                    if (player.payGold(getHealCost(10))) {
                        System.out.println("Charged to heal 10HP");
                        player.getPlayerShip().heal(10);
                        healMessage.setText("10 health restored");
                    } else {
                        healMessage.setText("Not enough money to repair ship");
                    }
                }
            }
        });
        // End of A4 change

        // A4: Added function to buy wood pieces
        getWoodBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(player.payGold(10)){
                    System.out.println("add 10 wood pieces");
                    player.addWood(10);
                    woodMessage.setText("You purchased 10 wood pieces.");
                }
                else{
                    woodMessage.setText("Not enough money to buy wood pieces.");
                }
            }
        });

        // A4: Changed table to be more visually appealing
        mainStage.addActor(titleText);
        mainStage.addActor(healTable);
        mainStage.addActor(woodTable);
        // End of A4 change

        Gdx.input.setInputProcessor(mainStage);
    }


    /**
     * Defines healing cost
     * Added in Assessment 4
     *
     * @param health - current health of player
     * @return - the cost to heal the player fully
     */
    // A4:
    public int getHealCost(int health){ // Function to get the cost to heal to full:
        // if statement ensures player pays at least 1 gold to heal
        if (health / 10 == 0){
            return 1;
        }
        // Formula for cost: Every 10 health costs 1 gold to heal
        return health / 10;
    }
    // End of A4 change

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESCAPE");
            pirateGame.setScreen(pirateGame.getSailingScene());
            dispose();
        }
        goldValueLabel.setText(Integer.toString(pirateGame.getPlayer().getGold()));
        pointsValueLabel.setText(Integer.toString(pirateGame.getPlayer().getPoints()));
        healthFromMax = player.getPlayerShip().getHealthFromMax();
        woodsValueLabel.setText(Integer.toString(pirateGame.getPlayer().getWoods()));
    }
}