package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.assets.StaticObjects;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ObjectItem extends Item {
    public static ObjectItem darkEnergy;

    public static void init() {
        darkEnergy = new ObjectItem(StaticObjects.condensedDarkEnergy, "Condensed Dark Energy", 501, false, true,
                80, 80, 56, 56, 1,"doesn't look very healthy...", 0, 0, null, true, false, false);
    }

    public ObjectItem(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display,
                    boolean canHold, boolean wallPlace, boolean stick) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        this.canHold = canHold;
        this.wallPlace = wallPlace;
        this.stick = stick;
    }

    public ObjectItem createNew(float x, float y, int amount, float forceX, float forceY) {
        ObjectItem i = new ObjectItem(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, canHold, wallPlace, stick);
        i.setPosition(x, y);
        i.count = amount;
        i.body = Tool.createBox((int)x, (int)y, width, height, false, true,1.75f, Constants.BIT_PROJECTILE, Constants.BIT_TILE, (short)0, i);
        if(forceX > 0) {
            i.direction = 1;
        } else {
            i.direction = 0;
        }
        i.body.applyForceToCenter(forceX, forceY, false);
        return i;
    }

    public ObjectItem createNew(int count) {
        ObjectItem i = new ObjectItem(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, canHold, wallPlace, stick);
        i.pickedUp = true;
        i.count = count;
        return i;
    }
}
