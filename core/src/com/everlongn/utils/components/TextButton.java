package com.everlongn.utils.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Sounds;
import com.everlongn.states.MenuState;
import com.everlongn.utils.components.UIComponent;

public class TextButton extends UIComponent {
    public boolean center;
    public boolean soundCanPlay;

    public TextButton(float x, float y, String text, boolean clickable) {
        super(x, y, text, clickable, MenuState.menuFont);
    }

    public TextButton(float x, float y, String text, boolean clickable, boolean center, BitmapFont font) {
        super(x, y, text, clickable, font);
        this.center = center;
        this.font = font;
        layout.setText(font, text);
        super.x -= layout.width/2;
    }

    @Override
    public void tick() {
        if(isHovering()) {
            hover = true;
            if(soundCanPlay) {
                Sounds.playSound(Sounds.buttonHover);
                soundCanPlay = false;
            }
        } else {
            hover = false;
            soundCanPlay = true;
        }

        updatePosition();
    }

    @Override
    public void render(SpriteBatch batch) {
        if(clickable) {
            if (hover) {
                font.setColor(Color.WHITE);
                font.draw(batch, text, x, y);
            } else {
                font.setColor(new Color(.3f, .3f, .3f, 1));
                font.draw(batch, text, x, y);
            }
        }
        else {
            font.setColor(new Color(.15f, .15f, .15f, 1));
            font.draw(batch, text, x, y);
        }
        font.setColor(Color.WHITE);
    }

    @Override
    public void onClick() {

    }
}
