package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.states.GameState;
import com.everlongn.utils.Tool;

public abstract class Tile {
    public static int TILESIZE = 50;

    public int x, y;
    public TextureRegion texture;
    public int id;
    public boolean solid, soft;
    public double health;

    public Body body;

    // adjacency test variables
    public int numAdjacent = 0, currentType = 0;
    public boolean left, right, up, down = false;
    public TextureRegion currentTexture;

    public Tile(TextureRegion texture, int x, int y, int id, boolean solid, boolean soft) {
        this.texture = texture;
        this.id = id;
        this.x = x;
        this.y = y;
        this.solid = solid;
        this.soft = soft;

        health = 100;
    }

    public static Tile createNew(int x, int y, int id) {
        if(id == 1)
            return new EarthTile(x, y);

        return null;
    }

    public void tick() {

    }

    public void damage(float damage) {
        health -= damage;
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);
        batch.end();
    }

    public void checkAdjacent() {
        numAdjacent = 0;
        left = false;
        right = false;
        up = false;
        down = false;

        if(x-1 >= 0 && GameState.tiles[x-1][y] != null) {
            left = true;
            numAdjacent++;
        }
        if(x+1 < GameState.worldWidth && GameState.tiles[x+1][y] != null) {
            right = true;
            numAdjacent++;
        }
        if(y-1 >= 0 && GameState.tiles[x][y-1] != null) {
            down = true;
            numAdjacent++;
        }
        if(y+1 < GameState.worldHeight && GameState.tiles[x][y+1] != null) {
            up = true;
            numAdjacent++;
        }
    }

    public Body getBody() {
        if(body == null)
            return null;
        else
            return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Rectangle getBound() {
        return new Rectangle(x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);
    }
}
