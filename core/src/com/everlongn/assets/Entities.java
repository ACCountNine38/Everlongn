package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entities {
    // creatures
    public static TextureRegion[][] spiderRun, hydraFly, warfWalk, warfAttack;
    public static TextureRegion[] spiderLeap, hydraBody, hydraHead, warfBody, warfHead;
    public static Texture spiderRunSprite, spiderLeapSprite, hydraWing1, hydraWing2, hydraHeadSprite, hydraBodySprite,
            warfWalkSprite1, warfWalkSprite2, warfWalkSprite3, warfBodySprite, warfHeadSprite, warfAttackSprite;

    // player
    public static TextureRegion[][] legRun, armRun, chestRun, headRun, legJump;
    public static TextureRegion[] armsHoldLeft, armsHoldRight, doubleArmsHold, shadowFriend;

    public static Texture legRunSprite, armRunSprite, chestRunSprite, headRunSprite, legJumpSprite,
            armHoldLeftSprite, armHoldRightSprite, doubleArmsHoldSprite, shadowFriendSprite;

    public static void init() {
        // creatures
        // spider
        spiderRunSprite = new Texture(Gdx.files.internal("creatures/dungonousSpawnling.png"),true);
        spiderRunSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        spiderRun = new TextureRegion[2][30];

        spiderLeapSprite = new Texture(Gdx.files.internal("creatures/dungonousSpawnlingJump.png"),true);
        spiderLeapSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        spiderLeap = new TextureRegion[2];
        spiderLeap[0] = new TextureRegion(spiderLeapSprite, 0, 0, 1080, 1080);
        spiderLeap[0].flip(true, false);
        spiderLeap[1] = new TextureRegion(spiderLeapSprite, 0, 0, 1080, 1080);

        for(int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (j + i * 5 < 30) {
                    spiderRun[1][j + i * 5] = new TextureRegion(spiderRunSprite, j * 1080, i * 1080, 1080, 1080);
                    TextureRegion temp1 = new TextureRegion(spiderRunSprite, j * 1080, i * 1080, 1080, 1080);
                    temp1.flip(true, false);
                    spiderRun[0][j + i * 5] = temp1;
                }
            }
        }

        // warf
        warfWalkSprite1 = new Texture(Gdx.files.internal("creatures/warfWalk1.png"),true);
        warfWalkSprite1.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        warfWalk = new TextureRegion[2][67];

        int currentIndex = 0;
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                warfWalk[1][j + i * 8] = new TextureRegion(warfWalkSprite1, j * 1200, i * 1200, 1200, 1200);
                TextureRegion temp1 = new TextureRegion(warfWalkSprite1, j * 1200, i * 1200, 1200, 1200);
                temp1.flip(true, false);
                warfWalk[0][j + i * 8] = temp1;
            }
        }
        currentIndex = 24;

        warfWalkSprite2 = new Texture(Gdx.files.internal("creatures/warfWalk2.png"),true);
        warfWalkSprite2.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                warfWalk[1][currentIndex + j + i * 8] = new TextureRegion(warfWalkSprite2, j * 1200, i * 1200, 1200, 1200);
                TextureRegion temp1 = new TextureRegion(warfWalkSprite2, j * 1200, i * 1200, 1200, 1200);
                temp1.flip(true, false);
                warfWalk[0][currentIndex + j + i * 8] = temp1;
            }
        }

        currentIndex = 48;

        warfWalkSprite3 = new Texture(Gdx.files.internal("creatures/warfWalk3.png"),true);
        warfWalkSprite3.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if(currentIndex + j + i * 8 < 67) {
                    warfWalk[1][currentIndex + j + i * 8] = new TextureRegion(warfWalkSprite3, j * 1200, i * 1200, 1200, 1200);
                    TextureRegion temp1 = new TextureRegion(warfWalkSprite3, j * 1200, i * 1200, 1200, 1200);
                    temp1.flip(true, false);
                    warfWalk[0][currentIndex + j + i * 8] = temp1;
                }
            }
        }

        warfBody = new TextureRegion[2];
        warfBodySprite = new Texture(Gdx.files.internal("creatures/warfWalkBody.png"),true);
        warfBodySprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        warfBody[1] = new TextureRegion(warfBodySprite, 0, 0, 1200, 1200);
        warfBody[0] = new TextureRegion(warfBodySprite, 0, 0, 1200, 1200);
        warfBody[0].flip(true, false);

        warfHead = new TextureRegion[2];
        warfHeadSprite = new Texture(Gdx.files.internal("creatures/warfWalkHead.png"),true);
        warfHeadSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        warfHead[1] = new TextureRegion(warfHeadSprite, 0, 0, 1200, 1200);
        warfHead[0] = new TextureRegion(warfHeadSprite, 0, 0, 1200, 1200);
        warfHead[0].flip(true, false);

        warfAttackSprite = new Texture(Gdx.files.internal("creatures/warfAttack.png"),true);
        warfAttackSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        warfAttack = new TextureRegion[2][59];

        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(j + i * 8 < 59) {
                    warfAttack[1][j + i * 8] = new TextureRegion(warfAttackSprite, j * 600, i * 600, 600, 600);
                    TextureRegion temp1 = new TextureRegion(warfAttackSprite, j * 600, i * 600, 600, 600);
                    temp1.flip(true, false);
                    warfAttack[0][j + i * 8] = temp1;
                }
            }
        }

        // hydra
        hydraWing1 = new Texture(Gdx.files.internal("creatures/hydraWing1.png"),true);
        hydraWing2 = new Texture(Gdx.files.internal("creatures/hydraWing2.png"),true);
        hydraWing1.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        hydraWing2.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        hydraHeadSprite = new Texture(Gdx.files.internal("creatures/hydraHead.png"),true);
        hydraBodySprite = new Texture(Gdx.files.internal("creatures/hydraBody.png"),true);
        hydraHeadSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        hydraBodySprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        hydraHead = new TextureRegion[2];
        hydraBody = new TextureRegion[2];

        hydraHead[0] = new TextureRegion(hydraHeadSprite, 0, 0, 500, 500);
        hydraHead[1] = new TextureRegion(hydraHeadSprite, 0, 0, 500, 500);
        hydraHead[1].flip(true, false);

        hydraBody[0] = new TextureRegion(hydraBodySprite, 0, 0, 500, 500);
        hydraBody[1] = new TextureRegion(hydraBodySprite, 0, 0, 500, 500);
        hydraBody[1].flip(true, false);

        hydraFly = new TextureRegion[2][2];

        hydraFly[0][0] = new TextureRegion(hydraWing1, 0, 0, 500, 500);
        hydraFly[1][0] = new TextureRegion(hydraWing1, 0, 0, 500, 500);
        hydraFly[1][0].flip(true, false);
        hydraFly[0][1] = new TextureRegion(hydraWing2, 0, 0, 500, 500);
        hydraFly[1][1] = new TextureRegion(hydraWing2, 0, 0, 500, 500);
        hydraFly[1][1].flip(true, false);

        // player
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
