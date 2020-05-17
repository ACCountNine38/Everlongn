package com.everlongn.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.BufferUtils;
import com.everlongn.utils.PerlinNoiseGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SplashState extends State {
    float count = 0f;
    private Texture background;

    public SplashState(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void tick(float delta) {
        count += delta;
        if(count >= 0.5) { // 2 seconds
            stateManager.setState(StateManager.CurrentState.MENU_STATE);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f,0f,0f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {

    }
}
