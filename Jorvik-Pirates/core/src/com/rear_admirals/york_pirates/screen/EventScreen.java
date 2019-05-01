package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rear_admirals.york_pirates.*;
import com.rear_admirals.york_pirates.base.BaseActor;
import com.rear_admirals.york_pirates.base.BaseScreen;
import com.rear_admirals.york_pirates.screen.combat.CombatScreen;

import javax.xml.soap.Text;
import java.util.ArrayList;

import static com.rear_admirals.york_pirates.College.Derwent;
import static com.rear_admirals.york_pirates.ShipType.Player;

public class EventScreen extends BaseScreen {
    private Ship playerShip;
    private Monster monster;

    // Map Variables
    private ArrayList<BaseActor> obstacleList;
    private ArrayList<BaseActor> removeList;
    private ArrayList<BaseActor> regionList;

    // Calculate game world dimensions
    private TiledMap tiledMap;

    // Variables to get map data from .tmx file
    private MapProperties mapProperties;
    private int mapTileWidth;
    private int mapTileHeight;

    private int tilePixelSize;
    private final int mapPixelWidth;
    private final int mapPixelHeight;

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera tiledCamera;
    private int[] backgroundLayers = {0, 1, 2};
    private int[] foregroundLayers = {3};

    private double timer;

    private SpriteBatch eventMsgBatch;
    private Texture eventMsgBox = new Texture(Gdx.files.internal("eventMsgBox.png"));
    private boolean startMsgAcknowledged = false;
    private boolean isEventLost = false;

    private Table startTable, winTable, lostTable;
    private Label eventStartLabel, eventWinLabel, eventLostLabel;
    private TextButton eventStartButton, eventWinButton, eventLostButton;

    public EventScreen(final PirateGame main){
        super(main);

        playerShip = new Ship(Player, "Event Ship", Derwent);
        monster = new Monster("monster-4.png");

        mainStage.addActor(playerShip);
        mainStage.addActor(monster);

        obstacleList = new ArrayList<BaseActor>();
        //removeList = new ArrayList<BaseActor>();
        regionList = new ArrayList<BaseActor>();

        // set up Tiled Map and associated properties/attributes (width/height)
        tiledMap = new TmxMapLoader().load("event_map.tmx");
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
        tiledCamera.setToOrtho(false, viewWidth, viewHeight);
        tiledCamera.update();

        // Create event start Table
        startTable = new Table();

        eventStartLabel = new Label("A sea monster has attacked your ship, navigate through the obstacles to escape!",
                main.getSkin(), "default_black");
        eventStartButton = new TextButton("Start", main.getSkin());

        startTable.add(eventStartLabel).fill().padBottom(viewHeight/40);
        startTable.row();
        startTable.add(eventStartButton);
        startTable.align(Align.center);
        startTable.setFillParent(true);

        eventStartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start button clicked");
                startMsgAcknowledged = true;
            }
        });

        // Create event win Table
        winTable = new Table();

        eventWinLabel = new Label("You successfully escaped the sea monster!",
                main.getSkin(), "default_black");
        eventWinButton = new TextButton("Return to sailing", main.getSkin());

        winTable.add(eventWinLabel).fill().padBottom(viewHeight/40);
        winTable.row();
        winTable.add(eventWinButton);
        winTable.align(Align.center);
        winTable.setFillParent(true);

        eventWinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Win button clicked");
                pirateGame.setScreen(pirateGame.getSailingScene());
                dispose();
                return true;
            }
        });

        // Create event lost Table
        lostTable = new Table();

        eventLostLabel = new Label("The sea monster caught you and damage your ship! You lost some gold and wood.",
                main.getSkin(), "default_black");
        eventLostButton = new TextButton("Return to sailing", main.getSkin());

        lostTable.add(eventLostLabel).fill().padBottom(viewHeight/40);
        lostTable.row();
        lostTable.add(eventLostButton);
        lostTable.align(Align.center);
        lostTable.setFillParent(true);

        eventLostButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Lost button clicked");
                if(main.getPlayer().getWoods()>=10){
                    main.getPlayer().setWoods(main.getPlayer().getWoods()-10);
                }else{
                    main.getPlayer().setWoods(0);
                }
                if(main.getPlayer().getGold()>=30){
                    main.getPlayer().setGold(main.getPlayer().getGold()-30);
                }else{
                    main.getPlayer().setGold(0);
                }
                pirateGame.setScreen(pirateGame.getSailingScene());
                dispose();
                return true;
            }
        });

        eventMsgBatch = new SpriteBatch();

        MapObjects objects = tiledMap.getLayers().get("ObjectData").getObjects();
        for (MapObject object : objects) {
            String name = object.getName();

            // all object data assumed to be stored as rectangles
            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            Rectangle r = rectangleObject.getRectangle();

            if (name.equals("player")) {
                playerShip.setPosition(r.x, r.y);
            } else if (name.equals("monster")) {
                monster.setPosition(r.x, r.y);
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
                else{
                    System.out.println("Not college/department: " + solid.getName());
                }
                obstacleList.add(solid);
            } else {
                System.err.println("Unknown PhysicsData object.");
            }
        }

        timer = 0f;
        uiStage.addActor(startTable);

        InputMultiplexer im = new InputMultiplexer(uiStage, mainStage);
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void update(float delta){
        //removeList.clear();

        if (startMsgAcknowledged && !isEndReached() && !isEventLost){
            this.monster.updateSpeed(this.playerShip.getX());
            this.playerShip.playerMove(delta);
            this.monster.move();
        }

        if (isEndReached()){
            // Stop monster and player from moving when end is reached
            this.monster.setSpeed(0);
            this.monster.setAccelerationXY(0,0);
            this.playerShip.setSpeed(0);

            uiStage.clear();
            uiStage.addActor(winTable);
        }

        if (playerShip.overlaps(monster, false)){
            isEventLost = true;
            this.monster.setSpeed(0);
            this.monster.setAccelerationXY(0,0);
            this.playerShip.setSpeed(0);

            uiStage.clear();
            uiStage.addActor(lostTable);
        }

        for (BaseActor obstacle : obstacleList) {
            if (playerShip.overlaps(obstacle, true)) {
                // Obstacle must be a college if college not null
                if (!(obstacle.getCollege() == null)) {
                    College college = obstacle.getCollege();
                    if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                        if (!playerShip.getCollege().getAlly().contains(college) && !obstacle.getCollege().isBossDead()) {
                            System.out.println("Enemy");
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

        /*for (BaseActor object : removeList) {
            object.remove();
        }*/

        // camera adjustment
        Camera mainCamera = mainStage.getCamera();

        // center camera on player
        mainCamera.position.x = playerShip.getX() + playerShip.getOriginX();

        // bound camera to layout
        mainCamera.position.x = MathUtils.clamp(mainCamera.position.x, viewWidth / 2, mapPixelWidth - viewWidth / 2);
        mainCamera.update();

        // adjust tilemap camera to stay in sync with main camera
        tiledCamera.position.x = mainCamera.position.x;
        tiledCamera.update();
        tiledMapRenderer.setView(tiledCamera);
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

        if (!startMsgAcknowledged || isEndReached() || isEventLost){
            eventMsgBatch.begin();
            eventMsgBatch.draw(eventMsgBox, Gdx.graphics.getWidth() / 2 - eventMsgBox.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - eventMsgBox.getHeight() / 2);
            eventMsgBatch.end();
            uiStage.draw();
        }

        if (!isEndReached() && !isEventLost){
            if (!playerShip.isAnchor()) {
                playerShip.addAccelerationAS(playerShip.getRotation(), 10000);
            } else {
                playerShip.setAccelerationXY(0, 0);
                playerShip.setDeceleration(500);
            }
        }
    }

    public boolean isEndReached(){
        //7960 is the x coordinate for the end of the obstacles
        return this.playerShip.getX() > 7960;
    }
}

