package com.everlongn.assets;

import com.badlogic.gdx.graphics.Texture;

public class Tiles {
    public static Texture airTile, temp, blackTile, decayLeft, decayRight;
    public static Texture earthTile;

    public static void init() {
        temp = new Texture("core//res//images//tiles//tempPlayer.png");
        blackTile = new Texture("core//res//images//tiles//blackTile.png");
        airTile = new Texture("core//res//images//tiles//airTile.png");
        earthTile = new Texture("core//res//images//tiles//earthTile.png");
        decayLeft = new Texture("core//res//images//tiles//decayLeft.png");
        decayRight = new Texture("core//res//images//tiles//decayRight.png");
    }
}
