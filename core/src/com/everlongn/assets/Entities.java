package com.everlongn.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entities {
    public static TextureRegion[][] legRun, armRun, chestRun, headRun;

    public static Texture legRunSprite, armRunSprite, chestRunSprite, headRunSprite;
    public static Texture body;

    public static void init() {
        body = new Texture("core//res//images//creatures//player//playerBody.png");

        legRunSprite = new Texture("core//res//images//creatures//player//legsRun.png");
        armRunSprite = new Texture("core//res//images//creatures//player//armsRun.png");
        chestRunSprite = new Texture("core//res//images//creatures//player//chestRun.png");
        headRunSprite = new Texture("core//res//images//creatures//player//headRun.png");

        legRun = new TextureRegion[2][60];
        armRun = new TextureRegion[2][60];
        chestRun = new TextureRegion[2][60];
        headRun = new TextureRegion[2][60];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(j + i*8 < 60) {
                    legRun[1][j + i*8] = new TextureRegion(legRunSprite, j*500, i*500, 500, 500);
                    TextureRegion temp1 = new TextureRegion(legRunSprite, j*500, i*500, 500, 500);
                    temp1.flip(true, false);
                    legRun[0][j + i*8] = temp1;

                    armRun[1][j + i*8] = new TextureRegion(armRunSprite, j*500, i*500, 500, 500);
                    TextureRegion temp2 = new TextureRegion(armRunSprite, j*500, i*500, 500, 500);
                    temp2.flip(true, false);
                    armRun[0][j + i*8] = temp2;

                    headRun[1][j + i*8] = new TextureRegion(headRunSprite, j*500, i*500, 500, 500);
                    TextureRegion temp3 = new TextureRegion(headRunSprite, j*500, i*500, 500, 500);
                    temp3.flip(true, false);
                    headRun[0][j + i*8] = temp3;

                    chestRun[1][j + i*8] = new TextureRegion(chestRunSprite, j*500, i*500, 500, 500);
                    TextureRegion temp4 = new TextureRegion(chestRunSprite, j*500, i*500, 500, 500);
                    temp4.flip(true, false);
                    chestRun[0][j + i*8] = temp4;
                }
            }
        }
    }
}
