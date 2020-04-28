package com.everlongn.world;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.tiles.AirTile;
import com.everlongn.tiles.DirtTile;
import com.everlongn.tiles.Tile;

import static com.everlongn.utils.Constants.PPM;

public class WorldGenerator {
    private ControlCenter c;
    public static EntityManager entityManager;
    public static Tile[][] tiles;

    public static int spawnX, spawnY;
    public static int worldWidth, worldHeight;

    public static Box2DDebugRenderer debug;
    public static World world;
    public static RayHandler rayHandler;

    public WorldGenerator(ControlCenter c, String path) { //add string path when loading a world
        this.c = c;

        // vector 2 takes a x and y gravity value
        world = new World(new Vector2(0, -9.81f), true);
        rayHandler = new RayHandler(world);
        debug = new Box2DDebugRenderer();
        loadWorld(path);
        createPlayer();
        rayHandler.setAmbientLight(.1f);
    }

    public void createPlayer() {
        entityManager = new EntityManager(c, new Player(c, spawnX*Tile.TILESIZE, spawnY*Tile.TILESIZE + 200, 50, 100));
    }

    public void loadWorld(String path) {
        Pixmap pixmap = new Pixmap(new FileHandle(path));

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

                if(red == 255 && green == 0 && blue == 0) {
                    tiles[x][worldHeight - 1 - y] = new DirtTile(x, y);
                } else if(red == 255 && green == 255 && blue == 255) {
                    spawnX = x;
                    spawnY = worldHeight - 1 - y;
                }
            }
        }
    }

    public void tick() {
        world.step(1/60f, 6, 2);
        rayHandler.update();
        entityManager.tick();
        rayHandler.setCombinedMatrix(ControlCenter.camera.combined.cpy().scl(PPM));
    }

    public void render(SpriteBatch batch) {
        for(int y = 0; y < tiles.length; y++) {
            for(int x = 0; x < tiles[y].length; x++) {
                if(tiles[x][y] != null)
                    tiles[x][y].render(batch);
                //getTile(x, y).render(batch, x, y);
            }
        }

        entityManager.render(batch);
        debug.render(world, c.getCamera().combined.scl(PPM));
        rayHandler.render();
    }

    public void dispose() {
        rayHandler.dispose();
        world.dispose();
        debug.dispose();
    }

//    public Tile getTile(int x, int y) {
//        if(x < 0 || y < 0 || x >= worldWidth || y>= worldHeight) {
//            return new AirTile(x, y);
//        }
//
//        Tile t = Tile.createNew(x, y, til)
//
//        if(t == null)
//            return Tile.airTile;
//
//        return t;
//    }
}
