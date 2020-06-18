package com.everlongn.walls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.states.GameState;
import com.everlongn.tiles.EarthTile;
import com.everlongn.tiles.Tile;

public class Wall {
    public static int TILESIZE = 50;

    public int x, y, id;
    public TextureRegion texture;
    public boolean solid;

    public Rectangle bounds;

    // adjacency test variables
    public int numAdjacent = 0, currentType = 0;
    public boolean left, right, up, down = false;
    public TextureRegion currentTexture;

    public Wall(TextureRegion texture, int x, int y, int id) {
        this.texture = texture;
        this.id = id;
        this.solid = solid;
        this.x = x;
        this.y = y;
    }

    public static Wall createNew(int x, int y, int id) {
        if (id == 1)
            return new EarthWall(x, y);

        return null;
    }

    public void tick() {

    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x * Tile.TILESIZE - Tile.TILESIZE / 2, y * Tile.TILESIZE - Tile.TILESIZE / 2, Tile.TILESIZE, Tile.TILESIZE);
        batch.end();
    }

    public void checkAdjacent() {
        numAdjacent = 0;
        left = false;
        right = false;
        up = false;
        down = false;

        if(x-1 >= 0 && GameState.walls[x-1][y] != null) {
            left = true;
            numAdjacent++;
        }
        if(x+1 < GameState.worldWidth && GameState.walls[x+1][y] != null) {
            right = true;
            numAdjacent++;
        }
        if(y-1 >= 0 && GameState.walls[x][y-1] != null) {
            down = true;
            numAdjacent++;
        }
        if(y+1 < GameState.worldHeight && GameState.walls[x][y+1] != null) {
            up = true;
            numAdjacent++;
        }
    }
}
