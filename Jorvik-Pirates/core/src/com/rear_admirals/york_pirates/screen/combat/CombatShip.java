package com.rear_admirals.york_pirates.screen.combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.rear_admirals.york_pirates.base.BaseActor;

/**
 * Creates the ship on screen
 */
public class CombatShip extends BaseActor {

    private float ship_size;
    private Texture texture;

    /**
     * CombatShip constructor
     *
     * @param shipFile - path to file for ship's texture
     * @param ship_size - size of ship on screen
     */
    public CombatShip(String shipFile, float ship_size){
        this.texture = new Texture(shipFile);
        this.ship_size = ship_size;
        this.setBounds(getX(), getY(), ship_size,ship_size);
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.setColor(1,1,1, alpha);
        batch.draw(texture, getX(), getY(), ship_size,ship_size);
    }
}