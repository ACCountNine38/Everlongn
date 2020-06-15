package com.everlongn.entities.staticEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.StaticEntity;
import com.everlongn.game.ControlCenter;
import com.everlongn.tiles.Tile;

import static com.everlongn.utils.Constants.PPM;

public class Tree extends StaticEntity {
    public Tile earth;

    public Tree(ControlCenter c, float x, float y, int width, int height, float density, Tile earth) {
        super(c, x, y, width, height, density);
        this.earth = earth;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(Tiles.blackTile, body.getPosition().x * PPM, body.getPosition().y * PPM, width, height);
        batch.end();
    }
}
