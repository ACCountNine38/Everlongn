package com.everlongn.walls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Tiles;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class EarthWall extends Wall {

    public EarthWall(int x, int y) {
        super(Tiles.earthWall, x, y, 1);
    }

    @Override
    public void tick() {
        checkAdjacent();
        if(numAdjacent == 4) {
            currentTexture = Tiles.earthWall;
        } else if(numAdjacent == 3) {
            if(!left) {
                currentTexture = Tiles.earthWallExpose1[0];
            } else if(!right) {
                currentTexture = Tiles.earthWallExpose1[1];
            } else {
                currentTexture = Tiles.earthWall;
            }
        } else if(numAdjacent == 2) {
            if (down && right) {
                currentTexture = Tiles.earthWallExpose2[0];
            } else if (down && left) {
                currentTexture = Tiles.earthWallExpose2[1];
            } else if (up && right) {
                currentTexture = Tiles.earthWallExpose2[2];
            } else if (up && left) {
                currentTexture = Tiles.earthWallExpose2[3];
            } else if (up && down) {
                currentTexture = Tiles.earthWallExpose2[4];
            } else {
                currentTexture = Tiles.earthWall;
            }

        } else if(numAdjacent == 1) {
            if(right) {
                currentTexture = Tiles.earthWallExpose3[0];
            } else if(left) {
                currentTexture = Tiles.earthWallExpose3[1];
            } else if(down) {
                currentTexture = Tiles.earthWallExpose3[2];
            } else if(up) {
                currentTexture = Tiles.earthWallExpose3[3];
            }
        } else if(numAdjacent == 0){
            currentTexture = Tiles.earthWallExpose4;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        checkAdjacent();
        batch.begin();
        if(currentTexture != null)
            batch.draw(currentTexture, x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, TILESIZE, TILESIZE);
        batch.end();
    }
}
