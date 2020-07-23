package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ThrowWeapons {
    public static Texture shurikenSprite, daggerSprite, throwKnifeSprite, tristarSprite, bombSprite;

    public static TextureRegion shuriken, daggerL, daggerR, throwKnifeL, throwKnifeR, tristar, bombL, bombR;

    public static void init() {
        tristarSprite = new Texture(Gdx.files.internal("throwing/ninjaStar.png"),true);
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

        bombSprite = new Texture(Gdx.files.internal("throwing/bomb.png"),true);
        bombSprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        bombR = new TextureRegion(bombSprite, 0, 0, bombSprite.getWidth(), bombSprite.getHeight());
        bombL = new TextureRegion(bombSprite, 0, 0, bombSprite.getWidth(), bombSprite.getHeight());
        bombL.flip(true, false);
    }
}
