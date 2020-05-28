package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ArcaneTrail extends Projectile {
    public ParticleEffect movingParticle, explosion;
    public int direction;
    public static float maxLife = 8;

    public float life, angle;

    public ArcaneTrail(ControlCenter c, float x, float y, float density, int direction, float angle) {
        super(c, x, y, 5, 5, density);
        this.direction = direction;
        this.angle = angle;

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                (short)Constants.BIT_PROJECTILE, (short)(Constants.BIT_TILE | Constants.BIT_ENEMY), (short)0, this);

        float newAngle = (float)(angle - Math.PI/10 + Math.random()*(Math.PI/5));
        speedX = (float)Math.sin(newAngle)*(12.5f);
        speedY = -(float)Math.cos(newAngle)*(12.5f);

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/arcaneTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/trailExplosion"), Gdx.files.internal(""));
        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
    }

    @Override
    public void tick() {
        if(!lifeOut) {
            moveByVelocityX();
            moveByVelocityY();
        } else {
            body.setLinearVelocity(0, 0);

            if(!exploded) {
                explosionTimer += Gdx.graphics.getDeltaTime();
                if(explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
        }

        life += Gdx.graphics.getDeltaTime();
        if(life > maxLife) {
            finish();
        }

        if(lifeOut && explosion.isComplete() && movingParticle.isComplete()) {
            GameState.world.destroyBody(body);
            active = false;
        }

        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());

        if(lifeOut) {
            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            explosion.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void explode() {
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(this.getBound()) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 500 + (float)Math.random()*100;
                    float angle = (float)(Math.random()*(Math.PI/4));
                    if(direction == 0) {
                        c.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    } else {
                        c.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    }

                    break;
                }
            }
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

    @Override
    public void finish() {
        lifeOut = true;
        movingParticle.getEmitters().get(0).setContinuous(false);
        explosion.start();
    }
}
