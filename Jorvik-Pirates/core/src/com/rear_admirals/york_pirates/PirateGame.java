package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rear_admirals.york_pirates.screen.MainMenu;
import com.rear_admirals.york_pirates.screen.SailingScreen;

/**
 * PirateGame handles the main gameloop, rendering things
 * on screen, etc
 * Child of LibGDX Game class
 */
public class PirateGame extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private Skin skin;
    private Player player;
	private SailingScreen sailingScene;
	public static Department Chemistry;
	public static Department Physics;
	//Added For Assessment 3
	public static Department Maths;
    //End Added

    /**
     * LibGDX method: Initialises the game
     */
    public void create() {
        Gdx.graphics.setTitle("York Pirates!");
        this.skin = new Skin(Gdx.files.internal("flat-earth-ui.json"));
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        player = new Player();
		Chemistry = new Department("Chemistry", "attack", this);
		Physics = new Department("Physics", "defence", this);
        //Added For Assessment 3
        Maths = new Department("Maths", "accuracy",this);
		//End Added
		this.sailingScene = new SailingScreen(this);
        setScreen(new MainMenu(this));
    }

    /**
     * LibGDX method: disposes of objects that are not needed
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    /**
     * LibGDX method: renders things on screen
     */
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    /**
     * LibGDX method: handles resizing of the game window
     * @param width - width of window
     * @param height - height of window
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    public Skin getSkin() {
        return this.skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Player getPlayer() {
        return this.player;
    }

    public SailingScreen getSailingScene() {
        return this.sailingScene;
    }
}