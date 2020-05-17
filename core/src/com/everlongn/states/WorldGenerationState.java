package com.everlongn.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.PerlinNoiseGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WorldGenerationState extends State {
    public GlyphLayout layout = new GlyphLayout();
    public static BitmapFont generatingFont = new BitmapFont(Gdx.files.internal("fonts/chalk22.fnt"));
    public static Random r = new Random();

    private int horizon, underground, chunkSize, worldWidth, worldHeight, spawnX, spawnY, seed, numSteps, currentStep;
    private String name, size, difficulty, mode, currentStage = "Setting Up...";

    private Pixmap caveMap, pixmap;
    private ArrayList<Integer> elevation;

    private int currentElevation, caveInstance;
    private float count;
    private boolean generated;

    public WorldGenerationState(StateManager stateManager, String name, int seed, String size, String difficulty, String mode) {
        super(stateManager);

        this.name = name;
        this.seed = seed;
        this.difficulty = difficulty;
        this.size = size;
        this.mode = mode;

        numSteps = 10;
    }

    @Override
    public void tick(float delta) {
        updateLayers(delta);

        if(!generated) {
            generate(currentStep);
        } else {
            currentStage = "Complete...";
            count += delta;
            if(count >= 0.5) { // 2 seconds
                stateManager.setState(StateManager.CurrentState.WORLD_SELECTION_STATE);
            }
        }
    }

    public void generate(int step) {
        if(step == 1) {
            currentStage = "Setting Up...";
            r.setSeed(seed);

            elevation = new ArrayList<Integer>();

            chunkSize = 25;
            if(size.equals("Small")) {
                worldWidth = 1500;
                worldHeight = 300;
            } else if(size.equals("Medium")) {
                worldWidth = 2000;
                worldHeight = 400;
            } else if(size.equals("Large")) {
                worldWidth = 3000;
                worldHeight = 500;
            }

            horizon = worldHeight/4;
            underground = horizon - 25;
            currentElevation = horizon;

            for(int i = 0; i < worldWidth/chunkSize; i++) {
                elevation.add(r.nextInt(38) - 20);
            }

            pixmap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);
            pixmap.setColor(1f/255f, 1f/255f, 1f/255f, 1);

            // perlin noise generation
            caveMap = PerlinNoiseGenerator.generatePixmap(worldWidth, worldHeight, 0, 200, 5);
            caveInstance = 150;
        }

        else if(step == 2) {
            currentStage = "Generating Realm...";
            for(int i = 0; i < worldWidth/chunkSize; i++) {
                for(int j = 0; j < chunkSize; j++) {
                    boolean drawn = false;
                    if(currentElevation < elevation.get(i) + horizon) {
                        if(currentElevation < elevation.get(i) + horizon) {
                            currentElevation += r.nextInt(4);

                            if(currentElevation > elevation.get(i) + horizon) {
                                currentElevation = elevation.get(i) + horizon;
                            }
                            drawn = true;
                            for(int h = currentElevation; h < worldHeight; h++) {
                                if(h < underground) {
                                    pixmap.drawPixel(i * chunkSize + j, h);
                                } else {
                                    int color = caveMap.getPixel(i * chunkSize + j, h);

                                    int red = color >>> 24;
                                    int green = (color & 0xFF0000) >>> 16;
                                    int blue = (color & 0xFF00) >>> 8;
                                    int alpha = color & 0xFF;

                                    if (red < caveInstance && green < caveInstance && blue < caveInstance) {
                                        pixmap.drawPixel(i * chunkSize + j, h);
                                    }
                                }
                            }
                            if(i*chunkSize + j == worldWidth/2) {
                                spawnX = i*chunkSize + j;
                                spawnY = currentElevation - 1;
                            }
                        }
                    } else if(currentElevation > elevation.get(i) + horizon) {
                        if(currentElevation > elevation.get(i) + horizon) {
                            currentElevation -= r.nextInt(4);

                            if(currentElevation < elevation.get(i) + horizon) {
                                currentElevation = elevation.get(i) + horizon;
                            }
                            drawn = true;
                            for(int h = currentElevation; h < worldHeight; h++) {
                                if(h < underground) {
                                    pixmap.drawPixel(i * chunkSize + j, h);
                                } else {
                                    int color = caveMap.getPixel(i * chunkSize + j, h);

                                    int red = color >>> 24;
                                    int green = (color & 0xFF0000) >>> 16;
                                    int blue = (color & 0xFF00) >>> 8;
                                    int alpha = color & 0xFF;

                                    if (red < caveInstance && green < caveInstance && blue < caveInstance) {
                                        pixmap.drawPixel(i * chunkSize + j, h);
                                    }
                                }
                            }
                            if(i*chunkSize + j == worldWidth/2) {
                                spawnX = i*chunkSize + j;
                                spawnY = currentElevation - 1;
                            }
                        }
                    }

                    if(!drawn) {
                        for(int h = currentElevation; h < worldHeight; h++) {
                            if(h < underground) {
                                pixmap.drawPixel(i * chunkSize + j, h);
                            } else {
                                int color = caveMap.getPixel(i * chunkSize + j, h);

                                int red = color >>> 24;
                                int green = (color & 0xFF0000) >>> 16;
                                int blue = (color & 0xFF00) >>> 8;
                                int alpha = color & 0xFF;

                                if (red < caveInstance && green < caveInstance && blue < caveInstance) {
                                    pixmap.drawPixel(i * chunkSize + j, h);
                                }
                            }
                        }

                        if(i*chunkSize + j == worldWidth/2) {
                            spawnX = i*chunkSize + j;
                            spawnY = currentElevation - 1;
                        }
                    }
                }
            }
        }

        else if(step == 3) {
            currentStage = "Finalizing...";
            pixmap.setColor(1f, 1f, 1f, 1f);
            pixmap.drawPixel(spawnX, spawnY);

            FileHandle file = Gdx.files.external("everlongn/worlds/" + name + ".png");
            PixmapIO.writePNG(file, pixmap);

            FileHandle data = Gdx.files.external("everlongn/data/" + name + ".txt");
            data.writeString(name + "\n", false);
            data.writeString(difficulty + "\n", true);
            data.writeString(size + "\n", true);
            data.writeString(seed + "\n", true);
            data.writeString(mode + "\n", true);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            data.writeString(formatter.format(date), true);
            data.writeString("\n", true);
//            data.writeString(spawnX + "\n", true);
//            data.writeString(spawnY + "\n", true);

            pixmap.dispose();
        }
        currentStep++;

        if(currentStep == numSteps) {
            generated = true;
            return;
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        layout.setText(generatingFont, currentStage);
        generatingFont.draw(batch, currentStage,
                ControlCenter.width/2 - layout.width/2, ControlCenter.height/2 - layout.height/2);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
