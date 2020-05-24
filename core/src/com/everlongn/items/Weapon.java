package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon extends Item {
    public Weapon(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description, float holdX, float holdY, TextureRegion[] display,
                    float drawSpeed, float swingSpeed, String[] elemental, boolean heavy) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display);
        this.drawSpeed = drawSpeed;
        this.swingSpeed = swingSpeed;
        this.elemental = elemental;
        this.heavy = heavy;
    }
}
