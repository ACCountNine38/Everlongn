package com.everlongn.assets;

import com.badlogic.gdx.graphics.Texture;
public class UI {
    public static Texture inventorySlot, hotbarSlot, selectedSlot;

    public static void init() {
        inventorySlot = new Texture("core//res//images//UI//inventorySlot.png");
        hotbarSlot = new Texture("core//res//images//UI//hotbarSlot.png");
        selectedSlot = new Texture("core//res//images//UI//selectedSlot.png");
    }
}

