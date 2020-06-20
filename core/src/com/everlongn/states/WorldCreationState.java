package com.everlongn.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.everlongn.assets.Tiles;
import com.everlongn.assets.UI;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.components.ImageLabel;
import com.everlongn.utils.components.TextButton;
import com.everlongn.utils.components.TextArea;
import com.everlongn.utils.components.TextImageButton;

public class WorldCreationState extends State {
    private TextArea seed, realmName;
    public GlyphLayout layout = new GlyphLayout();

    public TextButton back, confirm;
    public TextImageButton small, medium, large;
    public TextImageButton standard, intense, insane;
    public TextImageButton normal, hardcore;
    public ImageLabel panel, boarder;

    public boolean transitioning;
    public float transitionAlpha;
    public String worldSize, worldDifficulty, worldMode;

    public static BitmapFont worldCreationFont = new BitmapFont(Gdx.files.internal("fonts/chalk22.fnt"));
    public static BitmapFont worldCreationFont2 = new BitmapFont(Gdx.files.internal("fonts/chalk32.fnt"));

    public WorldCreationState(StateManager stateManager) {
        super(stateManager);

        initComponents();
    }

    private void initComponents() {
        boarder = new ImageLabel(ControlCenter.width/2 - 300, -600, 600, 600, UI.worldSelectBoarder);
        panel = new ImageLabel(ControlCenter.width/2 - 275, boarder.y + 75, 550, 475, UI.worldSelectPanel);

        boarder.activate(ControlCenter.height/2 - 300, 25, 10);
        panel.activate(ControlCenter.height/2 - 225, 25, 10);

        layout.setText(worldCreationFont2, "Realm Name:");
        realmName = new TextArea(ControlCenter.width/2 - 250, ControlCenter.height/2 + 250 - layout.height - 20 - layout.height - 40,
                500, "", true, worldCreationFont2, false, 20, true);

        layout.setText(worldCreationFont, "Seed: ");
        seed = new TextArea(realmName.x + layout.width + 25, realmName.y - layout.height - 45,
                500 - layout.width - 25, "", true, worldCreationFont, true, 9, true);

        back = new TextButton(30, 60, "Back", true);
        back.activate(60, 10, 2);

        layout.setText(MenuState.menuFont, "Confirm");
        confirm = new TextButton((int)(ControlCenter.width/2 - layout.width/2), ControlCenter.height/2 - 240,  "Confirm", false);

        int buttonWidth = 125;
        small = new TextImageButton(ControlCenter.width/2 - buttonWidth/2 - buttonWidth - 50, (int)(realmName.y - 155 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Small", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);
        medium = new TextImageButton(ControlCenter.width/2 - buttonWidth/2, (int)(realmName.y - 155 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Medium", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);
        large = new TextImageButton(ControlCenter.width/2 + buttonWidth/2 + 50, (int)(realmName.y - 155 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Large", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);

        standard = new TextImageButton(ControlCenter.width/2 - buttonWidth/2 - buttonWidth - 50, (int)(realmName.y - 245 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Standard", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);
        intense = new TextImageButton(ControlCenter.width/2 - buttonWidth/2, (int)(realmName.y - 245 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Intense", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);
        insane = new TextImageButton(ControlCenter.width/2 + buttonWidth/2 + 50, (int)(realmName.y - 245 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Insane", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);

        normal = new TextImageButton(ControlCenter.width/2 - buttonWidth - 25, (int)(realmName.y - 335 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Normal", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);
        hardcore = new TextImageButton(ControlCenter.width/2 + 25, (int)(realmName.y - 335 + seed.height/2 - buttonWidth/6),
                0, buttonWidth, buttonWidth/3, "Hardcore", UI.worldSelected, UI.worldSelect,
                worldCreationFont, true);
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
                WorldSelectionState.transitionAlpha = 1f;
                StateManager.states.push(new WorldGenerationState(stateManager, realmName.currentText,
                        Integer.parseInt(seed.currentText), worldSize, worldDifficulty, worldMode));
            }
            return;
        }

        tickButtons();


        if(back.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            WorldSelectionState.reversing = true;
            stateManager.setState(StateManager.CurrentState.WORLD_SELECTION_STATE);
        }

        seed.tick();
        realmName.tick();

        if(realmName.currentText.length() > 0 && (small.selected || medium.selected || large.selected) &&
                (standard.selected || intense.selected || insane.selected) &&
                (normal.selected || hardcore.selected)) {
            confirm.clickable = true;
        } else {
            confirm.clickable = false;
        }

        if(confirm.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            worldSize = "";
            if(large.selected) {
                worldSize = "Large";
            } else if(medium.selected) {
                worldSize = "Medium";
            } else if(small.selected) {
                worldSize = "Small";
            }

            worldDifficulty = "";
            if(standard.selected) {
                worldDifficulty = "Standard";
            } else if(intense.selected) {
                worldDifficulty = "Intense";
            } else if(insane.selected) {
                worldDifficulty = "Insane";
            }

            worldMode = "";
            if(normal.selected) {
                worldMode = "Normal";
            } else if(hardcore.selected) {
                worldMode = "Hardcore";
            }

            transitioning = true;
        }

        checkInput();
    }

    private void tickButtons() {
        back.tick();
        confirm.tick();
        small.tick();
        medium.tick();
        large.tick();
        standard.tick();
        intense.tick();
        insane.tick();
        normal.tick();
        hardcore.tick();

        boarder.tick();
        panel.tick();
    }

    private void checkInput() {
        if(seed.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            seed.focused = true;
            realmName.focused = false;
        } else if(realmName.hover && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            realmName.focused =  true;
            seed.focused = false;
        } else if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if(small.hover) {
                small.selected = true;
                medium.selected = false;
                large.selected = false;
            } else if(medium.hover) {
                small.selected = false;
                medium.selected = true;
                large.selected = false;
            } else if(large.hover) {
                small.selected = false;
                medium.selected = false;
                large.selected = true;
            }

            else if(standard.hover) {
                standard.selected = true;
                intense.selected = false;
                insane.selected = false;
            } else if(intense.hover) {
                standard.selected = false;
                intense.selected = true;
                insane.selected = false;
            } else if(insane.hover) {
                standard.selected = false;
                intense.selected = false;
                insane.selected = true;
            }

            else if(normal.hover) {
                normal.selected = true;
                hardcore.selected = false;
            } else if(hardcore.hover) {
                normal.selected = false;
                hardcore.selected = true;
            }

            realmName.focused =  false;
            seed.focused = false;
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(90f/255f, 90f/255f, 90f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setupBackground();

        batch.begin();

        batch.draw(UI.worldSelectBoarder, ControlCenter.width/2 - 300, ControlCenter.height/2 - 300, 600, 600);
        batch.draw(UI.worldSelectPanel, ControlCenter.width/2 - 275, ControlCenter.height/2 - 225, 550, 475);

        worldCreationFont.setColor(Color.WHITE);
        layout.setText(worldCreationFont2, "Realm Name:");
        worldCreationFont2.draw(batch, "Realm Name:", ControlCenter.width/2 - layout.width/2, ControlCenter.height/2 + 250 - 40 + realmName.height/2);
        layout.setText(worldCreationFont, "Seed: ");
        worldCreationFont.draw(batch, "Seed:", realmName.x, realmName.y - 45 + seed.height/2);
        layout.setText(worldCreationFont, "Realm Size:");
        worldCreationFont.draw(batch, "Realm Size:", ControlCenter.width/2 - layout.width/2, realmName.y - 100 + seed.height/2);
        layout.setText(worldCreationFont, "Difficulty:");
        worldCreationFont.draw(batch, "Difficulty:", ControlCenter.width/2 - layout.width/2, realmName.y - 190 + seed.height/2);
        layout.setText(worldCreationFont, "Mode:");
        worldCreationFont.draw(batch, "Mode:", ControlCenter.width/2 - layout.width/2, realmName.y - 280 + seed.height/2);

        renderButtons();

        if(transitioning) {
            batch.setColor(0f, 0f, 0f, transitionAlpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(1, 1, 1, 1);
        }

        batch.end();
    }

    private void renderButtons() {
        seed.render(batch);
        realmName.render(batch);
        back.render(batch);
        confirm.render(batch);

        small.render(batch);
        medium.render(batch);
        large.render(batch);

        standard.render(batch);
        intense.render(batch);
        insane.render(batch);

        normal.render(batch);
        hardcore.render(batch);
    }

    @Override
    public void dispose() {

    }
}
