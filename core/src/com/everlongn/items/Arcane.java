package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Arcane extends Weapon {
    public static Item shadowStaff, arcaneCaster, arcaneEruption, arcaneRebound, arcaneEscort, arcaneReflection, arcaneDevastation;

    public static void init() {
         shadowStaff = new Arcane(Items.shadowStaffR, "Shadow Manipulator", 200, false, false, 116, 116, 80, 80, 1, "Your shadow seem a bit abnormal...",
                53, 58, new TextureRegion[]{Items.shadowStaffL, Items.shadowStaffR}, new String[]{"Arcane", "Shadow"}, 3, 0.5f, 0);
         arcaneCaster = new Arcane(Items.arcaneCasterR, "Caster", 201, false, true, 100, 100, 58, 58, 1, "Power beyond your understanding...",
                52, 58, new TextureRegion[]{Items.arcaneCasterL, Items.arcaneCasterR}, new String[]{"Arcane", "Light"}, 1, 0.6f, 30);

         arcaneEruption = new Arcane(Items.arcaneEruptionR, "Eruption", 202, false, true, 108, 108, 76, 76, 1, "Power beyond your understanding...",
                57, 58, new TextureRegion[]{Items.arcaneEruptionL, Items.arcaneEruptionR}, new String[]{"Arcane", "Fire"}, 2f, 0.8f, 50);

         arcaneRebound = new Arcane(Items.arcaneRicochetR, "Rebound", 203, false, true, 108, 108, 68, 68, 1, "Power beyond your understanding...",
                52, 58, new TextureRegion[]{Items.arcaneRicochetL, Items.arcaneRicochetR}, new String[]{"Arcane", "Earth"}, 2, 0.6f, 40);

         arcaneEscort = new Arcane(Items.arcaneEscortR, "Escort", 204, false, true, 108, 108, 58, 58 , 1, "Power beyond your understanding...",
                54, 58, new TextureRegion[]{Items.arcaneEscortL, Items.arcaneEscortR}, new String[]{"Arcane", "Air"}, 1.25f, 0.6f, 20);

         arcaneReflection = new Arcane(Items.arcaneReflectionR, "Reflection", 205, false, true, 108, 108, 66, 66, 1, "Power beyond your understanding...",
                54, 58, new TextureRegion[]{Items.arcaneReflectionL, Items.arcaneReflectionR}, new String[]{"Arcane", "River"}, 0.75f, 0.3f, 25f);

         arcaneDevastation = new Arcane(Items.arcaneOblivionR, "Devastation", 206, false, true, 132, 132, 84, 84, 1, "Power beyond your understanding...",
                52, 58, new TextureRegion[]{Items.arcaneOblivionL, Items.arcaneOblivionR}, new String[]{"Arcane", "Doom"}, 0.5f, 0.1f, 3);
    }

    public Arcane(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                  float healthConsumption, float refreshSpeed, float burst) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental);

        this.healthConsumption = healthConsumption;
        this.refreshSpeed = refreshSpeed;
        this.burst = burst;

        Item.items[id] = this;
    }

    public Arcane createNew(int count) {
        Arcane i = new Arcane(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, healthConsumption, refreshSpeed, burst);
        i.pickedUp = true;
        i.count = count;
        return i;
    }

    public Arcane createNew(float x, float y, int amount, float forceX, float forceY) {
        Arcane i = new Arcane(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental, healthConsumption, refreshSpeed, burst);
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
