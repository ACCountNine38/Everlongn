package com.everlongn.items;

import com.badlogic.gdx.graphics.Texture;
import com.everlongn.assets.Items;

public class UtilItem extends Item {
    public static UtilItem shadowStaff = new UtilItem(Items.shadowStaff, "Rod of Shadows", 200, false, true, 50, 50, 1, "Your shadow seem a bit abnormal...",
            2);

    public float refreshSpeed;

    public UtilItem(Texture texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int capacity, String description,
                    float refreshSpeed) {
        super(texture, name, id, stackable, degeneratable, width, height, capacity, description);
    }
}