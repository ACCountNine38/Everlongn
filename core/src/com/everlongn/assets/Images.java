package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.everlongn.game.ControlCenter;

public class Images {

    //Tiles
    public static Texture airTile;
    public static Texture temp;
    public static Texture dirtTile;

    public static void init() {
        temp = new Texture("core//res//images//tiles//temp.png");
        airTile = new Texture("core//res//images//tiles//airTile.png");
        dirtTile = new Texture("core/res/images/tiles/dirtTile.png");
    }
}
