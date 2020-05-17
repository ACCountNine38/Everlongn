package com.everlongn.states;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.assets.Tiles;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.TextButton;

public class MenuState extends State {

    public static BitmapFont menuFont = new BitmapFont(Gdx.files.internal("fonts/menuOn.fnt"));

    private TextButton[] buttons = {new TextButton(30, -50, "Single Player", true),
            new TextButton(30, -50,  "Multiplayer", false),
            new TextButton(30, -50,  "Settings", false),
            new TextButton(30, -50,  "Credits", false),
            new TextButton(30, -50,  "Exit", true)};

    private float screenTransitionAlpha = 1f;
    public static boolean firstEnter;
    public boolean canSwitch, buttonPressed;

    public MenuState(StateManager stateManager) {
        super(stateManager);

        buttons[0].activate(260, 10, 2);
        buttons[1].activate(210, 10, 2);
        buttons[2].activate(160, 10, 2);
        buttons[3].activate(110, 10, 2);
        buttons[4].activate(60, 10, 2);

        if(!firstEnter) {
            for (int i = 0; i < layers.length; i++) {
                layers[i] = new Vector2();
                layers[i].x = 0;
                layers[i].y = 0;
            }
            for (int i = 0; i < layers2.length; i++) {
                layers2[i] = new Vector2();
                layers2[i].x = ControlCenter.width;
                layers2[i].y = 0;
            }

            menu = new World(new Vector2(0, 0), true);
            rayHandler = new RayHandler(menu);
            rayHandler.setAmbientLight(.6f);

            mouseLight = new PointLight(rayHandler, 20, new Color(0.2f, 0.2f, 0.2f, 1f),
                    lightSize, Gdx.input.getX(), ControlCenter.height - Gdx.input.getY());
        }
    }

    @Override
    public void tick(float delta) {
        if(!firstEnter) {
            if (screenTransitionAlpha > 0) {
                screenTransitionAlpha -= 0.02;
            } else {
                firstEnter = true;
            }
        }

        updateLayers(delta);

        for(int i = 0; i < buttons.length; i++) {
            buttons[i].tick();
            if(buttons[i].hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if(i == 0) {
                    buttonPressed = true;
                    for(int j = 0; j < buttons.length; j++) {
                        buttons[j].activate(-50, 13, 3);
                    }
                } else if(i == 4) {
                    System.exit(1);
                }
            }
        }
        if(buttons[0].y == -50 && buttonPressed) {
            canSwitch = true;
        }
        if(canSwitch) {
            stateManager.setState(StateManager.CurrentState.WORLD_SELECTION_STATE);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(90f/255f, 90f/255f, 90f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setupBackground();

        batch.begin();
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].render(batch);
        }

        if(screenTransitionAlpha > 0 && !firstEnter) {
            batch.setColor(0f, 0f, 0f, screenTransitionAlpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(1, 1, 1, 1);
        }

        batch.end();
    }

    @Override
    public void dispose() {

    }
}
