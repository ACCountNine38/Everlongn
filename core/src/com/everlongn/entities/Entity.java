package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

public abstract class Entity {

    protected int health, maxHealth, resistance, maxResistance;
    protected float x, y; //protected allow extended class to have access to them
    protected int width, height;
    protected String name;
    protected ArrayList<String> type = new ArrayList<String>();
    protected boolean active = true;

    protected ControlCenter c;

    protected Body body;

    public Entity(ControlCenter c, float x, float y, int width, int height) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // default values
        maxHealth = 100;
        health = 100;
        maxResistance = 10;
        resistance = 10;
        name = "UNNAMED";

        body = Tool.createBox((int)(x), (int)(y), 40, 80, false);
    }

    public abstract void tick();
    public abstract void render(SpriteBatch batch);

}
