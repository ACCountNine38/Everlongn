package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Herbs {
    public static Texture grass0, grass1, grass2;
    public static Texture tree1;

    public static void init() {
        grass0 = new Texture(Gdx.files.internal("herbs/Grass0.png"));
        grass1 = new Texture(Gdx.files.internal("herbs/Grass1.png"));
        grass2 = new Texture(Gdx.files.internal("herbs/Grass2.png"));
        tree1 = new Texture(Gdx.files.internal("herbs/tree1.png"));
    }
}
