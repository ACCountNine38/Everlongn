package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;

public abstract class Projectile extends Entity {
    public float speedX, speedY, explosionTimer;
    public boolean deactivate, lifeOut, exploded;

    public Projectile(ControlCenter c, float x, float y, int width, int height, float density) {
        super(c, x, y, width, height, density);
        health = 10000;
    }

    public void moveByVelocityX() {
        body.setLinearVelocity(speedX, body.getLinearVelocity().y);
    }

    public void moveByVelocityY() {
        body.setLinearVelocity(body.getLinearVelocity().x, speedY);
    }

    public void moveByForce(Vector2 force) {
        body.applyForceToCenter(force, false);
    }

    @Override
    public abstract void tick();

    @Override
    public abstract void render(SpriteBatch batch);

    public abstract void finish();

}
