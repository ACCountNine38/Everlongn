package com.everlongn.states;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.everlongn.assets.Tiles;
import com.everlongn.assets.UI;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.ImageLabel;
import com.everlongn.utils.TextButton;
import com.everlongn.utils.WorldSelectButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldSelectionState extends State  implements InputProcessor {
    public ArrayList<WorldSelectButton> worlds;
    public static BitmapFont buttonFont = new BitmapFont(Gdx.files.internal("fonts/chalk18.fnt"));
    public GlyphLayout layout = new GlyphLayout();

    public TextButton back, newRealm, deleteRealm, enterRealm, cancel, confirm;
    public ImageLabel panel, boarder;

    public static float scrollY, maxScroll, minScroll;

    private int selectedIndex = -1, switchCondition;
    private float transitionAlpha = 0f;
    private boolean transitioning, canSwitch, buttonPressed;

    public WorldSelectionState(StateManager stateManager) {
        super(stateManager);

        Gdx.input.setInputProcessor(this);
        boarder = new ImageLabel(ControlCenter.width/2 - 300, -600, 600, 600, UI.worldSelectBoarder);
        panel = new ImageLabel(ControlCenter.width/2 - 275, boarder.y + 75, 550, 475, UI.worldSelectPanel);
        back = new TextButton(30, -50, "Back", true);
        newRealm = new TextButton(ControlCenter.width/2 + 300 - 175, boarder.y + 65, "New", true);
        deleteRealm = new TextButton(ControlCenter.width/2 - 55, boarder.y + 65, "Delete", false);
        enterRealm = new TextButton(ControlCenter.width/2 - 300 + 80, boarder.y + 65, "Enter", false);

        boarder.activate(ControlCenter.height/2 - 300, 25, 10);
        panel.activate(ControlCenter.height/2 - 225, 25, 10);
        back.activate(60, 10, 2);
        newRealm.activate(ControlCenter.height/2 - 235, 25, 10);
        deleteRealm.activate(ControlCenter.height/2 - 235, 25, 10);
        enterRealm.activate(ControlCenter.height/2 - 235, 25, 10);

        layout.setText(MenuState.menuFont, "Confirm");
        confirm = new TextButton(ControlCenter.width/2 - (int)(layout.width) - 40, -50,  "Confirm", true);
        cancel = new TextButton(ControlCenter.width/2 + 40, -50,  "Cancel", true);

        worlds = new ArrayList<WorldSelectButton>();
        loadWorlds();
    }

    public void loadWorlds() {
        selectedIndex = -1;
        FileHandle dirHandle = null;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            dirHandle = Gdx.files.external("everlongn/data");
        }

        int numWorlds = 0;
        for (FileHandle entry: dirHandle.list()) {
            if(entry.extension().equals("txt")) {
                String read = entry.readString();
                String[] data = read.split("\n");

                boolean hardcore = false;
                if(data[4].equals("Hardcore")) {
                    hardcore = true;
                }

                FileHandle tilemap = Gdx.files.external("everlongn/realms/" + data[0] + "/tile.png");
                FileHandle wallMap = Gdx.files.external("everlongn/realms/" + data[0] + "/tile.png");

                if(tilemap.exists() && wallMap.exists()) {
                    worlds.add(new WorldSelectButton(ControlCenter.width / 2 - 265, ControlCenter.height / 2 - 235 + 360 + numWorlds * -110, "", true, MenuState.menuFont,
                            data[0], data[1], data[2], Integer.parseInt(data[3]), hardcore, data[5],
                            Gdx.files.external("everlongn/realms/" + data[0] + "/tile.png"),
                            Gdx.files.external("everlongn/realms/" + data[0] + "/wall.png"),
                            Gdx.files.external("everlongn/realms/" + data[0] + "/herbs.png")));

                    numWorlds++;
                }
            }
        }
    }

    @Override
    public void tick(float delta) {
        updateLayers(delta);

        if(transitioning) {
            transitionAlpha += 0.03;
            if (transitionAlpha >= 1f) {
                if(StateManager.states.size() >= 1) {
                    StateManager.states.pop().dispose();
                }
                StateManager.states.push(new WorldLoadingState(stateManager, worlds.get(selectedIndex).tilemap, worlds.get(selectedIndex).wallmap, worlds.get(selectedIndex).herbsMap));
            }
            return;
        }

        for(int i = 0; i < worlds.size(); i++) {
            worlds.get(i).tick();
            if(worlds.get(i).hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if(selectedIndex != -1) {
                    worlds.get(selectedIndex).selected = false;
                }
                selectedIndex = i;
                worlds.get(selectedIndex).selected = true;
            }
        }
        back.tick();
        newRealm.tick();
        deleteRealm.tick();
        enterRealm.tick();
        confirm.tick();
        cancel.tick();
        boarder.tick();
        panel.tick();

        if(selectedIndex != -1) {
            enterRealm.clickable = true;
            deleteRealm.clickable = true;
        }

        if(back.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            buttonPressed = true;
            switchCondition = 0;
            activateTransition();
        }

        if(enterRealm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if(selectedIndex != -1) {
                buttonPressed = true;
                switchCondition = 1;
                activateTransition();
            }
        }
        if(newRealm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            stateManager.setState(StateManager.CurrentState.WORLD_CREATION_STATE);
        }

        if(deleteRealm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedIndex != -1) {
            confirm.activate(ControlCenter.height/2 - 235, 10, 2);
            cancel.activate(ControlCenter.height/2 - 235, 10, 2);
            newRealm.activate(-50, 10, 2);
            enterRealm.activate(-50, 10, 2);
            deleteRealm.activate(-50, 10, 2);
        }

        if(selectedIndex == -1) {
            deleteRealm.clickable = false;
            enterRealm.clickable = false;
        }

        if(confirm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedIndex != -1) {
            Gdx.files.external("everlongn/data/" + worlds.get(selectedIndex).worldName + ".txt").delete();
            Gdx.files.external("everlongn/realms/" + worlds.get(selectedIndex).worldName).delete();

            worlds.clear();
            loadWorlds();

            confirm.activate(-50, 10, 2);
            cancel.activate(-50, 10, 2);
            newRealm.activate(ControlCenter.height/2 - 235, 10, 2);
            enterRealm.activate(ControlCenter.height/2 - 235, 10, 2);
            deleteRealm.activate(ControlCenter.height/2 - 235, 10, 2);

            selectedIndex = -1;
        }

        if(cancel.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedIndex != -1) {
            confirm.activate(-50, 10, 2);
            cancel.activate(-50, 10, 2);
            newRealm.activate(ControlCenter.height/2 - 235, 10, 2);
            enterRealm.activate(ControlCenter.height/2 - 235, 10, 2);
            deleteRealm.activate(ControlCenter.height/2 - 235, 10, 2);
        }

        if(boarder.y == -600 && buttonPressed) {
            canSwitch = true;
        }
        if(canSwitch) {
            if(switchCondition == 0)
                stateManager.setState(StateManager.CurrentState.MENU_STATE);
            else if(switchCondition == 1)
                transitioning = true;
        }
    }

    public void activateTransition() {
        boarder.activate(-600, 25, 2);
        panel.activate(boarder.ey + 75, 25, 2);
        back.activate(-50, 10, 2);
        newRealm.activate(boarder.ey + 65, 25, 2);
        deleteRealm.activate(boarder.ey + 65, 25, 2);
        enterRealm.activate(boarder.ey + 65, 25, 2);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(90f/255f, 90f/255f, 90f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setupBackground();

        batch.begin();

        boarder.render(batch);
        panel.render(batch);

        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = new Rectangle(ControlCenter.width/2 - 275,panel.y + 15,550,445);
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
        if (ScissorStack.pushScissors(scissors)) {
            for(int i = 0; i < worlds.size(); i++) {
                worlds.get(i).render(batch);
            }
            batch.flush();
            ScissorStack.popScissors();
        }
        clipBounds = new Rectangle(0,0,ControlCenter.width,ControlCenter.height);
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);

        back.render(batch);
        newRealm.render(batch);
        deleteRealm.render(batch);
        enterRealm.render(batch);
        confirm.render(batch);
        cancel.render(batch);

        if(transitioning) {
            batch.setColor(0f, 0f, 0f, transitionAlpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(1, 1, 1, 1);
        }
        batch.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if(amount == 1){
            scrollY += 5;
        }
        else if(amount == -1){
            scrollY -= 5;
        }
        return false;
    }
}
