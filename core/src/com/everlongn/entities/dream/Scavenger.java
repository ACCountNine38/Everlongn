package com.everlongn.entities.dream;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.Creature;
import com.everlongn.game.ControlCenter;

import static com.everlongn.utils.Constants.PPM;

public class Scavenger extends Creature {
    public Scavenger(ControlCenter c, float x, float y) {
        super(c, x, y, 50, 100, 1, 4);

        enemyList.add("player");

        setMaxHealth(150);
        setMaxResistance(20);
        knockbackResistance = 1.04f;
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
