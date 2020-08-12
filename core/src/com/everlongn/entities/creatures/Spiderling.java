package com.everlongn.entities.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.Animation;
import com.everlongn.entities.Creature;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Spiderling extends Creature {
    public Rectangle leapBound;
    public boolean leaping, landed, paused;
    public float landTimer, pausedTimer;

    public Spiderling(float x, float y, int size) {
        super(x, y, size, size, 1.5f, 2f + (float)Math.random()*1.5f);

        if(size < 90) {
            density = 2;
        }
        // base variables
        body = Tool.createEntity((int)(x), (int)(y), width - 10, height/2, false, density, true,
                Constants.BIT_ENEMY, (short)(Constants.BIT_TILE | Constants.BIT_PROJECTILE), (short)0, this);

        chase = new Animation[2];
        chase[0] = new Animation(1f/60f, Entities.spiderRun[0], true);
        chase[1] = new Animation(1f/60f, Entities.spiderRun[1], true);

        boundWidth = size - 20;
        boundHeight = height/2;

        setMaxHealth(size/3);
        setMaxResistance(5);

        enemyList.add("player");
        enemyList.add("hydra");
        form = "spider";
        type.add("spider");

        damage = size/6;

        knockbackResistance = 1.04f;
        jumpCondition = speed/2;

        vulnerableToArcane = true;

        for(int i = 0; i < destroyed.length; i++)
            destroyed[i].getEmitters().first().scaleSize(1.5f);
        jumpForce = size*7;

        sightHeight = 200;
        sightWidth = 800;
        sightBound = new Rectangle(body.getPosition().x*Constants.PPM - sightWidth/2, body.getPosition().y*Constants.PPM - sightHeight/2, sightWidth, sightHeight);

        // unique variables
        leapBound = new Rectangle(body.getPosition().x*Constants.PPM - 180 + width/2, body.getPosition().y*Constants.PPM - 180, 360, 360);
    }

    @Override
    public void tick() {
        if(alive) {
            if(status.equals("chase") || health < maxHealth) {
                sightWidth = 1600;
                sightHeight = 450;
            } else {
                sightWidth = 800;
                sightHeight = 500;
            }
            sightBound.setPosition(body.getPosition().x*Constants.PPM - sightWidth/2, body.getPosition().y*Constants.PPM - sightHeight/2);
            sightBound.setSize(sightWidth, sightHeight);
            if (target == null) {
                findTarget();
                natural();
            } else {
                if(paused) {
                    if(target.body != null) {
                        if (target.body.getPosition().x > body.getPosition().x) {
                            direction = 1;
                        } else if (target.body.getPosition().x < body.getPosition().x) {
                            direction = 0;
                        }
                    }
                    stunned = true;
                    pausedTimer += Gdx.graphics.getDeltaTime();
                    body.setLinearVelocity(0, body.getLinearVelocity().y);
                    if(pausedTimer > 0.5 && health > 0) {
                        stunned = false;
                        paused = false;
                        pausedTimer = 0;
                        leapStrike();
                    }
                } else if(landed) {
                    body.setLinearVelocity(body.getLinearVelocity().x/1.08f, body.getLinearVelocity().y);
                    landTimer += Gdx.graphics.getDeltaTime();
                    if(landTimer > Math.random()*0.4 + 0.6) {
                       landTimer = 0;
                       landed = false;
                       leaping = false;
                    }
                } else if(!leaping) {
                    leapBound.setPosition(body.getPosition().x * Constants.PPM - 180 + width/2, body.getPosition().y * Constants.PPM - 180);
                    chase();
                    if (leapBound.contains(target.body.getPosition().x*Constants.PPM, target.body.getPosition().y*Constants.PPM)) {
                        paused = true;
                    }
                } else {
                    body.setLinearVelocity(body.getLinearVelocity().x/1.02f, body.getLinearVelocity().y);
                    if(previousVelY == body.getLinearVelocity().y) {
                        canJump = true;
                    }
                    previousVelY = body.getLinearVelocity().y;
                    if(canJump) {
                        leaping = false;
                        landed = true;
                    }
                    if(getBound().overlaps(target.getBound())) {
                        landed = true;
                        leaping = false;
                        Sounds.playSound(Sounds.basicImpact[(int)(Math.random()*3)], 2f);
                        if(direction == 0)
                            target.body.applyForceToCenter(-550, 120, false);
                        else
                            target.body.applyForceToCenter(550, 120, false);
                        target.hurt(damage, this);
                        if(target instanceof Creature) {
                            Creature c = (Creature) target;
                            c.target = this;
                        }
                    }
                }
            }

            if(target != null && (!target.getBound().overlaps(sightBound) || target.health <= 0)) {
                target = null;
            }

            if (health <= 0) {
                health = 0;
                alive = false;
                body.setActive(false);
                finish();
            }
        } else {
            destroyed[destroyedDirection].update(Gdx.graphics.getDeltaTime());
            //destroyed[destroyedDirection].getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
            fadeAlpha-=0.15;
            if(fadeAlpha < 0) {
                fadeAlpha = 0;
            }
            if(destroyed[destroyedDirection].isComplete()) {
                for(int i = 0; i < destroyed.length; i++)
                    destroyed[i].dispose();
                GameState.world.destroyBody(body);
                active = false;
            }
        }
    }

    public void leapStrike() {
        if(!canJump)
            return;

        if(direction == 0) {
            body.applyForceToCenter(-width*6, width*7, false);
        } else {
            body.applyForceToCenter(width*6, width*7, false);
        }
        leaping = true;
        canJump = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(alive) {
            //batch.draw(Tiles.blackTile, leapBound.x, leapBound.y, leapBound.width, leapBound.height);
            if(leaping)
                batch.draw(Entities.spiderLeap[direction], body.getPosition().x * PPM, body.getPosition().y * PPM - height/3f, width, height);
            else if(getCurrentFrame() != null)
                batch.draw(getCurrentFrame(), body.getPosition().x * PPM, body.getPosition().y * PPM - height/3f, width, height);
        } else {
            batch.setColor(0f, 0f, 0f, fadeAlpha);
            if(leaping)
                batch.draw(Entities.spiderLeap[direction], body.getPosition().x * PPM, body.getPosition().y * PPM - height/3f, width, height);
            else if(getCurrentFrame() != null)
                batch.draw(getCurrentFrame(), body.getPosition().x * PPM, body.getPosition().y * PPM - height/3f, width, height);
            batch.setColor(1f, 1f, 1f, 1f);
            destroyed[destroyedDirection].draw(batch);
        }
        batch.end();
    }

    @Override
    public void finish() {
        int randomSound = (int)(Math.random()*3);
        Sounds.playSound(Sounds.spider[randomSound]);

        if(body.getLinearVelocity().x < 0) {
            destroyedDirection = 0;
        } else if(body.getLinearVelocity().x > 0) {
            destroyedDirection = 1;
        } else if(body.getLinearVelocity().x > 0) {
            destroyedDirection = 2;
        }
        destroyed[destroyedDirection].getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
        destroyed[destroyedDirection].start();
    }
}
