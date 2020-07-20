package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MeleeWeapons {
    // sword and blades
    public static Texture shortBladeSprite, metalSwordSprite, darkBaneSprite, broadSwordSprite, dragonDanceSprite;
    public static TextureRegion darkBaneL, darkBaneR, broadSwordL, broadSwordR, dragonDanceL, dragonDanceR,
            shortBladeL, shortBladeR, metalSwordL, metalSwordR;

    // axes
    public static Texture longAxeSprite, jawBreakerSprite, nightmareAxeSprite, shortAxeSprite, stoneAxeSprite;
    public static TextureRegion longAxeL, longAxeR, jawBreakerL, jawBreakerR, nightmareAxeL, nightmareAxeR,
            shortAxeL, shortAxeR, stoneAxeL, stoneAxeR;

    // pickaxes
    public static Texture stonePickaxeSprite;
    public static TextureRegion stonePickaxeL, stonePickaxeR;

    public static void init() {
        jawBreakerSprite = new Texture(Gdx.files.internal("tools/jawBreaker.png"), true);
        jawBreakerSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        jawBreakerR = new TextureRegion(jawBreakerSprite, 0, 0, jawBreakerSprite.getWidth(), jawBreakerSprite.getHeight());
        jawBreakerL = new TextureRegion(jawBreakerSprite, 0, 0, jawBreakerSprite.getWidth(), jawBreakerSprite.getHeight());
        jawBreakerL.flip(true, false);

        nightmareAxeSprite = new Texture(Gdx.files.internal("tools/nightmane.png"), true);
        nightmareAxeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        nightmareAxeR = new TextureRegion(nightmareAxeSprite, 0, 0, 500, 500);
        nightmareAxeL = new TextureRegion(nightmareAxeSprite, 0, 0, 500, 500);
        nightmareAxeL.flip(true, false);

        shortAxeSprite = new Texture(Gdx.files.internal("tools/shortAxe.png"), true);
        shortAxeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        shortAxeR = new TextureRegion(shortAxeSprite, 0, 0, 500, 500);
        shortAxeL = new TextureRegion(shortAxeSprite, 0, 0, 500, 500);
        shortAxeL.flip(true, false);

        stoneAxeSprite = new Texture(Gdx.files.internal("tools/stoneAxe.png"), true);
        stoneAxeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stoneAxeR = new TextureRegion(stoneAxeSprite, 0, 0, 500, 500);
        stoneAxeL = new TextureRegion(stoneAxeSprite, 0, 0, 500, 500);
        stoneAxeL.flip(true, false);

        stonePickaxeSprite = new Texture(Gdx.files.internal("tools/stonePickaxe.png"), true);
        stonePickaxeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stonePickaxeR = new TextureRegion(stonePickaxeSprite, 0, 0, 500, 500);
        stonePickaxeL = new TextureRegion(stonePickaxeSprite, 0, 0, 500, 500);
        stonePickaxeL.flip(true, false);

        darkBaneSprite = new Texture(Gdx.files.internal("melee/cursedSword.png"), true);
        darkBaneSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        darkBaneR = new TextureRegion(darkBaneSprite, 0, 0, darkBaneSprite.getWidth(), darkBaneSprite.getHeight());
        darkBaneL = new TextureRegion(darkBaneSprite, 0, 0, darkBaneSprite.getWidth(), darkBaneSprite.getHeight());
        darkBaneL.flip(true, false);

        broadSwordSprite = new Texture(Gdx.files.internal("melee/broadSword.png"), true);
        broadSwordSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        broadSwordR = new TextureRegion(broadSwordSprite, 0, 0, broadSwordSprite.getWidth(), broadSwordSprite.getHeight());
        broadSwordL = new TextureRegion(broadSwordSprite, 0, 0, broadSwordSprite.getWidth(), broadSwordSprite.getHeight());
        broadSwordL.flip(true, false);

        dragonDanceSprite = new Texture(Gdx.files.internal("melee/dragonDance.png"), true);
        dragonDanceSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        dragonDanceR = new TextureRegion(dragonDanceSprite, 0, 0, dragonDanceSprite.getWidth(), dragonDanceSprite.getHeight());
        dragonDanceL = new TextureRegion(dragonDanceSprite, 0, 0, dragonDanceSprite.getWidth(), dragonDanceSprite.getHeight());
        dragonDanceL.flip(true, false);

        shortBladeSprite = new Texture(Gdx.files.internal("melee/shortBlade.png"), true);
        shortBladeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        shortBladeR = new TextureRegion(shortBladeSprite, 0, 0, shortBladeSprite.getWidth(), shortBladeSprite.getHeight());
        shortBladeL = new TextureRegion(shortBladeSprite, 0, 0, shortBladeSprite.getWidth(), shortBladeSprite.getHeight());
        shortBladeL.flip(true, false);

        metalSwordSprite = new Texture(Gdx.files.internal("melee/metalSword.png"), true);
        metalSwordSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        metalSwordR = new TextureRegion(metalSwordSprite, 0, 0, metalSwordSprite.getWidth(), metalSwordSprite.getHeight());
        metalSwordL = new TextureRegion(metalSwordSprite, 0, 0, metalSwordSprite.getWidth(), metalSwordSprite.getHeight());
        metalSwordL.flip(true, false);

        longAxeSprite = new Texture(Gdx.files.internal("tools/longAxe.png"),true);
        longAxeSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        longAxeR = new TextureRegion(longAxeSprite, 0, 0, longAxeSprite.getWidth(), longAxeSprite.getHeight());
        longAxeL = new TextureRegion(longAxeSprite, 0, 0, longAxeSprite.getWidth(), longAxeSprite.getHeight());
        longAxeL.flip(true, false);
    }
}
