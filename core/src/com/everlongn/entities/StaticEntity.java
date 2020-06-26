package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;

public abstract class StaticEntity extends Entity{

    public StaticEntity(float x, float y, int width, int height, float density) {
        super(x, y, width, height, density);
    }

    @Override
    public abstract void tick();

    @Override
    public abstract void render(SpriteBatch batch);

}
