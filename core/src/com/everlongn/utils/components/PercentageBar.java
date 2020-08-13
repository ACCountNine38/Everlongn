package com.everlongn.utils.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Items;
import com.everlongn.assets.Tiles;
import com.everlongn.assets.UI;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.MenuState;

public class PercentageBar extends UIComponent {
    public static int selectedID = -1;
    public float selectedPercentage;
    public int clickID = -2;

    public PercentageBar(float x, float y, int width, int height, boolean clickable, int clickID, float startPercentage) {
        super(x, y, "", clickable, MenuState.menuFont);
        this.width = width;
        this.height = height;
        this.clickID = clickID;
        this.selectedPercentage = startPercentage;
    }

    @Override
    public void tick() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (ControlCenter.mousePos.x > x && ControlCenter.mousePos.x < x + width &&
                    ControlCenter.height - ControlCenter.mousePos.y > y && ControlCenter.height - ControlCenter.mousePos.y < y + height) {
                selectedID = clickID;
                selectedPercentage = 1 - ((x + width - ControlCenter.mousePos.x) / width);
            } else if(ControlCenter.height - ControlCenter.mousePos.y > y && ControlCenter.height - ControlCenter.mousePos.y < y + height) {
                if(selectedID == selectedID) {
                    selectedPercentage = 1 - ((x + width - ControlCenter.mousePos.x) / width);
                    if(selectedPercentage < 0f)
                        selectedPercentage = 0f;
                    else if(selectedPercentage > 1f)
                        selectedPercentage = 1f;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(UI.percentageBar, x, y + height/3, width, height/3);
        batch.draw(Items.omniEssence, x + width*selectedPercentage - height/2, y, height, height);
    }

    @Override
    public void onClick() {

    }
}
