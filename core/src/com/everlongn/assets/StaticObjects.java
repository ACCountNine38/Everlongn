package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StaticObjects {
    public static Texture condensedDarkEnergySprite;

    public static TextureRegion condensedDarkEnergy;

    public static void init() {
        condensedDarkEnergySprite = new Texture(Gdx.files.internal("staticEntity/neutrinoBomb.png"),true);
        condensedDarkEnergySprite.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        condensedDarkEnergy = new TextureRegion(condensedDarkEnergySprite, 0, 0, condensedDarkEnergySprite.getWidth(), condensedDarkEnergySprite.getHeight());
    }
}
