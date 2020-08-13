package com.everlongn.utils.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Sounds;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.components.UIComponent;

public class TextImageButton extends UIComponent {
    public float ey;
    public Texture on, off;
    public boolean selected, soundCanPlay;

    public TextImageButton(int x, int y, int ey, int width, int height, String text,
                           Texture on, Texture off, BitmapFont font, boolean clickable) {
        super(x, y, text, clickable, font);
        this.ey = ey;
        this.on = on;
        this.off = off;
        this.width = width;
        this.height = height;
    }

    @Override
    public void tick() {
        if(ControlCenter.mousePos.x > x && ControlCenter.mousePos.x < x + width &&
                ControlCenter.mousePos.y < ControlCenter.height - y && ControlCenter.mousePos.y > ControlCenter.height - y - height) {
            hover = true;
            if(soundCanPlay) {
                Sounds.playSound(Sounds.buttonHover);
                soundCanPlay = false;
            }
        } else {
            hover = false;
            soundCanPlay = true;
        }
        if(y < ey) {
            y += 5;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if(selected || hover) {
            batch.draw(on, x, y, width, height);
        } else {
            batch.draw(off, x, y, width, height);
        }
        if(clickable) {
            if (hover || selected) {
                font.setColor(Color.WHITE);
                layout.setText(font, text);
                font.draw(batch, text, x + width/2 - layout.width/2, y + height/2 + layout.height/2);
            } else {
                font.setColor(new Color(.3f, .3f, .3f, 1));
                font.draw(batch, text, x + width/2 - layout.width/2, y + height/2 + layout.height/2);
            }
        }
        else {
            font.setColor(new Color(.15f, .15f, .15f, 1));
            font.draw(batch, text, x + width/2 - layout.width/2, y + height/2 + layout.height/2);
        }
        font.setColor(Color.WHITE);
    }

    @Override
    public void onClick() {

    }
}
