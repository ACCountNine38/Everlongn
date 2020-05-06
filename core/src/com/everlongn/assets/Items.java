package com.everlongn.assets;

import com.badlogic.gdx.graphics.Texture;

public class Items {
    public static Texture log, stone;

    public static void init() {
        log = new Texture("core//res//images//items//miscellaneous//logItem.png");
        stone = new Texture("core//res//images//items//miscellaneous//stoneItem.png");
    }
}
