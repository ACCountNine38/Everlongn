package com.everlongn.tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Herbs;
import com.everlongn.assets.Tiles;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class EarthTile extends Tile {
    private Sprite grass = new Sprite(Herbs.grass1);
    private boolean rotate = false;

    private boolean leftFilled, rightFilled;

    private boolean decayed;

    public EarthTile(int x, int y) {
        super(Tiles.earthTile, x, y, 1, true);
        if((int)(Math.random()*2) == 0) {
            grass.flip(true, false);
        }
    }

    @Override()
    public void tick() {
        super.tick();

        if(x-1 >= 0 && GameState.tiles[x-1][y] != null) {
            leftFilled = true;
        } else {
            leftFilled = false;
            if(y+1 < GameState.worldHeight && GameState.tiles[x][y+1] == null) {
                decayed = true;
                texture = Tiles.decayLeft;
                if (body != null) {
                    GameState.world.destroyBody(body);
                    body = Tool.createDecayTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 0,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                }
            }
        }

        if(x+1 < GameState.worldWidth && GameState.tiles[x+1][y] != null) {
            rightFilled = true;
        } else {
            if(x+1 == GameState.worldWidth){
                rightFilled = true;
                return;
            }
            rightFilled = false;

            if(y+1 < GameState.worldHeight && GameState.tiles[x][y+1] == null) {
                decayed = true;
                texture = Tiles.decayRight;
                if (body != null) {
                    GameState.world.destroyBody(body);
                    body = Tool.createDecayTile(x * Tile.TILESIZE - TILESIZE / 2, y * Tile.TILESIZE - TILESIZE / 2, 1,
                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);

        if(leftFilled && !decayed) {
            batch.draw(Tiles.blackTile, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE/5, TILESIZE);
        }

        if(rightFilled && !decayed) {
            batch.draw(Tiles.blackTile, x*Tile.TILESIZE - TILESIZE/2 + TILESIZE/5*4, y*Tile.TILESIZE - TILESIZE/2, TILESIZE/5, TILESIZE);
        }

        if(y+1 < GameState.worldHeight && GameState.tiles[x][y+1] == null && !decayed) {
            if(rotate)
                batch.draw(grass, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2 + Tile.TILESIZE, TILESIZE, TILESIZE);
            else
                batch.draw(grass, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2 + Tile.TILESIZE, TILESIZE, TILESIZE);
        }
        batch.end();
    }
}
