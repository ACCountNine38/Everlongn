package com.everlongn.utils.frameworks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.everlongn.assets.UI;
import com.everlongn.utils.TextManager;

public class Telepathy {
    public static boolean focused;
    public int x, y, width, height;
    public String currentText = "";

    public Telepathy(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void tick() {
        if(!focused) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                focused = true;
            }
        } else {
            checkKeysTyped();
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if(currentText.equals("")) {
                    // exit text
                    focused = false;
                } else {
                    // send text
                    currentText = "";
                }
            } else if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                focused = false;
            }
        }
    }

    public void checkKeysTyped() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            currentText += "F";
        }
        InputListener il = new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                currentText += Input.Keys.toString(keycode);
                return super.keyDown(event, keycode);
            }
        };
    }

    public void render(SpriteBatch batch) {
        if(focused) {
            batch.draw(UI.worldSelect, x, y, width, height);
            TextManager.draw(currentText, x, y + height/2 + 10, Color.WHITE, 1f, false);
        }
    }
}
