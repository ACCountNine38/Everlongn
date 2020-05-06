package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

public abstract class Entity {
    protected int health, maxHealth, resistance, maxResistance;
    protected float x, y, density; //protected allow extended class to have access to them
    protected int width, height;
    protected String name;
    protected ArrayList<String> type = new ArrayList<String>();
    protected boolean active = true;

    protected ControlCenter c;
    //protected SpriteBatch batch;

    protected Body body;

    public Entity(ControlCenter c, float x, float y, int width, int height, float density) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.density = density;
        //batch = c.getSpriteBatch();

        // default values
        maxHealth = 100;
        health = 100;
        maxResistance = 10;
        resistance = 10;
        name = "UNNAMED";

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, density);
    }

    public abstract void tick();
    public abstract void render(SpriteBatch batch);

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
