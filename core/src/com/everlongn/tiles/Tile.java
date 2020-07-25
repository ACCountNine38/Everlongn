package com.everlongn.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.EntityManager;
import com.everlongn.items.Item;
import com.everlongn.items.TileItem;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public abstract class Tile {
    public static int TILESIZE = 50;

    public int x, y;
    public TextureRegion texture;
    public int id;
    public boolean solid, soft, digged, dropped, exploded;
    public float health, alpha = 1f;

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

        if(health <= 0) {
            ParticleEffect explosion2 = new ParticleEffect();
            //if(!exploded) {
                explosion2.load(Gdx.files.internal("particles/digParticle"), Gdx.files.internal(""));
                explosion2.getEmitters().first().scaleSize(2);
                explosion2.getEmitters().first().setPosition(x * Tile.TILESIZE, y * Tile.TILESIZE - TILESIZE / 2);
                explosion2.start();
                EntityManager.particles.add(explosion2);
            //}
            digged = true;
        } else {
            if(!exploded) {
                ParticleEffect explosion = new ParticleEffect();
                explosion.load(Gdx.files.internal("particles/digParticle"), Gdx.files.internal(""));
                explosion.getEmitters().first().setPosition(x * Tile.TILESIZE, y * Tile.TILESIZE - TILESIZE / 2);
                explosion.start();
                EntityManager.particles.add(explosion);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.begin();

        if(digged) {
            alpha -= 0.05;
            if(alpha <= 0)
                alpha = 0;
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        }
        batch.draw(texture, x*Tile.TILESIZE - TILESIZE/2, y*Tile.TILESIZE - TILESIZE/2, TILESIZE, TILESIZE);
        if(digged) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
        }
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
