package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.College;
import com.rear_admirals.york_pirates.ShipType;
import com.rear_admirals.york_pirates.screen.combat.CombatScreen;
import com.rear_admirals.york_pirates.base.BaseActor;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.base.BaseScreen;
import com.rear_admirals.york_pirates.Ship;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.rear_admirals.york_pirates.College.*;
import static com.rear_admirals.york_pirates.PirateGame.*;
import static com.rear_admirals.york_pirates.ShipType.*;

public class SailingScreen extends BaseScreen {

    private Ship playerShip;

    //Map Variables
    private ArrayList<BaseActor> obstacleList;
    private ArrayList<BaseActor> removeList;
    private ArrayList<BaseActor> regionList;

    //calculate game world dimensions
    private TiledMap tiledMap;

    // A4: Added variables to get map data from file instead of hard coding into class
    private MapProperties mapProperties;
    private int mapTileWidth;
    private int mapTileHeight;

    private int tilePixelSize;
    private final int mapPixelWidth;
    private final int mapPixelHeight;
    // End of A4 change

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera tiledCamera;
    private int[] backgroundLayers = {0, 1, 2};
    private int[] foregroundLayers = {3};

    // A4: Altered labels
    private Label healthValueLabel, healthTextLabel;
    private Label goldValueLabel, goldTextLabel;
    private Label pointsValueLabel, pointsTextLabel;
    // End of A4 change

    private Label mapMessage;
    private Label hintMessage;

    //Altered For Assessment 3
    private double timer;
    //End Altered

    public SailingScreen(final PirateGame main) {
        super(main);

        playerShip = main.getPlayer().getPlayerShip();
        System.out.println(playerShip.getName());

        mainStage.addActor(playerShip);
        System.out.println("playerShip added");

        // A4: Cleaned up code for UI table, added display for health
        Table uiTable = new Table();

        /* Creates labels for the health, gold, and points display.
        These displays are separated into two labels each:
        A "TextLabel": These labels are composed of a text element (either the world "Points" or "Gold")
        A "ValueLabel": These labels are the integer value associated to the Text Labels (e.g. 40 for gold)
        */

        healthTextLabel = new Label("Health: ", main.getSkin(), "default_black");
        healthValueLabel = new Label(Integer.toString(main.getPlayer().getPlayerShip().getHealth()), main.getSkin(), "default_black");
        healthValueLabel.setAlignment(Align.left);

        goldTextLabel = new Label("Gold: ", main.getSkin(), "default_black");
        goldValueLabel = new Label(Integer.toString(main.getPlayer().getGold()), main.getSkin(), "default_black");
        goldValueLabel.setAlignment(Align.left);

        pointsTextLabel = new Label("Points: ", main.getSkin(), "default_black");
        pointsValueLabel = new Label(Integer.toString(main.getPlayer().getPoints()), main.getSkin(), "default_black");
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

        mapMessage = new Label("", main.getSkin(), "default_black");
        hintMessage = new Label("", main.getSkin(), "default_black");

        Table messageTable = new Table();
        messageTable.add(mapMessage);
        messageTable.row();
        messageTable.add(hintMessage);

        messageTable.setFillParent(true);
        messageTable.top();

        uiStage.addActor(messageTable);

        obstacleList = new ArrayList<BaseActor>();
        removeList = new ArrayList<BaseActor>();
        regionList = new ArrayList<BaseActor>();

        // set up tile map, renderer and camera
        // A4: Changed map variables to initialise from tmx map files
        // set up Tiled Map and associated properties/attributes (width/height)
        tiledMap = new TmxMapLoader().load("assessment_four_map.tmx");
        mapProperties = tiledMap.getProperties();
        mapTileWidth = mapProperties.get("width", Integer.class);
        mapTileHeight = mapProperties.get("height", Integer.class);
        tilePixelSize = mapProperties.get("tilewidth", Integer.class);
        mapPixelWidth = tilePixelSize * mapTileWidth;
        mapPixelHeight = tilePixelSize * mapTileHeight;

        // Setup renderer
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Setup camera
        tiledCamera = new OrthographicCamera();
        tiledCamera.setToOrtho(false, viewwidth, viewheight);
        tiledCamera.update();

        MapObjects objects = tiledMap.getLayers().get("ObjectData").getObjects();
        for (MapObject object : objects) {
            String name = object.getName();

            // all object data assumed to be stored as rectangles
            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            Rectangle r = rectangleObject.getRectangle();

            if (name.equals("player")) {
                playerShip.setPosition(r.x, r.y);
            } else {
                System.err.println("Unknown tilemap object: " + name);
            }
        }

        objects = tiledMap.getLayers().get("PhysicsData").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle r = rectangleObject.getRectangle();

                BaseActor solid = new BaseActor();
                solid.setPosition(r.x, r.y);
                solid.setSize(r.width, r.height);
                solid.setName(object.getName());
                solid.setRectangleBoundary();
                String objectName = object.getName();

                //Altered For Assessment 3
                if (objectName.equals("derwent")) solid.setCollege(College.Derwent);
                else if (objectName.equals("james")) solid.setCollege(College.James);
                else if (objectName.equals("vanbrugh")) solid.setCollege(College.Vanbrugh);
                else if (objectName.equals("goodricke")) solid.setCollege(College.Goodricke);
                else if (objectName.equals("langwith")) solid.setCollege(College.Langwith);
                else if (objectName.equals("chemistry")) solid.setDepartment(Chemistry);
                else if (objectName.equals("physics")) solid.setDepartment(Physics);
                else if (objectName.equals("maths")) solid.setDepartment(Maths);
                //End Altered
                else{
                    System.out.println("Not college/department: " + solid.getName());
                }
                obstacleList.add(solid);
            } else {
                System.err.println("Unknown PhysicsData object.");
            }
        }

        objects = tiledMap.getLayers().get("RegionData").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle r = rectangleObject.getRectangle();

                BaseActor region = new BaseActor();
                region.setPosition(r.x, r.y);
                region.setSize(r.width, r.height);
                region.setRectangleBoundary();
                region.setName(object.getName());

                //Altered For Assessment 3
                if (object.getName().equals("derwentregion")) region.setCollege(College.Derwent);
                else if (object.getName().equals("jamesregion")) region.setCollege(College.James);
                else if (object.getName().equals("vanbrughregion")) region.setCollege(College.Vanbrugh);
                else if (object.getName().equals("goodrickeregion")) region.setCollege(College.Goodricke);
                else if (object.getName().equals("langwithregion")) region.setCollege(College.Langwith);
                else if (object.getName().equals("chemistrydepartment")) region.setDepartment(Chemistry);
                else if (object.getName().equals("physicsdepartment")) region.setDepartment(Physics);
                else if (object.getName().equals("mathsdepartment")) region.setDepartment(Maths);
                regionList.add(region);
                //End Altered
            } else {
                System.err.println("Unknown RegionData object.");
            }
        }

        timer = 0f;

        InputMultiplexer im = new InputMultiplexer(uiStage, mainStage);
        Gdx.input.setInputProcessor(im);

        // Debug processor
//        System.out.println("IP: im");
    }

    @Override
    public void update(float delta) {
        removeList.clear();
        goldValueLabel.setText(Integer.toString(pirateGame.getPlayer().getGold()));
        this.playerShip.playerMove(delta);

        Boolean x = false;
        for (BaseActor region : regionList) {
            String name = region.getName();
            //Altered For Assessment 3
            //If in a colleges territory
            if (playerShip.overlaps(region, false) && !(region.getName().contains("chemistry") || region.getName().contains("physics") || region.getName().contains("maths"))) {
                x = true;
                mapMessage.setText(capitalizeFirstLetter(name.substring(0, name.length() - 6)) + " Territory");

                //Roll a random chance to see if you are meeting an enemy ship
                int roll = ThreadLocalRandom.current().nextInt(0, 10001);
                int enemyChance = Integer.parseInt(pointsValueLabel.getText().toString()) / 2;
                if (enemyChance > 100) {
                    enemyChance = 100;
                } else if (enemyChance < 10) {
                    enemyChance = 10;
                }
                if (roll <= enemyChance) {
                    System.out.println("Enemy Found in " + name);
                    College college = region.getCollege();
                    if (!playerShip.getCollege().getAlly().contains(college)) {
                        System.out.println(name);
                        //Start combat
                        pirateGame.setScreen(new CombatScreen(pirateGame, new Ship(Enemy, college)));
                    }
                }
            }
            //End Altered
        }

        if (!x) {
            mapMessage.setText("Neutral Territory");
        }


        Boolean y = false;
        for (BaseActor obstacle : obstacleList) {
            String name = obstacle.getName();
            if (playerShip.overlaps(obstacle, true)) {
                y = true;
                if (!(obstacle.getDepartment() == null)) {
                    mapMessage.setText(capitalizeFirstLetter(name) + " Island");
                    hintMessage.setText("Press F to interact");
                    if (Gdx.input.isKeyPressed(Input.Keys.F))
                        pirateGame.setScreen(new DepartmentScreen(pirateGame, obstacle.getDepartment()));
                }
                // Obstacle must be a college if college not null
                else if (!(obstacle.getCollege() == null)) {
                    mapMessage.setText(capitalizeFirstLetter(name) + " Island");
                    hintMessage.setText("Press F to interact");
//                    System.out.println("A college");
                    College college = obstacle.getCollege();
                    if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                        System.out.println("A college");
                        if (!playerShip.getCollege().getAlly().contains(college) && !obstacle.getCollege().isBossDead()) {
                            System.out.println("Enemy");

                            //Altered For Assessment 3
                            String collegeMet = obstacle.getCollege().getName();
                            Ship collegeDefender;
                            if (collegeMet == "James") {
                                collegeDefender = new Ship(ShipType.James, College.James, true);
                            } else if (collegeMet == "Vanbrugh") {
                                collegeDefender = new Ship(ShipType.Van, College.Vanbrugh, true);
                            } else if (collegeMet == "Goodricke") {
                                collegeDefender = new Ship(ShipType.Good, College.Goodricke, true);
                            } else if (collegeMet == "Langwith") {
                                collegeDefender = new Ship(ShipType.Lan, College.Langwith, true);
                            } else {
                                throw new IllegalStateException("College not recognised");
                            }
                            pirateGame.setScreen(new CombatScreen(pirateGame, collegeDefender));
                            //End Altered
                        } else {
                            System.out.println("Ally");
                            pirateGame.setScreen(new CollegeScreen(pirateGame, college));
                        }
                    }
                } else {
//                    System.out.println("Pure obstacle");
                }
            }
        }

        if (!y) hintMessage.setText("");

        for (BaseActor object : removeList) {
            object.remove();
        }

        // camera adjustment
        Camera mainCamera = mainStage.getCamera();

        // center camera on playerenemy
        mainCamera.position.x = playerShip.getX() + playerShip.getOriginX();
        mainCamera.position.y = playerShip.getY() + playerShip.getOriginY();

        // bound camera to layout
        mainCamera.position.x = MathUtils.clamp(mainCamera.position.x, viewwidth / 2, mapPixelWidth - viewwidth / 2);
        mainCamera.position.y = MathUtils.clamp(mainCamera.position.y, viewheight / 2, mapPixelHeight - viewheight / 2);
        mainCamera.update();

        // adjust tilemap camera to stay in sync with main camera
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.position.y = mainCamera.position.y;
        tiledCamera.update();
        tiledMapRenderer.setView(tiledCamera);

        //Altered For Assessment 3
        //Exponentially increases points
        timer += delta;
        if (timer > 1) {
            int points_gain = Integer.parseInt(pointsValueLabel.getText().toString()) / 250;
            if (points_gain == 0) {
                points_gain = 1;
            }
            pirateGame.getPlayer().addPoints(points_gain);
            timer -= 1;
        }
        //End Altered

        pointsValueLabel.setText(Integer.toString(pirateGame.getPlayer().getPoints()));
    }

    @Override
    public void render(float delta) {
        uiStage.act(delta);

        mainStage.act(delta);
        update(delta);

        // render
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.render(backgroundLayers);
        mainStage.draw();

        tiledMapRenderer.render(foregroundLayers);

        uiStage.draw();

        if (!playerShip.isAnchor()) {
            playerShip.addAccelerationAS(playerShip.getRotation(), 10000);
        } else {
            playerShip.setAccelerationXY(0, 0);
            playerShip.setDeceleration(500);
        }
    }

    @Override
    public void dispose() {
        mainStage.dispose();
        uiStage.dispose();
        playerShip.getSailingTexture().dispose();
    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}