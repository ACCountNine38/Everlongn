package com.everlongn.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;

public class TextManager {
    public static BitmapFont bfont = new BitmapFont(Gdx.files.internal("fonts/chalk14.fnt"));
    public static GlyphLayout layout = new GlyphLayout();

    public static SpriteBatch batch;

    public static void draw(java.lang.CharSequence str, int x, int y, Color color, float scale, boolean center) {
        bfont.setColor(color);
        bfont.getData().setScale(scale);
        if(center) {
            layout.setText(bfont, str);
            bfont.draw(batch, str, x - layout.width/2, y);
        } else {
            bfont.draw(batch, str, x, y);
        }
    }

    public static void draw(java.lang.CharSequence str, int x, int y, Color color, float scale, boolean center, BitmapFont font) {
        font.setColor(color);
        font.getData().setScale(scale);
        if(center) {
            layout.setText(font, str);
            font.draw(batch, str, x - layout.width/2, y);
        } else {
            font.draw(batch, str, x, y);
        }
    }

    public static void analogDraw(java.lang.CharSequence str, int x, int y, Color color) {
        bfont.setColor(color);
        layout.setText(bfont, str);
        bfont.draw(batch, str, x - layout.width, y);
    }
}
