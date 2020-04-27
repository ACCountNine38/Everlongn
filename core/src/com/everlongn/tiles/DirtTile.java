package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.everlongn.assets.Images;
import com.everlongn.tiles.Tile;

public class DirtTile extends Tile {
    public DirtTile(int id) {
        super(new Texture("core//res//images//tiles//temp.png"), id, true);
    }
}
