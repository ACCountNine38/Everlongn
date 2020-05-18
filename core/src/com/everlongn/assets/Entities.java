package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entities {
    public static TextureRegion[][] legRun, armRun, chestRun, headRun, legJump;
    public static TextureRegion[] armsHold;

    public static Texture legRunSprite, armRunSprite, chestRunSprite, headRunSprite, legJumpSprite, armHoldSprite;

    public static void init() {
        legRunSprite = new Texture(Gdx.files.internal("player/legsRun.png"),true);
        legRunSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        legJumpSprite = new Texture(Gdx.files.internal("player/legsJump.png"),true);
        legJumpSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        armRunSprite = new Texture(Gdx.files.internal("player/armsRun.png"), true);
        armRunSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        chestRunSprite = new Texture(Gdx.files.internal("player/chestRun.png"), true);
        chestRunSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        headRunSprite = new Texture(Gdx.files.internal("player/headRun.png"), true);
        headRunSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        armHoldSprite = new Texture(Gdx.files.internal("player/armsHold.png"), true);
        armHoldSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        armsHold = new TextureRegion[2];
        armsHold[1] = new TextureRegion(armHoldSprite, 0, 0, 500, 500);
        TextureRegion temp0 = new TextureRegion(armHoldSprite, 0, 0, 500, 500);
        temp0.flip(true, false);
        armsHold[0] = temp0;

        legRun = new TextureRegion[2][60];
        legJump = new TextureRegion[2][60];
        armRun = new TextureRegion[2][60];
        chestRun = new TextureRegion[2][60];
        headRun = new TextureRegion[2][60];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(j + i*8 < 60) {
                    legRun[1][j + i * 8] = new TextureRegion(legRunSprite, j * 500, i * 500, 500, 500);
                    TextureRegion temp1 = new TextureRegion(legRunSprite, j * 500, i * 500, 500, 500);
                    temp1.flip(true, false);
                    legRun[0][j + i * 8] = temp1;

                    legJump[1][j + i*8] = new TextureRegion(legJumpSprite, j*500, i*500, 500, 500);
                    TextureRegion temp11 = new TextureRegion(legJumpSprite, j*500, i*500, 500, 500);
                    temp11.flip(true, false);
                    legJump[0][j + i*8] = temp11;

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
