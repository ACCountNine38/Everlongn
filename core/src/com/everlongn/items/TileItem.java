package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class TileItem extends Item {
    public static Item earth;

    public static void init() {
        earth = new TileItem(Items.earthItem, "Earth", 500, true, true,
                42, 42, 46, 46, 16,"doesn't look very healthy...", 0, 0, null);
    }

    public TileItem(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
    }

    public TileItem createNew(float x, float y, int amount, float forceX, float forceY) {
        TileItem i = new TileItem(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        i.setPosition(x, y);
        i.count = amount;
        i.body = Tool.createBox((int)x, (int)y, width, height, false, 1.75f, Constants.BIT_PROJECTILE, Constants.BIT_TILE, (short)0, i);
        if(forceX > 0) {
            i.direction = 1;
        } else {
            i.direction = 0;
        }
        i.body.applyForceToCenter(forceX, forceY, false);
        return i;
    }

    public Item createNew(int count) {
        TileItem i = new TileItem(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        i.pickedUp = true;
        i.count = count;
        return i;
    }
}
