package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StaticObjects {
    public static Texture condensedDarkEnergySprite, ropeBitSprite, chainBitSprite, corpseBody1, corpseBody2, corpseHeadSprite, corpseLegsSprite,
            stickPollSprites[];

    public static TextureRegion condensedDarkEnergy, ropeBit, chainBit, corpseBody[][], corpseHead[], corpseLegs[], stickPoll[][];

    public static void init() {
        condensedDarkEnergySprite = new Texture(Gdx.files.internal("staticEntity/neutrinoBomb.png"),true);
        condensedDarkEnergySprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        condensedDarkEnergy = new TextureRegion(condensedDarkEnergySprite, 0, 0, condensedDarkEnergySprite.getWidth(), condensedDarkEnergySprite.getHeight());

        ropeBitSprite = new Texture(Gdx.files.internal("staticEntity/ropeBit.png"),true);
        ropeBitSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        ropeBit = new TextureRegion(ropeBitSprite, 0, 0, ropeBitSprite.getWidth(), ropeBitSprite.getHeight());

        chainBitSprite = new Texture(Gdx.files.internal("staticEntity/chainBit.png"),true);
        chainBitSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        chainBit = new TextureRegion(chainBitSprite, 0, 0, chainBitSprite.getWidth(), chainBitSprite.getHeight());

        // corpse
        corpseBody = new TextureRegion[2][2];
        corpseBody1 = new Texture(Gdx.files.internal("staticEntity/corpse/corpseBody1.png"),true);
        corpseBody1.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        corpseBody[0][0] = new TextureRegion(corpseBody1, 0, 0, corpseBody1.getWidth(), corpseBody1.getHeight());
        corpseBody[0][0].flip(true, false);
        corpseBody[0][1] = new TextureRegion(corpseBody1, 0, 0, corpseBody1.getWidth(), corpseBody1.getHeight());
        corpseBody2 = new Texture(Gdx.files.internal("staticEntity/corpse/corpseBody2.png"),true);
        corpseBody2.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        corpseBody[1][0] = new TextureRegion(corpseBody2, 0, 0, corpseBody2.getWidth(), corpseBody2.getHeight());
        corpseBody[1][0].flip(true, false);
        corpseBody[1][1] = new TextureRegion(corpseBody2, 0, 0, corpseBody2.getWidth(), corpseBody2.getHeight());

        corpseHead = new TextureRegion[2];
        corpseHeadSprite = new Texture(Gdx.files.internal("staticEntity/corpse/corpseHead.png"),true);
        corpseHeadSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        corpseHead[0] = new TextureRegion(corpseHeadSprite, 0, 0, corpseHeadSprite.getWidth(), corpseHeadSprite.getHeight());
        corpseHead[0].flip(true, false);
        corpseHead[1] = new TextureRegion(corpseHeadSprite, 0, 0, corpseHeadSprite.getWidth(), corpseHeadSprite.getHeight());

        corpseLegs = new TextureRegion[2];
        corpseLegsSprite = new Texture(Gdx.files.internal("staticEntity/corpse/corpseBottom.png"),true);
        corpseLegsSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        corpseLegs[0] = new TextureRegion(corpseLegsSprite, 0, 0, corpseLegsSprite.getWidth(), corpseLegsSprite.getHeight());
        corpseLegs[0].flip(true, false);
        corpseLegs[1] = new TextureRegion(corpseLegsSprite, 0, 0, corpseLegsSprite.getWidth(), corpseLegsSprite.getHeight());

        stickPollSprites = new Texture[5];
        stickPoll = new TextureRegion[5][2];
        for(int i = 0; i < 5; i++) {
            stickPollSprites[i] = new Texture(Gdx.files.internal("staticEntity/corpse/stickPoll"+(i+1)+".png"),true);
            stickPollSprites[i].setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

            stickPoll[i][0] = new TextureRegion(stickPollSprites[i], 0, 0, stickPollSprites[i].getWidth(), stickPollSprites[i].getHeight());
            stickPoll[i][0].flip(true, false);
            stickPoll[i][1] = new TextureRegion(stickPollSprites[i], 0, 0, stickPollSprites[i].getWidth(), stickPollSprites[i].getHeight());
        }
    }
}
