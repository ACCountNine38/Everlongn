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

public class ArcaneTrail extends Projectile {
    public ParticleEffect movingParticle;
    public int direction;
    public static float maxLife = 60;

    public float life, finishCounter, figureAlpha, angle;
    public boolean lifeOut, casted;

    public ArcaneTrail(ControlCenter c, float x, float y, int width, int height, float density, int direction, float angle) {
        super(c, x, y, width, height, density);
        this.direction = direction;
        this.angle = angle;

        speedX = (float)Math.cos(angle)*10;
        speedY = (float)Math.sin(angle)*10;

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/arcaneTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                Constants.BIT_PARTICLE, (short)(Constants.BIT_TILE | Constants.BIT_ENEMY), (short)0);
    }

    @Override
    public void tick() {
        moveByVelocityX();
        moveByVelocityY();

        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        batch.end();
    }
}
