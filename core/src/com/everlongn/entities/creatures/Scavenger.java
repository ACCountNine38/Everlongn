package com.everlongn.entities.creatures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.Creature;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Scavenger extends Creature {
    public Scavenger(ControlCenter c, float x, float y) {
        super(c, x, y, 50, 100, 1, 2.5f);

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, density, true,
                Constants.BIT_ENEMY, (short)(Constants.BIT_PLAYER | Constants.BIT_TILE | Constants.BIT_PROJECTILE), (short)0, this);

        enemyList.add("player");

        setMaxHealth(150);
        setMaxResistance(20);
        knockbackResistance = 1.04f;

        vulnerableToArcane = true;
    }

    @Override
    public void tick() {
        if(target == null) {
            findTarget();
        } else {
            chase();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(Tiles.earthTile, body.getPosition().x * PPM, body.getPosition().y * PPM, width, height);
        batch.end();
    }
}
