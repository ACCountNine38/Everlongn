package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Shadow extends Projectile {
    public ParticleEffect movingParticle;
    public Player player;
    public int direction;

    public float life;
    public boolean lifeOut;

    public Shadow(ControlCenter c, float x, float y, int width, int height, Player player, int direction, float forceX, float forceY) {
        super(c, x, y, width, height, 1);
        this.player = player;
        this.direction = direction;
        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/shadowTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                Constants.BIT_PARTICLE, (short)(Constants.BIT_TILE), (short)0);

        moveByForce(new Vector2(forceX, forceY));
    }

    @Override
    public void tick() {
        life += Gdx.graphics.getDeltaTime();
        if(life > 5) {
            movingParticle.getEmitters().get(0).setContinuous(false);
            lifeOut = true;
        }

        if(lifeOut && movingParticle.isComplete()) {
            active = false;
        }
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());

        body.setLinearVelocity(body.getLinearVelocity().x/1.03f, body.getLinearVelocity().y);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        batch.end();
    }
}
