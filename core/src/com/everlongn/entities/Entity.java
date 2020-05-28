package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

public abstract class Entity {
    public int health, maxHealth, resistance, maxResistance;
    public float x, y, density; //protected allow extended class to have access to them
    public int width, height;
    public String name;
    public ArrayList<String> type = new ArrayList<String>();
    public boolean active = true, stunned, knockbackResistant, vulnerableToArcane;

    public ControlCenter c;
    //protected SpriteBatch batch;

    public Body body;

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

//        body = Tool.createEntity((int)(x), (int)(y), width, height, false, density, true,
//                Constants.BIT_ENEMY, (short)(Constants.BIT_PLAYER | Constants.BIT_TILE), (short)0);
    }

    public abstract void tick();
    public abstract void render(SpriteBatch batch);

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setMaxHealth(int health) {
        maxHealth = health;
        this.health = health;
    }

    public void setMaxResistance(int resistance) {
        maxResistance = resistance;
        this.resistance = resistance;
    }

    public Rectangle getBound() {
        return new Rectangle(body.getPosition().x*Constants.PPM, body.getPosition().y*Constants.PPM, width, height);
    }
}
