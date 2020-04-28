package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;
import com.everlongn.game.Inputs;

import java.util.ArrayList;

public abstract class Creature extends Entity {
    protected float speed, velX, velY;;
    protected int direction, damage;

    public Creature(ControlCenter c, float x, float y, int width, int height) {
        super(c, x, y, width, height);

        // default values
        this.speed = speed;
        type.add("creature");
        damage = 50;
        direction = 0; // 0-Left, 1-Right
        speed = 1;
    }

    public void move() {
        x += velX;
        y += velY;
    }
}
