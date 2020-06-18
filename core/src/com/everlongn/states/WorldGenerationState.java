package com.everlongn.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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

    private Pixmap caveMap, tileMap, wallMap, herbsMap;
    private ArrayList<Integer> elevation;
    private ArrayList<String> biomes;
    private ArrayList<Integer> vegetation;

    private int currentElevation, caveInstance, previousTreeX;
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
            if(count >= 0.5) {
                stateManager.setState(StateManager.CurrentState.WORLD_SELECTION_STATE);
            }
        }
    }

    public void generate(int step) {
        if(step == 1) {
            currentStage = "Setting Up...";
            r.setSeed(seed);

            elevation = new ArrayList<>();
            biomes = new ArrayList<>();
            vegetation = new ArrayList<>();

            if(size.equals("Small")) {
                worldWidth = 1500;
                worldHeight = 400;
                chunkSize = 50;
            } else if(size.equals("Medium")) {
                worldWidth = 2100;
                worldHeight = 600;
                chunkSize = 75;
            } else if(size.equals("Large")) {
                worldWidth = 3000;
                worldHeight = 600;
                chunkSize = 100;
            }

            horizon = worldHeight/4;
            underground = horizon - 25;
            currentElevation = horizon;

            for(int i = 0; i < worldWidth/chunkSize; i++) {
                biomes.add("plain");
                elevation.add(4 - r.nextInt(9));
                vegetation.add(r.nextInt(10));
            }

            int numMountains = r.nextInt(5) + 3;
            for(int i = 0; i < numMountains; i++) {
                int index = r.nextInt(biomes.size());
                while(biomes.get(index).equals("mountain") || index == 0 || index == biomes.size()-1) {
                    index = r.nextInt(biomes.size());
                }
                biomes.set(index, "mountain");
                elevation.set(index, -(r.nextInt(25) + 15));
                vegetation.set(index, r.nextInt(6));
            }

            tileMap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);
            tileMap.setColor(1f/255f, 1f/255f, 1f/255f, 1);

            wallMap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);
            wallMap.setColor(1f/255f, 1f/255f, 1f/255f, 1);

            herbsMap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);

            // perlin noise generation
            caveMap = PerlinNoiseGenerator.generatePixmap(worldWidth, worldHeight, 0, 200, 5);
            caveInstance = 150;
        }

        else if(step == 2) {
            currentStage = "Generating Terrain...";
            for(int i = 0; i < worldWidth/chunkSize; i++) {
                if(biomes.get(i).equals("mountain")) {
                    int transition = 4;
                    int adjusting = 0;
                    int peekIndex = 0;
                    int[] heights = new int[chunkSize];
                    boolean isAdjusting = false;
                    boolean reachedTop = false;
                    for(int j = 0; j < chunkSize; j++) {
                        boolean drawn = false;
                        if(transition > 0) {
                            if(!reachedTop) {
                                transition--;
                                // generating upwards
                                if(r.nextInt(3) == 0)
                                    currentElevation--;
                            }
                        } else {
                            if(adjusting > 0) {
                                if (!reachedTop) {
                                    adjusting--;
                                    if(r.nextInt(3) == 0)
                                        currentElevation--;
                                }
                            } else {
                                if (!reachedTop) {
                                    int increment = r.nextInt(3) + 1;
                                    if(r.nextInt(3) >= 0)
                                        currentElevation -= increment;
                                }
                            }
                        }
                        if(!reachedTop) {
                            heights[j] = currentElevation;
                        } else {
                            if(j >= chunkSize - peekIndex) {
                                currentElevation = heights[chunkSize - j - 1];
                            }
                        }

                        if(!reachedTop && !isAdjusting && elevation.get(i) + horizon + 6 >= currentElevation) {
                            adjusting = 100;
                            isAdjusting = true;
                        }

                        if(currentElevation <= elevation.get(i) + horizon && !reachedTop) {
                            currentElevation = elevation.get(i) + horizon;
                            reachedTop = true;
                            peekIndex = j;
                        }

                        finishTerrainShape(i, j);
                    }
                } else {
                    for(int j = 0; j < chunkSize; j++) {
                        if (currentElevation < elevation.get(i) + horizon) {
                            if (r.nextInt(4) == 1) {
                                currentElevation++;
                            }
                        } else if (currentElevation > elevation.get(i) + horizon) {
                            if (r.nextInt(4) == 1) {
                                currentElevation--;
                            }
                        }

                        finishTerrainShape(i, j);
                    }
                }
            }
        }
        else if(step == 3) {
            currentStage = "Generating Caves...";
            ArrayList<Integer> caveIndex = new ArrayList<Integer>();
            for(int i = 2; i < biomes.size() - 2; i++) {
                if(biomes.get(i).equals("mountain")) {
                    if(r.nextInt(4) == 0) {
                        caveIndex.add(i);
                    }
                }
            }

            while(caveIndex.size() < 3) {
                for(int i = 2; i < biomes.size() - 2; i++) {
                    if(biomes.get(i).equals("mountain") && !caveIndex.contains(i)) {
                        if(r.nextInt(4) == 0) {
                            caveIndex.add(i);
                        }
                    }
                }
            }

            for(int i = 0; i < caveIndex.size(); i++) {
                int direction = r.nextInt(2);
                digMainCave(caveIndex.get(i), direction);
            }
        }
        else if(step == 4) {
            currentStage = "Finalizing...";
            tileMap.setColor(1f, 1f, 1f, 1f);
            tileMap.drawPixel(spawnX, spawnY);

            FileHandle file = Gdx.files.external("everlongn/realms/" + name + "/tile.png");
            PixmapIO.writePNG(file, tileMap);

            FileHandle wallFile = Gdx.files.external("everlongn/realms/" + name + "/wall.png");
            PixmapIO.writePNG(wallFile, wallMap);

            FileHandle herbsFile = Gdx.files.external("everlongn/realms/" + name + "/herbs.png");
            PixmapIO.writePNG(herbsFile, herbsMap);

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

//            FileHandle herb = Gdx.files.external("everlongn/herbs/" + name + ".txt");
//            for(int i = 0; i < herbs.size(); i++) {
//                herb.writeString(herbs.get(i).name + " " + herbs.get(i).x + " " + herbs.get(i).y, true);
//            }

            tileMap.dispose();
            wallMap.dispose();
            herbsMap.dispose();
        }
        currentStep++;

        if(currentStep == numSteps) {
            generated = true;
            return;
        }
    }

    public void digMainCave(int startChunk, int direction) {
        tileMap.setColor(1f, 0, 0, 1f);
        int xStart = 0;
        if(direction == 0) {
            xStart = startChunk*chunkSize;
        } else {
            xStart = (startChunk + 1) * chunkSize;
        }
        int yStart = horizon - 2 - r.nextInt(8);
        int yEnd = yStart - (r.nextInt(3) + 4);

        int level1 = worldHeight/4*2;
        int level2 = worldHeight/4*2;
        int level3 = worldHeight/4*3;

        while(yStart < worldHeight-1 && xStart < worldWidth-1 && yStart > 1 && xStart > 1) {
            if(yStart < horizon + 30) {
                int limit = 6;
                int low = 3;
                for(int i = 0; i < yStart - yEnd; i++) {
                    tileMap.drawPixel(xStart, yStart-i);
                }
                if(r.nextInt(2) == 0) {
                    int random = r.nextInt(4);
                    yStart += random;
                    yEnd += random;
                    if(yStart - yEnd > limit) {
                        yEnd = yStart - limit;
                    } else if(yStart - yEnd < low) {
                        yEnd = yStart - low;
                    }
                }
                if(direction == 0) {
                    xStart++;
                } else {
                    xStart--;
                }
            }
            else {
                int limit = 20;
                if(yStart > level3) {
                    limit = 25;
                } else if(yStart > level2) {
                    limit = 20;
                }
                int low = 8;
                for(int i = 0; i < yStart - yEnd; i++) {
                    tileMap.drawPixel(xStart, yStart-i);
                }
                if(yStart < level1) {
                    if(r.nextInt(250) == 0) {
                        digSubCave(xStart, yStart, r.nextInt(2), r.nextInt(600) + 50);
                    }
                }
                if(yStart < level2) {
                    yStart += r.nextInt(4);
                    yEnd += r.nextInt(4);
                    if (yStart - yEnd > limit) {
                        yEnd = yStart - limit;
                    } else if (yStart - yEnd < low) {
                        yEnd = yStart - low;
                    }
                    if(r.nextInt(300) == 0) {
                        digSubCave(xStart, yStart, direction, r.nextInt(500) + 50);
                    }
                } else if(yStart < level3) {
                    if (r.nextInt(5) == 0) {
                        yStart += r.nextInt(4);
                        yEnd += r.nextInt(4);
                        if (yStart - yEnd > limit) {
                            yEnd = yStart - limit;
                        } else if (yStart - yEnd < low) {
                            yEnd = yStart - low;
                        }
                    }
                    if(r.nextInt(450) == 0) {
                        digSubCave(xStart, yStart, direction, r.nextInt(400) + 50);
                    }
                } else {
                    if (r.nextInt(8) == 0) {
                        yStart += r.nextInt(4);
                        yEnd += r.nextInt(4);
                        if (yStart - yEnd > limit) {
                            yEnd = yStart - limit;
                        } else if (yStart - yEnd < low) {
                            yEnd = yStart - low;
                        }
                    }
                }
                if(direction == 0) {
                    xStart++;
                } else {
                    xStart--;
                }
                if(r.nextInt(30) == 0) {
                    if(direction == 0) {
                        direction = 1;
                    } else {
                        direction = 0;
                    }
                }
            }
        }
    }

    public void digSubCave(int xStart, int yStart, int direction, int numBlocks) {
        int limit = 10;
        int low = 4;

        int yEnd = yStart - (r.nextInt(3) + 4);
        while(yStart < worldHeight-1 && xStart < worldWidth-1 && yStart > 1 && xStart > 1 && numBlocks > 0) {
            numBlocks--;

            if (r.nextInt(6) == 0) {
                yStart += r.nextInt(3);
                yEnd += r.nextInt(4);
                if (yStart - yEnd > limit) {
                    yEnd = yStart - limit;
                } else if (yStart - yEnd < low) {
                    yEnd = yStart - low;
                }
            }

            for (int i = 0; i < yStart - yEnd; i++) {
                tileMap.drawPixel(xStart, yStart - i);
            }

            if (direction == 0) {
                xStart++;
            } else {
                xStart--;
            }

            if (r.nextInt(200) == 0) {
                if (direction == 0) {
                    direction = 1;
                } else {
                    direction = 0;
                }
            }
            if (r.nextInt(600) == 0) {
                digSubCave(xStart, yStart, r.nextInt(2), r.nextInt(400) + 50);
            }
        }
    }

    public void finishTerrainShape(int i, int j) {
        for(int h = currentElevation; h < worldHeight; h++) {
            tileMap.drawPixel(i * chunkSize + j, h);
//                int red = color >>> 24;
//                int green = (color & 0xFF0000) >>> 16;
//                int blue = (color & 0xFF00) >>> 8;
//                int alpha = color & 0xFF;
        }

        // generating trees
        herbsMap.setColor(new Color(1, 0, 0, 1));
        if(vegetation.get(i) == 0) {
            // nothing happens
        } else if(vegetation.get(i) < 4) {
            int treeSize = r.nextInt(3) + 9;
            if(r.nextInt(15) < 1 && currentElevation-treeSize > 0 && previousTreeX != i * chunkSize + j-1) {
                herbsMap.drawLine(i * chunkSize + j, currentElevation, i * chunkSize + j, currentElevation-treeSize);
                herbsMap.setColor(new Color(130f/255f, 64f/255f, 12f/255f, 1));
                herbsMap.drawPixel(i * chunkSize + j, currentElevation);
                previousTreeX = i * chunkSize + j;
            }
        } else if(vegetation.get(i) <= 7) {
            int treeSize = r.nextInt(3) + 9;
            if(r.nextInt(10) < 2 && currentElevation-treeSize > 0 && previousTreeX != i * chunkSize + j-1) {
                herbsMap.drawLine(i * chunkSize + j, currentElevation, i * chunkSize + j, currentElevation-treeSize);
                herbsMap.setColor(new Color(130f/255f, 64f/255f, 12f/255f, 1));
                herbsMap.drawPixel(i * chunkSize + j, currentElevation);
                previousTreeX = i * chunkSize + j;
            }
        } else if(vegetation.get(i) == 8) {
            int treeSize = r.nextInt(3) + 9;
            if(r.nextInt(10) < 4 && currentElevation-treeSize > 0 && previousTreeX != i * chunkSize + j-1) {
                herbsMap.drawLine(i * chunkSize + j, currentElevation, i * chunkSize + j, currentElevation-treeSize);
                herbsMap.setColor(new Color(130f/255f, 64f/255f, 12f/255f, 1));
                herbsMap.drawPixel(i * chunkSize + j, currentElevation);
                previousTreeX = i * chunkSize + j;
            }
        } else if(vegetation.get(i) == 9) {
            int treeSize = r.nextInt(3) + 9;
            if(r.nextInt(10) < 6 && currentElevation-treeSize > 0 && previousTreeX != i * chunkSize + j-1) {
                herbsMap.drawLine(i * chunkSize + j, currentElevation, i * chunkSize + j, currentElevation-treeSize);
                herbsMap.setColor(new Color(130f/255f, 64f/255f, 12f/255f, 1));
                herbsMap.drawPixel(i * chunkSize + j, currentElevation);
                previousTreeX = i * chunkSize + j;
            }
        }

        for(int h = currentElevation + 2; h < worldHeight; h++) {
            wallMap.drawPixel(i * chunkSize + j, h);
        }
        if(i*chunkSize + j == worldWidth/2) {
            spawnX = i*chunkSize + j;
            spawnY = currentElevation - 1;
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