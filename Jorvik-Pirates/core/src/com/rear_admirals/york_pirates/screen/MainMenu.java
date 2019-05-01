package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.base.BaseScreen;
import com.rear_admirals.york_pirates.screen.combat.CombatScreen;
import com.rear_admirals.york_pirates.Ship;

import static com.rear_admirals.york_pirates.PirateGame.Chemistry;
import static com.rear_admirals.york_pirates.ShipType.*;
import static com.rear_admirals.york_pirates.College.*;

/**
 * Handles the main menu GUI
 */
public class MainMenu extends BaseScreen {
    private Stage stage;

    private float screen_width;
    private float screen_height;

    public MainMenu(final PirateGame pirateGame) {
        super(pirateGame);

        Gdx.graphics.setTitle("York Pirates!");

        // Layout Properties
        Container<Table> tableContainer = new Container<Table>();
        tableContainer.setFillParent(true);
        tableContainer.setPosition(0, 0);
        tableContainer.align(Align.center);
        Table table = new Table();
        stage = new Stage(new FitViewport(1920, 1080));

        screen_width = stage.getWidth();
        screen_height = stage.getHeight();

        // Debugging
        System.out.println(screen_width + ", " + screen_height);

        Label title = new Label("Rear Admirals", pirateGame.getSkin(), "title");
        title.setAlignment(Align.center);

        TextButton sailing_mode = new TextButton("Start Game", pirateGame.getSkin()); // Starts sailing mode.
        TextButton combat_mode = new TextButton("Go to Combat Mode", pirateGame.getSkin());
        TextButton college_mode = new TextButton("Go to College screen", pirateGame.getSkin());
        TextButton department_mode = new TextButton("Go to Department screen", pirateGame.getSkin());
        TextButton weather_mode = new TextButton("Go to Monster screen", pirateGame.getSkin());
        //Added For Assessment 3
        TextButton exit_game = new TextButton("Exit Game", pirateGame.getSkin());
        //End Added
        //A4: add scoreboard.
        TextButton scoreboard_screen = new TextButton("Scoreboard",pirateGame.getSkin());

        // Allows button to be clickable, and sets process for when clicked.
        combat_mode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Altered For Assessment 3
                pirateGame.setScreen(new CombatScreen(pirateGame, new Ship(Player, Derwent)));
                //End Altered
                dispose();
            }
        });

        sailing_mode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pirateGame.setScreen(pirateGame.getSailingScene());
                dispose();
            }
        });

        college_mode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pirateGame.setScreen(new CollegeScreen(pirateGame, Derwent));
                dispose();
            }
        });

        department_mode.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pirateGame.setScreen(new DepartmentScreen(pirateGame, Chemistry));
                dispose();
            }
        });

        weather_mode.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                pirateGame.setScreen(new EventScreen(pirateGame));
                dispose();
            }
        });

        //Added For Assessment 3
        exit_game.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(1);
                dispose();
            }
        });
        //End Added

        //A4: add scoreboard
        scoreboard_screen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pirateGame.setScreen(new ScoreboardScreen(pirateGame));
                dispose();
            }
        });
        //End Added

        tableContainer.setActor(table);


        table.add(title).padBottom(viewWidth / 20).width(viewWidth / 2);
        table.row(); // Ends the current row
        table.add(sailing_mode).uniform().padBottom(viewHeight / 40).size(viewWidth / 2, viewHeight / 10);
        //A4: Remove unnecessary button for testing purposes for the final product.
//        table.row();
//        table.add(new Label("These are for demo purposes, to show implementation of combat and colleges.", pirateGame.getSkin()));
//        table.row();
//        table.add(combat_mode).uniform().padBottom(viewHeight / 40).fill();
//        table.row();
//        table.add(college_mode).uniform().fill().padBottom(viewHeight / 40);
//        table.row();
//        table.add(department_mode).uniform().fill().padBottom(viewHeight / 40);
//        table.row();
//        table.add(weather_mode).uniform().fill().padBottom(viewHeight / 40);
        table.row();
        table.add(exit_game).uniform().padBottom(viewHeight / 40).size(viewWidth / 2, viewHeight / 10);
        table.row();
        table.add(scoreboard_screen).uniform().padBottom(viewHeight / 40).size(viewWidth / 2, viewHeight / 10);

        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
        System.out.println("IP: stage");
    }

    @Override
    public void update(float delta) {
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}