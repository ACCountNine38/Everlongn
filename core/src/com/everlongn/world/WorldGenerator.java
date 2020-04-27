package com.everlongn.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.tiles.Tile;

import static com.everlongn.utils.Constants.PPM;

public class WorldGenerator {
    private ControlCenter c;
    public static EntityManager entityManager;
    public static int[][] tiles;

    public static int spawnX, spawnY;
    public static int worldWidth, worldHeight;

    public static Box2DDebugRenderer debug;
    public static World world;

    public WorldGenerator(ControlCenter c, String path) { //add string path when loading a world
        this.c = c;

        // vector 2 takes a x and y gravity value
        world = new World(new Vector2(0, -16f), false);
        debug = new Box2DDebugRenderer();
        loadWorld(path);
        createPlayer();
    }

    public void createPlayer() {
        entityManager = new EntityManager(c, new Player(c, spawnX*Tile.TILESIZE, spawnY*Tile.TILESIZE + 200, 50, 100));
    }

    public void loadWorld(String path) {
        Pixmap pixmap = new Pixmap(new FileHandle(path));

        worldWidth = pixmap.getWidth();
        worldHeight = pixmap.getHeight();

        tiles = new int[worldWidth][worldHeight];

        for(int y=0; y < worldHeight; y++){
            for(int x=0; x < worldWidth; x++){
                int color = pixmap.getPixel(x, y);
                String hexColor = Integer.toHexString(color);
                int red = color >>> 24;
                int green = (color & 0xFF0000) >>> 16;
                int blue = (color & 0xFF00) >>> 8;
                int alpha = color & 0xFF;

                if(red == 255 && green == 0 && blue == 0) {
                    tiles[x][worldHeight - 1 - y] = 1;
                } else if(red == 255 && green == 255 && blue == 255) {
                    tiles[x][worldHeight - 1 - y] = 0;
                    spawnX = x;
                    spawnY = worldHeight - 1 - y;
                }
            }
        }
    }

    public void tick() {
        world.step(1/60f, 1, 1);
        entityManager.tick();
    }

    public void render(SpriteBatch batch) {
        for(int y = 0; y < tiles.length; y++) {
            for(int x = 0; x < tiles[y].length; x++) {
                getTile(x, y).render(batch, x, y);
            }
        }

        entityManager.render(batch);
        debug.render(world, c.getCamera().combined.scl(PPM));
    }

    public void dispose() {
        world.dispose();
        debug.dispose();
    }

    public Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= worldWidth || y>= worldHeight || tiles[x][y] < 0) {
            return Tile.airTile;
        }

        Tile t = Tile.tiles[tiles[x][y]];

        if(t == null)
            return Tile.airTile;

        return t;
    }
}
