package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entities {
    public static TextureRegion[][] legRun, armRun, chestRun, headRun, legJump;
    public static TextureRegion[] armsHoldLeft, armsHoldRight, doubleArmsHold, shadowFriend;

    public static Texture legRunSprite, armRunSprite, chestRunSprite, headRunSprite, legJumpSprite,
            armHoldLeftSprite, armHoldRightSprite, doubleArmsHoldSprite, shadowFriendSprite;

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

        armHoldLeftSprite = new Texture(Gdx.files.internal("player/armsHoldLeft.png"), true);
        armHoldLeftSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        armHoldRightSprite = new Texture(Gdx.files.internal("player/armsHoldRight.png"), true);
        armHoldRightSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        doubleArmsHoldSprite = new Texture(Gdx.files.internal("player/doubleArmsHold.png"), true);
        doubleArmsHoldSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        shadowFriendSprite = new Texture(Gdx.files.internal("player/shadowFriend.png"), true);
        shadowFriendSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        shadowFriend = new TextureRegion[2];
        shadowFriend[1] = new TextureRegion(shadowFriendSprite, 0, 0, 2083, 2083);
        TextureRegion temp0 = new TextureRegion(shadowFriendSprite, 0, 0, 2083, 2083);
        temp0.flip(true, false);
        shadowFriend[0] = temp0;

        doubleArmsHold = new TextureRegion[2];
        doubleArmsHold[1] = new TextureRegion(doubleArmsHoldSprite, 0, 0, 500, 500);
        temp0 = new TextureRegion(doubleArmsHoldSprite, 0, 0, 500, 500);
        temp0.flip(true, false);
        doubleArmsHold[0] = temp0;

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

        armsHoldLeft = new TextureRegion[2];
        armsHoldLeft[1] = new TextureRegion(armHoldLeftSprite, 0, 0, 500, 500);
        temp0 = new TextureRegion(armHoldLeftSprite, 0, 0, 500, 500);
        temp0.flip(true, false);
        armsHoldLeft[0] = temp0;

        armsHoldRight = new TextureRegion[2];
        armsHoldRight[1] = new TextureRegion(armHoldRightSprite, 0, 0, 500, 500);
        temp0 = new TextureRegion(armHoldRightSprite, 0, 0, 500, 500);
        temp0.flip(true, false);
        armsHoldRight[0] = temp0;
    }
}
