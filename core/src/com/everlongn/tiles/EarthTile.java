package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Hurbs;
import com.everlongn.assets.Images;
import com.everlongn.assets.Tiles;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;

public class EarthTile extends Tile {
    private Sprite grass = new Sprite(Hurbs.grass1);
    private boolean rotate = false;

    private boolean leftFilled, rightFilled;

    public EarthTile(int x, int y) {
        super(Tiles.earthTile, x, y, 1, true);
        if((int)(Math.random()*2) == 0) {
            grass.flip(true, false);
        }
    }

    @Override()
    public void tick() {
        if(x-1 >= 0 && GameState.tiles[x-1][y] != null) {
            leftFilled = true;
        } else {
            leftFilled = false;
        }

        if(x+1 < GameState.worldWidth && GameState.tiles[x+1][y] != null) {
            rightFilled = true;
        } else {
            if(x+1 == GameState.worldWidth){
                rightFilled = true;
                return;
            }
            rightFilled = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);

        if(leftFilled) {
            batch.draw(Tiles.blackTile, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE/5, TILESIZE);
        }

        if(rightFilled) {
            batch.draw(Tiles.blackTile, x*Tile.TILESIZE - TILESIZE/2 + TILESIZE/5*4, y*Tile.TILESIZE - TILESIZE/2, TILESIZE/5, TILESIZE);
        }

        if(y+1 < GameState.worldHeight && GameState.tiles[x][y+1] == null) {
            if(rotate)
                batch.draw(grass, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2 + Tile.TILESIZE, TILESIZE, TILESIZE);
            else
                batch.draw(grass, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2 + Tile.TILESIZE, TILESIZE, TILESIZE);
        }
        batch.end();
    }
}
