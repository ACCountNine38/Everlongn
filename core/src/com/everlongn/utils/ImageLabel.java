package com.everlongn.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.states.MenuState;

public class ImageLabel extends UIComponent {
    private Texture texture;

    public int width, height;

    public ImageLabel(float x, float y, int width, int height, Texture texture) {
        super(x, y, "", false, MenuState.menuFont);
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    @Override
    public void tick() {
        updatePosition();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    @Override
    public void onClick() {

    }
}
