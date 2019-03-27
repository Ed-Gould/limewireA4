package com.rear_admirals.york_pirates.screen.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.screen.VictoryScreen;
import com.rear_admirals.york_pirates.screen.combat.attacks.*;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.Player;
import com.rear_admirals.york_pirates.base.BaseScreen;
import com.rear_admirals.york_pirates.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

import static com.rear_admirals.york_pirates.College.Langwith;

public class CombatScreen extends BaseScreen {

    // screen layout variables
    private float button_pad_bottom;
    private float button_pad_right;

    // Labels changed throughout the scene
    public Label descriptionLabel;
    private Label playerHPLabel;
    private Label enemyHPLabel;

    // Health bars of both ships
    private ProgressBar playerHP;
    private ProgressBar enemyHP;

    // Image textures and images for the various stages
    private Texture bg_texture;
    private Texture wood_texture;
    private Image background;
    private Image background_wood;

    public Player player;
    public Ship enemy;

    //Added For Assessment 3
    //Player and Enemy Stat Labels
    private Label playerStatTitle;
    private Label playerAttack;
    private Label playerDefense;
    private Label playerAccuracy;
    private Label enemyStatTitle;
    private Label enemyAttack;
    private Label enemyDefense;
    private Label enemyAccuracy;
    //End Added

    // Control the layout of the stage
    private Table completeAttackTable;
    private Table attackTable;
    private Table rootTable;
    private Table descriptionTable;
    private Container<Table> tableContainer;
    //Added For Assessment 3
    private Table playerStats;
    private Table enemyStats;
    //End Added

    // Written text box
    private TextButton textBox;

    // Variables used in handling combat
    private Stack<Attack> combatStack;
    private static List<Attack> enemyAttacks;
    private Attack currentAttack;
    private BattleEvent queuedCombatEvent;

    // Variables used in text animation
    private float delayTime = 0;
    private boolean textAnimation = false;
    private int animationIndex = 0;
    private String displayText = "";

    public CombatScreen(final PirateGame pirateGame, Ship enemy) {
        // Calls superclass BaseScreen
        super(pirateGame);

        // This constructor also replaces the create function that a stage would typically have.
        this.pirateGame = pirateGame;
        this.player = pirateGame.getPlayer();
        this.enemy = enemy;

        // Load the skin for this screen
        pirateGame.setSkin(new Skin(Gdx.files.internal("flat-earth-ui.json")));

        combatStack = new Stack();

        // Sets size constants for the scene depending on viewport, also sets button padding constants for use in tables
        button_pad_bottom = viewHeight / 24f;
        button_pad_right = viewWidth / 32f;

        // Insantiate the image textures for use within the scene as backgrounds.
        bg_texture = new Texture("water_texture_sky.png");
        background = new Image(bg_texture);
        background.setSize(viewWidth, viewHeight);

        wood_texture = new Texture("wood_vertical_board_texture.png");
        background_wood = new Image(wood_texture);
        background_wood.setSize(viewWidth, viewHeight);

        // Create a Container which takes up the whole screen (used for layout purposes)
        tableContainer = new Container<Table>();
        tableContainer.setFillParent(true);
        tableContainer.setPosition(0, 0);
        tableContainer.align(Align.bottom);

        // Instantiate some different tables used throughout scene
        rootTable = new Table();
        descriptionTable = new Table();
        attackTable = new Table();
        //Added For Assessment 3
        playerStats = new Table();
        enemyStats = new Table();


        // Instantiates Player and Enemy stat Labels
        playerStatTitle = new Label("Player Stats: ", pirateGame.getSkin(), "default_black");
        playerAttack = new Label("Attack: " + Integer.toString(player.getPlayerShip().getAttack()),
                pirateGame.getSkin(), "default_black");
        playerDefense = new Label("Defense: " + Integer.toString(player.getPlayerShip().getDefence()),
                pirateGame.getSkin(), "default_black");
        playerAccuracy = new Label("Accuracy: " + Integer.toString(player.getPlayerShip().getAccuracy()),
                pirateGame.getSkin(), "default_black");
        enemyStatTitle = new Label("Enemy Stats: ", pirateGame.getSkin(), "default_black");
        enemyAttack = new Label("Attack: " + Integer.toString(enemy.getAttack()), pirateGame.getSkin(),
                "default_black");
        enemyDefense = new Label("Defense: " + Integer.toString(enemy.getDefence()), pirateGame.getSkin(),
                "default_black");
        enemyAccuracy = new Label("Accuracy: " + Integer.toString(enemy.getAccuracy()), pirateGame.getSkin(),
                "default_black");
        //End Added

        // Instantiate both the ships for the battle
        CombatShip myShip = new CombatShip("ship1.png", viewWidth / 3);
        CombatShip enemyShip = new CombatShip("ship2.png", viewWidth / 3);

        Label shipName = new Label(player.getPlayerShip().getName(), pirateGame.getSkin(), "default_black");
        playerHP = new ProgressBar(0, player.getPlayerShip().getHealthMax(), 0.1f, false, pirateGame.getSkin());
        playerHPLabel = new Label(player.getPlayerShip().getHealth() + "/" + player.getPlayerShip().getHealthMax(),
                pirateGame.getSkin());

        playerHP.getStyle().background.setMinHeight(playerHP.getPrefHeight() * 2); //Setting vertical size of
        // progress slider (Class implementation is slightly weird)
        playerHP.getStyle().knobBefore.setMinHeight(playerHP.getPrefHeight());

        Label enemyName = new Label(enemy.getName(), pirateGame.getSkin(), "default_black");
        enemyHP = new ProgressBar(0, enemy.getHealthMax(), 0.1f, false, pirateGame.getSkin());
        enemyHPLabel = new Label(enemy.getHealth() + "/" + enemy.getHealthMax(), pirateGame.getSkin());

        playerHP.setValue(player.getPlayerShip().getHealthMax());
        enemyHP.setValue(enemy.getHealthMax());

        Table playerHPTable = new Table();
        Table enemyHPTable = new Table();

        playerHPTable.add(playerHPLabel).padRight(viewWidth / 36f);
        playerHPTable.add(playerHP).width(viewWidth / 5);

        enemyHPTable.add(enemyHPLabel).padRight(viewWidth / 36f);
        enemyHPTable.add(enemyHP).width(viewWidth / 5);

        Label screenTitle = new Label("Combat Mode", pirateGame.getSkin(), "title_black");
        screenTitle.setAlignment(Align.center);

        textBox = new TextButton("You encountered a " + enemy.getCollege().getName() + " " + enemy.getType() + "!",
                pirateGame.getSkin());
        textBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (textAnimation) {
                    textAnimation = false;
                    textBox.setText(displayText);
                } else {
                    System.out.println("Button clicked, running combat handler with event " + queuedCombatEvent.toString());
                    textBox.setText("");
                    updateHP();
                    combatHandler(queuedCombatEvent);
                }
            }
        });

        // Control combat
        this.queuedCombatEvent = BattleEvent.NONE;
        currentAttack = null;

        // Instantiation of the combat buttons. Attack and Flee are default attacks, the rest can be modified within
        // player class.
        final AttackButton button1 = new AttackButton(Attack.attackMain, pirateGame.getSkin());
        buttonListener(button1);
        final AttackButton button2 = new AttackButton(player.attacks.get(0), pirateGame.getSkin());
        buttonListener(button2);
        final AttackButton button3 = new AttackButton(player.attacks.get(1), pirateGame.getSkin());
        buttonListener(button3);
        final AttackButton button4 = new AttackButton(player.attacks.get(2), pirateGame.getSkin());
        buttonListener(button4);
        final AttackButton fleeButton = new AttackButton(Flee.attackFlee, pirateGame.getSkin(), "red");
        buttonListener(fleeButton);

        descriptionLabel = new Label("What would you like to do?", pirateGame.getSkin());
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        descriptionTable.center();
        descriptionTable.add(descriptionLabel).uniform().pad(0, button_pad_right, 0, button_pad_right).size(viewWidth / 2 - button_pad_right * 2, viewHeight / 12).top();
        descriptionTable.row();
        descriptionTable.add(fleeButton).uniform();

        attackTable.row();
        attackTable.add(button1).uniform().width(viewWidth / 5).padRight(button_pad_right);
        attackTable.add(button2).uniform().width(viewWidth / 5);
        attackTable.row().padTop(button_pad_bottom);
        attackTable.add(button3).uniform().width(viewWidth / 5).padRight(button_pad_right);
        attackTable.add(button4).uniform().width(viewWidth / 5);

        rootTable.row().width(viewWidth * 0.8f);
        rootTable.add(screenTitle).colspan(2);
        rootTable.row();
        rootTable.add(shipName);
        rootTable.add(enemyName);
        rootTable.row().fillX();
        rootTable.add(myShip);
        rootTable.add(enemyShip);
        rootTable.row();
        rootTable.add(playerHPTable);
        rootTable.add(enemyHPTable);
        rootTable.row();
        rootTable.add(textBox).colspan(2).fillX().height(viewHeight / 9f).pad(viewHeight / 12, 0, viewHeight / 12, 0);
        tableContainer.setActor(rootTable);

        completeAttackTable = new Table();
        completeAttackTable.setFillParent(true);
        completeAttackTable.align(Align.bottom);
        completeAttackTable.row().expandX().padBottom(viewHeight / 18f);
        completeAttackTable.add(descriptionTable).width(viewWidth / 2);
        completeAttackTable.add(attackTable).width(viewWidth / 2);

        //Added For Assessment 3
        playerStats.setFillParent(true);
        playerStats.align(Align.left);
        playerStats.add(playerStatTitle).width(viewWidth);
        playerStats.row();
        playerStats.add(playerAttack).width(viewWidth);
        playerStats.row();
        playerStats.add(playerDefense).width(viewWidth);
        playerStats.row();
        playerStats.add(playerAccuracy).width(viewWidth);

        enemyStats.setFillParent(true);
        enemyStats.align(Align.right);
        enemyStats.add(enemyStatTitle).width(viewWidth / 9f);
        enemyStats.row();
        enemyStats.add(enemyAttack).width(viewWidth / 9f);
        enemyStats.row();
        enemyStats.add(enemyDefense).width(viewWidth / 9f);
        enemyStats.row();
        enemyStats.add(enemyAccuracy).width(viewWidth / 9f);
        //End Added


        background_wood.setVisible(false);
        completeAttackTable.setVisible(false);
        mainStage.addActor(background_wood);
        mainStage.addActor(completeAttackTable);
        //Added For Assessment 3
        mainStage.addActor(playerStats);
        mainStage.addActor(enemyStats);
        //End Added

        uiStage.addActor(background);
        uiStage.addActor(tableContainer);

        // Setup Enemy attacks - may need to be modified if you want to draw attacks from enemy's class
        enemyAttacks = new ArrayList<Attack>();
        enemyAttacks.add(Attack.attackMain);
        enemyAttacks.add(GrapeShot.attackGrape);
        enemyAttacks.add(Attack.attackSwivel);

        Gdx.input.setInputProcessor(uiStage);

        System.out.println(viewWidth + "," + viewHeight + " AND " + Gdx.graphics.getWidth() + "," + Gdx.graphics.getHeight());
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.draw();
        uiStage.act();
        mainStage.draw();
        mainStage.act();
        labelAnimationUpdate(delta);
    }

    @Override
    public void dispose() {
        uiStage.dispose();
        mainStage.dispose();
        bg_texture.dispose();
    }


    public void toggleAttackStage() {
        // This method toggles the visibility of the players attack moves and changes input processor to relevant stage
        if (background_wood.isVisible()) {
            background_wood.setVisible(false);
            completeAttackTable.setVisible(false);
            Gdx.input.setInputProcessor(uiStage);
        } else {
            background_wood.setVisible(true);
            completeAttackTable.setVisible(true);
            Gdx.input.setInputProcessor(mainStage);
        }
    }

    // combat Handler
    //  This function handles the ship combat using BattleEvent enum type
    public void combatHandler(BattleEvent status) {
        //Debugging
        System.out.println("Running combatHandler with status: " + status.toString());

        if (!combatStack.empty()) {
            currentAttack = combatStack.pop();
        }

        switch (status) {
            case NONE:
                toggleAttackStage();
                break;
            case PLAYER_MOVE:
                toggleAttackStage();
                textBox.setStyle(pirateGame.getSkin().get("default", TextButton.TextButtonStyle.class));
                System.out.println("Running players move");
                if (currentAttack.isSkipMoveStatus()) {
                    System.out.println("Charging attack");
                    currentAttack.setSkipMoveStatus(false);
                    combatStack.push(currentAttack);
                    dialog("Charging attack " + currentAttack.getName(), BattleEvent.ENEMY_MOVE);
                } else if (currentAttack.getName() == "FLEE") {
                    if (currentAttack.doAttack(player.getPlayerShip(), enemy) == 1) {
                        System.out.println("Flee successful");
                        dialog("Flee successful!", BattleEvent.PLAYER_FLEES);
                    } else {
                        System.out.println("Flee Failed");
                        dialog("Flee failed.", BattleEvent.ENEMY_MOVE);
                    }
                } else {
                    int damage = currentAttack.doAttack(player.getPlayerShip(), enemy); // Calls the attack function
                    // on the player and stores damage output
                    // This selection statement returns Special Charge attacks to normal state
                    if (currentAttack.isSkipMove()) {
                        currentAttack.setSkipMoveStatus(true);
                    }

                    if (damage == 0) {
                        System.out.println("Player " + currentAttack.getName() + " MISSED, damage dealt: " + damage + ", Player Ship Health: " + player.getPlayerShip().getHealth() + ", Enemy Ship Health: " + enemy.getHealth());
                        dialog("Attack Missed", BattleEvent.ENEMY_MOVE);
                    } else {
                        System.out.println("Player " + currentAttack.getName() + " SUCCESSFUL, damage dealt: " + damage + ", Player Ship Health: " + player.getPlayerShip().getHealth() + ", Enemy Ship Health: " + enemy.getHealth());
                        if (player.getPlayerShip().getHealth() <= 0) {
                            System.out.println("Player has died");
                            dialog("You dealt " + damage + " with " + currentAttack.getName() + "!",
                                    BattleEvent.PLAYER_DIES);
                        } else if (enemy.getHealth() <= 0) {
                            System.out.println("Enemy has died");
                            dialog("You dealt " + damage + " with " + currentAttack.getName() + "!",
                                    BattleEvent.ENEMY_DIES);
                        } else {
                            dialog("You dealt " + damage + " with " + currentAttack.getName() + "!",
                                    BattleEvent.ENEMY_MOVE);
                        }
                    }
                }
                break;
            case ENEMY_MOVE:
                System.out.println("Running enemy move");
                textBox.setStyle(pirateGame.getSkin().get("red", TextButton.TextButtonStyle.class));
                Attack enemyAttack = enemyAttacks.get(ThreadLocalRandom.current().nextInt(0, 3));
                int damage = enemyAttack.doAttack(enemy, player.getPlayerShip());
                String message;
                if (damage == 0) {
                    System.out.println("Enemy " + enemyAttack.getName() + " ATTACK MISSED");
                    message = "Enemies " + enemyAttack.getName() + " missed.";
                } else {
                    System.out.println("ENEMY " + enemyAttack.getName() + " SUCCESSFUL, damage dealt: " + damage + "," +
                            " Player Ship Health: " + player.getPlayerShip().getHealth() + ", Enemy Ship Health: " + enemy.getHealth());
                    message = "Enemy " + enemy.getName() + " dealt " + damage + " with " + enemyAttack.getName() + "!";
                }

                if (player.getPlayerShip().getHealth() <= 0) {
                    System.out.println("Player has died");
                    dialog("Enemies " + enemyAttack.getName() + " hit you for " + damage, BattleEvent.PLAYER_DIES);
                } else if (enemy.getHealth() <= 0) {
                    System.out.println("Enemy has died");
                    dialog("Enemies " + enemyAttack.getName() + " hit you for " + damage, BattleEvent.ENEMY_DIES);
                } else {
                    if (currentAttack.isSkipMove() != currentAttack.isSkipMoveStatus()) {
                        System.out.println("Loading charged attack");
                        dialog(message, BattleEvent.PLAYER_MOVE);
                    } else {
                        dialog(message, BattleEvent.NONE);
                    }
                }
                break;
            case PLAYER_DIES:
                //Altered For Assessment 3
                textBox.setStyle(pirateGame.getSkin().get("red", TextButton.TextButtonStyle.class));
                player.addGold(-player.getGold() / 2);
                player.setPoints(0);
                player.getPlayerShip().setHealth(player.getPlayerShip().getHealthMax());
                enemy.setVisible(false);
                player.getPlayerShip().setSpeed(0);
                player.getPlayerShip().setAccelerationXY(0,0);
                player.getPlayerShip().setAnchor(true);
                System.out.println("END OF COMBAT");
                toggleAttackStage();
                pirateGame.setScreen(new VictoryScreen(pirateGame, false));
                break;
            case ENEMY_DIES:
                textBox.setStyle(pirateGame.getSkin().get("default", TextButton.TextButtonStyle.class));
                player.addGold(enemy.getGoldValue());
                player.addPoints(enemy.getPointValue());
                player.getPlayerShip().setSpeed(0);
                player.getPlayerShip().setAccelerationXY(0,0);
                if (enemy.getIsBoss() == true) {
                    enemy.getCollege().setBossDead(true);
                    this.player.getPlayerShip().getCollege().addAlly(this.enemy.getCollege());
                    dialog("Congratulations, you have defeated Enemy " + enemy.getName(), BattleEvent.SCENE_RETURN);
                } else {
                    dialog("Congratulations, you have defeated Enemy " + enemy.getName(), BattleEvent.SCENE_RETURN);
                }
                break;
            case PLAYER_FLEES:
                textBox.setStyle(pirateGame.getSkin().get("red", TextButton.TextButtonStyle.class));
                player.addPoints(-5);
                dialog("You Managed to Flee Successfully " + enemy.getName(), BattleEvent.SCENE_RETURN);
                break;
            case SCENE_RETURN:
                enemy.setVisible(false);
                player.getPlayerShip().setSpeed(0);
                player.getPlayerShip().setAccelerationXY(0, 0);
                player.getPlayerShip().setAnchor(true);
                System.out.println("END OF COMBAT");
                toggleAttackStage();
                if (Langwith.isBossDead()) {
                    pirateGame.setScreen(new VictoryScreen(pirateGame, true));
                } else {
                    pirateGame.setScreen(pirateGame.getSailingScene());
                }
                //End Alter
                break;
        }
    }

    // Button Listener Classes - creates a hover listener for any button passed through

    public void buttonListener(final AttackButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                descriptionLabel.setText(button.getDesc());
            }

            ;

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                descriptionLabel.setText("What would you like to do?");
            }

            ;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                combatStack.push(button.getAttack());
                combatHandler(BattleEvent.PLAYER_MOVE);
            }
        });
    }

    public void buttonListener(final AttackButton button, final String message) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                descriptionLabel.setText(button.getDesc());
            }

            ;

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                descriptionLabel.setText("Choose an option");
            }

            ;

            @Override
            public void clicked(InputEvent event, float x, float y) {
                button.setText(message);
            }
        });
    }

    // This method updates the player HP bar and text values
    public void updateHP() {
        enemyHP.setAnimateDuration(1);
        playerHP.setAnimateDuration(1);

        if (enemy.getHealth() <= 0) {
            enemy.setHealth(0);
        }

        if (player.getPlayerShip().getHealth() <= 0) {
            player.getPlayerShip().setHealth(0);
        }

        enemyHPLabel.setText(enemy.getHealth() + "/" + enemy.getHealthMax());
        enemyHP.setValue(enemy.getHealth());

        playerHPLabel.setText(player.getPlayerShip().getHealth() + "/" + player.getPlayerShip().getHealthMax());
        playerHP.setValue(player.getPlayerShip().getHealth());
    }

    // Updates and displays text box
    public void dialog(String message, final BattleEvent nextEvent) {
        queuedCombatEvent = nextEvent;

        if (background_wood.isVisible()) {
            toggleAttackStage();
        }

        displayText = message;
        animationIndex = 0;
        textAnimation = true;
    }

    // This method controls the animation of the dialog label
    public void labelAnimationUpdate(float dt) {
        if (textAnimation) {
            delayTime += dt;

            if (animationIndex > displayText.length()) {
                textAnimation = false;
            }

            if (delayTime >= 0.05f) {
                textBox.setText(displayText.substring(0, animationIndex));
                animationIndex++;
                delayTime = 0;
            }
        }
    }
}