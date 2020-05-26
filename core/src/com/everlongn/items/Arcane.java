package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;

public class Arcane extends Weapon {
    public static Arcane shadowStaff = new Arcane(Items.shadowStaffR, "Shadow Manipulator", 200, false, false, 100, 100, 1, "Your shadow seem a bit abnormal...",
            53, 58, new TextureRegion[]{Items.shadowStaffL, Items.shadowStaffR}, new String[]{"Shadow", "Arcane"}, 10, 2, 0);

    public static Arcane arcaneCaster = new Arcane(Items.arcaneCasterR, "Arcane Caster", 201, false, true, 100, 100, 1, "Power beyond your understanding...",
            53, 58, new TextureRegion[]{Items.arcaneCasterL, Items.arcaneCasterR}, new String[]{"Arcane"}, 10, 2, 0);

    public Arcane(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental,
                  int healthConsumption, float refreshSpeed, int burst) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display, elemental);

        this.healthConsumption = healthConsumption;
        this.refreshSpeed = refreshSpeed;
        this.burst = burst;
    }

    public Arcane createNew(int count) {
        Arcane i = new Arcane(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display, elemental, healthConsumption, refreshSpeed, burst);
        i.pickedUp = true;
        i.count = count;
        return i;
    }

    @Override
    public void tick() {

    }

    public void onClick() {

    }
}
