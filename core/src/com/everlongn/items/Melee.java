package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;

public class Melee extends Weapon {
    public static Melee barkBane = new Melee(Items.darkBaneR, "Dark Bane", 200, false, false, 100, 100,
            1, "Its power consumes you...", 26, 26, new TextureRegion[]{Items.darkBaneL, Items.darkBaneR}, false, 8, 16, new String[]{"shadow"}, 120, 0.1f, 550);

    public static Melee broadSword = new Melee(Items.broadSwordR, "Broad Sword", 201, false, false, 125, 125,
            1, "Slow but powerful...", 22, 24, new TextureRegion[]{Items.broadSwordL, Items.broadSwordR}, true, 5, 20, new String[]{"steel"}, 250, 0.2f, 800);

    public Melee(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description, float holdX, float holdY, TextureRegion[] display, boolean heavy, float drawSpeed, float swingSpeed, String[] elemental,
                 int damage, float critChance, int force) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display, drawSpeed, swingSpeed, elemental, heavy);
        this.damage = damage;
        this.critChance = critChance;
        this.force = force;
    }

    public Melee createNew(int count) {
        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display, heavy, drawSpeed, swingSpeed, elemental, damage, critChance, force);
        i.pickedUp = true;
        i.count = count;
        return i;
    }
}
