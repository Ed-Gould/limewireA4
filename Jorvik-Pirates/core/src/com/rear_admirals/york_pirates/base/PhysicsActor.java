package com.rear_admirals.york_pirates.base;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.rear_admirals.york_pirates.base.BaseActor;

/**
 * Handles the actors' actions on screen, e.g collisions and movement
 */
public class PhysicsActor extends BaseActor {

    private Vector2 velocity;
    private Vector2 acceleration;

    // maximum speed
    private float maxSpeed;

    // speed reduction, in pixels/second, when not accelerating
    private float deceleration;

    // should image rotate to match velocity?
    private boolean autoAngle;

    private boolean anchor;

    /**
     * PhysicsActor Constructor
     */
    public PhysicsActor() {
        this.velocity = new Vector2();
        this.acceleration = new Vector2();
        this.maxSpeed = 9999;
        this.deceleration = 0;
        this.autoAngle = false;
        this.anchor = true;
    }

    // acceleration/deceleration methods

    /**
     * sets the acceleration of the actor in both axes
     *
     * @param ax - acceleration in X direction
     * @param ay - acceleration in Y direction
     */
    public void setAccelerationXY(float ax, float ay) {
        acceleration.set(ax, ay);
    }

    // set acceleration from angle and speed
    public void setAccelerationAS(float angleDeg, float speed) {
        acceleration.x = speed * MathUtils.cosDeg(angleDeg);
        acceleration.y = speed * MathUtils.sinDeg(angleDeg);
    }

    public void setDeceleration(float d) {
        deceleration = d;
    }

    public float getSpeed() {
        return velocity.len();
    }

    public void setSpeed(float s) {
        velocity.setLength(s);
    }

    public void setMaxSpeed(float ms) {
        maxSpeed = ms;
    }

    public float getMotionAngle() {
        return MathUtils.atan2(velocity.y, velocity.x) * MathUtils.radiansToDegrees;
    }

    public void addAccelerationAS(float angle, float amount) {
        acceleration.add(amount * MathUtils.cosDeg(angle), amount * MathUtils.sinDeg(angle));
    }

    /**
     * LibGDX method to display actions on screen
     *
     * @param dt - delta time, used to calculate where the BaseActor is at that time
     */
    public void act(float dt) {
        super.act(dt);

        // apply acceleration
        velocity.add(acceleration.x * dt, acceleration.y * dt);

        // decrease velocity when not accelerating
        if (acceleration.len() < 0.01) {
            float decelerateAmount = deceleration * dt;
            if (getSpeed() < decelerateAmount) setSpeed(0);
            else setSpeed(getSpeed() - decelerateAmount);
        }

        // cap at max speed
        if (getSpeed() > maxSpeed) setSpeed(maxSpeed);

        // apply velocity
        moveBy(velocity.x * dt, velocity.y * dt);

        // rotate image when moving
        if (autoAngle && getSpeed() > 0.1) setRotation(getMotionAngle());
    }

    public void setAnchor(boolean anchor) {
        this.anchor = anchor;
    }

    public boolean isAnchor() {
        return anchor;
    }
}
