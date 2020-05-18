package com.everlongn.items;

import com.badlogic.gdx.graphics.Texture;

public class Weapon extends Item {
    public float damage, critChance, attackSpeed;

    public Weapon(Texture texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description,
                 float damage, float critChance, float attackSpeed) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description);
    }
}
