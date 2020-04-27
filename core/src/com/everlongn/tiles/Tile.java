package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public abstract class Tile {
    public static int TILESIZE = 50;

    public static Tile tiles[] = new Tile[256];
    protected Texture texture;
    protected int id;

    //Tile initialization
    public static Tile airTile = new AirTile(0);
    public static Tile dirtTile = new DirtTile(1);

    protected Body body;

    protected boolean solid;

    public Tile(Texture texture, int id, boolean solid) {
        this.texture = texture;
        this.id = id;
        this.solid = solid;

        tiles[id] = this;
        body = Tool.createBox(0, 0, TILESIZE, TILESIZE, solid);
    }

    public void tick() {

    }

    public void render(SpriteBatch batch, float x, float y) {
        body.setTransform((int)x, (int)y, 0);

        batch.draw(texture, x*Tile.TILESIZE*Constants.PPM, y*Tile.TILESIZE*Constants.PPM, TILESIZE, TILESIZE);
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
