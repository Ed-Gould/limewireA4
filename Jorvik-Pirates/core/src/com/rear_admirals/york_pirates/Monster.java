package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.base.PhysicsActor;

/**
 * The Monster class to create the monster in
 * the monster chase minigame
 */
public class Monster extends PhysicsActor {
    private Texture texture;

    private static final int minSpeed = 225;
    private static final int maxSpeed = 675;

    /**
     * Monster Constructor
     *
     * @param textureString - the path to the file containing
     * the desired texture for the monster
     */
    public Monster(String textureString){
        this.texture = new Texture(Gdx.files.internal(textureString));
        this.setWidth(this.texture.getWidth());
        this.setHeight(this.texture.getHeight());
        this.setOrigin(Align.topLeft);
        this.setMaxSpeed(minSpeed);
        this.setEllipseBoundary();
        this.setAnchor(false);
    }

    /**
     * Handles the movements of the monster across the screen in the minigame
     */
    public void move(){
        this.setAccelerationXY(0, 0);
        this.addAccelerationAS(this.getRotation(), 10000);
        //this.setX(getX()+1.5f);
        this.setAnchor(false);
    }

    /**
     * Balance the monster's movement speed to keep a consistent difficulty
     * if the monster is closer to the player's ship it will slow down
     * but will speed up if it's far away
     *
     * @param shipX - the player ship's X coordinate
     */
    public void updateSpeed(float shipX){
        if (distanceToShip(shipX) > 1500) this.setMaxSpeed(maxSpeed);
        else if (distanceToShip(shipX) < 500) this.setMaxSpeed(minSpeed);
        else this.setMaxSpeed(distanceToShip(shipX) * 0.45f);
    }

    /**
     * calculates horizontal distance between monster and player's ship
     *
     * @param shipX - player ship's X coordinate
     * @return horizontal distance betweeen monster and ship
     */
    public float distanceToShip(float shipX){
        return Math.abs(shipX - this.getX());
    }

    /**
     * LibGDX method to draw monster on screen
     *
     * @param batch - the window canvas to be drawn on
     * @param alpha - transparency of monster object on screen
     */
    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1, 1, 1, alpha);
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
    }
}
