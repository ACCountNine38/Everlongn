package com.everlongn.states;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.everlongn.assets.Sounds;
import com.everlongn.entities.Entity;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.staticEntity.Tree;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.items.Item;
import com.everlongn.tiles.EarthTile;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Chunk;
import com.everlongn.utils.Constants;
import com.everlongn.walls.EarthWall;
import com.everlongn.walls.Wall;
import com.everlongn.world.BackgroundManager;

public class WorldLoadingState extends State {
    private String currentStage = "Loading...";
    public GlyphLayout layout = new GlyphLayout();
    public static BitmapFont loadingFont = new BitmapFont(Gdx.files.internal("fonts/chalk22.fnt"));

    public FileHandle tilemap, wallmap, herbFile, debrisMap;

    private boolean generated, terminate;
    private int currentStep, numSteps;
    private float count, statusTimer;

    private float health, maxHealth, playerX, playerY;
    private String playerForm;

    private String[] allowedVersions = new String[]{"0.2.0"};

    public WorldLoadingState(StateManager stateManager, FileHandle tilemap, FileHandle wallmap, FileHandle herbFile, FileHandle debrisMap, int difficulty, String mode, String name) {
        super(stateManager);

        GameState.difficulty = difficulty;
        GameState.mode = mode;
        GameState.name = name;

        this.tilemap = tilemap;
        this.wallmap = wallmap;
        this.herbFile = herbFile;
        this.debrisMap = debrisMap;
        currentStep = 0;
        numSteps = 5;
    }

    public void loadMeta() {
        FileHandle meta = null;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            meta = Gdx.files.external("everlongn/meta/" + GameState.name + ".txt");
        }

        String read = meta.readString();
        String[] data = read.split("\n");
        String version = data[0];
        if(version.equals("1")) {
            version = "0.1.0";
        }
        boolean passed = false;
        for(String s: allowedVersions) {
            if(s.equals(version)) {
                passed = true;
                break;
            }
        }
        if(!passed) {
            ErrorLoadingState.reason = 1;
            ErrorLoadingState.version = version;
            stateManager.setState(StateManager.CurrentState.ERROR_LOADING_STATE);
            terminate = true;
            return;
        }

        try {
            GameState.spawnX = Float.parseFloat(data[1]);
            GameState.spawnY = Float.parseFloat(data[2]);
            playerX = Float.parseFloat(data[3]);
            playerY = Float.parseFloat(data[4]);
            maxHealth = Integer.parseInt(data[5]);
            health = Integer.parseInt(data[6]);
            playerForm = data[7];

            Inventory.inventory = new Item[18];
            for(int i = 8; i < Inventory.inventory.length + 5; i++) {
                if(!data[i].equals("null")) {
                    String[] info = data[i].split(" ");
                    int id = Integer.parseInt(info[0]);
                    int amount = Integer.parseInt(info[1]);
                    Inventory.inventory[i - 8] = Item.items[id].createNew(amount);
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            ErrorLoadingState.reason = 2;
            stateManager.setState(StateManager.CurrentState.ERROR_LOADING_STATE);
            terminate = true;
            return;
        } catch(NumberFormatException error2) {
            ErrorLoadingState.reason = 2;
            stateManager.setState(StateManager.CurrentState.ERROR_LOADING_STATE);
            terminate = true;
            return;
        }
    }

    public void generate(int step, float delta) {
        if(terminate) {
            return;
        }

        count += delta;
        if(count < 0.5) {
            return;
        } else {
            count = 0;
        }

        if(step == 0) {
            currentStage = "Initializing Realm...";
            statusTimer+=delta;
            if(statusTimer > 0.1f) {
                statusTimer = 0;
                loadMeta();
                initializing();
                currentStep++;
            }
        } else if (step == 1) {
            currentStage = "Loading Realm...";
            statusTimer+=delta;
            if(statusTimer > 0.1f) {
                statusTimer = 0;
                loadWorld(tilemap, wallmap, herbFile);
                currentStep++;
            }
        } else if(step == 2) {
            currentStage = "Loading Chunks...";
            statusTimer+=delta;
            if(statusTimer > 0.1f) {
                statusTimer = 0;
                GameState.chunks = new Chunk[GameState.worldWidth / GameState.chunkSize][GameState.worldHeight / GameState.chunkSize];

                for (int i = 0; i < GameState.worldWidth / GameState.chunkSize; i++) {
                    for (int j = 0; j < GameState.worldHeight / GameState.chunkSize; j++) {
                        GameState.chunks[i][j] = new Chunk(false);
                    }
                }
                currentStep++;
            }
        } else if(step == 3) {
            statusTimer+=delta;
            if(statusTimer > 0.1f) {
                statusTimer = 0;
                currentStage = "Finalizing...";
                createPlayer();
                currentStep++;
            }
        } else {
            currentStep++;
        }
    }

    public void createPlayer() {
        GameState.entityManager = new EntityManager(c, new Player(playerX, playerY,
                25, 110, maxHealth, health));

        EntityManager.player.form = playerForm;

        Vector3 position = ControlCenter.camera.position;
        position.x = GameState.spawnX;
        position.y = GameState.spawnY;
        GameState.parallaxBackground.position.set(position);

        for(int i = 0; i < BackgroundManager.layers.length; i++) {
            BackgroundManager.layers[i] = new Vector2();
            BackgroundManager.layers[i].x = 1280/2 - 25*Tile.TILESIZE;
            BackgroundManager.layers[i].y = 800/2 - 25*Tile.TILESIZE + 120;
        }

        ControlCenter.camera.position.x = playerX; //getting back to scale by *PPM
        ControlCenter.camera.position.y = playerY + 200;
        ControlCenter.camera.update();//397 × 581
    }

    public void loadWorld(FileHandle tilemap, FileHandle wallmap, FileHandle herbsFile) {
        if(tilemap == null || wallmap == null || herbsFile == null) {
            ErrorLoadingState.reason = 2;
            stateManager.setState(StateManager.CurrentState.ERROR_LOADING_STATE);
            terminate = true;
            return;
        }

        Pixmap tiles = new Pixmap(tilemap);
        Pixmap walls = new Pixmap(wallmap);
        Pixmap debris = new Pixmap(debrisMap);

        Sounds.ambientPercentage = 1;
        Sounds.sfxPercentage = 1;

        GameState.worldWidth = tiles.getWidth();
        GameState.worldHeight = tiles.getHeight();

        GameState.tiles = new Tile[GameState.worldWidth][GameState.worldHeight];
        GameState.walls = new Wall[GameState.worldWidth][GameState.worldHeight];
        GameState.herbs = new Entity[GameState.worldWidth][GameState.worldHeight];
        GameState.debris = new int[GameState.worldWidth][GameState.worldHeight];
        GameState.lightmap = new PointLight[GameState.worldWidth][GameState.worldHeight];
        GameState.occupied = new boolean[GameState.worldWidth][GameState.worldHeight];

        // loading tiles
        try {
            for (int y = 0; y < GameState.worldHeight; y++) {
                for (int x = 0; x < GameState.worldWidth; x++) {
                    int color = tiles.getPixel(x, y);
                    String hexColor = Integer.toHexString(color);
                    int red = color >>> 24;
                    int green = (color & 0xFF0000) >>> 16;
                    int blue = (color & 0xFF00) >>> 8;
                    int alpha = color & 0xFF;

                    if (red == 1 && green == 1 && blue == 1) {
                        GameState.tiles[x][GameState.worldHeight - 1 - y] = new EarthTile(x, GameState.worldHeight - 1 - y);

                        int debrisColor = debris.getPixel(x, y);
                        int dred = debrisColor >>> 24;
                        int dgreen = (debrisColor & 0xFF0000) >>> 16;
                        int dblue = (debrisColor & 0xFF00) >>> 8;
                        if (dred == 0 && dgreen == 0 && dblue == 255) {
                            GameState.tiles[x][GameState.worldHeight - 1 - y].containType = 1;
                            GameState.debris[x][GameState.worldHeight - 1 - y] = 1;
                        } else {
                            GameState.tiles[x][GameState.worldHeight - 1 - y].containType = 0;
                            GameState.debris[x][GameState.worldHeight - 1 - y] = 0;
                        }
                    }
                }
            }

            // loading walls
            for (int y = 0; y < GameState.worldHeight; y++) {
                for (int x = 0; x < GameState.worldWidth; x++) {
                    int color = walls.getPixel(x, y);
                    String hexColor = Integer.toHexString(color);
                    int red = color >>> 24;
                    int green = (color & 0xFF0000) >>> 16;
                    int blue = (color & 0xFF00) >>> 8;
                    int alpha = color & 0xFF;

                    if (red == 1 && green == 1 && blue == 1) {
                        GameState.walls[x][GameState.worldHeight - 1 - y] = new EarthWall(x, GameState.worldHeight - 1 - y);
                    }
                }
            }

            String read = herbsFile.readString();
            String[] data = read.split("\n");
            for (int i = 0; i < data.length; i++) {
                String[] object = data[i].split(" ");
                if (object[0].equals("Tree")) {
                    int treeX = Integer.parseInt(object[1]);
                    int treeY = Integer.parseInt(object[2]);
                    int treeHeight = Integer.parseInt(object[3]);
                    // System.out.println((GameState.worldHeight - 1 - treeY) + " " + GameState.worldHeight);
                    GameState.herbs[treeX][GameState.worldHeight - 1 - treeY] = new Tree(treeX, (GameState.worldHeight - 1 - treeY), treeHeight);
                    for (int j = 0; j < treeHeight; j++) {
                        if (GameState.worldHeight - 1 - treeY + j < GameState.worldHeight)
                            GameState.occupied[treeX][GameState.worldHeight - 1 - treeY + j] = true;
                    }
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            ErrorLoadingState.reason = 2;
            stateManager.setState(StateManager.CurrentState.ERROR_LOADING_STATE);
            terminate = true;
            return;
        }
    }

    public void initializing() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        GameState.hud = new OrthographicCamera();
        GameState.hud.setToOrtho(false, width/ControlCenter.SCALE, height/ControlCenter.SCALE);
        GameState.hud2 = new OrthographicCamera();
        GameState.hud2.setToOrtho(false, width/ControlCenter.SCALE, height/ControlCenter.SCALE);
        GameState.parallaxBackground = new OrthographicCamera();
        GameState.parallaxBackground.setToOrtho(false, width/ControlCenter.SCALE, height/ControlCenter.SCALE);

        // vector 2 takes a x and y gravity value
        GameState.world = new World(new Vector2(0, -20f), false);
        GameState.rayHandler = new RayHandler(GameState.world);
        GameState.debug = new Box2DDebugRenderer();

        GameState.rayHandler = new RayHandler(GameState.world);
        GameState.rayHandler.setAmbientLight(0f);
    }

    @Override
    public void tick(float delta) {
        updateLayers(delta);

        if(!generated) {
            generate(currentStep, delta);

            if(currentStep == numSteps) {
                generated = true;
                count = 0;
                currentStage = "Complete...";
            }
        } else {
            count += delta;
            if(count >= 1f) { // 1 second timer
                Sounds.gameAmbient.setVolume(1);
                Sounds.playMusic(Sounds.gameAmbient, 2);
                stateManager.setState(StateManager.CurrentState.GAME_STATE);
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        layout.setText(loadingFont, currentStage);
        loadingFont.draw(batch, currentStage,
                ControlCenter.width/2 - layout.width/2, ControlCenter.height/2 - layout.height/2);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
