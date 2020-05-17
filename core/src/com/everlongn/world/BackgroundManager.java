package com.everlongn.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Backgrounds;
import com.everlongn.game.ControlCenter;
import com.everlongn.tiles.Tile;

public class BackgroundManager {

    public static Vector2[] layers = new Vector2[3];

    public void render(SpriteBatch batch) {
        batch.begin();

        for(int l = layers.length-1; l >=0; l--) {
            for(int x = 0; x < 51; x++) {
                for(int y = 14; y < 31; y++) {
                    int additionalX = 0;
                    while(layers[l].x + x * Tile.TILESIZE + additionalX < 1280 / 2 - 25 * Tile.TILESIZE) {
                        additionalX += 2550;
                    }
                    while(layers[l].x + x * Tile.TILESIZE + additionalX > 1280 / 2 + 26 * Tile.TILESIZE) {
                        additionalX -= 2550;
                    }
                    if(layers[l].x + x * Tile.TILESIZE + additionalX > -Tile.TILESIZE*2
                        && layers[l].x + x * Tile.TILESIZE + additionalX < ControlCenter.width + Tile.TILESIZE*2) {
                        batch.draw(Backgrounds.dream[l][x][y], layers[l].x + x * Tile.TILESIZE + additionalX,
                                layers[l].y + y * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE);
                    }
                }
            }
        }

        batch.end();
    }
}