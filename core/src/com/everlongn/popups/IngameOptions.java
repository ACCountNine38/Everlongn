package com.everlongn.popups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.UI;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.states.MenuState;
import com.everlongn.utils.TextManager;
import com.everlongn.utils.components.PercentageBar;
import com.everlongn.utils.components.TextButton;

public class IngameOptions extends Popup{
    public static float displayAlpha;
    public TextButton achievements, exit, controls;
    public PercentageBar masterVolume, sfx;

    public IngameOptions() {
        controls = new TextButton(ControlCenter.width/2, ControlCenter.height/2 - 50, "Controls", true, true, MenuState.menuFont);
        achievements = new TextButton(ControlCenter.width/2, ControlCenter.height/2 - 125, "Achievements", true, true, MenuState.menuFont);
        exit = new TextButton(ControlCenter.width/2, ControlCenter.height/2 - 200, "Save and Exit", true, true, MenuState.menuFont);

        masterVolume = new PercentageBar(ControlCenter.width/2 - 50, ControlCenter.height/2 + 135, 260, 54, true, 1, 1f);
        sfx = new PercentageBar(ControlCenter.width/2 - 50, ControlCenter.height/2 + 70, 260, 54, true, 2, 1f);

    }

    @Override
    public void tick() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            active = !active;
        }

        if(active) {
            if(displayAlpha < 1f) {
                displayAlpha += 0.1f;
                if(displayAlpha > 1f) {
                    displayAlpha = 1f;
                }
            }
            controls.tick();
            achievements.tick();
            exit.tick();
            masterVolume.tick();
            Sounds.ambientPercentage = masterVolume.selectedPercentage;
            sfx.tick();
            Sounds.sfxPercentage = sfx.selectedPercentage;
            if(exit.hover && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                GameState.exiting = true;
            }
        } else {
            if(displayAlpha > 0f) {
                displayAlpha -= 0.1f;
                if(displayAlpha < 0f) {
                    displayAlpha = 0f;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(displayAlpha > 0) {
            batch.setColor(batch.getColor().r, batch.getColor().b, batch.getColor().g, displayAlpha);
            batch.draw(UI.stain5, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(0f, 0f, 0f, displayAlpha);
            batch.draw(UI.worldSelectBoarder, ControlCenter.width/2 - 250, ControlCenter.height/2 - 300, 500, 600);
            TextManager.draw("Settings", ControlCenter.width/2, ControlCenter.height/2 + 250, Color.WHITE, 1, true, MenuState.menuFont);

            TextManager.draw("Ambient:", ControlCenter.width/2 - 150, ControlCenter.height/2 + 180, Color.WHITE, 1, true, MenuState.menuFont);
            TextManager.draw("SFX:", ControlCenter.width/2 - 150, ControlCenter.height/2 + 115, Color.WHITE, 1, true, MenuState.menuFont);

            controls.render(batch);
            achievements.render(batch);
            exit.render(batch);
            masterVolume.render(batch);
            sfx.render(batch);

            batch.setColor(1f, 1f, 1f, 1f);
        }
        batch.end();
    }
}
