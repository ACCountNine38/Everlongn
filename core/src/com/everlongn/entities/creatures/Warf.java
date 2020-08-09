package com.everlongn.entities.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Sounds;
import com.everlongn.entities.Animation;
import com.everlongn.entities.Creature;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Warf extends Creature {
    public boolean enraged = true;

    public Warf(float x, float y, int size) {
        super(x, y, size, size, 5f, 0.25f + (float)Math.random()*0.1f);

        // base variables
        body = Tool.createEntity((int)(x), (int)(y), width/12, height/2 - height/8, false, density, true,
                Constants.BIT_ENEMY, (short)(Constants.BIT_TILE | Constants.BIT_PROJECTILE), (short)0, this);

        chase = new Animation[2];
        chase[0] = new Animation(1f/20f, Entities.warfWalk[0], true);
        chase[1] = new Animation(1f/20f, Entities.warfWalk[1], true);

        boundWidth = size - 20;
        boundHeight = height/2;

        setMaxHealth(height);
        setMaxResistance(15);

        enemyList.add("spider");
        enemyList.add("hydra");
        form = "warf";
        type.add("warf");

        damage = size/6;

        knockbackResistance = 1.06f;
        jumpCondition = speed/2;

        vulnerableToArcane = true;

        for(int i = 0; i < destroyed.length; i++)
            destroyed[i].getEmitters().first().scaleSize(1.5f);
        jumpForce = size*7;

        sightHeight = 400;
        sightWidth = 400;
        sightBound = new Rectangle(body.getPosition().x*Constants.PPM - sightWidth/2, body.getPosition().y*Constants.PPM - sightHeight/2, sightWidth, sightHeight);
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
                chase();
                if(enraged) {

                } else {

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

    public void natural() {
        status = "natural";
        transitionTimer = 0;
        naturalTimer+=Gdx.graphics.getDeltaTime();
        if(naturalTimer > naturalRotation && canSwitchNatural()) {
            naturalTimer = 0;
            if(naturalStatus < 2) {
                naturalStatus = (int)(Math.random()*3);
            } else {
                naturalStatus = 2;
            }

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

    public boolean canSwitchNatural() {
        if(direction == 0 && (chase[0].currentIndex == 0 || chase[0].currentIndex == chase[0].textures.length - 1)) {
            return true;
        } else if(direction == 1 && (chase[1].currentIndex == 0 || chase[1].currentIndex == chase[1].textures.length - 1)) {
            return true;
        }

        return false;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(alive) {
            //batch.draw(Tiles.blackTile, leapBound.x, leapBound.y, leapBound.width, leapBound.height);
            if(getCurrentFrame() != null)
                batch.draw(getCurrentFrame(), body.getPosition().x * PPM - width/2 + width/24, body.getPosition().y * PPM - height/3f, width, height);
        } else {
            batch.setColor(0f, 0f, 0f, fadeAlpha);
            if(getCurrentFrame() != null)
                batch.draw(getCurrentFrame(), body.getPosition().x * PPM - width/2 + width/24, body.getPosition().y * PPM - height/3f, height, height);
            batch.setColor(1f, 1f, 1f, 1f);
            destroyed[destroyedDirection].draw(batch);
        }
        batch.end();
    }

    @Override
    public void finish() {
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
