package com.everlongn.entities.projectiles;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.entities.*;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ArcaneEruption extends Projectile {
    public ParticleEffect movingParticle, explosion;
    public int direction;

    public float angle, actionForce;

    public ArcaneEruption(ControlCenter c, float x, float y, float density, int direction, float angle, float forceX, float forceY) {
        super(c, x, y, 4, 4, density);
        this.direction = direction;
        this.angle = angle;

        body = Tool.createEntity((int) (x), (int) (y), width, height, false, density, false,
                (short) Constants.BIT_PROJECTILE, (short) (Constants.BIT_TILE | Constants.BIT_ENEMY), (short) 0, this);

        if(direction == 0) {
            if(forceX > 0) {
                forceX *= -1;
            }
        } else {
            if(forceX < 0) {
                forceX *= -1;
            }
        }

        actionForce = Math.abs(forceX);

        moveByForce(new Vector2(forceX, forceY));

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/eruptionTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/eruptionExplosion"), Gdx.files.internal(""));
        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

        maxMovingRadius = 500;
        maxExplodingRadius = 1000;
        light = new PointLight(GameState.rayHandler, 300, new Color(0.05f, 0.03f, 0.03f, 1f), 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM);
        light.setSoft(true);
    }

    @Override
    public void tick() {
        if(!lifeOut) {
            body.setLinearVelocity((float) (body.getLinearVelocity().x / 1.03), body.getLinearVelocity().y);

            currentRadius+=20;
            if(currentRadius > maxMovingRadius)
                currentRadius = maxMovingRadius;
            light.setDistance(currentRadius);
            light.setPosition(body.getPosition().x * Constants.PPM,
                    body.getPosition().y * Constants.PPM);
        } else {
            body.setLinearVelocity(0, 0);

            if(maxReached) {
                currentRadius -= 25;
                if (currentRadius <= 0)
                    currentRadius = 0;
            } else {
                currentRadius += 25;
                if (currentRadius > maxExplodingRadius) {
                    currentRadius = maxMovingRadius;
                    maxReached = true;
                }
            }
            light.setDistance(currentRadius);
            light.setPosition(body.getPosition().x * Constants.PPM,
                    body.getPosition().y * Constants.PPM);

            if(!exploded) {
                explosionTimer += Gdx.graphics.getDeltaTime();
                if(explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
        }

        if (lifeOut && explosion.isComplete() && movingParticle.isComplete() && currentRadius <= 0) {
            GameState.world.destroyBody(body);
            explosion.dispose();
            movingParticle.dispose();
            light.remove();
            active = false;
        }

        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());

        if(lifeOut) {
            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            explosion.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        if(lifeOut) {
            explosion.draw(batch);
        }
        batch.end();
    }

    public void explode() {
        Rectangle explosionRectangle = new Rectangle(body.getPosition().x*Constants.PPM+2 - 100, body.getPosition().y*Constants.PPM+2 - 100,
                200, 200);

        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(explosionRectangle) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 700 + actionForce * 10;
                    float angle = (float)(Math.random()*(Math.PI/6) + Math.PI/8);
                    // c.body.getPosition().x*Constants.PPM + c.width/2 < body.getPosition().x*Constants.PPM + 2
                    if(direction == 0) {
                        c.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*(800+actionForce), false);
                    } else {
                        c.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*(800+actionForce), false);
                    }
                }
            }
        }
    }

    @Override
    public void finish() {
        lifeOut = true;
        movingParticle.getEmitters().get(0).setContinuous(false);
        explosion.start();
    }
}