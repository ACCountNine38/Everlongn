package com.everlongn.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.assets.Sounds;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Melee extends Weapon {
    public static Item barkBane, broadSword, dragondance;

    public static void init() {
        barkBane = new Melee(Items.darkBaneR, "Dark Bane", 101, false, false, 100, 100, 60, 60,
                1, "Its power consumes you...", 26, 26, new TextureRegion[]{Items.darkBaneL, Items.darkBaneR},  new String[]{"shadow"}, 120, 8, 16,0.1f, 550, false, Sounds.bladeSwing1);

        broadSword = new Melee(Items.broadSwordR, "Broad Sword", 102, false, false, 125, 125, 74, 74,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Sweeping Target",
                22, 24, new TextureRegion[]{Items.broadSwordL, Items.broadSwordR}, new String[]{"steel"}, 100, 4.5f, 20, 0.2f, 800, true, Sounds.bladeSwing1);

        dragondance = new Melee(Items.dragonDanceR, "Dragondance", 103, false, false, 150, 150, 80, 80,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 30, 32, new TextureRegion[]{Items.dragonDanceL, Items.dragonDanceR}, new String[]{"dragon"}, 150, 10, 30, 0.2f, 600, false, Sounds.swordSwing1);
    }

    public Melee(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                 int damage, float drawSpeed, float swingSpeed, float critChance, int force, boolean heavy, Sound swingSound) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental);
        this.drawSpeed = drawSpeed;
        this.swingSpeed = swingSpeed;
        this.damage = damage;
        this.critChance = critChance;
        this.force = force;
        this.heavy = heavy;
        this.swingSound = swingSound;

        Item.items[id] = this;
    }

    public Melee createNew(int count) {
        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, heavy, swingSound);
        i.pickedUp = true;
        i.count = count;
        return i;
    }

    public Melee createNew(float x, float y, int amount, float forceX, float forceY) {
        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, heavy, swingSound);
        i.setPosition(x, y);
        i.count = amount;
        i.body = Tool.createBox((int)x, (int)y, width, height, false, 0.25f, Constants.BIT_PROJECTILE, Constants.BIT_TILE, (short)0, i);
        if(forceX > 0) {
            i.direction = 1;
        } else {
            i.direction = 0;
        }
        i.body.applyForceToCenter(forceX, forceY, false);
        return i;
    }
}
