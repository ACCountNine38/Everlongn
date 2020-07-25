package com.everlongn.entities.staticEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.assets.Herbs;
import com.everlongn.assets.StaticObjects;
import com.everlongn.entities.StaticEntity;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;

import static com.everlongn.utils.Constants.PPM;

public class CondensedDarkEnergy extends StaticEntity {
    public CondensedDarkEnergy(float x, float y) {
        super(x, y, 60, 60, 50);

        resetHealth(1);
        resistance = 0;
        baseRegenAmount = 0f;
    }

    @Override
    public void tick() {

    }

    public Rectangle getBound() {
        return new Rectangle(body.getPosition().x*Constants.PPM - width/2, body.getPosition().y*Constants.PPM - height/2, width, height);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(exploded) {
            alpha -= 0.05;
            if(alpha <= 0)
                alpha = 0;
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        }

        batch.draw(StaticObjects.condensedDarkEnergy, x*Constants.PPM - width/2, x*Constants.PPM - height/2, width, height);

        if(exploded)
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);

        batch.end();
    }

    @Override
    public void finish() {

    }
}
