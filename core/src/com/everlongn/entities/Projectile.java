package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.game.ControlCenter;

public abstract class Projectile extends Entity {
    public float speedX, speedY, explosionTimer, damage;
    public boolean lifeOut, exploded, pickedUp;
    public Entity source;

    // light effects
    public float maxMovingRadius, maxExplodingRadius, currentRadius;
    public boolean maxReached;
    public PointLight light;

    public Projectile(float x, float y, int width, int height, float density) {
        super(x, y, width, height, density);
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

}
