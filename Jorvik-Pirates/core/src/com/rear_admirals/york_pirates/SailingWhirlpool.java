package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.base.BaseActor;
import com.rear_admirals.york_pirates.base.PhysicsActor;

import java.util.ArrayList;

public class SailingWhirlpool extends PhysicsActor {
    private Texture texture;
    private int direction;

    private static final int speed = 50;

    public SailingWhirlpool(){
        this.texture = new Texture((Gdx.files.internal("whirlpool1.png")));
        this.setWidth(this.texture.getWidth());
        this.setHeight(this.texture.getHeight());
        this.setOrigin(Align.center);
        this.setEllipseBoundary();
        this.setAnchor(false);
        this.setMaxSpeed(speed);
    }

    public void setRandomPosition(ArrayList<BaseActor> obstacleList){
        this.setX((int) (Math.random() * 4500));
        this.setY((int) (Math.random() * 3000));
        for (BaseActor obstacle : obstacleList) {
            if(this.overlaps(obstacle,false)){
                setRandomPosition(obstacleList);
            }
        }
    }

    public void move(boolean changePosition,ArrayList<BaseActor> obstacleList) {
        if (!changePosition) {
            this.addAction(Actions.rotateBy(2, 1));
        } else {
            setRandomPosition(obstacleList);
        }
    }


    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1, 1, 1, alpha);
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
    }
}
