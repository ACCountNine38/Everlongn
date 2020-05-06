package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.utils.Tool;

public abstract class Tile {
    public static int TILESIZE = 50;

    protected Texture texture;
    protected int id;

    protected Body body;

    protected boolean solid;

    protected int x, y;

    public Tile(Texture texture, int x, int y, int id, boolean solid) {
        this.texture = texture;
        this.id = id;
        this.solid = solid;
        //tiles[id] = this;
        this.x = x;
        this.y = y;

        body = Tool.createBox(x*Tile.TILESIZE, y*Tile.TILESIZE, TILESIZE, TILESIZE, solid, 1f);
    }

    public static Tile createNew(int x, int y, int id) {
        if(id == 1)
            return new EarthTile(x, y);

        return null;
    }

    public void tick() {

    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);
        batch.end();
    }

    public boolean isSolid() {
        return solid;
    }

    public int getId() {
        return id;
    }

    public Texture getTexture() {
        return texture;
    }

    public Body getBody() { return body; }
}
