package com.everlongn.entities.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.*;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

import static com.everlongn.utils.Constants.PPM;

public class Hydra extends Creature {
    public float directedAngle, currentVelX, currentVelY, stunnTimer, targetX, targetY,
        stabTimer, pausedTimer, resetAngle, resetTimer, stabCooldown, readyTimer;
    public boolean stunnedCheck, stabbing, findingNewLocation, paused, reset, ready, canHurt;

    public Rectangle stabBound;

    public ArrayList<Entity> damaged;

    public Hydra(float x, float y, int size) {
        super(x, y, size, size, 3f, 1f + (float)Math.random()*1.5f);

        // base variables
        body = Tool.createEntity((int)(x), (int)(y), width/8, height/2, false, density, true,
                Constants.BIT_ENEMY, (short)(Constants.BIT_TILE | Constants.BIT_PROJECTILE), (short)0, this);

        chase = new Animation[2];
        chase[0] = new Animation(1f/50f, Entities.hydraFly[0], true);
        chase[1] = new Animation(1f/50f, Entities.hydraFly[1], true);

        damaged = new ArrayList<>();

        boundWidth = width/8;
        boundHeight = height/2;

        setMaxHealth(size);
        setMaxResistance(10);

        enemyList.add("player");
        enemyList.add("spider");
        form = "hydra";
        type.add("hydra");

        damage = size/5;

        knockbackResistance = 1.05f;

        vulnerableToArcane = true;

        for(int i = 0; i < destroyed.length; i++)
            destroyed[i].getEmitters().first().scaleSize(1.5f);

        sightWidth = 650;
        sightHeight = 1000;
        sightBound = new Rectangle(body.getPosition().x*Constants.PPM - sightWidth/2, body.getPosition().y*Constants.PPM - sightHeight/2, sightWidth, sightHeight);

        // unique variables
        stabBound = new Rectangle(body.getPosition().x*Constants.PPM - 200, body.getPosition().y*Constants.PPM - 200, 400, 400);
    }

    @Override
    public void tick() {
        if(alive) {
            if (status.equals("chase") || health != maxHealth) {
                sightWidth = 1600;
                sightHeight = 1000;
            } else {
                sightWidth = 650;
                sightHeight = 1000;
            }
            sightBound.setPosition(body.getPosition().x * Constants.PPM - sightWidth / 2, body.getPosition().y * Constants.PPM - sightHeight / 2);
            sightBound.setSize(sightWidth, sightHeight);

            if(stabCooldown > 0) {
                stabCooldown -= ControlCenter.delta;
                if(stabCooldown < 0) {
                    stabCooldown = 0f;
                }
            }

            // test different AI conditions
            if(ready) {
                chase[direction].tick(Gdx.graphics.getDeltaTime());
                readyTimer -= ControlCenter.delta;
                if(readyTimer <= 0) {
                    pausedTimer -= ControlCenter.delta;
                    if(pausedTimer <= 0) {
                        ready = false;
                        stabbing = true;
                        currentVelX *= -2;
                        currentVelY *= -2;
                        paused = true;
                        pausedTimer = (float)(Math.random()*0.25f) + 0.25f;
                        stabCooldown = 5f;
                    }
                }
                if(readyTimer > 0 && pausedTimer > 0) {
                    body.setLinearVelocity(currentVelX, currentVelY);
                } else {
                    body.setLinearVelocity(0, 0);
                }
            } else if(stabbing) {
                if(target != null)
                    stab();
                else {
                    stabTimer = 0;
                    stabbing = false;
                    reset = true;
                    resetAngle = (float)Math.PI/4 + (float)(Math.random()*(Math.PI/2));
                    resetTimer = (float)(Math.random()*0.25f) + 0.5f;
                }
            } else if(!stunned) {
                chase[direction].tick(Gdx.graphics.getDeltaTime());
                stunnedCheck = false;
                if(paused) {
                    pausedTimer -= ControlCenter.delta;
                    body.setLinearVelocity( - 1f + (float)(Math.random()*2),  - 1f + (float)(Math.random()*2));
                    if(pausedTimer <= 0) {
                        paused = false;
                    }
                } else if(reset) {
                    resetTimer -= ControlCenter.delta;
                    body.setLinearVelocity(speed * 5 * (float) Math.cos(resetAngle), speed * 5 * (float) Math.sin(resetAngle));

                    if (resetTimer <= 0) {
                        reset = false;
                        paused = true;
                        pausedTimer = (float)(Math.random()*0.25f) + 0.25f;
                    }
                } else if(target == null) {
                    natural();
                    findTarget();
                } else {
                    aggro();
                    if (target != null && (!target.getBound().overlaps(sightBound) || target.health <= 0)) {
                        target = null;
                    }
                }
            } else {
                chase[direction].tick(Gdx.graphics.getDeltaTime());
                if(stunnTimer > 0.1) {
                    body.setLinearVelocity(body.getLinearVelocity().x/=1.04f, currentVelY/4);
                    if (Math.abs(body.getLinearVelocity().x) <= 0.25) {
                        stunned = false;
                        stunnTimer = 0;
                    }
                } else {
                    stunnTimer += ControlCenter.delta;
                    currentVelX = body.getLinearVelocity().x;
                    currentVelY = body.getLinearVelocity().y;
                }
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
            fadeAlpha -= 0.15;
            if (fadeAlpha < 0) {
                fadeAlpha = 0;
            }
            if (destroyed[destroyedDirection].isComplete()) {
                for (int i = 0; i < destroyed.length; i++)
                    destroyed[i].dispose();
                GameState.world.destroyBody(body);
                active = false;
            }
        }
    }

    public void aggro() {
        status = "attacking";
        canHurt = true;
        stabBound.setPosition(body.getPosition().x*Constants.PPM - 200, body.getPosition().y*Constants.PPM - 200);
        if(target.body.getPosition().x < body.getPosition().x) {
            direction = 0;
        } else {
            direction = 1;
        }

        if(!findingNewLocation || body.getLinearVelocity().x == 0 || body.getLinearVelocity().y == 0) {
            findingNewLocation = true;
            targetX = target.body.getPosition().x * Constants.PPM - 150 + (float)(Math.random()*300) - target.width/2;
            targetY = target.body.getPosition().y * Constants.PPM - 50 + (float)(Math.random()*400);
        }

        float ex = targetX;
        float ey = targetY;
        float sx = body.getPosition().x * Constants.PPM;
        float sy = body.getPosition().y * Constants.PPM;

        directedAngle = (float)Math.atan2(ey - sy, ex - sx);

        currentVelX = speed*2*(float)Math.cos(directedAngle);
        currentVelY = speed*2*(float)Math.sin(directedAngle) - 1f + (float)(Math.random()*2);
        body.setLinearVelocity(currentVelX, currentVelY);

        if(findingNewLocation && sx > targetX - speed && sx < targetX + speed
                && sy > targetY - speed && sy < targetY + speed) {
            findingNewLocation = false;
            paused = true;
            pausedTimer = (float)(Math.random()*0.25f) + 0.25f;
            return;
        }

        if(stabBound.contains(target.getBound()) && stabCooldown == 0) {
            directedAngle = (float)Math.atan2((target.body.getPosition().y * Constants.PPM - target.height/2) - sy,
                    target.body.getPosition().x * Constants.PPM - target.width/2 - sx);
            currentVelX = -(speed * 5.5f * (float) Math.cos(directedAngle));
            currentVelY = -(speed * 5.5f * (float) Math.sin(directedAngle));
            ready = true;
            readyTimer = 0.25f;
            pausedTimer = 0.35f;
        }
    }

    public void stab() {
        stabTimer += ControlCenter.delta;
        if(canHurt && target.getBound().overlaps(getBound())) {
            damage(target);
        }
//        if(canHurt) {
//            for(int i = 0; i < EntityManager.entities.size(); i++) {
//                Entity e = EntityManager.entities.get(i);
//                if(e.getBound().overlaps(getBound()) && e.health > 0 && e != this) {
//                    for(int j = 0; j < e.type.size(); j++) {
//                        if(enemyList.contains(e.type.get(j))) {
//                            damage(e);
//                        }
//                    }
//                }
//            }
//        }
        if(body.getLinearVelocity().y == 0) {
            stabTimer = 0;
            stabbing = false;
            reset = true;
            resetAngle = (float)Math.PI/4 + (float)(Math.random()*(Math.PI/2));
            resetTimer = (float)(Math.random()*0.25f) + 0.5f;
            body.setLinearVelocity(0, 0);
            return;
        }
        if(stabTimer >= 0.25f) {
            stabTimer = 0;
            stabbing = false;
            body.setLinearVelocity(0, 0);
            return;
        } else {
            body.setLinearVelocity(currentVelX, currentVelY);
        }
    }

    public void damage(Entity target) {
        target.hurt(damage);

        canHurt = false;
        if(target instanceof Player) {
            if(direction == 0)
                ((Player) target).xThrust -= width/12;
            else
                ((Player) target).xThrust += width/12;
        } else {
            if(direction == 0)
                target.body.applyForceToCenter(-width*3, 0, false);
            else
                target.body.applyForceToCenter(width*3, 0, false);
        }

        if(target instanceof Creature) {
            Creature c = (Creature) target;
            c.target = this;
        }
    }

    public void natural() {
        status = "natural";
        transitionTimer = 0;
        naturalTimer+=Gdx.graphics.getDeltaTime();
        if(naturalTimer > naturalRotation) {
            naturalTimer = 0;
            naturalStatus = (int)(Math.random()*2);
            naturalRotation = (int)(Math.random()*4) + 1;
            if(naturalStatus == 0) {
                directedAngle = (float)(Math.random()*Math.PI*2);
                currentVelX = speed*(float)Math.cos(directedAngle);
                currentVelY = speed*(float)Math.sin(directedAngle) - 1f + (float)(Math.random()*2);
            } else {
                directedAngle = (float)(Math.random()*Math.PI*2);
                currentVelX = speed/3*(float)Math.cos(directedAngle);
                currentVelY = speed/3*(float)Math.sin(directedAngle) - 1f + (float)(Math.random()*2);
            }
        }

        body.setLinearVelocity(currentVelX, currentVelY);

        if(currentVelX > 0) {
            direction = 1;
        } else if(currentVelX < 0) {
            direction = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(alive) {
            //batch.draw(Tiles.blackTile, getBound().x, getBound().y, getBound().width, getBound().height);
            batch.draw(Entities.hydraBody[direction], body.getPosition().x * PPM - width/2 + width/16, body.getPosition().y * PPM - 20, width, height);
            batch.draw(chase[direction].getFrame(), body.getPosition().x * PPM - width/2 + width/16, body.getPosition().y * PPM - 20, width, height);
            batch.draw(Entities.hydraHead[direction], body.getPosition().x * PPM - width/2 + width/16, body.getPosition().y * PPM - 20, width, height);
        } else {
            batch.setColor(0f, 0f, 0f, fadeAlpha);
            batch.draw(Entities.hydraBody[direction], body.getPosition().x * PPM - width/2 + width/16, body.getPosition().y * PPM - 20, width, height);
            batch.draw(chase[direction].getFrame(), body.getPosition().x * PPM - width/2 + width/16, body.getPosition().y * PPM - 20, width, height);
            batch.draw(Entities.hydraHead[direction], body.getPosition().x * PPM - width/2 + width/16, body.getPosition().y * PPM - 20, width, height);
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
