package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class Items {
    public static Texture log, stone;

    public static Texture shadowStaff;

    public static void init() {
        log = new Texture(Gdx.files.internal("items/logItem.png"),true);
        log.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        stone = new Texture(Gdx.files.internal("items/stoneItem.png"), true);
        stone.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        shadowStaff = new Texture(Gdx.files.internal("magic/shadowStaff.png"), true);
        shadowStaff.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }
}
