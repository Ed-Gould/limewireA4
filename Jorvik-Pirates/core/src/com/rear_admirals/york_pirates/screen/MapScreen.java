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
import com.rear_admirals.york_pirates.*;
import com.rear_admirals.york_pirates.base.BaseScreen;

public class MapScreen extends BaseScreen {
    private Player player;
    private SailingMonster sailingMonster;
    private SailingWhirlpool sailingWhirlpool;
    private Texture background,ship,whirlpool,monster;
    private Image bgImage,shipImage,sirenImage,whirlpoolImage,monsterImage;
    private float scalX,scalY;
    private float eventSize;

    public MapScreen(PirateGame main, SailingMonster sailingMonster, SailingWhirlpool sailingWhirlpool) {
        super(main);
        this.player = main.getPlayer();
        this.sailingMonster = sailingMonster;
        this.sailingWhirlpool = sailingWhirlpool;

        scalX = 4992/mainStage.getWidth();
        scalY = 3328/mainStage.getHeight();
        eventSize = mainStage.getWidth()/25;

        background = new Texture(Gdx.files.internal("map_A4.png"));
        ship = new Texture(Gdx.files.internal("map_ship.png"));
        whirlpool = new Texture(Gdx.files.internal("map_whirlpool.png"));
        monster = new Texture(Gdx.files.internal("map_monster.png"));

        bgImage = new Image(background);
        shipImage = new Image(ship);
        whirlpoolImage = new Image(whirlpool);
        monsterImage = new Image(monster);

        bgImage.setWidth(mainStage.getWidth());
        bgImage.setHeight(mainStage.getHeight());

        setImage(shipImage,player.getPlayerShip().getX(),player.getPlayerShip().getY());
        //event label testing
        setImage(whirlpoolImage,sailingWhirlpool.getX(),sailingWhirlpool.getY());
        setImage(monsterImage,sailingMonster.getX(),sailingMonster.getY());

        mainStage.addActor(bgImage);
        mainStage.addActor(shipImage);
        mainStage.addActor(whirlpoolImage);
        mainStage.addActor(monsterImage);

        Gdx.input.setInputProcessor(mainStage);
    }

    public void setImage(Image image, float x, float y){
        image.setHeight(eventSize);
        image.setWidth(eventSize);
        image.setX(x/scalX);
        image.setY(y/scalY);
    }

        @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            System.out.println("Map closed");
            pirateGame.setScreen(pirateGame.getSailingScene());
            dispose();
        }
    }
}
