package com.everlongn.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.assets.MeleeWeapons;
import com.everlongn.assets.Sounds;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Melee extends Weapon {
    public static Item barkBane, broadSword, dragondance, shortBlade, metalSword;
    public static Item longAxe, shortAxe, nightmane, jawBreaker, stoneAxe;
    public static Item stonePickaxe;

    public static void init() {
        // swords
        barkBane = new Melee(MeleeWeapons.darkBaneR, "Dark Bane", 101, false, true, 100, 100, 60, 60,
                1, "Its power consumes you...", 26, 26, new TextureRegion[]{MeleeWeapons.darkBaneL, MeleeWeapons.darkBaneR},  new String[]{"shadow"}, 120, 8, 16,0.1f, 550, false, Sounds.bladeSwing1, false, false);

        dragondance = new Melee(MeleeWeapons.dragonDanceR, "Nightbane", 103, false, false, 180, 180, 84, 84,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 40, 38, new TextureRegion[]{MeleeWeapons.dragonDanceL, MeleeWeapons.dragonDanceR}, new String[]{"nightmare"}, 150, 10, 30, 0.2f, 600, false, Sounds.swordSwing1, false, false);

        metalSword = new Melee(MeleeWeapons.metalSwordR, "Thin Sword", 105, false, true, 100, 100, 64, 64,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 16, 23, new TextureRegion[]{MeleeWeapons.metalSwordL, MeleeWeapons.metalSwordR}, new String[]{"steel"}, 30, 7.5f, 20, 0.2f, 400, false, Sounds.swordSwing4, false, false);

        // blades
        broadSword = new Melee(MeleeWeapons.broadSwordR, "Long Blade", 102, false, true, 125, 125, 74, 74,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Sweeping Target", 22, 24, new TextureRegion[]{MeleeWeapons.broadSwordL, MeleeWeapons.broadSwordR}, new String[]{"steel"}, 100, 4.5f, 20, 0.2f, 800, true, Sounds.bladeSwing1, false, false);

        shortBlade = new Melee(MeleeWeapons.shortBladeR, "Short Blade", 104, false, true, 75, 75, 56, 56,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 17, 20, new TextureRegion[]{MeleeWeapons.shortBladeL, MeleeWeapons.shortBladeR}, new String[]{"steel"}, 40, 5, 25, 0.2f, 500, true, Sounds.bladeSwing2, false, false);

        // axes
        longAxe = new Melee(MeleeWeapons.longAxeR, "Long Axe", 106, false, true, 130, 130, 66, 66,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 26, 33, new TextureRegion[]{MeleeWeapons.longAxeL, MeleeWeapons.longAxeR}, new String[]{"steel"}, 60, 7.5f, 20, 0.2f, 400, false, Sounds.swordSwing4,true, false);

        shortAxe = new Melee(MeleeWeapons.shortAxeR, "Short Axe", 107, false, true, 80, 80, 56, 56,
                1, "temp", 16, 23, new TextureRegion[]{MeleeWeapons.shortAxeL, MeleeWeapons.shortAxeR}, new String[]{"steel"}, 35, 10f, 20, 0.2f, 400, false, Sounds.swordSwing4, true, false);

        stoneAxe = new Melee(MeleeWeapons.stoneAxeR, "Stone Axe", 108, false, true, 100, 100, 56, 56,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 24, 31, new TextureRegion[]{MeleeWeapons.stoneAxeL, MeleeWeapons.stoneAxeR}, new String[]{"steel"}, 30, 4.5f, 20, 0.2f, 400, false, Sounds.swordSwing4,true, false);

        nightmane = new Melee(MeleeWeapons.nightmareAxeR, "Nightmane", 109, false, true, 170, 170, 84, 84,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 36, 43, new TextureRegion[]{MeleeWeapons.nightmareAxeL, MeleeWeapons.nightmareAxeR}, new String[]{"steel"}, 240, 5, 30, 0.2f, 1000, true, Sounds.bladeSwing1, true, false);

        jawBreaker = new Melee(MeleeWeapons.jawBreakerR, "Jaw Breaker", 110, false, true, 160, 160, 84, 84,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 38, 45, new TextureRegion[]{MeleeWeapons.jawBreakerL, MeleeWeapons.jawBreakerR}, new String[]{"steel"}, 180, 6f, 30, 0.2f, 500, false, Sounds.bladeSwing1, true, false);

        // pickaxe
        stonePickaxe = new Melee(MeleeWeapons.stonePickaxeR, "Stone Pickaxe", 111, false, true, 100, 100, 56, 56,
                1, "A heavy but powerful blade. Hold left while in midair to halt. Single Target", 24, 31, new TextureRegion[]{MeleeWeapons.stonePickaxeL, MeleeWeapons.stonePickaxeR}, new String[]{"steel"}, 25, 4.5f, 20, 0.2f, 400, false, Sounds.swordSwing4,false, true);

    }

    public Melee(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                 int damage, float drawSpeed, float swingSpeed, float critChance, int force, boolean heavy, Sound swingSound, boolean isAxe, boolean isPick) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental);
        this.drawSpeed = drawSpeed;
        this.swingSpeed = swingSpeed;
        this.damage = damage;
        this.critChance = critChance;
        this.force = force;
        this.heavy = heavy;
        this.swingSound = swingSound;
        this.isAxe = isAxe;
        this.isPick = isPick;

        Item.items[id] = this;
    }

    public Melee createNew(int count) {
        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, heavy, swingSound, isAxe, isPick);
        i.pickedUp = true;
        i.count = count;
        return i;
    }

    public Melee createNew(float x, float y, int amount, float forceX, float forceY) {
        Melee i = new Melee(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, damage, drawSpeed, swingSpeed, critChance, force, heavy, swingSound, isAxe, isPick);
        i.setPosition(x, y);
        i.count = amount;
        i.body = Tool.createBox((int)x, (int)y, width, height, false, true, 0.25f, Constants.BIT_PROJECTILE, Constants.BIT_TILE, (short)0, i);
        if(forceX > 0) {
            i.direction = 1;
        } else {
            i.direction = 0;
        }
        i.body.applyForceToCenter(forceX, forceY, false);
        return i;
    }
}
