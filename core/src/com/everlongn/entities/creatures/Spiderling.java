package com.everlongn.entities.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.Animation;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.items.Melee;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Spiderling extends Creature {
    public Rectangle leapBound;
    public boolean leaping, landed;
    public float landTimer;

    public Spiderling(ControlCenter c, float x, float y, int size) {
        super(c, x, y, size, size, 1.5f, 2f + (float)Math.random()*1.5f);

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

        setMaxHealth(35 + (width - 100));
        setMaxResistance(5);

        enemyList.add("player");

        damage = size/8;

        knockbackResistance = 1.04f;
        jumpCondition = speed/2;

        vulnerableToArcane = true;

        destroyed.getEmitters().first().scaleSize(1.5f);
        jumpForce = size*7;

        // unique variables
        leapBound = new Rectangle(body.getPosition().x*Constants.PPM - 200, body.getPosition().y*Constants.PPM - 200, 400, 400);
    }

    @Override
    public void tick() {
        if(alive) {
            if (target == null) {
                findTarget();
            } else {
                if(landed) {
                    body.setLinearVelocity(body.getLinearVelocity().x/1.08f, body.getLinearVelocity().y);
                    landTimer += Gdx.graphics.getDeltaTime();
                    if(landTimer > 1f) {
                       landTimer = 0;
                       landed = false;
                       leaping = false;
                    }
                } else if(!leaping) {
                    leapBound.setPosition(body.getPosition().x * Constants.PPM - 200, body.getPosition().y * Constants.PPM - 200);
                    chase();
                    if (leapBound.contains(target.body.getPosition().x*Constants.PPM, target.body.getPosition().y*Constants.PPM)) {
                        leapStrike();
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
                        if(direction == 0)
                            target.body.applyForceToCenter(-550, 150, false);
                        else
                            target.body.applyForceToCenter(550, 150, false);
                        target.hurt(damage, GameState.difficulty);
                    }
                }
            }

            if (health <= 0) {
                health = 0;
                alive = false;
                body.setActive(false);
                finish();
            }
        } else {
            destroyed.update(Gdx.graphics.getDeltaTime());
            destroyed.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
            fadeAlpha-=0.15;
            if(fadeAlpha < 0) {
                fadeAlpha = 0;
            }
            if(destroyed.isComplete()) {
                destroyed.dispose();
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
            destroyed.draw(batch);
        }
        batch.end();
    }

    @Override
    public void finish() {
        int randomSound = (int)(Math.random()*3);
        Sounds.playSound(Sounds.spider[randomSound]);
        destroyed.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM - 10);
        destroyed.start();
    }
}
