package com.rear_admirals.york_pirates.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rear_admirals.york_pirates.PirateGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new PirateGame(), config);
    }
}
