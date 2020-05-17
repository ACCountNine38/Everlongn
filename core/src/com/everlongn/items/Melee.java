package com.everlongn.items;

import com.badlogic.gdx.graphics.Texture;
import com.everlongn.assets.Items;

public class Melee extends Item {
    public Melee woodenStaff = new Melee(Items.log, "Wooden Staff", 100, false, true, 30, 30, 1, "Basic staff for basic use.",
            10, 2, 0.5f);

    public float damage, critChance, attackSpeed;

    public Melee(Texture texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description,
                 float damage, float critChance, float attackSpeed) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description);
    }
}
