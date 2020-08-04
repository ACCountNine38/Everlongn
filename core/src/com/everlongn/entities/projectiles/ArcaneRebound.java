package com.everlongn.entities.projectiles;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Sounds;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ArcaneRebound extends Projectile {
    public ParticleEffect movingParticle, explosion;
    public int direction, maxBounce, numBounce;

    public float life, angle, yForce;
    public static Color color = new Color(0.00f, 0.01f, 0.00f, 1f);

    public ArcaneRebound(float x, float y, float density, int direction, float angle, float damage) {
        super(x, y, 5, 5, density);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;

        yForce = 100;
        maxBounce = 3 + (int)(Math.random()*5);
        body = Tool.createBall((int) (x), (int) (y), 5, 0.9f,
                (short) Constants.BIT_PROJECTILE, (short) (Constants.BIT_TILE | Constants.BIT_ENEMY), (short) 0, this);

        float newAngle = (float) (angle - Math.PI / 10 + Math.random() * (Math.PI / 5));

        float forceX = (float)Math.abs(Math.sin(newAngle)*25);
        if(direction == 0) {
            moveByForce(new Vector2(-forceX, -(float)Math.cos(newAngle)*25));
        } else {
            moveByForce(new Vector2(forceX, -(float)Math.cos(newAngle)*25));
        }

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/bounceTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/bounceExplosion"), Gdx.files.internal(""));
        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

        maxMovingRadius = 350;
        maxExplodingRadius = 450;
        light = new PointLight(GameState.rayHandler, 300, color, 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM);
        light.setSoft(true);
    }

    @Override
    public void tick() {
        if(body.getLinearVelocity().x < -0.5)
            direction = 0;
        else if(body.getLinearVelocity().x > 0.5) {
            direction = 1;
        }

        if (lifeOut) {
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

            if (!exploded) {
                explosionTimer += Gdx.graphics.getDeltaTime();
                if (explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
        } else {
            currentRadius+=25;
            if(currentRadius > maxMovingRadius)
                currentRadius = maxMovingRadius;
            light.setDistance(currentRadius);
            light.setPosition(body.getPosition().x * Constants.PPM,
                    body.getPosition().y * Constants.PPM);
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

        if (lifeOut) {
            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            explosion.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void explode() {
        Rectangle explosionRectangle = new Rectangle(body.getPosition().x*Constants.PPM+2 - 35, body.getPosition().y*Constants.PPM+2 - 35,
                70, 70);

        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(explosionRectangle) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 600;
                    float angle = (float)(Math.random()*(Math.PI/6) + Math.PI/8);
                    // c.body.getPosition().x*Constants.PPM + c.width/2 < body.getPosition().x*Constants.PPM + 2
                    if(direction == 0) {
                        c.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*(force), false);
                    } else {
                        c.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*(force), false);
                    }
                    c.hurt(damage);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        if (lifeOut) {
            explosion.draw(batch);
        }
        batch.end();
    }

    public void destroy() {
        lifeOut = true;
        movingParticle.getEmitters().get(0).setContinuous(false);
        explosion.start();
    }

    @Override
    public void finish() {
        if(numBounce >= maxBounce) {
            lifeOut = true;
            movingParticle.getEmitters().get(0).setContinuous(false);
            explosion.start();
        } else {
            numBounce++;
            Sounds.playSound(Sounds.bounce, (float)(maxBounce - numBounce)/maxBounce);
        }
    }
}