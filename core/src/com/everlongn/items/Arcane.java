package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class Arcane extends Weapon {
    public static Arcane shadowStaff = new Arcane(Items.shadowStaffR, "Shadow Manipulator", 200, false, false, 110, 110, 76, 76, 1, "Your shadow seem a bit abnormal...",
            53, 58, new TextureRegion[]{Items.shadowStaffL, Items.shadowStaffR}, new String[]{"Shadow", "Arcane"}, 10, 0.5f, 0);

    public static Arcane arcaneCaster = new Arcane(Items.arcaneCasterR, "Caster", 201, false, true, 100, 100, 58, 58, 1, "Power beyond your understanding...",
            52, 58, new TextureRegion[]{Items.arcaneCasterL, Items.arcaneCasterR}, new String[]{"Arcane"}, 10, 0.5f, 0);

    public static Arcane arcaneEruption = new Arcane(Items.arcaneEruptionR, "Eruption", 202, false, true, 108, 108, 76, 76, 1, "Power beyond your understanding...",
            52, 58, new TextureRegion[]{Items.arcaneEruptionL, Items.arcaneEruptionR}, new String[]{"Arcane"}, 10, 0.8f, 0);

    public static Arcane arcaneRebound = new Arcane(Items.arcaneRicochetR, "Rebound", 202, false, true, 108, 108, 68, 68, 1, "Power beyond your understanding...",
            52, 58, new TextureRegion[]{Items.arcaneRicochetL, Items.arcaneRicochetR}, new String[]{"Arcane"}, 10, 0.6f, 0);

    public static Arcane arcaneEscort = new Arcane(Items.arcaneEscortR, "Escort", 202, false, true, 108, 108, 58, 58 , 1, "Power beyond your understanding...",
            52, 58, new TextureRegion[]{Items.arcaneEscortL, Items.arcaneEscortR}, new String[]{"Arcane"}, 10, 0.6f, 0);

    public Arcane(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                  int healthConsumption, float refreshSpeed, int burst) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display, elemental);

        this.healthConsumption = healthConsumption;
        this.refreshSpeed = refreshSpeed;
        this.burst = burst;
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
