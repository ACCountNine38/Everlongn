package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ArcaneEruption extends Projectile {
    public ParticleEffect movingParticle;
    public int direction;
    public static float maxLife = 8;

    public float life, finishCounter, figureAlpha, angle;
    public boolean lifeOut, casted;

    public ArcaneEruption(ControlCenter c, float x, float y, float density, int direction, float angle, float forceX, float forceY) {
        super(c, x, y, 5, 5, density);
        this.direction = direction;
        this.angle = angle;

        body = Tool.createEntity((int) (x), (int) (y), width, height, false, density, false,
                (short) Constants.BIT_PROJECTILE, (short) (Constants.BIT_TILE | Constants.BIT_ENEMY), (short) 0);

        moveByForce(new Vector2(forceX, forceY));

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/arcaneTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();
    }

    @Override
    public void tick() {
        body.setLinearVelocity((float)(body.getLinearVelocity().x/1.03), body.getLinearVelocity().y);
//        if ((Math.abs(body.getLinearVelocity().x) < Math.abs(speedX) - 0.5 || Math.abs(body.getLinearVelocity().x) > Math.abs(speedX) + 0.5 ||
//                Math.abs(body.getLinearVelocity().y) < Math.abs(speedY) - 0.5 || Math.abs(body.getLinearVelocity().y) > Math.abs(speedY) + 0.5)
//                && !lifeOut && casted) {
//            lifeOut = true;
//            movingParticle.getEmitters().get(0).setContinuous(false);
//        }
//        moveByVelocityX();
//        moveByVelocityY();
        casted = true;

        life += Gdx.graphics.getDeltaTime();
        if (life > maxLife) {
            lifeOut = true;
            movingParticle.getEmitters().get(0).setContinuous(false);
        }

        if (lifeOut && movingParticle.isComplete()) {
            GameState.world.destroyBody(body);
            active = false;
        }

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