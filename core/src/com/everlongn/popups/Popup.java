package com.everlongn.popups;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Popup {
    public boolean active;

    public abstract void tick();
    public abstract void render(SpriteBatch batch);
}
