package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.utils.Tool;

public abstract class Tile {
    public static int TILESIZE = 50;

    //public static Tile tiles[] = new Tile[256];
    protected Texture texture;
    protected int id;

    //Tile initialization
    //public static Tile airTile = new AirTile(0);
    //public static Tile dirtTile = new DirtTile(1);

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

        body = Tool.createBox(x*Tile.TILESIZE, y*Tile.TILESIZE, TILESIZE, TILESIZE, solid);
    }

    public static Tile createNew(int x, int y, int id) {
        if(id == 0)
            return new AirTile(x, y);
        else if(id == 1)
            return new DirtTile(x, y);

        return null;
    }

    public void tick() {

    }

    public void render(SpriteBatch batch) {
        //body.setTransform((int)x, (int)y, 0);

        batch.draw(texture, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);
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
}
