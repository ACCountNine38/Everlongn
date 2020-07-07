package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Items {
    public static Texture logSprite, stoneSprite, omniEssenceSprite, omniCrystalSprite, tristarSprite;
    public static Texture shadowStaffSprite, darkBaneSprite, broadSwordSprite, dragonDanceSprite, arcaneCasterSprite, arcaneEruptionSprite,
        arcaneRicochetSprite, arcaneEscortSprite, arcaneReflectionSprite, arcaneOblivionSprite;
    public static Texture shurikenSprite, daggerSprite, throwKnifeSprite;

    public static TextureRegion log, stone, omniEssence, omniCrystal, tristar;
    public static TextureRegion shadowStaffL, shadowStaffR, arcaneCasterL, arcaneCasterR, arcaneEruptionL, arcaneEruptionR,
            arcaneRicochetL, arcaneRicochetR, arcaneEscortL, arcaneEscortR, arcaneReflectionL, arcaneReflectionR,
            arcaneOblivionL, arcaneOblivionR;
    public static TextureRegion darkBaneL, darkBaneR, broadSwordL, broadSwordR, dragonDanceL, dragonDanceR;
    public static TextureRegion shuriken, daggerL, daggerR, throwKnifeL, throwKnifeR;

    public static void init() {
        logSprite = new Texture(Gdx.files.internal("items/logItem.png"),true);
        logSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        log = new TextureRegion(logSprite, 0, 0, logSprite.getWidth(), logSprite.getHeight());

        tristarSprite = new Texture(Gdx.files.internal("throwing/tristar.png"),true);
        tristarSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        tristar = new TextureRegion(tristarSprite, 0, 0, tristarSprite.getWidth(), tristarSprite.getHeight());

        shurikenSprite = new Texture(Gdx.files.internal("throwing/shuriken.png"),true);
        shurikenSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        shuriken = new TextureRegion(shurikenSprite, 0, 0, shurikenSprite.getWidth(), shurikenSprite.getHeight());

        daggerSprite = new Texture(Gdx.files.internal("throwing/dagger.png"),true);
        daggerSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        daggerR = new TextureRegion(daggerSprite, 0, 0, daggerSprite.getWidth(), daggerSprite.getHeight());
        daggerL = new TextureRegion(daggerSprite, 0, 0, daggerSprite.getWidth(), daggerSprite.getHeight());
        daggerL.flip(true, false);

        throwKnifeSprite = new Texture(Gdx.files.internal("throwing/throwKnife.png"),true);
        throwKnifeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        throwKnifeR = new TextureRegion(throwKnifeSprite, 0, 0, throwKnifeSprite.getWidth(), throwKnifeSprite.getHeight());
        throwKnifeL = new TextureRegion(throwKnifeSprite, 0, 0, throwKnifeSprite.getWidth(), throwKnifeSprite.getHeight());
        throwKnifeL.flip(true, false);

        stoneSprite = new Texture(Gdx.files.internal("items/stoneItem.png"), true);
        stoneSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stone = new TextureRegion(stoneSprite, 0, 0, logSprite.getWidth(), logSprite.getHeight());

        omniEssenceSprite = new Texture(Gdx.files.internal("items/OmniEssence.png"), true);
        omniEssenceSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        omniEssence = new TextureRegion(omniEssenceSprite, 0, 0, omniEssenceSprite.getWidth(), omniEssenceSprite.getHeight());

        omniCrystalSprite = new Texture(Gdx.files.internal("items/OmniCrystal.png"), true);
        omniCrystalSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        omniCrystal = new TextureRegion(omniCrystalSprite, 0, 0, omniCrystalSprite.getWidth(), omniCrystalSprite.getHeight());

        arcaneCasterSprite = new Texture(Gdx.files.internal("arcane/arcaneCaster.png"), true);
        arcaneCasterSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneCasterR = new TextureRegion(arcaneCasterSprite, 0, 0, arcaneCasterSprite.getWidth(), arcaneCasterSprite.getHeight());
        TextureRegion temp = new TextureRegion(arcaneCasterSprite, 0, 0, arcaneCasterSprite.getWidth(), arcaneCasterSprite.getHeight());
        temp.flip(true, false);
        arcaneCasterL = temp;

        arcaneEruptionSprite = new Texture(Gdx.files.internal("arcane/arcaneEruption.png"), true);
        arcaneEruptionSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneEruptionR = new TextureRegion(arcaneEruptionSprite, 0, 0, arcaneEruptionSprite.getWidth(), arcaneEruptionSprite.getHeight());
        temp = new TextureRegion(arcaneEruptionSprite, 0, 0, arcaneEruptionSprite.getWidth(), arcaneEruptionSprite.getHeight());
        temp.flip(true, false);
        arcaneEruptionL = temp;

        arcaneRicochetSprite = new Texture(Gdx.files.internal("arcane/arcaneRicochet.png"), true);
        arcaneRicochetSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneRicochetR = new TextureRegion(arcaneRicochetSprite, 0, 0, arcaneRicochetSprite.getWidth(), arcaneRicochetSprite.getHeight());
        temp = new TextureRegion(arcaneRicochetSprite, 0, 0, arcaneRicochetSprite.getWidth(), arcaneRicochetSprite.getHeight());
        temp.flip(true, false);
        arcaneRicochetL = temp;

        arcaneEscortSprite = new Texture(Gdx.files.internal("arcane/arcaneEscort.png"), true);
        arcaneEscortSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneEscortR = new TextureRegion(arcaneEscortSprite, 0, 0, arcaneEscortSprite.getWidth(), arcaneEscortSprite.getHeight());
        temp = new TextureRegion(arcaneEscortSprite, 0, 0, arcaneEscortSprite.getWidth(), arcaneEscortSprite.getHeight());
        temp.flip(true, false);
        arcaneEscortL = temp;

        arcaneReflectionSprite = new Texture(Gdx.files.internal("arcane/arcaneReflection.png"), true);
        arcaneReflectionSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneReflectionR = new TextureRegion(arcaneReflectionSprite, 0, 0, arcaneReflectionSprite.getWidth(), arcaneReflectionSprite.getHeight());
        temp = new TextureRegion(arcaneReflectionSprite, 0, 0, arcaneReflectionSprite.getWidth(), arcaneReflectionSprite.getHeight());
        temp.flip(true, false);
        arcaneReflectionL = temp;

        arcaneOblivionSprite = new Texture(Gdx.files.internal("arcane/arcaneOblivion.png"), true);
        arcaneOblivionSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        arcaneOblivionR = new TextureRegion(arcaneOblivionSprite, 0, 0, arcaneOblivionSprite.getWidth(), arcaneOblivionSprite.getHeight());
        temp = new TextureRegion(arcaneOblivionSprite, 0, 0, arcaneOblivionSprite.getWidth(), arcaneOblivionSprite.getHeight());
        temp.flip(true, false);
        arcaneOblivionL = temp;

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
