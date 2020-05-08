package com.everlongn.states;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.tiles.EarthTile;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.PerlinNoiseGenerator;
import com.everlongn.utils.TextManager;
import com.everlongn.utils.Tool;
import com.everlongn.world.BackgroundManager;

import java.util.ArrayList;
import java.util.Random;

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

    public static Random r = new Random(1002);

    public GameState(StateManager stateManager) {
        super(stateManager);
        //world = new WorldGenerator(c, "core//res//worlds//tempWorld.png");
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        hud = new OrthographicCamera();
        hud.setToOrtho(false, width/ControlCenter.SCALE, height/ControlCenter.SCALE);
        parallexBackground = new OrthographicCamera();
        parallexBackground.setToOrtho(false, width/ControlCenter.SCALE, height/ControlCenter.SCALE);

        // vector 2 takes a x and y gravity value
        world = new World(new Vector2(0, -20f), false);
        rayHandler = new RayHandler(world);
        debug = new Box2DDebugRenderer();

        loadWorld();

        loadWorld(Gdx.files.external("everlongn/worlds/tempWorldddd.png"));
        createPlayer();
        rayHandler.setAmbientLight(.2f);

        inventory = new Inventory(c);
        background = new BackgroundManager();

    }

    public void loadWorld() {

        ArrayList<Integer> elevation = new ArrayList<Integer>();

        // set the horizon
        int horizon = 150;
        int underground = 175;
        int currentElevation = horizon;

        int chunkSize = 25;
        int worldWidth = 1000;
        int worldHeight = 500;

        for(int i = 0; i < worldWidth/chunkSize; i++) {
            elevation.add(r.nextInt(38) - 20);
        }

        Pixmap pixmap = new Pixmap(worldWidth, worldHeight, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f/255f, 1f/255f, 1f/255f, 1);

        // perlin noise generation
        Pixmap caveMap = PerlinNoiseGenerator.generatePixmap(worldWidth, worldHeight, 0, 200, 5);
        int caveInstance = 150;

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
                            spawnX = i*chunkSize + j - 1;
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
                            spawnX = i*chunkSize + j - 1;
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
                    //drawTile(currentElevation, caveMap, pixmap, i * chunkSize + j, caveInstance);
                    if(i*chunkSize + j == worldWidth/2) {
                        spawnX = i*chunkSize + j - 1;
                        spawnY = currentElevation - 1;
                    }
                }
            }
        }

        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.drawPixel(spawnX, spawnY);

        FileHandle file = Gdx.files.external("everlongn/worlds/tempWorldddd.png");
        PixmapIO.writePNG(file, pixmap);
        FileHandle file2 = Gdx.files.external("everlongn/worlds/perlin.png");
        PixmapIO.writePNG(file2, caveMap);
        pixmap.dispose();
    }

    public void drawTile(int currentElevation, Pixmap caveMap, Pixmap pixmap, int x, int caveInstance) {
        for(int h = currentElevation; h < worldHeight; h++) {
            int color = caveMap.getPixel(x, h);

            int red = color >>> 24;
            int green = (color & 0xFF0000) >>> 16;
            int blue = (color & 0xFF00) >>> 8;
            int alpha = color & 0xFF;

            if(red < caveInstance && green < caveInstance && blue < caveInstance) {
                pixmap.drawPixel(x, h);
            }
        }
    }
//                    for(int h = currentElevation; h < worldHeight; h++) {
//                        int color = caveMap.getPixel(i*chunkSize + j, h);
//                        String hexColor = Integer.toHexString(color);
//                        int red = color >>> 24;
//                        int green = (color & 0xFF0000) >>> 16;
//                        int blue = (color & 0xFF00) >>> 8;
//                        int alpha = color & 0xFF;
//
//                        if(red < caveInstance && green < caveInstance && blue < caveInstance) {
//                            pixmap.drawPixel(i * chunkSize + j, h);
//                        }
//                    }

    public void createPlayer() {
        entityManager = new EntityManager(c, new Player(c, spawnX*Tile.TILESIZE, spawnY*Tile.TILESIZE, 18, 60, 2.5f));

        Vector3 position = ControlCenter.camera.position;
        position.x = spawnX*Tile.TILESIZE;
        position.y = spawnY*Tile.TILESIZE;
        parallexBackground.position.set(position);

        for(int i = 0; i < BackgroundManager.layers.length; i++) {
            BackgroundManager.layers[i] = new Vector2();
            BackgroundManager.layers[i].x = 1280/2 - 25*Tile.TILESIZE;
            BackgroundManager.layers[i].y = 800/2 - 25*Tile.TILESIZE + 120;
        }

        ControlCenter.camera.position.x = spawnX*Tile.TILESIZE; //getting back to scale by *PPM
        ControlCenter.camera.position.y = spawnY*Tile.TILESIZE + 200;
        ControlCenter.camera.update();//397 × 581
    }

    public void loadWorld(FileHandle file) {
        Pixmap pixmap = new Pixmap(file);

        worldWidth = pixmap.getWidth();
        worldHeight = pixmap.getHeight();

        tiles = new Tile[worldWidth][worldHeight];

        for(int y=0; y < worldHeight; y++){
            for(int x=0; x < worldWidth; x++){
                int color = pixmap.getPixel(x, y);
                String hexColor = Integer.toHexString(color);
                int red = color >>> 24;
                int green = (color & 0xFF0000) >>> 16;
                int blue = (color & 0xFF00) >>> 8;
                int alpha = color & 0xFF;

                if(red == 1 && green == 1 && blue == 1) {
                    tiles[x][worldHeight - 1 - y] = new EarthTile(x, worldHeight - 1 - y);
                }
                else if(red == 255 && green == 255 && blue == 255) {
                    spawnX = x;
                    spawnY = worldHeight - 1 - y;
                }
            }
        }
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
        rayHandler.setCombinedMatrix(camera.combined.cpy().scl(PPM));
    }

    public void updateTiles() {
        // tick limits, update tiles that users can see
        xStart = (int) (Math.max(0, (camera.position.x - ControlCenter.width/2) / Tile.TILESIZE));
        xEnd = (int) Math.min(ControlCenter.width, (camera.position.x + ControlCenter.width/2) / Tile.TILESIZE + 2);
        yStart = (int) (Math.max(0, (camera.position.y - ControlCenter.height/2) / Tile.TILESIZE));
        yEnd = (int) Math.min(ControlCenter.height, (camera.position.y + ControlCenter.height/2) / Tile.TILESIZE + 2);

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                if(x < worldWidth && x >= 0 && y < worldHeight && y >= 0 && tiles[x][y] != null) {
                    if(tiles[x][y].getBody() == null) {
                        tiles[x][y].setBody(Tool.createBox(x*Tile.TILESIZE, y*Tile.TILESIZE,
                                Tile.TILESIZE, Tile.TILESIZE, true, 1f));
                    }
                    tiles[x][y].tick();
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
