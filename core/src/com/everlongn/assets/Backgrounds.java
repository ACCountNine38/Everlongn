package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Backgrounds {
    public static Texture dreamLayer1, dreamLayer2, dreamLayer3;
    public static TextureRegion[][] menu = new TextureRegion[8][2];

    public static TextureRegion[][][] dream;

    public static void init() {
        for(int i = 0 ; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                menu[i][j] = new TextureRegion(new Texture(Gdx.files.internal("backgrounds/menu" + (i+1) + ".png")), 0, 0, 2560, 1600);
            }
            menu[i][1].flip(true, false);
        }

        dreamLayer1 = new Texture(Gdx.files.internal("backgrounds/dreamLayer1.png"));
        dreamLayer2 = new Texture(Gdx.files.internal("backgrounds/dreamLayer2.png"));
        dreamLayer3 = new Texture(Gdx.files.internal("backgrounds/dreamLayer3.png"));

        dream = new TextureRegion[3][][];
        dream[0] = new TextureRegion[51][51];
        for(int j = 0; j < 51; j++) {
            for(int i = 0; i < 51; i++) {
                dream[0][i][50 - j] = new TextureRegion(dreamLayer1, i*50, j*50, 50, 50);
            }
        }

        dream[1] = new TextureRegion[51][51];
        for(int j = 0; j < 51; j++) {
            for(int i = 0; i < 51; i++) {
                dream[1][i][50 - j] = new TextureRegion(dreamLayer2, i*50, j*50, 50, 50);
            }
        }

        dream[2] = new TextureRegion[51][51];
        for(int j = 0; j < 51; j++) {
            for(int i = 0; i < 51; i++) {
                dream[2][i][50 - j] = new TextureRegion(dreamLayer3, i*50, j*50, 50, 50);
            }
        }

    }
}
