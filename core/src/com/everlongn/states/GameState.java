package com.everlongn.states;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.dream.Scavenger;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.tiles.EarthTile;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.*;
import com.everlongn.world.BackgroundManager;
import com.everlongn.world.WorldContactListener;

import static com.everlongn.utils.Constants.PPM;

public class GameState extends State {

    public static Box2DDebugRenderer debug;
    public static EntityManager entityManager;
    public static Tile[][] tiles;

    public static int spawnX, spawnY, xStart, xEnd, yStart, yEnd;
    public static int worldWidth, worldHeight;

    public static World world;
    public static RayHandler rayHandler;

    public static boolean frameSkip = true;
    public static OrthographicCamera hud, parallexBackground;

    public static Inventory inventory;
    public static BackgroundManager background;

    private float screenTransitionAlpha = 1f;

    // world settings//
    public static FileHandle file;
    public static int chunkSize = 25;
    public static Chunk[][] chunks;
    ///////////////////

    public GameState(StateManager stateManager) {
        super(stateManager);

        TextManager.bfont = new BitmapFont(Gdx.files.internal("fonts/chalk14.fnt"));

        rayHandler.setAmbientLight(.2f);

        inventory = new Inventory(c);
        background = new BackgroundManager();
        world.setContactListener(new WorldContactListener());
    }

    public void tick(float delta) {
        world.step(1/60f, 6, 2);
        if(screenTransitionAlpha > 0) {
            screenTransitionAlpha -= 0.008;
        }
        updateTiles();
        inventory.tick();
        rayHandler.update();
        entityManager.tick();
        batch.setProjectionMatrix(camera.combined);
        rayHandler.setCombinedMatrix(camera);
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
                                    tiles[x][y].setBody(Tool.createBox(x * Tile.TILESIZE, y * Tile.TILESIZE, Tile.TILESIZE,
                                            Tile.TILESIZE, true, 1f,
                                            Constants.BIT_TILE, (short)(Constants.BIT_PLAYER | Constants.BIT_ENEMY | Constants.BIT_PARTICLE | Constants.BIT_PROJECTILE), (short)0, this.getClass()));
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
                                    world.destroyBody(tiles[x][y].getBody());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void render() {
        Gdx.gl.glClearColor(.82f, .82f, .83f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //rayHandler.updateAndRender();
        batch.setProjectionMatrix(parallexBackground.combined);
        background.render(batch);

        batch.setProjectionMatrix(ControlCenter.camera.combined);
        entityManager.render(batch);

        renderTiles(batch);

        batch.setProjectionMatrix(hud.combined);
        inventory.render(batch);
        batch.begin();
        if(ControlCenter.DEBUG) {
            TextManager.draw("FPS: " + Gdx.graphics.getFramesPerSecond(),
                    20, 40, Color.WHITE, 1, false);
            TextManager.draw("Inventory Open: " + Inventory.extended,
                    20, 60, Color.WHITE, 1, false);
            TextManager.draw("Velocity X: " + EntityManager.player.getBody().getLinearVelocity().x + "   " +
                            "Velocity Y: " + EntityManager.player.getBody().getLinearVelocity().y,
                    20, 80, Color.WHITE, 1, false);
            TextManager.draw("Mouse Location: " + Gdx.input.getX() + ", " + Gdx.input.getY(),
                    20, 100, Color.WHITE, 1, false);
            TextManager.draw("Chunk Location: " + Player.currentChunkX + ", " + Player.currentChunkY,
                    20, 120, Color.WHITE, 1, false);
            TextManager.draw("Jump: " + Player.jump + "   Fall: " + Player.fall,
                    20, 140, Color.WHITE, 1, false);
            TextManager.draw("Aim: " + Player.aimAngle + "   Melee Attack: " + Player.meleeAttack,
                    20, 160, Color.WHITE, 1, false);
            TextManager.draw("Halt: " + Player.halt + "   HaltForce: " + Player.haltForce*50,
                    20, 180, Color.WHITE, 1, false);
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

    public void renderTiles(SpriteBatch batch) {
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if(x < worldWidth && x >= 0 && y < worldHeight && y >= 0 && tiles[x][y] != null)
                    tiles[x][y].render(batch);
            }
        }
    }

    public void dispose() {
        rayHandler.dispose();
        world.dispose();
        batch.dispose();
    }
}