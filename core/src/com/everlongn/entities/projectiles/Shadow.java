package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Entities;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Shadow extends Projectile {
    public ParticleEffect movingParticle;
    public Player player;
    public int direction;
    public static float maxLife = 60;

    public float life, finishCounter, figureAlpha;
    public boolean lifeOut, casted;

    public Shadow(ControlCenter c, float x, float y, int width, int height, Player player, int direction, float forceX, float forceY, boolean casted) {
        super(c, x, y, width, height, 1);
        this.player = player;
        this.direction = direction;
        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                Constants.BIT_PARTICLE, (short)(Constants.BIT_TILE), (short)0, this);

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/shadowTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        moveByForce(new Vector2(forceX, forceY));

        if(casted)
            figureAlpha = 1;
    }

    @Override
    public void tick() {
        life += Gdx.graphics.getDeltaTime();
        if(life > maxLife) {
            movingParticle.getEmitters().get(0).setContinuous(false);
            lifeOut = true;
        }

        if(lifeOut && movingParticle.isComplete()) {
            finishCounter += Gdx.graphics.getDeltaTime();
        }

        if(finishCounter == 1) {
            active = false;
        }

        if(lifeOut) {
            figureAlpha = 0;
        }

        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());

        body.setLinearVelocity(body.getLinearVelocity().x/1.03f, body.getLinearVelocity().y);

        if(Math.abs(body.getLinearVelocity().x) < 0.1 && Math.abs(body.getLinearVelocity().y) < 0.1 && figureAlpha < 1 && !lifeOut) {
            figureAlpha += 0.01;
            if(figureAlpha > 0.5f) {
                figureAlpha = 0.5f;
            }
        } else {
            figureAlpha -= 0.05f;
            if(figureAlpha < 0f) {
                figureAlpha = 0f;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        batch.setColor(0f, 0f, 0f, figureAlpha);
        batch.draw(Entities.shadowFriend[direction], body.getPosition().x*Constants.PPM - 114/2, body.getPosition().y*Constants.PPM, 114, 114);
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();
    }

    @Override
    public void finish() {

    }
}
