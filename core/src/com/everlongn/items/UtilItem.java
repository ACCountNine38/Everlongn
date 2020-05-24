package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.assets.Items;

public class UtilItem extends Item {
    public static UtilItem shadowStaff = new UtilItem(Items.shadowStaffR, "Rod of Shadows", 200, false, true, 100, 100, 1, "Your shadow seem a bit abnormal...",
            53, 58, new TextureRegion[]{Items.shadowStaffL, Items.shadowStaffR}, 2);

    public float refreshSpeed;

    public UtilItem(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description, float holdX, float holdY, TextureRegion[] display,
                    float refreshSpeed) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display);
        this.holdX = holdX;
        this.holdY = holdY;
        this.refreshSpeed = refreshSpeed;
    }
}