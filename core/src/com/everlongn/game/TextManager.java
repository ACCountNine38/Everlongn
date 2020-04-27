package com.everlongn.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextManager {
    private static BitmapFont bfont = new BitmapFont();

    public static SpriteBatch batch;

    public static void render(java.lang.CharSequence str) {
        if(batch == null)
            return;
        //bfont.draw(batch, str, 10, 20);
    }
}
