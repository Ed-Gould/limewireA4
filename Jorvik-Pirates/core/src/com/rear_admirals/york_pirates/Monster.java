package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.base.PhysicsActor;


public class Monster extends PhysicsActor {
    private Texture texture;

    private static final int minSpeed = 225;
    private static final int maxSpeed = 675;

    public Monster(String textureString){
        this.texture = new Texture(Gdx.files.internal(textureString));
        this.setWidth(this.texture.getWidth());
        this.setHeight(this.texture.getHeight());
        this.setOrigin(Align.topLeft);
        this.setMaxSpeed(minSpeed);
        this.setEllipseBoundary();
        this.setAnchor(false);
    }

    public void move(){
        this.setAccelerationXY(0, 0);
        this.addAccelerationAS(this.getRotation(), 10000);
        //this.setX(getX()+1.5f);
        this.setAnchor(false);
    }

    public void updateSpeed(float shipX){
        if (distanceToShip(shipX) > 1500) this.setMaxSpeed(maxSpeed);
        else if (distanceToShip(shipX) < 500) this.setMaxSpeed(minSpeed);
        else this.setMaxSpeed(distanceToShip(shipX) * 0.45f);
    }

    public float distanceToShip(float shipX){
        return Math.abs(shipX - this.getX());
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1, 1, 1, alpha);
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
    }
}
