package com.everlongn.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;

public abstract class State {
    // references
    protected StateManager stateManager;
    protected ControlCenter c;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    public State(StateManager stateManager) {
        this.stateManager = stateManager;
        c = stateManager.getC();
        batch = c.getSpriteBatch();
        camera = c.getCamera();
    }

    public void resize(int w, int h) {
       camera.setToOrtho(false, w, h);
    }

    public abstract void tick(float delta);
    public abstract void render();
    public abstract void dispose();
}

