package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Items;
import com.everlongn.assets.Sounds;
import com.everlongn.entities.*;
import com.everlongn.items.Throwing;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Rock extends Throw {
    //public ParticleEffect explosion;

    public Rock(float x, float y, int direction, float angle, float damage) {
        super(x, y, 5, 5, 1);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;

        body = Tool.createBall((int) (x), (int) (y), 5, 0.9f,
                (short) Constants.BIT_PROJECTILE, (short) (Constants.BIT_TILE | Constants.BIT_ENEMY), (short) 0, this);

        float forceX = (float)(Math.abs(Math.sin(angle)*25));
        float forceY = (float)(Math.cos(angle)*25);

        if(direction == 0) {
            moveByForce(new Vector2(-forceX, -forceY));
        } else {
            moveByForce(new Vector2(forceX, -forceY));
        }

        throwBound = new Rectangle(0, 0, Throwing.triStar.width, Throwing.triStar.height);
//        explosion = new ParticleEffect();
//        explosion.load(Gdx.files.internal("particles/bounceExplosion"), Gdx.files.internal(""));
//        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
    }

    @Override
    public void tick() {
        if(!lifeOut) {
            if(direction == 0)
                rotation += 8;
            else
                rotation -= 8;
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

        if(lifeOut) {
            GameState.world.destroyBody(body);
            active = false;
        }

//        if(lifeOut && explosion.isComplete() && currentRadius <= 0) {
//            GameState.world.destroyBody(body);
//            explosion.dispose();
//            light.remove();
//            active = false;
//        }
//
//        if(lifeOut) {
//            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
//            explosion.update(Gdx.graphics.getDeltaTime());
//        }
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
                    c.hurt(damage, GameState.difficulty);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(body != null)
            batch.draw(Items.stone, body.getPosition().x*Constants.PPM - Throwing.stone.width/2, body.getPosition().y*Constants.PPM - Throwing.stone.height/2, Throwing.stone.width/2, Throwing.stone.width/2,
                    Throwing.stone.width, Throwing.stone.height, 1f, 1f, rotation);
//        if (lifeOut) {
//            explosion.draw(batch);
//        }
        batch.end();
    }

    public void destroy() {
        lifeOut = true;
        //explosion.start();
    }

    @Override
    public void finish() {
        lifeOut = true;
        Sounds.playSound(Sounds.bounce);
    }
}
