package com.rear_admirals.york_pirates.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.College;
import com.rear_admirals.york_pirates.PirateGame;
import com.rear_admirals.york_pirates.Player;
import com.rear_admirals.york_pirates.base.BaseScreen;

public class MapScreen extends BaseScreen {
    private Player player;
    private Texture background,ship;
    private Image bgImage,shipImage;
    private float scalX,scalY;

    public MapScreen(PirateGame main) {
        super(main);
        this.player = main.getPlayer();

        scalX = 4992/mainStage.getWidth();
        scalY = 3328/mainStage.getHeight();

        background = new Texture(Gdx.files.internal("map_A4.png"));
        ship = new Texture(Gdx.files.internal("map_ship.png"));
        bgImage = new Image(background);
        shipImage = new Image(ship);
        bgImage.setWidth(mainStage.getWidth());
        bgImage.setHeight(mainStage.getHeight());

        shipImage.setWidth(mainStage.getWidth()/40);
        shipImage.setHeight(mainStage.getWidth()/40);
        shipImage.setX(player.getPlayerShip().getX()/scalX);
        shipImage.setY(player.getPlayerShip().getY()/scalY);

        mainStage.addActor(bgImage);
        mainStage.addActor(shipImage);
        Gdx.input.setInputProcessor(mainStage);



    }
        @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            System.out.println("ESCAPE");
            pirateGame.setScreen(pirateGame.getSailingScene());
            dispose();
        }
    }
}
