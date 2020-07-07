package com.everlongn.states;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.assets.UI;
import com.everlongn.entities.Entity;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.staticEntity.Tree;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.popups.IngameOptions;
import com.everlongn.tiles.EarthTile;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.*;
import com.everlongn.utils.frameworks.Telepathy;
import com.everlongn.walls.EarthWall;
import com.everlongn.walls.Wall;
import com.everlongn.world.BackgroundManager;
import com.everlongn.world.WorldContactListener;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.everlongn.utils.Constants.PPM;

public class GameState extends State {
    // screen settings //
    public static OrthographicCamera hud, parallaxBackground;
    public static  float screenTransitionAlpha = 1f;
    public static boolean frameSkip = true;
    ///////////////////

    // world settings //
    public static FileHandle file;
    public static Chunk[][] chunks;
    public static EntityManager entityManager;
    public static World world;
    public static RayHandler rayHandler;
    public static Tile[][] tiles;
    public static Wall[][] walls;
    public static Entity[][] herbs;
    public static PointLight[][] lightmap;
    public static BackgroundManager background;
    public static String mode, name;
    public static boolean[][] occupied;
    public static boolean exiting;
    public static int chunkSize = 20, worldWidth, worldHeight, difficulty;
    ///////////////////

    // UI //
    public static IngameOptions options;
    ///////////////////

    // Player Related Fields //
    public static float spawnX, spawnY;
    public static int xStart, xEnd, yStart, yEnd;

    public static float[] stainAlpha = new float[]{0f, 0f, 0f, 0f, 0f};
    boolean layerFlash;
    public static Inventory inventory;
    public static Telepathy telepathy;
    ///////////////////

    // Cursor Selections //
    public static boolean attackHover, defaultCursor, aiming, charging, itemHover;
    ///////////////////

    // debug //
    public static Box2DDebugRenderer debug;
    public static boolean lightsOn = true;
    ///////////////////

    public GameState(StateManager stateManager) {
        super(stateManager);

        TextManager.bfont = new BitmapFont(Gdx.files.internal("fonts/chalk14.fnt"));

        // reset debug variables
        ControlCenter.DEBUG_RENDER = false;
        ControlCenter.DEBUG = false;
        lightsOn = true;

        inventory = new Inventory(c);
        telepathy = new Telepathy(15, 10, 400, 35);
        background = new BackgroundManager();
        options = new IngameOptions();
        world.setContactListener(new WorldContactListener());
        exiting = false;
    }

    public void tick(float delta) {
        // updating world and transition
        world.step(1/60f, 6, 2);
        if(screenTransitionAlpha > 0) {
            screenTransitionAlpha -= 0.008;
        }

        // updating world related fields
        itemHover = false;
        updateTiles();
        updateWalls();
        rayHandler.update();
        updateStaticEntity();
        entityManager.tick();
        inventory.tick();
        options.tick();
        telepathy.tick();
        updateCursor();
        batch.setProjectionMatrix(camera.combined);
        // Shader.instance.update();
        rayHandler.setCombinedMatrix(camera);
        rayHandler.setBlurNum(3);
        rayHandler.setShadows(true);
        updateStainAlpha();

        // special effects
        if(Player.blink && !Player.blinkAlphaMax) {
            screenTransitionAlpha+=0.2;
            if(screenTransitionAlpha > 1) {
                screenTransitionAlpha = 1;
                Player.blinkAlphaMax = true;
            }
        } else if(Player.blink && Player.blinkAlphaMax) {
            screenTransitionAlpha-=0.2;
            if(screenTransitionAlpha < 0) {
                screenTransitionAlpha = 0;
                Player.blink = false;
                Player.blinkAlphaMax = false;
            }
        }
//        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
//            Gdx.graphics.setWindowedMode(ControlCenter.width,ControlCenter.height);
//        } else if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
//            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
//        }

        // processing world exit
        if(exiting) {
            screenTransitionAlpha += 0.05f;
            if(Sounds.gameAmbient.getVolume() > 0) {
                Sounds.gameAmbient.setVolume(Sounds.gameAmbient.getVolume() - 0.02f);
            } else {
                Sounds.gameAmbient.setVolume(0);
            }
            if(screenTransitionAlpha >= 1f && Sounds.gameAmbient.getVolume() == 0) {
                screenTransitionAlpha = 1f;
                camera.position.set(ControlCenter.width/2, ControlCenter.height/2, 0);
                camera.update();
                WorldSelectionState.transitioning = false;
                WorldSelectionState.transitionAlpha = 0f;
                WorldSelectionState.reversing = false;
                WorldSelectionState.canSwitch = false;
                WorldSelectionState.fadeAlpha = 1f;
                WorldSelectionState.exitFromGame = true;

                Sounds.gameAmbient.stop();

                stateManager.setState(StateManager.CurrentState.WORLD_SELECTION_STATE);

                exiting = false;
            }
        }
    }

    public static void save() {
        Pixmap tilemap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);
        Pixmap wallmap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);

        for(int i = 0; i < worldWidth; i++) {
            for(int j = 0; j < worldHeight; j++) {
                if(tiles[i][GameState.worldHeight - 1 - j] instanceof EarthTile) {
                    tilemap.setColor(1f/255f, 1f/255f, 1f/255f, 1);
                    tilemap.drawPixel(i, j);
                }
                if(walls[i][GameState.worldHeight - 1 - j] instanceof EarthWall) {
                    wallmap.setColor(1f/255f, 1f/255f, 1f/255f, 1);
                    wallmap.drawPixel(i, j);
                }
            }
        }

        FileHandle tileFile = Gdx.files.external("everlongn/realms/tile/" + name + ".png");
        PixmapIO.writePNG(tileFile, tilemap);

        FileHandle wallFile = Gdx.files.external("everlongn/realms/wall/" + name + ".png");
        PixmapIO.writePNG(wallFile, wallmap);

        FileHandle herbsFile = Gdx.files.external("everlongn/realms/herb/" + name + ".txt");
        boolean firstEnter = false;
        for(int i = 0; i < worldWidth; i++) {
            for(int j = 0; j < worldHeight; j++) {
                if(herbs[i][j] instanceof Tree) {
                    Tree temp = (Tree)herbs[i][j];
                    if(!firstEnter) {
                        firstEnter = true;
                        herbsFile.writeString("Tree " + i + " " + (worldHeight - 1 - j) + " " + temp.height + "\n", false);
                    } else {
                        herbsFile.writeString("Tree " + i + " " + (worldHeight - 1 - j) + " " + temp.height + "\n", true);
                    }
                }
            }
        }

        tilemap.dispose();
        wallmap.dispose();

        FileHandle meta = Gdx.files.external("everlongn/meta/" + name + ".txt");
        meta.writeString("1\n", false);
        meta.writeString(EntityManager.player.body.getPosition().x*Tile.TILESIZE + "\n", true);
        meta.writeString(EntityManager.player.body.getPosition().y*Tile.TILESIZE + "\n", true);
        meta.writeString(Math.round(EntityManager.player.maxHealth) + "\n", true);
        meta.writeString(Math.round(EntityManager.player.health) + "\n", true);
        for(int i = 0; i < Inventory.inventory.length; i++) {
            if(Inventory.inventory[i] == null) {
                meta.writeString( "null\n", true);
            } else {
                meta.writeString( Inventory.inventory[i].id + " " + Inventory.inventory[i].count + "\n", true);
            }
        }
        meta.writeString("\n", true);
    }

    public void updateCursor() {
        if(options.active) {
            Tool.changeCursor(0);
        } else if(charging) {
            Tool.changeCursor(1);
        } else if(itemHover) {
            Tool.changeCursor(4);
        } else if(attackHover) {
            Tool.changeCursor(3);
        } else if(aiming) {
            Tool.changeCursor(2);
        } else  {
            Player.forceCharge = 0;
            Tool.changeCursor(0);
        }
    }

    public void updateTiles() {
        // update the tiles within 5 chunk range
        updateChunks();
        // render limits, render tiles that users can see
        xStart = (int) (Math.max(0, (camera.position.x - ControlCenter.width/2) / Tile.TILESIZE));
        // worldWidth is actually worldWidth*TileSize/PPM, but TileSize = PPM so value used is worldWidth
        xEnd = (int) Math.min(worldWidth, (camera.position.x + ControlCenter.width/2) / Tile.TILESIZE + 2);
        yStart = (int) (Math.max(0, (camera.position.y - ControlCenter.height/2) / Tile.TILESIZE));
        yEnd = (int) Math.min(worldHeight, (camera.position.y + ControlCenter.height/2) / Tile.TILESIZE + 2);
    }

    public void updateWalls() {
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if(x < worldWidth && x >= 0 && y < worldHeight && y >= 0 && walls[x][y] != null) {
                    walls[x][y].tick();
                }
            }
        }
    }

    public void updateStaticEntity() {
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if(herbs[x][y] != null) {
                    herbs[x][y].tick();
                }
            }
        }
    }

    public void updateChunks() {
        for(int i = 0; i < chunks.length; i++) {
            for(int j = 0; j < chunks[i].length; j++) {
                if(i >= Player.currentChunkX - 2 && i <= Player.currentChunkX + 2 &&
                        j >= Player.currentChunkY - 2 && j <= Player.currentChunkY + 2) {
                    if(!chunks[i][j].active) {
                        chunks[i][j].active = true;
                        for(int x = i*chunkSize; x < i*chunkSize + chunkSize; x++) {
                            for(int y = j*chunkSize; y < j*chunkSize + chunkSize; y++) {
                                if(tiles[x][y] != null) {
                                    tiles[x][y].checkAdjacent();
                                    tiles[x][y].currentType = 0;
                                    if(tiles[x][y].body == null && tiles[x][y].numAdjacent != 4) {
                                        tiles[x][y].body = Tool.createTile(x * Tile.TILESIZE - Tile.TILESIZE / 2, y * Tile.TILESIZE - Tile.TILESIZE / 2, 4, true, true, true, true,
                                                Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, tiles[x][y]);
                                        tiles[x][y].currentType = 1;
                                    }
                                    tiles[x][y].tick();
                                }
                            }
                        }
                    }

                } else {
                    if(chunks[i][j].active) {
                        chunks[i][j].active = false;
                        for(int x = i*chunkSize; x < i*chunkSize + chunkSize; x++) {
                            for(int y = j*chunkSize; y < j*chunkSize + chunkSize; y++) {
                                if(tiles[x][y] != null) {
                                    if(tiles[x][y].body != null) {
                                        world.destroyBody(tiles[x][y].getBody());
                                        tiles[x][y].body = null;
                                    }
                                } else {
                                    if(lightmap[x][y] != null) {
                                        lightmap[x][y].remove();
                                        lightmap[x][y] = null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateStainAlpha() {
        if(EntityManager.player.getHealthPercentage() <= 0.7f) {
            if(stainAlpha[0] > 0.95) {
                layerFlash = false;
            } else if(stainAlpha[0] < 0.55) {
                layerFlash = true;
            }
            if(!layerFlash) {
                stainAlpha[0]-=0.01;
            } else {
                stainAlpha[0]+=0.01;
            }
        } else if(EntityManager.player.getHealthPercentage() <= 0.85f) {
            if(stainAlpha[0] > (1-(EntityManager.player.getHealthPercentage() + 0.15f))*5f) {
                layerFlash = false;
            } else if(stainAlpha[0] < 0) {
                layerFlash = true;
            }
            if(!layerFlash) {
                stainAlpha[0]-=0.005;
            } else {
                stainAlpha[0]+=0.005;
            }
        } else {
            stainAlpha[0]-=0.005;
            if(stainAlpha[0] < 0)
                stainAlpha[0] = 0;
        }

        if(EntityManager.player.getHealthPercentage() <= 0.65f) {
            if(stainAlpha[1] > (1-(EntityManager.player.getHealthPercentage() + 0.35f))*2.5f) {
                stainAlpha[1]-=0.015;
            } else if(stainAlpha[1] < (1-(EntityManager.player.getHealthPercentage() + 0.35f))*2.5f) {
                stainAlpha[1]+=0.015;
            }
            if(stainAlpha[4] > (1-(EntityManager.player.getHealthPercentage() + 0.35f))*2f) {
                stainAlpha[4]-=0.01;
            } else if(stainAlpha[4] < (1-(EntityManager.player.getHealthPercentage() + 0.35f))*2f) {
                stainAlpha[4]+=0.01;
            }
        } else {
            if(stainAlpha[1] > (1-(EntityManager.player.getHealthPercentage() + 0.35f))*2.5f) {
                stainAlpha[1]-=0.015;
            }
            if(stainAlpha[4] > (1-(EntityManager.player.getHealthPercentage() + 0.35f))*2f) {
                stainAlpha[4]-=0.01;
            }
        }
        if(stainAlpha[1] > 1) {
            stainAlpha[1] = 1;
        } else if(stainAlpha[1] < 0) {
            stainAlpha[1] = 0;
        }
        if(stainAlpha[4] > 1) {
            stainAlpha[4] = 1;
        } else if(stainAlpha[4] < 0) {
            stainAlpha[4] = 0;
        }

        if(EntityManager.player.getHealthPercentage() <= 0.5f) {
            if(stainAlpha[2] > (1-(EntityManager.player.getHealthPercentage() + 0.5f))*5f) {
                stainAlpha[2]-=0.0125;
            } else if(stainAlpha[2] < (1-(EntityManager.player.getHealthPercentage() + 0.5f))*5f) {
                stainAlpha[2]+=0.0125;
            }
        } else {
            if(stainAlpha[2] > (1-(EntityManager.player.getHealthPercentage() + 0.5f))*5f) {
                stainAlpha[2]-=0.0125;
            }
        }
        if(stainAlpha[2] > 1) {
            stainAlpha[2] = 1;
        } else if(stainAlpha[0] < 0) {
            stainAlpha[2] = 0;
        }

        if(EntityManager.player.getHealthPercentage() <= 0.35f) {
            if(stainAlpha[3] > (1-(EntityManager.player.getHealthPercentage() + 0.65f))*5f) {
                stainAlpha[3]-=0.0125;
            } else if(stainAlpha[3] < (1-(EntityManager.player.getHealthPercentage() + 0.65f))*5f) {
                stainAlpha[3]+=0.0125;
            }
        } else {
            if(stainAlpha[3] > (1-(EntityManager.player.getHealthPercentage() + 0.65f))*5f) {
                stainAlpha[3]-=0.0125;
            }
        }
        if(stainAlpha[3] > 1) {
            stainAlpha[3] = 1;
        } else if(stainAlpha[0] < 0) {
            stainAlpha[3] = 0;
        }
    }

    public void render() {
        Gdx.gl.glClearColor(.75f, .75f, .76f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(parallaxBackground.combined);
        background.render(batch);

        batch.setProjectionMatrix(ControlCenter.camera.combined);

        renderWalls(batch);
        renderStaticEntity(batch);
        entityManager.render(batch);
        renderTiles(batch);

        if(lightsOn)
            rayHandler.render();

        batch.setProjectionMatrix(hud.combined);
        renderStain(batch);
        inventory.render(batch);
        options.render(batch);

        batch.begin();

        if(ControlCenter.DEBUG) {
            TextManager.analogDraw("Ticks: " + Math.round(ControlCenter.delta*1000) + "ms   FPS: " + Gdx.graphics.getFramesPerSecond(),
                    ControlCenter.width - 15, 20, Color.WHITE);
            TextManager.analogDraw("World Size: " + GameState.worldWidth + " by " + GameState.worldHeight,
                    ControlCenter.width - 15, 40, Color.WHITE);
            String diff = "";
            if(GameState.difficulty == 0) {
                diff = "Standard";
            } else if(GameState.difficulty == 1) {
                diff = "Intense";
            } else if(GameState.difficulty == 2) {
                diff = "Insane";
            }
            TextManager.analogDraw("Difficulty: " + diff + "   Mode: " + GameState.mode,
                    ControlCenter.width - 15, 60, Color.WHITE);
            TextManager.analogDraw("Velocity X: " + Math.round(EntityManager.player.getBody().getLinearVelocity().x) + "   " +
                            "Velocity Y: " + Math.round(EntityManager.player.getBody().getLinearVelocity().y),
                    ControlCenter.width - 15, 80, Color.WHITE);
            TextManager.analogDraw("Mouse Location: " + Gdx.input.getX() + ", " + Gdx.input.getY(),
                    ControlCenter.width - 15, 100, Color.WHITE);
            TextManager.analogDraw("Chunk Location: " + Player.currentChunkX + ", " + Player.currentChunkY,
                    ControlCenter.width - 15, 120, Color.WHITE);
            TextManager.analogDraw("Relative Location: " + Player.currentTileX + ", " + Player.currentTileY,
                    ControlCenter.width - 15, 140, Color.WHITE);
            TextManager.analogDraw("Jump: " + EntityManager.player.jump + "   Fall: " + EntityManager.player.fall + "   Can Jump: " + EntityManager.player.canJump,
                    ControlCenter.width - 15, 160, Color.WHITE);
            TextManager.analogDraw("Angle Between Mouse: " + Player.aimAngle,
                    ControlCenter.width - 15, 180, Color.WHITE);
            TextManager.analogDraw("Halt Attack: " + Player.halt + "   HaltForce: " + Player.haltForce*50,
                    ControlCenter.width - 15, 200, Color.WHITE);
            TextManager.analogDraw("Player Health: " + Math.round(EntityManager.player.health) + "   Percentage: " + Math.round(EntityManager.player.getHealthPercentage()*100) + "%",
                    ControlCenter.width - 15, 220, Color.WHITE);
        }

        telepathy.render(batch);

        if(Player.forceCharge > 0) {
            batch.draw(UI.chargeCursor,Gdx.input.getX() - 38 + 16,  ControlCenter.height-Gdx.input.getY() - 38 - 16, 76, 76);
            batch.draw(UI.chargeOrb,Gdx.input.getX() - Player.forceCharge/Player.forceMax*32/2 + 16,  ControlCenter.height-Gdx.input.getY() - Player.forceCharge/Player.forceMax*32/2 + 1 - 16, Player.forceCharge/Player.forceMax*32, Player.forceCharge/Player.forceMax*32);
            Tool.changeCursor(1);
        }

        if(screenTransitionAlpha > 0) {
            batch.setColor(0f, 0f, 0f, screenTransitionAlpha);
            batch.draw(Tiles.blackTile, 0, 0, ControlCenter.width, ControlCenter.height);
            batch.setColor(1, 1, 1, 1);
        }
        batch.end();

        if(ControlCenter.DEBUG_RENDER) {
            debug.render(world, camera.combined.scl(PPM));
        }
    }

    public void renderStain(SpriteBatch batch) {
        batch.begin();
        if(stainAlpha[0] > 0) {
            batch.setColor(1f, 1f, 1f, stainAlpha[0]);
            batch.draw(UI.stain1, 0, 0, ControlCenter.width, ControlCenter.height);
        }
        if(stainAlpha[1] > 0) {
            batch.setColor(1f, 1f, 1f, stainAlpha[1]);
            batch.draw(UI.stain2, 0, 0, ControlCenter.width, ControlCenter.height);
        }
        if(stainAlpha[2] > 0) {
            batch.setColor(1f, 1f, 1f, stainAlpha[2]);
            batch.draw(UI.stain3, 0, 0, ControlCenter.width, ControlCenter.height);
        }
        if(stainAlpha[3] > 0) {
            batch.setColor(1f, 1f, 1f, stainAlpha[3]);
            batch.draw(UI.stain4, 0, 0, ControlCenter.width, ControlCenter.height);
        }
        if(stainAlpha[4] > 0) {
            batch.setColor(1f, 1f, 1f, stainAlpha[4]);
            batch.draw(UI.stain5, 0, 0, ControlCenter.width, ControlCenter.height);
        }
        batch.setColor(1, 1, 1, 1);
        batch.end();
    }

    public void renderTiles(SpriteBatch batch) {
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if(x < worldWidth && x >= 0 && y < worldHeight && y >= 0 && tiles[x][y] != null) {
                    tiles[x][y].render(batch);
                }

                if (lightmap[x][y] == null && tiles[x][y] == null) {
                    if (walls[x][y] == null) {
                        lightmap[x][y] = new PointLight(rayHandler, 100, Color.BLACK, 100, x * Tile.TILESIZE, y * Tile.TILESIZE);
                    } else if(walls[x][y].numAdjacent != 4) {
                        lightmap[x][y] = new PointLight(rayHandler, 100, Color.BLACK, 100, x * Tile.TILESIZE, y * Tile.TILESIZE);
                    }
                }
            }
        }
    }

    public void renderStaticEntity(SpriteBatch batch) {
        for(int i = 0; i < chunks.length; i++) {
            for (int j = 0; j < chunks[i].length; j++) {
                if (i >= Player.currentChunkX - 2 && i <= Player.currentChunkX + 2 &&
                        j >= Player.currentChunkY - 2 && j <= Player.currentChunkY + 2) {
                    if (chunks[i][j].active) {
                        for (int x = i * chunkSize; x < i * chunkSize + chunkSize; x++) {
                            for (int y = j * chunkSize; y < j * chunkSize + chunkSize; y++) {
                                if(herbs[x][y] != null) {
                                    herbs[x][y].render(batch);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void renderWalls(SpriteBatch batch) {
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if(x < worldWidth && x >= 0 && y < worldHeight && y >= 0 && walls[x][y] != null) {
                    walls[x][y].render(batch);
                }
            }
        }
    }

    public void dispose() {
        rayHandler.dispose();
        debug.dispose();
        world.dispose();
        //batch.dispose();
    }
}