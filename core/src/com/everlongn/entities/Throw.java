package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.tiles.Tile;

import java.util.ArrayList;

public abstract class Throw extends Entity {
    public float speedX, speedY, explosionTimer, damage, life, angle, rotation, lockX, lockY;
    public boolean lifeOut, exploded, pickedUp, despawn, collected;
    public int direction;

    public Rectangle throwBound;
    public Entity attached;
    public Tile locked;
    public ArrayList<Entity> damaged = new ArrayList<>();

    public Throw(float x, float y, int width, int height, float density) {
        super(x, y, width, height, density);
        health = 10000;
    }

    public void moveByForce(Vector2 force) {
        body.applyForceToCenter(force, false);
    }

    public void lockEntity(Entity e) {
        attached = e;
        lockX = body.getPosition().x - e.body.getPosition().x;
        lockY = body.getPosition().y - e.body.getPosition().y;
    }
    public void lockTile(Tile t) {
        locked = t;
        lockX = body.getPosition().x - t.body.getPosition().x;
        lockY = body.getPosition().y - t.body.getPosition().y;
    }

    @Override
    public abstract void tick();

    @Override
    public abstract void render(SpriteBatch batch);
}