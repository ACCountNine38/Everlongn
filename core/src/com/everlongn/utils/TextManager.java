package com.everlongn.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;

public class TextManager {
    private static BitmapFont bfont = new BitmapFont(Gdx.files.internal("chalk14.fnt"));
    private static GlyphLayout layout = new GlyphLayout();

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
}
