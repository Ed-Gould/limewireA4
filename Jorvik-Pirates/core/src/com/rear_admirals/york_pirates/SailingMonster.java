package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.base.BaseActor;
import com.rear_admirals.york_pirates.base.PhysicsActor;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles the monster as displayed on the screen when sailing through the map
 */
public class SailingMonster extends PhysicsActor {
    private String name;
    private Texture texture;
    private int direction;

    private static final int speed = 250;

    /**
     * SailingMonster constructor
     * @param textureString
     */
    public SailingMonster(String textureString){
        this.texture = new Texture((Gdx.files.internal(textureString)));
        this.setWidth(this.texture.getWidth());
        this.setHeight(this.texture.getHeight());
        this.setOrigin(Align.center);
        this.setEllipseBoundary();
        this.setAnchor(false);
        this.setMaxSpeed(speed);
    }

    //Get a random direction.
    public int getRandomDirection(){
        return (int)((Math.random()*360));
    }

    public void move(ArrayList<BaseActor> obstacleList) {

        for (BaseActor obstacle : obstacleList) {

            if (this.overlaps(obstacle, false)) {
                this.setAnchor(true);
                direction = getRandomDirection();
                this.rotateBy(direction);
                this.setAnchor(false);
                this.setAccelerationXY(0, 0);
            }
            else{
                this.setAnchor(false);
                this.setAccelerationXY(0, 0);
                this.addAccelerationAS(this.getRotation(), 10000);
            }
        }
    }

    public void reverseVelocity(){
        this.rotateBy(180);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1, 1, 1, alpha);
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
    }
}
