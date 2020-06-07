package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon extends Item {
    public Weapon(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        this.elemental = elemental;
    }
}
