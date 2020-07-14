package com.everlongn.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Axe extends Item {
    public Axe(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display,
               float axePower, float drawSpeed, float swingSpeed, Sound swingSound) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        this.axePower = axePower;
        this.drawSpeed = drawSpeed;
        this.swingSpeed = swingSpeed;

        items[id] = this;
    }

//    public Axe createNew(int count) {
//        Axe i = new Axe(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, swingSound);
//        i.pickedUp = true;
//        i.count = count;
//        return i;
//    }
//
//    public Melee createNew(float x, float y, int amount, float forceX, float forceY) {
//        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, heavy, swingSound);
//        i.setPosition(x, y);
//        i.count = amount;
//        i.body = Tool.createBox((int)x, (int)y, width, height, false, 0.25f, Constants.BIT_PROJECTILE, Constants.BIT_TILE, (short)0, i);
//        if(forceX > 0) {
//            i.direction = 1;
//        } else {
//            i.direction = 0;
//        }
//        i.body.applyForceToCenter(forceX, forceY, false);
//        return i;
//    }
}
