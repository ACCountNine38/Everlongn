package com.everlongn.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.TextManager;

public class ErrorLoadingState extends State {
    public float alpha = 1f;
    public static int reason;
    public boolean transitioning;
    public static String version;

    public ErrorLoadingState(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void tick(float delta) {
        if(ControlCenter.leftClick) {
            transitioning = true;
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(90f/255f, 90f/255f, 90f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setupBackground();

        batch.begin();

        if(reason == 1) {
            TextManager.draw("Outdated version("+version+"), tap anywhere to continue...",
                    ControlCenter.width/2, ControlCenter.height/2, Color.WHITE, 1, true, MenuState.menuFont);
        } else {
            TextManager.draw("File data has been inaccurately edited, tap anywhere to continue...",
                    ControlCenter.width/2, ControlCenter.height/2, Color.WHITE, 1, true, MenuState.menuFont);
        }

        if(!transitioning) {
            alpha -= 0.03f;
            if(alpha < 0)
                alpha = 0;
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
        } else {
            alpha += 0.03f;
            if(alpha >= 1f) {
                alpha = 1f;
                WorldSelectionState.transitioning = false;
                WorldSelectionState.transitionAlpha = 0f;
                WorldSelectionState.reversing = false;
                WorldSelectionState.canSwitch = false;
                WorldSelectionState.fadeAlpha = 1f;
                WorldSelectionState.exitFromGame = true;
                Sounds.playSound(Sounds.buttonClick);
                stateManager.setState(StateManager.CurrentState.WORLD_SELECTION_STATE);
            }
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
        }
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
