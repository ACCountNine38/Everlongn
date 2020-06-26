package com.everlongn.entities.staticEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Herbs;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.StaticEntity;
import com.everlongn.game.ControlCenter;
import com.everlongn.tiles.Tile;

import static com.everlongn.utils.Constants.PPM;

public class Tree extends StaticEntity {

    public Tree(float x, float y, int height) {
        super(x, y, 50, height, 5);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(Herbs.tree1, x * PPM - Tile.TILESIZE*2 - Tile.TILESIZE/2, y * PPM - Tile.TILESIZE*2, Tile.TILESIZE*5, height);
        batch.end();
    }

    @Override
    public void finish() {

    }
}
