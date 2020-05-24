package com.everlongn.entities.projectiles;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;

import java.awt.*;

public class Shadow extends Projectile {
    public ParticleEffect movingParticle;
    public Player player;
    public int direction;

    public Shadow(ControlCenter c, float x, float y, int width, int height, Player player, int direction) {
        super(c, x, y, width, height, 1);
        this.player = player;
        this.direction = direction;
//        movingParticle.getEmitters().get(0).getTint().setColors();
        speedX = 4;
    }

    @Override
    public void tick() {
        if(body.getLinearVelocity().y > 1) {
            if (direction == 0) {
                moveByVelocityX();
            }
        } else {

        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        batch.end();
    }
}
