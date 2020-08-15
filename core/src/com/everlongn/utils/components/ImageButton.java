package com.everlongn.utils.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Sounds;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.MenuState;

public class ImageButton extends UIComponent {
    public Texture[] img;
    public boolean soundCanPlay;

    public ImageButton(float x, float y, float width, float height, boolean clickable, Texture[] img) {
        super(x, y, "", clickable, MenuState.menuFont);

        this.img = img;
        this.width = width;
        this.height = height;
    }

    @Override
    public void tick() {
        if(ControlCenter.touchPos.x > x && ControlCenter.touchPos.x < x + width &&
                ControlCenter.touchPos.y > y && ControlCenter.touchPos.y < y + height) {
            hover = true;
        } else {
            hover = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(clickable) {
            if(hover) {
                batch.draw(img[1], x-5, y-5, width+10, height+10);
            } else {
                batch.draw(img[0], x, y, width, height);
            }
        } else {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.15f);
            batch.draw(img[0], x, y, width, height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);
        }
    }

    @Override
    public void onClick() {

    }
}
