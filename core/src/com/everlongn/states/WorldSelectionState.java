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
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.assets.UI;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Tool;
import com.everlongn.utils.components.ImageLabel;
import com.everlongn.utils.components.TextButton;
import com.everlongn.utils.components.WorldSelectButton;

import java.util.ArrayList;

public class WorldSelectionState extends State  implements InputProcessor {
    public ArrayList<WorldSelectButton> worlds;
    public static BitmapFont buttonFont = new BitmapFont(Gdx.files.internal("fonts/chalk18.fnt"));
    public GlyphLayout layout = new GlyphLayout();

    public TextButton back, newRealm, deleteRealm, enterRealm, cancel, confirm;
    public ImageLabel panel, boarder;

    public static float scrollY, maxScroll, minScroll;

    private int selectedIndex = -1, switchCondition;
    public static float transitionAlpha = 0f, fadeAlpha;
    public static boolean transitioning, canSwitch, buttonPressed;
    public static boolean reversing, exitFromGame;

    public static ArrayList<String> names = new ArrayList<String>();

    public WorldSelectionState(StateManager stateManager) {
        super(stateManager);

        Gdx.input.setInputProcessor(this);
        if(!reversing) {
            boarder = new ImageLabel(ControlCenter.width / 2 - 300, -600, 600, 600, UI.worldSelectBoarder);
            panel = new ImageLabel(ControlCenter.width / 2 - 275, boarder.y + 75, 550, 475, UI.worldSelectPanel);
            back = new TextButton(30, -50, "Back", true);
            newRealm = new TextButton(ControlCenter.width / 2 + 300 - 175, boarder.y + 65, "New", true);
            deleteRealm = new TextButton(ControlCenter.width / 2 - 55, boarder.y + 65, "Delete", false);
            enterRealm = new TextButton(ControlCenter.width / 2 - 300 + 80, boarder.y + 65, "Enter", false);

            boarder.activate(ControlCenter.height / 2 - 300, 25, 10);
            panel.activate(ControlCenter.height / 2 - 225, 25, 10);
            back.activate(60, 10, 2);
            newRealm.activate(ControlCenter.height / 2 - 235, 25, 10);
            deleteRealm.activate(ControlCenter.height / 2 - 235, 25, 10);
            enterRealm.activate(ControlCenter.height / 2 - 235, 25, 10);
            reversing = false;
        } else {
            boarder = new ImageLabel(ControlCenter.width / 2 - 300, ControlCenter.height / 2 - 300, 600, 600, UI.worldSelectBoarder);
            panel = new ImageLabel(ControlCenter.width / 2 - 275, ControlCenter.height / 2 - 225, 550, 475, UI.worldSelectPanel);
            back = new TextButton(30, 60, "Back", true);
            newRealm = new TextButton(ControlCenter.width / 2 + 300 - 175, ControlCenter.height / 2 - 235, "New", true);
            deleteRealm = new TextButton(ControlCenter.width / 2 - 55, ControlCenter.height / 2 - 235, "Delete", false);
            enterRealm = new TextButton(ControlCenter.width / 2 - 300 + 80, ControlCenter.height / 2 - 235, "Enter", false);
        }

        layout.setText(MenuState.menuFont, "Confirm");
        confirm = new TextButton(ControlCenter.width/2 - (int)(layout.width) - 40, -50,  "Confirm", true);
        cancel = new TextButton(ControlCenter.width/2 + 40, -50,  "Cancel", true);

        worlds = new ArrayList<>();
        loadWorlds();

        Sounds.menuMusic.setVolume(1);
        Sounds.playMusic(Sounds.menuMusic, 0.75f);
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

                FileHandle tilemap = Gdx.files.external("everlongn/realms/tile/" + data[0] + ".png");
                FileHandle wallMap = Gdx.files.external("everlongn/realms/wall/" + data[0] + ".png");
                FileHandle herbMap = Gdx.files.external("everlongn/realms/herb/" + data[0] + ".txt");

                if(tilemap.exists() && wallMap.exists() && herbMap.exists()) {
                    worlds.add(new WorldSelectButton(ControlCenter.width / 2 - 265, ControlCenter.height / 2 - 235 + 360 + numWorlds * -110, "", true, MenuState.menuFont,
                            data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5],
                            Gdx.files.external("everlongn/realms/tile/" + data[0] + ".png"),
                            Gdx.files.external("everlongn/realms/wall/" + data[0] + ".png"),
                            Gdx.files.external("everlongn/realms/herb/" + data[0] + ".txt"),
                            Gdx.files.external("everlongn/realms/debris/" + data[0] + ".png")));

                    numWorlds++;
                    if(!names.contains(data[0]))
                        names.add(data[0]);
                }
            }
        }
    }

    @Override
    public void tick(float delta) {
        Tool.changeCursor(0);
        if(exitFromGame) {
            fadeAlpha -= 0.03;
            if(fadeAlpha <= 0) {
                fadeAlpha = 0;
                exitFromGame = false;
            }
        }
        updateLayers(delta);
        if(transitioning) {
            transitionAlpha += 0.03;
            if(Sounds.menuMusic.getVolume()-0.02f > 0) {
                Sounds.menuMusic.setVolume(Sounds.menuMusic.getVolume() - 0.02f);
            } else {
                Sounds.menuMusic.setVolume(0);
            }
            if (transitionAlpha >= 1f && Sounds.menuMusic.getVolume() <= 0) {
                if(StateManager.states.size() >= 1) {
                    StateManager.states.pop().dispose();
                }
                int diff = 0;
                if(worlds.get(selectedIndex).difficulty.equals("Intense")) {
                    diff = 1;
                } else if(worlds.get(selectedIndex).difficulty.equals("Insane")) {
                    diff = 2;
                }

                Sounds.menuMusic.stop();
                StateManager.states.push(new WorldLoadingState(stateManager, worlds.get(selectedIndex).tilemap, worlds.get(selectedIndex).wallmap, worlds.get(selectedIndex).herbsMap, worlds.get(selectedIndex).debrisMap, diff, worlds.get(selectedIndex).mode, worlds.get(selectedIndex).worldName));
            }
            return;
        }

        if(reversing) {
            transitionAlpha -= 0.03;
            if(transitionAlpha < 0) {
                transitionAlpha = 0;
                reversing = false;
            }
        }

        for(int i = 0; i < worlds.size(); i++) {
            worlds.get(i).tick();
            if(worlds.get(i).hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                // Sounds.playSound(Sounds.buttonClick);
                if(selectedIndex != -1) {
                    worlds.get(selectedIndex).selected = false;
                }
                selectedIndex = i;
                worlds.get(selectedIndex).selected = true;

                confirm.activate(-50, 10, 2);
                cancel.activate(-50, 10, 2);
                newRealm.activate(ControlCenter.height/2 - 235, 10, 2);
                enterRealm.activate(ControlCenter.height/2 - 235, 10, 2);
                deleteRealm.activate(ControlCenter.height/2 - 235, 10, 2);
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
            Sounds.playSound(Sounds.buttonClick);
            buttonPressed = true;
            switchCondition = 0;
            activateTransition();
        }

        if(enterRealm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Sounds.playSound(Sounds.buttonClick);
            if(selectedIndex != -1) {
                buttonPressed = true;
                switchCondition = 1;
                activateTransition();
            }
        }

        if(newRealm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Sounds.playSound(Sounds.buttonClick);
            stateManager.setState(StateManager.CurrentState.WORLD_CREATION_STATE);
        }

        if(deleteRealm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && selectedIndex != -1) {
            Sounds.playSound(Sounds.buttonClick);
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
            Sounds.playSound(Sounds.buttonClick);
            Gdx.files.external("everlongn/data/" + worlds.get(selectedIndex).worldName + ".txt").delete();
            Gdx.files.external("everlongn/meta/" + worlds.get(selectedIndex).worldName + ".txt").delete();
            Gdx.files.external("everlongn/realms/tile/" + worlds.get(selectedIndex).worldName + ".png").delete();
            Gdx.files.external("everlongn/realms/wall/" + worlds.get(selectedIndex).worldName + ".png").delete();
            Gdx.files.external("everlongn/realms/herb/" + worlds.get(selectedIndex).worldName + ".txt").delete();
            Gdx.files.external("everlongn/realms/debris/" + worlds.get(selectedIndex).worldName + ".png").delete();

            names.remove(worlds.get(selectedIndex).worldName);
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
            Sounds.playSound(Sounds.buttonClick);
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
            if(switchCondition == 0) {
                canSwitch = false;
                stateManager.setState(StateManager.CurrentState.MENU_STATE);
            }
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

        // transition between menu, world creation, and world loading
        if(transitioning || reversing) {
            batch.setColor(0f, 0f, 0f, transitionAlpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(1, 1, 1, 1);
        }
        if(fadeAlpha > 0) {
            batch.setColor(0f, 0f, 0f, fadeAlpha);
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
            scrollY += 10;
        }
        else if(amount == -1){
            scrollY -= 10;
        }
        return false;
    }
}
