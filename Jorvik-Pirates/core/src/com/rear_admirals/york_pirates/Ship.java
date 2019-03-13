package com.rear_admirals.york_pirates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.rear_admirals.york_pirates.College;
import com.rear_admirals.york_pirates.ShipType;
import com.rear_admirals.york_pirates.base.PhysicsActor;

import static com.rear_admirals.york_pirates.College.Derwent;

public class Ship extends PhysicsActor {
    private String name;
    private int attack;
    private int defence;
    private int accuracy;
    private int health;
    private ShipType type;
    private int healthMax;
    private Texture sailingTexture;
    private College college;
    private boolean isBoss = false;
    //Added For Assessment 3
    private int pointValue;
    private int goldValue;
    //End Added

    // For testing purposes only. Use of this constructor in-game WILL cause errors.
    @Deprecated
    public Ship() {
        this.name = "DEBUG SHIP";
        this.attack = 5;
        this.defence = 5;
        this.accuracy = 5;
        this.healthMax = defence * 20;
        this.health = healthMax;
        this.college = Derwent;
        //Added For Assessment 3
        this.pointValue = 20;
        this.goldValue = 20;
        //End Added
    }

    public Ship(ShipType type, College college) {
        this.name = college.getName() + " " + type.getName();
        this.attack = type.getAttack();
        this.defence = type.getDefence();
        this.accuracy = type.getAccuracy();
        this.healthMax = defence * 20;
        this.health = healthMax;
        this.college = college;
        //Added For Assessment 3
        this.pointValue = 20;
        this.goldValue = 20;
        //End Added
        this.type = type;
        this.sailingTexture = new Texture(Gdx.files.internal("ship (1).png"));
        setupShip();
    }

    //Added For Assessment 3
    /**
     * Constructor for ship
     * @param type Type of ship from ShipType. Determines stats
     * @param college College Allegiance
     * @param isBoss Whether or not this ship is the boss ship for a college
     */
    public Ship(ShipType type, College college, boolean isBoss) {
        this.name = college.getName() + " " + type.getName();
        this.attack = type.getAttack();
        this.defence = type.getDefence();
        this.accuracy = type.getAccuracy();
        this.healthMax = defence * 20;
        this.health = healthMax;
        this.college = college;
        this.pointValue = type.getPointValue();
        this.goldValue = type.getGoldValue();
        this.type = type;
        this.sailingTexture = new Texture(Gdx.files.internal("ship (1).png"));
        this.isBoss = isBoss;
        setupShip();
    }
    //End Added

    public Ship(ShipType type, College college, String texturePath) {
        this.name = college.getName() + " " + type.getName();
        this.attack = type.getAttack();
        this.defence = type.getDefence();
        this.accuracy = type.getAccuracy();
        this.healthMax = defence * 20;
        this.health = healthMax;
        this.college = college;
        this.type = type;
        this.sailingTexture = new Texture(Gdx.files.internal(texturePath));
        setupShip();
    }

    public Ship(ShipType type, String name, College college) {
        this(type, college);
        this.name = name;
    }

    public Ship(int attack, int defence, int accuracy, ShipType type, College college, String name, boolean isBoss) {
        this.attack = attack;
        this.defence = defence;
        this.accuracy = accuracy;
        this.type = type;
        this.name = name;
        this.healthMax = defence * 20;
        this.college = college;
        //Added For Assessment 3
        this.pointValue = 200;
        this.goldValue = 100;
        //End Added
        this.health = healthMax;
        this.sailingTexture = new Texture(Gdx.files.internal("ship (1).png"));
        this.isBoss = isBoss;
        setupShip();
    }

    public void setupShip() {
        this.setWidth(this.sailingTexture.getWidth());
        this.setHeight(this.sailingTexture.getHeight());
        this.setOriginCentre();
        this.setMaxSpeed(500);
        this.setDeceleration(500);
        this.setEllipseBoundary();
    }

    public void playerMove(float dt) {
        this.setAccelerationXY(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.rotateBy(90 * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.rotateBy(-90 * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.setAnchor(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.setAnchor(true);
        }
    }

    public void damage(int value) {
        health = health - value;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(1, 1, 1, alpha);
        batch.draw(new TextureRegion(sailingTexture), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
    }

    // Getters and Setters
    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public int getHealthMax() {
        return healthMax;
    }

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void addAttack(int increase){
        this.attack = attack + increase;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
        this.healthMax = defence * 20;
    }

    public void addDefence(int increase){
        this.defence = defence + increase;
        this.healthMax = (defence) * 20;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // A4: Added function to get health from max
    public int getHealthFromMax(){
        return this.healthMax - this.health;
    }
    // End of A4 change

    public String getType() {
        return type.getName();
    }

    public Texture getSailingTexture() {
        return this.sailingTexture;
    }

    public boolean getIsBoss() {
        return this.isBoss;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    //Added For Assessment 3
    public int getGoldValue() {
        return this.goldValue;
    }

    public int getPointValue() {
        return this.pointValue;
    }
    //End Added
}