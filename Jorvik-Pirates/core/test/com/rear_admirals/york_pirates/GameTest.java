package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.rear_admirals.york_pirates.Department;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.Player;
import com.rear_admirals.york_pirates.screen.SailingScreen;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.badlogic.gdx.backends.headless.HeadlessApplication;

import java.awt.*;

import static com.rear_admirals.york_pirates.PirateGame.Chemistry;

/**
 * Sets up the LibGDX headless environment so that Mockito can be used
 * Running JUnit tests on their own does not initialise LibGDX so anything
 * in your tests that are dependent on it will not work
 * So Mockito is used to mock the LibGDX objects
 * Gets rid of those dependencies so no worrying about the implementation of
 * anything other than the method you're focused on testing
 *
 * Note - to use Mockito, you have to download the JAR (or use the one conveniently
 * placed in the 'libs' folder) and add it to the project modules in Project Structure
 */
public class GameTest {

    public static Application application;

    // Before running any tests, initialise application with headless backend
    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        });

        // Use Mockito to mock OpenGL methods since we are running headlessly
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    // Cleans up after testing
    @AfterClass
    public static void cleanUp() {
        application.exit();
        application = null;
    }
}
