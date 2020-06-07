package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;

public class Melee extends Weapon {
    public static Melee barkBane = new Melee(Items.darkBaneR, "Dark Bane", 200, false, false, 100, 100, 60, 60,
            1, "Its power consumes you...", 26, 26, new TextureRegion[]{Items.darkBaneL, Items.darkBaneR},  new String[]{"shadow"}, 120, 8, 16,0.1f, 550, false);

    public static Melee broadSword = new Melee(Items.broadSwordR, "Broad Sword", 201, false, false, 125, 125, 74, 74,
            1, "Slow but powerful...", 22, 24, new TextureRegion[]{Items.broadSwordL, Items.broadSwordR}, new String[]{"steel"}, 250, 4.5f, 20, 0.2f, 800, true);

    public static Melee dragondance = new Melee(Items.dragonDanceR, "Dragondance", 202, false, false, 150, 150, 80, 80,
            1, "Breath of the dragon...", 30, 32, new TextureRegion[]{Items.dragonDanceL, Items.dragonDanceR}, new String[]{"dragon"}, 300, 10, 30, 0.2f, 600, false);

    public Melee(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                 int damage, float drawSpeed, float swingSpeed, float critChance, int force, boolean heavy) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental);
        this.drawSpeed = drawSpeed;
        this.swingSpeed = swingSpeed;
        this.damage = damage;
        this.critChance = critChance;
        this.force = force;
        this.heavy = heavy;
    }

    public Melee createNew(int count) {
        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, heavy);
        i.pickedUp = true;
        i.count = count;
        return i;
    }
}
