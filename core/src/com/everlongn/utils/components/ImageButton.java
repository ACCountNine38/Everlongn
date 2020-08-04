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
        if(Gdx.input.getX() > x && Gdx.input.getX() < x + width &&
                ControlCenter.height - Gdx.input.getY() > y && ControlCenter.height - Gdx.input.getY() < y + height) {
            hover = true;
//            if(soundCanPlay) {
//                Sounds.playSound(Sounds.buttonHover);
//                soundCanPlay = false;
//            }
        } else {
            hover = false;
            //soundCanPlay = true;
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
