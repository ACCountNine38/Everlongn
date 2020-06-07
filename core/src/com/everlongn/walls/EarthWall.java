package com.everlongn.walls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Tiles;
import com.everlongn.tiles.Tile;

public class EarthWall extends Wall {
    public EarthWall(int x, int y) {
        super(Tiles.earthWall, x, y, 1);
    }

    @Override
    public void render(SpriteBatch batch) {
        checkAdjacent();
        batch.begin();
        batch.draw(texture, x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, TILESIZE, TILESIZE);
        batch.end();
    }
}
