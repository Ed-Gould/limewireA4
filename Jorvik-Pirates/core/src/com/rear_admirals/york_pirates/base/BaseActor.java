package com.rear_admirals.york_pirates.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.rear_admirals.york_pirates.College;
import com.rear_admirals.york_pirates.Department;

/**
 * BaseActor is the abstract base class for all entities such as
 * the player, their ship, or enemy ships as well as college/department
 * islands and obstacles.
 * Extends LibGDX Group class
 */
public class BaseActor extends Group {

    private TextureRegion region;
    private Polygon boundingPolygon;
    private College college;
    private Department department;

    /**
     * Basic abstract constructor
     */
    public BaseActor() {
        super();
        region = new TextureRegion();
        boundingPolygon = null;
        college = null;
        department = null;
    }

    /**
     * Calls LibGDX to update the BaseActor object's actions on screen
     * based on the delta time from the last render on screen.
     * @param dt - delta time, used to calculate where the BaseActor's
     *             action should be at that point in time
     */
    public void act(float dt) {
        super.act(dt);
    }

    /**
     * Calls LibGDX to draw the BaseActor on the screen (batch) specified.
     * @param batch - the window canvas to be drawn on
     * @param parentAlpha - ==============================================
     */
    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (isVisible()) batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }

    /**
     * Sets boundary for rectangle shaped actors.
     * Used for things college/department islands
     */
    public void setRectangleBoundary() {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }

    /**
     * Sets boundaries for ellipse shaped actors such
     * as ships
     */
    public void setEllipseBoundary() {
        // number of vertices;
        int n = 8;
        float w = getWidth();
        float h = getHeight();
        float[] vertices = new float[2 * n];
        for (int i = 0; i < n; i++) {
            float t = i * 6.28f / n;
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(t) + w / 2;
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(t) + h / 2;
        }
        boundingPolygon = new Polygon(vertices);
        boundingPolygon.setOrigin(getOriginX(), getOriginY());
    }

    public Polygon getBoundingPolygon() {
        boundingPolygon.setPosition(getX(), getY());
        boundingPolygon.setRotation(getRotation());
        return boundingPolygon;
    }

    /**
     * Determine if the collision polygons of two BaseActor
     * objects overlap.
     * If (resolve == true), then when there is overlap, move
     * this BaseActor
     * along minimum translation vector until there is no
     * overlap.
     */
    public boolean overlaps(BaseActor other, boolean resolve) {
        Polygon poly1 = this.getBoundingPolygon();
        Polygon poly2 = other.getBoundingPolygon();

        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) return false;

        Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
        boolean polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if (polyOverlap && resolve) {
            this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        }

        float significant = 0.5f;
        return (polyOverlap && (mtv.depth > significant));
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public void setOriginCentre() {
        if (getWidth() == 0) System.err.println("error: actor size not set");
        setOrigin(getWidth() / 2, getHeight() / 2);
    }
}