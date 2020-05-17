package com.everlongn.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.states.MenuState;

public class TextButton extends UIComponent {

    public TextButton(float x, float y, String text, boolean clickable) {
        super(x, y, text, clickable, MenuState.menuFont);
    }

    @Override
    public void tick() {
        if(isHovering()) {
            hover = true;
        } else {
            hover = false;
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
