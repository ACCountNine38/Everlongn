package com.everlongn.states;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.assets.Backgrounds;
import com.everlongn.game.ControlCenter;

public abstract class State {
    // references
    public StateManager stateManager;
    public ControlCenter c;
    public static SpriteBatch batch;
    public static OrthographicCamera camera;

    public static Vector2[] layers = new Vector2[4];
    public static Vector2[] layers2 = new Vector2[4];

    public static RayHandler rayHandler;
    public static World menu;
    public static PointLight mouseLight;
    public static float lightSize = 200, lightSizeIncreaseCounter;
    public static boolean increaseSize = false;

    public State(StateManager stateManager) {
        this.stateManager = stateManager;
        c = stateManager.getC();
        batch = c.getSpriteBatch();
        camera = c.getCamera();
    }

    public void updateLayers(float delta) {
        mouseLight.setPosition(ControlCenter.touchPos.x + 8, ControlCenter.touchPos.y - 8);
        mouseLight.setDistance(lightSize);

        lightSizeIncreaseCounter += delta;
        if(lightSizeIncreaseCounter >= 0.05) { // 2 seconds
            if(increaseSize) {
                lightSize++;
                if(lightSize >= 200) {
                    increaseSize = false;
                }
            } else {
                lightSize--;
                if(lightSize <= 100) {
                    increaseSize = true;
                }
            }
        }

        layers[0].x -= 1.5f;
        layers[1].x -= 1f;
        layers[2].x -= 0.7f;
        layers[3].x -= 0.5f;

        layers2[0].x -= 1.5f;
        layers2[1].x -= 1f;
        layers2[2].x -= 0.7f;
        layers2[3].x -= 0.5f;

        for(int i = 0; i < layers.length; i++) {
            if(layers[i].x + ControlCenter.width < 0) {
                layers[i].x = layers2[i].x + ControlCenter.width;
            }
            if(layers2[i].x + ControlCenter.width < 0) {
                layers2[i].x = layers[i].x + ControlCenter.width;
            }
        }

        rayHandler.update();
        batch.setProjectionMatrix(camera.combined);
        rayHandler.setCombinedMatrix(camera);
    }

    public void drawMenuBackground() {
        batch.draw(Backgrounds.menu[0][0], 0, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[1][0], layers[3].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[1][1], layers2[3].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[2][0], 0, 0, ControlCenter.width, ControlCenter.height);

        batch.draw(Backgrounds.menu[3][0], layers[2].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[3][1], layers2[2].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[4][0], 0, 0, ControlCenter.width, ControlCenter.height);

        batch.draw(Backgrounds.menu[5][0], layers[1].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[5][1], layers2[1].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[6][0], 0, 0, ControlCenter.width, ControlCenter.height);

        batch.draw(Backgrounds.menu[7][0], layers[0].x, 0, ControlCenter.width, ControlCenter.height);
        batch.draw(Backgrounds.menu[7][1], layers2[0].x, 0, ControlCenter.width, ControlCenter.height);
    }

    public void setupBackground() {
        batch.begin();
        drawMenuBackground();
        batch.end();
        rayHandler.render();
    }

    public void resize(int w, int h) {
       camera.setToOrtho(false, w, h);
    }

    public abstract void tick(float delta);
    public abstract void render();
    public abstract void dispose();
}

