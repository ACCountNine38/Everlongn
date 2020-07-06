package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Throwing extends Weapon {
    public static Throwing stone, triStar, shuriken, dagger, throwKnife;

    public static void init() {
        stone = new Throwing(Items.stone, "Rock", 300, true, true,
                26, 26, 44, 44, 32, "looks very durable", 1, 10, new TextureRegion[]{Items.stone, Items.stone}, new String[]{"Throwing", "Earth"}, 5f, 15f, 20);
        triStar = new Throwing(Items.tristar, "Shuriken", 301, true, true,
                40, 40, 50, 50, 128, "sharp", 12, 18, new TextureRegion[]{Items.tristar, Items.tristar}, new String[]{"Throwing", "Wind"}, 10f, 20f, 28);
        shuriken = new Throwing(Items.shuriken, "Shredder", 302, true, false,
                64, 64, 60, 60, 2, "sharp", 19, 26, new TextureRegion[]{Items.shuriken, Items.shuriken}, new String[]{"Throwing", "Shadow"}, 10f, 20f, 80);
        dagger = new Throwing(Items.shuriken, "Dagger", 303, true, false,
                50, 50, 60, 60, 64, "sharp", 5, 5, new TextureRegion[]{Items.shuriken, Items.shuriken}, new String[]{"Throwing", "Shadow"}, 10f, 20f, 80);
        throwKnife = new Throwing(Items.shuriken, "Throw Knife", 304, true, false,
                56, 56, 60, 60, 16, "sharp", 5, 5, new TextureRegion[]{Items.shuriken, Items.shuriken}, new String[]{"Throwing", "Shadow"}, 10f, 20f, 80);
    }

    public Throwing(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                    float drawSpeed, float throwSpeed, float throwingDamage) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental);

        this.drawSpeed = drawSpeed;
        this.throwSpeed = throwSpeed;
        this.throwingDamage = throwingDamage;
        Item.items[id] = this;
    }

    public Throwing createNew(float x, float y, int amount, float forceX, float forceY) {
        Throwing i = new Throwing(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, drawSpeed, throwSpeed, throwingDamage);
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

    public Throwing createNew(int count) {
        Throwing i = new Throwing(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, drawSpeed, throwSpeed, throwingDamage);
        i.pickedUp = true;
        i.count = count;
        return i;
    }
}
