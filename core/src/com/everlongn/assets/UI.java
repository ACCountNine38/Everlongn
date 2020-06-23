package com.everlongn.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
public class UI {
    public static Texture inventorySlot, hotbarSlot, selectedSlot, stain1, stain2, stain3, stain4, stain5;
    public static Texture worldSelect, worldSelected, worldSelectPanel, worldSelectBoarder, hardcore, normal,
        chargeCursor, chargeOrb, percentageBar;

    public static void init() {
        inventorySlot = new Texture(Gdx.files.internal("UI/inventorySlot.png"));
        hotbarSlot = new Texture(Gdx.files.internal("UI/hotbarSlot.png"));
        selectedSlot = new Texture(Gdx.files.internal("UI/selectedSlot.png"));

        chargeCursor = new Texture(Gdx.files.internal("UI/chargeCursor.png"), true);
        chargeCursor.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        chargeOrb = new Texture(Gdx.files.internal("UI/chargeOrb.png"), true);
        chargeOrb.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        worldSelect = new Texture(Gdx.files.internal("UI/worldSelect.png"));
        worldSelected = new Texture(Gdx.files.internal("UI/worldSelected.png"));
        worldSelectPanel = new Texture(Gdx.files.internal("UI/worldSelectPanel.png"));
        worldSelectBoarder = new Texture(Gdx.files.internal("UI/worldSelectBoarder.png"));
        hardcore = new Texture(Gdx.files.internal("UI/hardcore.png"), true);
        hardcore.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        normal = new Texture(Gdx.files.internal("UI/normal.png"), true);
        normal.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        percentageBar = new Texture(Gdx.files.internal("UI/percentageBar.png"), true);
        percentageBar.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);

        stain1 = new Texture(Gdx.files.internal("UI/stain1.png"), true);
        stain1.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stain2 = new Texture(Gdx.files.internal("UI/stain2.png"), true);
        stain2.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stain3 = new Texture(Gdx.files.internal("UI/stain3.png"), true);
        stain3.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stain4 = new Texture(Gdx.files.internal("UI/stain4.png"), true);
        stain4.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
        stain5 = new Texture(Gdx.files.internal("UI/stain5.png"), true);
        stain5.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    }
}

