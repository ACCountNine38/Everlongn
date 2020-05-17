package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
public class UI {
    public static Texture inventorySlot, hotbarSlot, selectedSlot;
    public static Texture worldSelect, worldSelected, worldSelectPanel, worldSelectBoarder, hardcore, normal;

    public static void init() {
        inventorySlot = new Texture(Gdx.files.internal("UI/inventorySlot.png"));
        hotbarSlot = new Texture(Gdx.files.internal("UI/hotbarSlot.png"));
        selectedSlot = new Texture(Gdx.files.internal("UI/selectedSlot.png"));

        worldSelect = new Texture(Gdx.files.internal("UI/worldSelect.png"));
        worldSelected = new Texture(Gdx.files.internal("UI/worldSelected.png"));
        worldSelectPanel = new Texture(Gdx.files.internal("UI/worldSelectPanel.png"));
        worldSelectBoarder = new Texture(Gdx.files.internal("UI/worldSelectBoarder.png"));
        hardcore = new Texture(Gdx.files.internal("UI/hardcore.png"), true);
        hardcore.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        normal = new Texture(Gdx.files.internal("UI/normal.png"), true);
        normal.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }
}

