package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Items {
    public static Texture logSprite, stoneSprite, omniEssenceSprite, omniCrystalSprite,
            demiEssenceSprite, demiCrystalSprite, spiderLimbSprite, mossyFluidSprite, glowStickSprite,
            earthItemSprite, aerogelSprite;

    public static TextureRegion log, stone, omniEssence, omniCrystal, demiEssence, demiCrystal, spiderLimb, mossyFluid,
        glowStickL, glowStickR, earthItem, aerogel;

    public static void init() {
        logSprite = new Texture(Gdx.files.internal("items/logItem.png"),true);
        logSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        log = new TextureRegion(logSprite, 0, 0, logSprite.getWidth(), logSprite.getHeight());

        earthItemSprite = new Texture(Gdx.files.internal("items/earthItem.png"),true);
        earthItemSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        earthItem = new TextureRegion(earthItemSprite, 0, 0, earthItemSprite.getWidth(), earthItemSprite.getHeight());

        spiderLimbSprite = new Texture(Gdx.files.internal("items/spiderLimb.png"),true);
        spiderLimbSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        spiderLimb = new TextureRegion(spiderLimbSprite, 0, 0, spiderLimbSprite.getWidth(), spiderLimbSprite.getHeight());

        mossyFluidSprite = new Texture(Gdx.files.internal("items/mossyFluid.png"),true);
        mossyFluidSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        mossyFluid = new TextureRegion(mossyFluidSprite, 0, 0, mossyFluidSprite.getWidth(), mossyFluidSprite.getHeight());

        glowStickSprite = new Texture(Gdx.files.internal("items/glowStick.png"),true);
        glowStickSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        glowStickR = new TextureRegion(glowStickSprite, 0, 0, glowStickSprite.getWidth(), glowStickSprite.getHeight());
        glowStickL = new TextureRegion(glowStickSprite, 0, 0, glowStickSprite.getWidth(), glowStickSprite.getHeight());
        glowStickL.flip(true, false);

        stoneSprite = new Texture(Gdx.files.internal("items/stoneItem.png"), true);
        stoneSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stone = new TextureRegion(stoneSprite, 0, 0, logSprite.getWidth(), logSprite.getHeight());

        omniEssenceSprite = new Texture(Gdx.files.internal("items/OmniEssence.png"), true);
        omniEssenceSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        omniEssence = new TextureRegion(omniEssenceSprite, 0, 0, omniEssenceSprite.getWidth(), omniEssenceSprite.getHeight());

        omniCrystalSprite = new Texture(Gdx.files.internal("items/OmniCrystal.png"), true);
        omniCrystalSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        omniCrystal = new TextureRegion(omniCrystalSprite, 0, 0, omniCrystalSprite.getWidth(), omniCrystalSprite.getHeight());

        demiEssenceSprite = new Texture(Gdx.files.internal("items/DemiEssence.png"), true);
        demiEssenceSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        demiEssence = new TextureRegion(demiEssenceSprite, 0, 0, demiEssenceSprite.getWidth(), demiEssenceSprite.getHeight());

        demiCrystalSprite = new Texture(Gdx.files.internal("items/DemiCrystal.png"), true);
        demiCrystalSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        demiCrystal = new TextureRegion(demiCrystalSprite, 0, 0, demiCrystalSprite.getWidth(), demiCrystalSprite.getHeight());

        demiCrystalSprite = new Texture(Gdx.files.internal("items/DemiCrystal.png"), true);
        demiCrystalSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        demiCrystal = new TextureRegion(demiCrystalSprite, 0, 0, demiCrystalSprite.getWidth(), demiCrystalSprite.getHeight());

        aerogelSprite = new Texture(Gdx.files.internal("items/aerogelItem.png"), true);
        aerogelSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        aerogel = new TextureRegion(aerogelSprite, 0, 0, aerogelSprite.getWidth(), aerogelSprite.getHeight());

        ArcaneWeapons.init();
        MeleeWeapons.init();
        ThrowWeapons.init();
    }
}
