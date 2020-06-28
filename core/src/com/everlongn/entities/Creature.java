package com.everlongn.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.everlongn.assets.Tiles;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;

import java.util.ArrayList;

public abstract class Creature extends Entity {
    public float speed, currentSpeed, knockbackResistance, previousVelY, fadeAlpha = 1f, jumpForce,
        jumpCondition, naturalRotation = (int)(Math.random()*4) + 2, sightHeight, sightWidth;
    public int direction, damage, bonusDamage, destroyedDirection, naturalStatus = (int)(Math.random()*3);
    public boolean canJump = true, jump, fall, airborn, alive = true, jumpable;
    public String status = "";

    public Entity target;
    public ArrayList<String> enemyList = new ArrayList<String>();

    public Animation[] chase, attack;
    public ParticleEffect[] destroyed = new ParticleEffect[3];

    public Rectangle sightBound;

    // timers
    public float naturalTimer, yChangeTimer, transitionTimer;

    public Creature(float x, float y, int width, int height, float density, float speed) {
        super(x, y, width, height, density);

        // default values
        this.speed = speed;
        currentSpeed = speed;
        type.add("creature");
        damage = 50;
        direction = 0; // 0-Left, 1-Right

        destroyed[2] = new ParticleEffect();
        destroyed[2].load(Gdx.files.internal("particles/destroyed"), Gdx.files.internal(""));
        destroyed[0] = new ParticleEffect();
        destroyed[0].load(Gdx.files.internal("particles/destroyedLeft"), Gdx.files.internal(""));
        destroyed[1] = new ParticleEffect();
        destroyed[1].load(Gdx.files.internal("particles/destroyedRight"), Gdx.files.internal(""));
    }

    public void move() {
        if(!stunned) {
            testBasicJump();
            if (direction == 0)
                body.setLinearVelocity(-speed, body.getLinearVelocity().y);
            else {
                body.setLinearVelocity(speed, body.getLinearVelocity().y);
            }
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x/knockbackResistance, body.getLinearVelocity().y);
            if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0) {
                stunned = false;
            }
        }
    }

    public void testBasicJump() {
        if(previousVelY == body.getLinearVelocity().y) {
            canJump = true;
        }
        previousVelY = body.getLinearVelocity().y;
        if(!jumpable) {
            return;
        }
        if(Math.abs(body.getLinearVelocity().x) < jumpCondition && canJump) {
            body.applyForceToCenter(0, jumpForce,false);
            canJump = false;
        }
    }

    public void findTarget() {
        Entity possibleTarget = null;

        for(int i = 0; i < EntityManager.entities.size(); i++) {
            Entity e = EntityManager.entities.get(i);
            if(e instanceof Creature && e != this) {
                Creature c = (Creature)e;
                for(int j = 0; j < c.type.size(); j++) {
                    if(enemyList.contains(c.type.get(j)) && sightBound.overlaps(c.getBound())) {
                        if(possibleTarget == null) {
                            possibleTarget = c;
                        } else {
                            if(Math.abs(x - possibleTarget.x) > Math.abs(x - c.x)) {
                                possibleTarget = c;
                            }
                        }
                        break;
                    }
                }

            }
        }
        target = possibleTarget;
    }

    public TextureRegion getCurrentFrame() {
        if(status.equals("chase") && chase[direction].getFrame() != null) {
            return chase[direction].getFrame();
        } else if(status.equals("natural") && chase[direction].getFrame() != null) {
            return chase[direction].getFrame();
        }

        return null;
    }

    public void chase() {
        transitionTimer += Gdx.graphics.getDeltaTime();
        if(transitionTimer < 0.5)
            return;
        if(target == null || target.body == null || !active || !target.active)
            return;
        if(!target.active) {
            target = null;
        }
        if(target.body.getPosition().x*Constants.PPM + target.width/2 < body.getPosition().x*Constants.PPM + width/2) {
            direction = 0;
        } else {
            direction = 1;
        }
        chase[direction].tick(Gdx.graphics.getDeltaTime());
        status = "chase";
        move();
    }

    public void natural() {
        status = "natural";
        transitionTimer = 0;
        naturalTimer+=Gdx.graphics.getDeltaTime();
        if(naturalTimer > naturalRotation) {
            naturalTimer = 0;
            naturalStatus = (int)(Math.random()*3);
            naturalRotation = (int)(Math.random()*4) + 2;
        }

        if(naturalStatus == 0) {
            direction = 0;
            body.setLinearVelocity(-speed, body.getLinearVelocity().y);
            chase[direction].tick(Gdx.graphics.getDeltaTime());
        } else if(naturalStatus == 1) {
            direction = 1;
            body.setLinearVelocity(speed, body.getLinearVelocity().y);
            chase[direction].tick(Gdx.graphics.getDeltaTime());
        } else {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
    }
}
