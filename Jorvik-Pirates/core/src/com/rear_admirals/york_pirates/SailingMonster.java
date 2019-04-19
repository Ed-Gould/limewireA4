package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.rear_admirals.york_pirates.base.PhysicsActor;

public class SailingMonster extends PhysicsActor {
    private String name;
    private Texture texture;

    private static final int speed = 250;

    public SailingMonster(String textureString){
        this.texture = new Texture((Gdx.files.internal(textureString)));
        this.setWidth(this.texture.getWidth());
        this.setHeight(this.texture.getHeight());
        this.setOrigin(Align.topLeft);
        this.setEllipseBoundary();
        this.setAnchor(false);
    }

    //public void move() {

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1, 1, 1, alpha);
        batch.draw(new TextureRegion(texture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
    }
}
