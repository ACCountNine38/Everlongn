package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.everlongn.assets.Images;
import com.everlongn.tiles.Tile;

public class DirtTile extends Tile {
    public DirtTile(int x, int y) {
        super(Images.dirtTile, x, y, 1, true);
    }
}
