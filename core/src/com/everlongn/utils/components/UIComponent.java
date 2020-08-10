package com.everlongn.utils.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;

/*
fix hydra directional attack
 */
public abstract class UIComponent {
    public GlyphLayout layout = new GlyphLayout();
    public float width, height, x, y;
    public String text;
    public boolean clickable, canPress = true, hover;
    public BitmapFont font;
    public boolean appear;

    public float ey, currentSpeed, defaultSpeed, reboundSpeed;
    public boolean active = true, upshift;

    public UIComponent(float x, float y, String text, boolean clickable, BitmapFont font) {
        layout.setText(font, text);
        width = layout.width;
        height = layout.height;
        this.x = x;
        this.y = y;
        this.text = text;
        this.clickable = clickable;
        this.font = font;
    }

    public void updatePosition() {
        if(y < ey && active) {
            y += currentSpeed;
            if(y >= ey) {
                y = ey;
                active = false;
                upshift = true;
            }
        } else if(y > ey && active) {
            y -= currentSpeed;
            if(y < ey) {
                y = ey;
                active = false;
            }
        }

        if(upshift) {
            currentSpeed-=2;
            if(currentSpeed <= reboundSpeed)
                currentSpeed = reboundSpeed;
            if(currentSpeed > reboundSpeed)
                y += currentSpeed;
            else {
                y -= currentSpeed;
                if(y < ey) {
                    y = ey;
                    upshift = false;
                }
            }
        }
    }

    public void activate(float ey, float speed, float reboundSpeed) {
        this.ey = ey;
        active = true;
        currentSpeed = speed;
        defaultSpeed = speed;
        upshift = false;
        this.reboundSpeed = reboundSpeed;
    }

    public boolean isHovering() {
        if(clickable) {
            if (ControlCenter.touchPos.x >= x && ControlCenter.touchPos.x < x + width &&
                    ControlCenter.touchPos.y < y && ControlCenter.touchPos.y >= y-height) {
                return true;
            }
        }
        return false;
    }

    public abstract void tick();
    public abstract void render(SpriteBatch batch);
    public abstract void onClick();
}
