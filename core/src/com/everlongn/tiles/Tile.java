package com.everlongn.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.states.GameState;
import com.everlongn.utils.Tool;

public abstract class Tile {
    public static int TILESIZE = 50;

    public Texture texture;
    public int id;

    public Body body;

    public boolean solid;

    public int x, y;

    public Tile(Texture texture, int x, int y, int id, boolean solid) {
        this.texture = texture;
        this.id = id;
        this.solid = solid;
        this.x = x;
        this.y = y;

        //body = Tool.createBox(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE, solid, 1f);
    }

    public static Tile createNew(int x, int y, int id) {
        if(id == 1)
            return new EarthTile(x, y);

        return null;
    }

    public void tick() {
//        if(x > GameState.xStart && x < GameState.xEnd && y > GameState.yStart && y < GameState.xEnd && body == null) {
//            body = Tool.createBox(x*TILESIZE, y*TILESIZE, TILESIZE, TILESIZE, solid, 1f);
//        } else {
//            if(body != null) {
//                GameState.world.destroyBody(body);
//            }
//        }
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

    public Body getBody() {
        if(body == null)
            return null;
        else
            return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
