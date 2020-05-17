package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Tiles {
    public static Texture airTile, blackTile, decayLeft, decayRight;
    public static Texture earthTile;

    public static void init() {
        blackTile = new Texture(Gdx.files.internal("tiles/blackTile.png"));
        airTile = new Texture(Gdx.files.internal("tiles/airTile.png"));
        earthTile = new Texture(Gdx.files.internal("tiles/earthTile.png"));
        decayLeft = new Texture(Gdx.files.internal("tiles/decayLeft.png"));
        decayRight = new Texture(Gdx.files.internal("tiles/decayRight.png"));
    }
}
