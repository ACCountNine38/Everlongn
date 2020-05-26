package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Items {
    public static Texture logSprite, stoneSprite;
    public static Texture shadowStaffSprite, darkBaneSprite, broadSwordSprite, dragonDanceSprite, arcaneCasterSprite;

    public static TextureRegion log, stone;
    public static TextureRegion shadowStaffL, shadowStaffR, arcaneCasterL, arcaneCasterR;
    public static TextureRegion darkBaneL, darkBaneR, broadSwordL, broadSwordR, dragonDanceL, dragonDanceR;

    public static void init() {
        logSprite = new Texture(Gdx.files.internal("items/logItem.png"),true);
        logSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        log = new TextureRegion(logSprite, 0, 0, logSprite.getWidth(), logSprite.getHeight());

        stoneSprite = new Texture(Gdx.files.internal("items/stoneItem.png"), true);
        stoneSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stone = new TextureRegion(stoneSprite, 0, 0, logSprite.getWidth(), logSprite.getHeight());

        arcaneCasterSprite = new Texture(Gdx.files.internal("arcane/arcaneCaster.png"), true);
        arcaneCasterSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneCasterR = new TextureRegion(arcaneCasterSprite, 0, 0, arcaneCasterSprite.getWidth(), arcaneCasterSprite.getHeight());
        TextureRegion temp = new TextureRegion(arcaneCasterSprite, 0, 0, arcaneCasterSprite.getWidth(), arcaneCasterSprite.getHeight());
        temp.flip(true, false);
        arcaneCasterL = temp;

        shadowStaffSprite = new Texture(Gdx.files.internal("arcane/shadowStaff.png"), true);
        shadowStaffSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        shadowStaffR = new TextureRegion(shadowStaffSprite, 0, 0, shadowStaffSprite.getWidth(), shadowStaffSprite.getHeight());
        temp = new TextureRegion(shadowStaffSprite, 0, 0, shadowStaffSprite.getWidth(), shadowStaffSprite.getHeight());
        temp.flip(true, false);
        shadowStaffL = temp;

        darkBaneSprite = new Texture(Gdx.files.internal("melee/cursedSword.png"), true);
        darkBaneSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        darkBaneR = new TextureRegion(darkBaneSprite, 0, 0, darkBaneSprite.getWidth(), darkBaneSprite.getHeight());
        temp = new TextureRegion(darkBaneSprite, 0, 0, darkBaneSprite.getWidth(), darkBaneSprite.getHeight());
        temp.flip(true, false);
        darkBaneL = temp;

        broadSwordSprite = new Texture(Gdx.files.internal("melee/broadSword.png"), true);
        broadSwordSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        broadSwordR = new TextureRegion(broadSwordSprite, 0, 0, broadSwordSprite.getWidth(), broadSwordSprite.getHeight());
        temp = new TextureRegion(broadSwordSprite, 0, 0, broadSwordSprite.getWidth(), broadSwordSprite.getHeight());
        temp.flip(true, false);
        broadSwordL = temp;

        dragonDanceSprite = new Texture(Gdx.files.internal("melee/dragonDance.png"), true);
        dragonDanceSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        dragonDanceR = new TextureRegion(dragonDanceSprite, 0, 0, dragonDanceSprite.getWidth(), dragonDanceSprite.getHeight());
        temp = new TextureRegion(dragonDanceSprite, 0, 0, dragonDanceSprite.getWidth(), dragonDanceSprite.getHeight());
        temp.flip(true, false);
        dragonDanceL = temp;
    }
}
