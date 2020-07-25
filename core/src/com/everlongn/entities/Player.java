package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.creatures.Spiderling;
import com.everlongn.entities.projectiles.*;
import com.everlongn.entities.staticEntity.CondensedDarkEnergy;
import com.everlongn.entities.staticEntity.Tree;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.*;
import com.everlongn.states.GameState;
import com.everlongn.tiles.EarthTile;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;
import com.everlongn.world.BackgroundManager;

import java.util.LinkedList;
import java.util.Queue;

import static com.everlongn.utils.Constants.PPM;

public class Player extends Creature {

    public static boolean movingHorizontal, onhold,
            weaponActive = true, meleeAttack, meleeRecharge, throwAttack, throwRecharge, halt, godMode,
            tilePlacing, canPlace;

    private boolean cameraXStopped, armSwingUp = true;

    // global item related variables
    public static boolean inCombat, inventoryHold, blink, blinkAlphaMax, haltReset, heavySoundPlayed, thrown;
    public static Rectangle itemCollectBound, itemPickBound;
    public static String previousItem = "";
    public static float bonusArcanePercentage = 1, bonusMeleePercentage = 1, bonusThrowingPercentage = 1, bonusRangedPercentage = 1;

    // special item related variables
    private boolean eruptionHold, shadowHold;
    public static Queue<Shadow> shadows = new LinkedList<Shadow>();
    public PointLight arcaneLight;
    public boolean casted, canThrow;
    public float arcaneLightSize, maxLightRadius, throwAngle;

    // animation variables ------
    private boolean legAnimation, armAnimation, bodyAnimation, headAnimation, jumpAnimation;

    private Animation[] legsRun = new Animation[2];
    private Animation[] legsJump = new Animation[2];
    private Animation[] armsRun = new Animation[2];
    private Animation[] headRun = new Animation[2];
    private Animation[] chestRun = new Animation[2];

    private float currentSpeed, targetAngle;
    public static float aimAngle, haltForce, shootAngle, cdr, camX, camY;
    public static float armRotationRight, armRotationLeft = 15;

    public static int currentChunkX, currentChunkY, currentTileX, currentTileY, horizontalForce;

    private ParticleEffect smoke, eruptionCharge, shadowCharge, shadowExplosion;
    public static float forceCharge, forceMax, airbornTimer, particleTimer, landSoundTimer;
    public boolean landSound;

    // Abilities
    public static boolean dash, dashing, canDash;
    public static float dashTimer, dashResetTimer;
    public static int dashDirection;
    public ParticleEffect dashEffect = null;

    // Passives
    public static boolean duoToss, triToss, glaiveLord;

    // Others

    // Testing variables

    public Player(float x, float y, int width, int height, float density, float maxHealth, float health) {
        super(x, y, width, height, density, 5);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.maxHealth = maxHealth;
        this.health = health;

        baseRegenAmount = 0.1f;
        type.add("player");
        boundWidth = 16;
        boundHeight = 100;
        knockbackResistance = 0;

        legsRun[0] = new Animation(1f/70f, Entities.legRun[0], true);
        legsRun[1] = new Animation(1f/70f, Entities.legRun[1], true);

        legsJump[0] = new Animation(1f/70f, Entities.legJump[0], true);
        legsJump[1] = new Animation(1f/70f, Entities.legJump[1], true);

        armsRun[0] = new Animation(1f/70f, Entities.armRun[0], true);
        armsRun[1] = new Animation(1f/70f, Entities.armRun[1], true);

        chestRun[0] = new Animation(1f/70f, Entities.chestRun[0], true);
        chestRun[1] = new Animation(1f/70f, Entities.chestRun[1], true);

        headRun[0] = new Animation(1f/70f, Entities.headRun[0], true);
        headRun[1] = new Animation(1f/70f, Entities.headRun[1], true);

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, density, false,
                Constants.BIT_ENEMY, (short)(Constants.BIT_TILE), (short)0, this);

        // particle effects
        eruptionCharge = new ParticleEffect();
        eruptionCharge.load(Gdx.files.internal("particles/eruptionHold"), Gdx.files.internal(""));

        shadowCharge = new ParticleEffect();
        shadowCharge.load(Gdx.files.internal("particles/shadowHold"), Gdx.files.internal(""));

        shadowExplosion = new ParticleEffect();
        shadowExplosion.load(Gdx.files.internal("particles/shadowExplosion"), Gdx.files.internal(""));

        arcaneLight = new PointLight(GameState.rayHandler, 400, new Color(0.05f, 0.05f, 0.05f, 1), 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM + 80);

        itemCollectBound = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM + 80, 20, 20);
        itemPickBound = new Rectangle(body.getPosition().x*PPM - 75, body.getPosition().y*PPM - height/2 - 50, 150, 200);

        team = 1;
    }

    public void checkSpecialCase() {
        // check arcane light
        if(casted || eruptionHold) {
            arcaneLightSize += 20;
            if(arcaneLightSize >= maxLightRadius) {
                arcaneLightSize = maxLightRadius;
                casted = false;
            }
        } else {
            arcaneLightSize -= 20;
            if(arcaneLightSize <= 0) {
                arcaneLightSize = 0;
            }
        }
        if(arcaneLightSize > 0) {
            arcaneLight.setDistance(arcaneLightSize);
        }

        // airborn check
        if(airborn) {
            airbornTimer += ControlCenter.delta;
            if(airbornTimer > 0.25) {
                airborn = false;
            }
        }

        updateThrust();

        // check fall damage
        if(Math.abs(previousVelY - body.getLinearVelocity().y) > 15 && !dash) {
            health -= (Math.abs(previousVelY - body.getLinearVelocity().y)-15) * 5;
        }

        // check jump timer
        if(body.getLinearVelocity().y > previousVelY - 0.01 && body.getLinearVelocity().y < previousVelY + 0.01) {
            yChangeTimer += ControlCenter.delta;
            if (yChangeTimer > 0.01) {
                fall = false;
                if(!canJump) {
                    Sounds.playSound(Sounds.steps[0], 0.5f);
                    landSound = false;
                }
                canJump = true;
                haltReset = true;
                yChangeTimer = 0;
            }
        } else {
            fall = true;
            canJump = false;
            previousVelY = body.getLinearVelocity().y;
            yChangeTimer = 0;
        }

        itemCollectBound.setPosition(body.getPosition().x*PPM, body.getPosition().y*PPM + 80);
        itemPickBound.setPosition(body.getPosition().x*PPM - 75, body.getPosition().y*PPM - height/2 - 50);

        if(dashEffect != null && !dashEffect.isComplete()) {
            dashEffect.update(ControlCenter.delta);
            dashEffect.setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            if(dashEffect.isComplete()) {
                dashEffect.dispose();
                dashEffect = null;
            }
        }

        if(dashing) {
            if(direction == 0) {
                body.setLinearVelocity(-50, -2);
            } else {
                body.setLinearVelocity(50, -2);
            }
            dashTimer += ControlCenter.delta;
            if(dashTimer > 0.1f) {
                dashing = false;
                dashTimer = 0;
            }
        }
    }

    @Override
    public void tick() {
        checkSpecialCase();
        if(godMode) {
            health = maxHealth;
        }

        currentChunkX = (int)(body.getPosition().x/GameState.chunkSize);
        currentChunkY = (int)(body.getPosition().y/GameState.chunkSize);

        currentTileX = (int)(body.getPosition().x);
        currentTileY = (int)(body.getPosition().y);

        cameraUpdate();
        horizontalForce = 0;
        if(!GameState.telepathy.focused) {
            inputUpdate();
        }
        checkMovement();
        animationUpdate();

        regenerate();

        if(body.getLinearVelocity().y <= 0 && jump && !airborn) {
            fall = true;
            jump = false;
        }

        if(body.getLinearVelocity().y < -50) {
            body.setLinearVelocity(body.getLinearVelocity().x + xThrust, -50);
        }

        if(jump && !Gdx.input.isKeyPressed(Input.Keys.W) && body.getLinearVelocity().y > 0) {
            body.setLinearVelocity(body.getLinearVelocity().x + xThrust, body.getLinearVelocity().y/1.15f);
        }

        checkItemOnHold();
    }

    public void animationUpdate() {
        if(legAnimation) {
            legsRun[0].tick(ControlCenter.delta);
            legsRun[1].tick(ControlCenter.delta);
            if((legsRun[direction].currentIndex == 29 || legsRun[direction].currentIndex == 58) && canJump) {
                Sounds.playSound(Sounds.steps[(int)(Math.random()*3)], 0.5f);
            }
            if(!landSound) {
                landSoundTimer += ControlCenter.delta;
                if (landSoundTimer >= 0.5f) {
                    landSoundTimer = 0;
                    landSound = true;
                }
            }
        }

        if(jumpAnimation) {
            legsJump[0].tick(ControlCenter.delta);
            legsJump[1].tick(ControlCenter.delta);
        }

        if(armAnimation) {
            armsRun[0].tick(ControlCenter.delta);
            armsRun[1].tick(ControlCenter.delta);
        }

        if(bodyAnimation) {
            chestRun[0].tick(ControlCenter.delta);
            chestRun[1].tick(ControlCenter.delta);
        }

        if(headAnimation) {
            headRun[0].tick(ControlCenter.delta);
            headRun[1].tick(ControlCenter.delta);
        }

        if(eruptionHold)
            eruptionCharge.update(ControlCenter.delta);
        else {
            if(!eruptionCharge.isComplete()) {
                eruptionCharge.update(ControlCenter.delta);
            }
        }

        if(shadowHold)
            shadowCharge.update(ControlCenter.delta);
        else {
            if(!shadowCharge.isComplete()) {
                shadowCharge.update(ControlCenter.delta);
            }
        }

        if(blink) {
            shadowExplosion.update(ControlCenter.delta);
        } else {
            if(!shadowExplosion.isComplete()) {
                shadowExplosion.update(ControlCenter.delta);
            }
        }
    }

    public void checkItemOnHold() {
        GameState.aiming = false;
        GameState.charging = false;
        GameState.empty = false;
        if(Inventory.inventory[Inventory.selectedIndex] == null) {
            onhold = false;
            return;
        }
        if((inCombat && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) || inventoryHold) {
            inCombat = false;
        }

        if(inventoryHold) {
            return;
        }
        if(Inventory.inventory[Inventory.selectedIndex] instanceof Melee) {
            onhold = true;
            throwAttack = false;
            thrown = false;
            tilePlacing = false;
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                haltReset = false;
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !meleeAttack && !GameState.options.active) {
                meleeAttack = true;
                inCombat = true;
                targetAngle = 155;

                if (aimAngle < 155)
                    armSwingUp = true;
                else {
                    targetAngle = 0;
                }
            }
            boolean found = false;
            for(int i = 0; i < EntityManager.entities.size(); i++) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player) &&
                        EntityManager.entities.get(i).getBound().contains(
                                EntityManager.player.mouseWorldPos().x, EntityManager.player.mouseWorldPos().y)) {
                    found = true;
                    break;
                }
            }
            if(found) {
                GameState.attackHover = true;
            } else {
                GameState.attackHover = false;
            }
            checkMeleeCombat();
        }
        else if(Inventory.inventory[Inventory.selectedIndex] instanceof Arcane) {
            onhold = true;
            GameState.aiming = true;
            meleeAttack = false;
            heavySoundPlayed = false;
            throwAttack = false;
            thrown = false;
            tilePlacing = false;
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                inCombat = true;
            }
            checkArcaneCombat();
        }
        else if(Inventory.inventory[Inventory.selectedIndex] instanceof Throwing) {
            onhold = true;
            GameState.aiming = true;
            meleeAttack = false;
            heavySoundPlayed = false;
            tilePlacing = false;

            Throwing t = (Throwing)(Inventory.inventory[Inventory.selectedIndex]);
            if(t.hold) {
                targetAngle = 185;
                checkHoldThrowing();
            } else {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !throwAttack && !GameState.options.active) {
                    throwAttack = true;
                    inCombat = true;
                    targetAngle = 185;

                    if (aimAngle < 185)
                        armSwingUp = true;
                    else {
                        targetAngle = 0;
                    }
                }
                checkThrowing();
            }
        }
        else if(Inventory.inventory[Inventory.selectedIndex] instanceof TileItem) {
            heavySoundPlayed = false;
            onhold = false;
            meleeAttack = false;
            throwAttack = false;
            thrown = false;
            tilePlacing = true;
            GameState.empty = true;
            checkTilePlacement();
        }
        else if(Inventory.inventory[Inventory.selectedIndex] instanceof ObjectItem) {
            heavySoundPlayed = false;
            onhold = false;
            meleeAttack = false;
            throwAttack = false;
            thrown = false;
            tilePlacing = true;
            GameState.empty = true;
            checkObjectPlacement();
        }
        else {
            GameState.defaultCursor = true;
            heavySoundPlayed = false;
            onhold = false;
            meleeAttack = false;
            throwAttack = false;
            thrown = false;
            tilePlacing = false;
        }
        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) || GameState.options.active) {
            if(eruptionHold)
                eruptionHold = false;
            if(shadowHold)
                shadowHold = false;
            forceCharge = 0;
            cdr = 0;
        }
    }

    public void checkObjectPlacement() {
        Rectangle attackRectangle;
        if(direction == 0) {
            attackRectangle = new Rectangle(body.getPosition().x*PPM - Tile.TILESIZE*2 + width - Tile.TILESIZE/2, body.getPosition().y*PPM - Tile.TILESIZE,
                    Tile.TILESIZE*2 + Tile.TILESIZE/2, height + Tile.TILESIZE * 2);
        } else {
            attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM - Tile.TILESIZE,
                    Tile.TILESIZE*2 + Tile.TILESIZE/2, height + Tile.TILESIZE * 2);
        }
        int tileX = (int)((mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
        int tileY = (int)((mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
        Rectangle tileRectangle = new Rectangle(tileX*PPM - Tile.TILESIZE/2, tileY*PPM - Tile.TILESIZE/2, Tile.TILESIZE, Tile.TILESIZE);
        boolean overlap = false;
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(tileRectangle)) {
                overlap = true;
                break;
            }
        }
        if(!overlap)
            for (int y = GameState.yStart; y < GameState.yEnd; y++) {
                for (int x = GameState.xStart; x < GameState.xEnd; x++) {
                    if(x < GameState.worldWidth && x >= 0 && y < GameState.worldHeight && y >= 0 && GameState.herbs[x][y] != null) {
                        if(GameState.herbs[x][y].getBound().overlaps(tileRectangle)) {
                            overlap = true;
                            break;
                        }
                    }
                }
                if(overlap)
                    break;
            }
        int numAdjacent = 0;
        if(tileX-1 >= 0 && GameState.tiles[tileX-1][tileY] != null) {
            numAdjacent++;
        }
        if(tileX+1 < GameState.worldWidth && GameState.tiles[tileX+1][tileY] != null) {
            numAdjacent++;
        }
        if(tileY-1 >= 0 && GameState.tiles[tileX][tileY-1] != null) {
            numAdjacent++;
        }
        if(tileY+1 < GameState.worldHeight && GameState.tiles[tileX][tileY+1] != null) {
            numAdjacent++;
        }
        if(!Inventory.inventory[Inventory.selectedIndex].stick) {
            if(tileY-1 >= 0 && GameState.tiles[tileX][tileY-1] != null) {
                GameState.tiles[tileX][tileY-1].checkAdjacent();
                if(GameState.tiles[tileX][tileY-1].numAdjacent <= 1) {
                    canPlace = false;
                    return;
                }
                if(GameState.tiles[tileX][tileY-1].numAdjacent == 2 && !(GameState.tiles[tileX][tileY-1].up && GameState.tiles[tileX][tileY-1].down)) {
                    canPlace = false;
                    return;
                }
            } else {
                canPlace = false;
                return;
            }
        }
        if(tileX >= 0 && tileX < GameState.worldWidth && tileY >= 0 && tileY < GameState.worldHeight
                && GameState.tiles[tileX][tileY] == null && (numAdjacent > 0 || GameState.walls[tileX][tileY] != null) &&
                attackRectangle.overlaps(tileRectangle) && !overlap) {
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (Inventory.inventory[Inventory.selectedIndex].name.equals("Condensed Dark Energy")) {
                    GameState.herbs[tileX][tileY] = new CondensedDarkEnergy(tileX, tileY);
                    Inventory.inventory[Inventory.selectedIndex].count--;
                    ParticleEffect explosion = new ParticleEffect();
                    explosion.load(Gdx.files.internal("particles/digParticle"), Gdx.files.internal(""));
                    explosion.getEmitters().first().scaleSize(2);
                    explosion.getEmitters().first().setPosition(tileX * Tile.TILESIZE, tileY * Tile.TILESIZE - Tile.TILESIZE / 2);
                    explosion.start();
                    EntityManager.particles.add(explosion);
                }
            }
            canPlace = true;
        } else {
            canPlace = false;
        }
    }

    public void checkTilePlacement() {
        Rectangle attackRectangle;
        if(direction == 0) {
            attackRectangle = new Rectangle(body.getPosition().x*PPM - Tile.TILESIZE*2 + width - Tile.TILESIZE/2, body.getPosition().y*PPM - Tile.TILESIZE,
                    Tile.TILESIZE*2 + Tile.TILESIZE/2, height + Tile.TILESIZE * 2);
        } else {
            attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM - Tile.TILESIZE,
                    Tile.TILESIZE*2 + Tile.TILESIZE/2, height + Tile.TILESIZE * 2);
        }
        int tileX = (int)((mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
        int tileY = (int)((mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
        Rectangle tileRectangle = new Rectangle(tileX*PPM - Tile.TILESIZE/2, tileY*PPM - Tile.TILESIZE/2, Tile.TILESIZE, Tile.TILESIZE);
        boolean overlap = false;
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(tileRectangle)) {
                overlap = true;
                break;
            }
        }
        if(!overlap)
            for (int y = GameState.yStart; y < GameState.yEnd; y++) {
                for (int x = GameState.xStart; x < GameState.xEnd; x++) {
                    if(x < GameState.worldWidth && x >= 0 && y < GameState.worldHeight && y >= 0 && GameState.herbs[x][y] != null) {
                        if(GameState.herbs[x][y].getBound().overlaps(tileRectangle)) {
                            overlap = true;
                            break;
                        }
                    }
                }
                if(overlap)
                    break;
            }
        int numAdjacent = 0;
        if(tileX-1 >= 0 && GameState.tiles[tileX-1][tileY] != null) {
            numAdjacent++;
        }
        if(tileX+1 < GameState.worldWidth && GameState.tiles[tileX+1][tileY] != null) {
            numAdjacent++;
        }
        if(tileY-1 >= 0 && GameState.tiles[tileX][tileY-1] != null) {
            numAdjacent++;
        }
        if(tileY+1 < GameState.worldHeight && GameState.tiles[tileX][tileY+1] != null) {
            numAdjacent++;
        }
        if(tileX >= 0 && tileX < GameState.worldWidth && tileY >= 0 && tileY < GameState.worldHeight
                && GameState.tiles[tileX][tileY] == null && (numAdjacent > 0 || GameState.walls[tileX][tileY] != null) &&
                attackRectangle.overlaps(tileRectangle) && !overlap) {
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (Inventory.inventory[Inventory.selectedIndex].name.equals("Earth")) {
                    GameState.tiles[tileX][tileY] = new EarthTile(tileX, tileY);
                    Inventory.inventory[Inventory.selectedIndex].count--;
                    ParticleEffect explosion = new ParticleEffect();
                    explosion.load(Gdx.files.internal("particles/digParticle"), Gdx.files.internal(""));
                    explosion.getEmitters().first().scaleSize(2);
                    explosion.getEmitters().first().setPosition(tileX * Tile.TILESIZE, tileY * Tile.TILESIZE - Tile.TILESIZE / 2);
                    explosion.start();
                    EntityManager.particles.add(explosion);
                }
                GameState.tiles[tileX][tileY].tick();
                if (tileX + 1 < GameState.worldWidth && GameState.tiles[tileX + 1][tileY] != null) {
                    GameState.tiles[tileX + 1][tileY].tick();
                }
                if (tileX - 1 >= 0 && GameState.tiles[tileX - 1][tileY] != null) {
                    GameState.tiles[tileX - 1][tileY].tick();
                }
                if (tileY + 1 < GameState.worldHeight && GameState.tiles[tileX][tileY + 1] != null) {
                    GameState.tiles[tileX][tileY + 1].tick();
                }
                if (tileY - 1 >= 0 && GameState.tiles[tileX][tileY - 1] != null) {
                    GameState.tiles[tileX][tileY - 1].tick();
                }
            }
            canPlace = true;
        } else {
            canPlace = false;
        }
    }

    public void checkHoldThrowing() {
        throwAttack = false;
        if(direction == 0) {
            if(aimAngle > 0) {
                aimAngle = -aimAngle;
            }
            if(throwRecharge) {
                float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() > ControlCenter.width/2) {
                    tempAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }

                if(aimAngle < tempAngle - 5) {
                    aimAngle+=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                } else if(aimAngle > tempAngle + 5) {
                    aimAngle-=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                } else {
                    throwRecharge = false;
                    thrown = false;
                }
            } else if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !thrown) {
                aimAngle -= Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                if (aimAngle <= -targetAngle) {
                    aimAngle = -targetAngle;
                    canThrow = true;
                    thrown = false;
                } else {
                    canThrow = false;
                }
            } else if(canThrow) {
                aimAngle += Inventory.inventory[Inventory.selectedIndex].throwSpeed;
                if (aimAngle >= 0) {
                    aimAngle = 0;
                    canThrow = false;
                    throwRecharge = true;
                }
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (width/2);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (width/2);

                float throwX = body.getPosition().x * PPM + width / 2 + xAim;
                float throwY = body.getPosition().y * PPM + 46 + 23 + yAim;

                Vector3 vec = ControlCenter.camera.project(new Vector3(throwX, throwY, 0));

                float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() + 8 - vec.x),
                        Gdx.input.getY() - 8 - vec.y));
                if (Gdx.input.getX() > ControlCenter.width/2) {
                    tempAngle = (float) Math.toDegrees(Math.atan2((((ControlCenter.width - Gdx.input.getX()) - vec.x + 8)),
                            ControlCenter.height - vec.y - 8));
                }

                if(aimAngle > tempAngle - Inventory.inventory[Inventory.selectedIndex].throwSpeed &&
                        aimAngle < tempAngle + Inventory.inventory[Inventory.selectedIndex].throwSpeed && !thrown) {
                    thrown = true;
                    int amount = 0;
                    if(glaiveLord) {
                        if(Inventory.inventory[Inventory.selectedIndex].count < 5) {
                            amount = Inventory.inventory[Inventory.selectedIndex].count;
                        } else {
                            amount = 5;
                        }
                    } else if(triToss) {
                        if(Inventory.inventory[Inventory.selectedIndex].count < 3) {
                            amount = Inventory.inventory[Inventory.selectedIndex].count;
                        } else {
                            amount = 3;
                        }
                    } else if(duoToss) {
                        if(Inventory.inventory[Inventory.selectedIndex].count < 2) {
                            amount = Inventory.inventory[Inventory.selectedIndex].count;
                        } else {
                            amount = 2;
                        }
                    } else {
                        amount = 1;
                    }

                    Inventory.inventory[Inventory.selectedIndex].count-=amount;
                    findShootAngle(xAim, yAim);

                    for(int i = 0; i < amount; i++) {
                        Throw temp = null;
                        if(Inventory.inventory[Inventory.selectedIndex].name.equals("Rock")) {
                            Sounds.playSound(Sounds.throwWeapon);
                            temp = new Rock(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                        }
                        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Shredder")) {
                            Sounds.playSound(Sounds.shurikenThrow);
                            temp = new Shuriken(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                        }
                        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Throw Knife")) {
                            Sounds.playSound(Sounds.throwWeapon);
                            temp = new ThrowKnife(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                        }
                        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Condensed Energy")) {
                            Sounds.playSound(Sounds.throwWeapon);
                            temp = new Bomb(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), 0);
                        }
                        EntityManager.throwing.add(temp);
                    }
                }
            } else {
                aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() > ControlCenter.width / 2) {
                    aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }
                thrown = false;
            }
            armRotationRight = aimAngle + 45 - 360;
        }

        else {
            if(aimAngle < 0) {
                aimAngle = Math.abs(aimAngle);
            }
            if(throwRecharge) {
                float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() < ControlCenter.width/2) {
                    tempAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }

                if(aimAngle < tempAngle - 5) {
                    aimAngle+=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                } else if(aimAngle > tempAngle + 5) {
                    aimAngle-=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                } else {
                    throwRecharge = false;
                    thrown = false;
                }
            } else if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !thrown) {
                aimAngle += Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                if (aimAngle >= targetAngle) {
                    aimAngle = targetAngle;
                    canThrow = true;
                    thrown = false;
                } else {
                    canThrow = false;
                }
            } else if(canThrow) {
                aimAngle -= Inventory.inventory[Inventory.selectedIndex].throwSpeed;
                if (aimAngle <= 0) {
                    aimAngle = 0;
                    canThrow = false;
                    throwRecharge = true;
                }

                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (width/2);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (width/2);

                float throwX = body.getPosition().x * PPM + width / 2 + xAim;
                float throwY = body.getPosition().y * PPM + 46 + 23 + yAim;

                Vector3 vec = ControlCenter.camera.project(new Vector3(throwX, throwY, 0));

                float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() + 8 - vec.x),
                        Gdx.input.getY() - 8 - vec.y));
                if (Gdx.input.getX() < ControlCenter.width/2) {
                    tempAngle = (float) Math.toDegrees(Math.atan2((((ControlCenter.width - Gdx.input.getX()) - vec.x + 8)),
                            ControlCenter.height - vec.y - 8));
                }

                if(aimAngle > tempAngle - Inventory.inventory[Inventory.selectedIndex].throwSpeed &&
                        aimAngle < tempAngle + Inventory.inventory[Inventory.selectedIndex].throwSpeed && !thrown) {
                    thrown = true;
                    int amount = 0;
                    if(glaiveLord) {
                        if(Inventory.inventory[Inventory.selectedIndex].count < 5) {
                            amount = Inventory.inventory[Inventory.selectedIndex].count;
                        } else {
                            amount = 5;
                        }
                    } else if(triToss) {
                        if(Inventory.inventory[Inventory.selectedIndex].count < 3) {
                            amount = Inventory.inventory[Inventory.selectedIndex].count;
                        } else {
                            amount = 3;
                        }
                    } else if(duoToss) {
                        if(Inventory.inventory[Inventory.selectedIndex].count < 2) {
                            amount = Inventory.inventory[Inventory.selectedIndex].count;
                        } else {
                            amount = 2;
                        }
                    } else {
                        amount = 1;
                    }

                    Inventory.inventory[Inventory.selectedIndex].count-=amount;
                    findShootAngle(xAim, yAim);

                    for(int i = 0; i < amount; i++) {
                        Throw temp = null;
                        if(Inventory.inventory[Inventory.selectedIndex].name.equals("Rock")) {
                            Sounds.playSound(Sounds.throwWeapon);
                            temp = new Rock(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                        }
                        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Shredder")) {
                            Sounds.playSound(Sounds.shurikenThrow);
                            temp = new Shuriken(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                        }
                        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Throw Knife")) {
                            Sounds.playSound(Sounds.throwWeapon);
                            temp = new ThrowKnife(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                        }
                        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Condensed Energy")) {
                            Sounds.playSound(Sounds.throwWeapon);
                            temp = new Bomb(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), 0);
                        }
                        EntityManager.throwing.add(temp);
                    }
                }
            } else {
                aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() < ControlCenter.width/2) {
                    aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }
                thrown = false;
            }
            armRotationRight = aimAngle + 45 - 90;
        }
    }

    public void checkThrowing() {
        if(direction == 0) {
            if(throwAttack) {
                if(aimAngle > 0) {
                    aimAngle = -aimAngle;
                }
                if(throwRecharge) {
                    float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                    if (Gdx.input.getX() > ControlCenter.width/2) {
                        tempAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                        ControlCenter.width / 2),
                                Gdx.input.getY() - ControlCenter.height / 2));
                    }

                    if(aimAngle < tempAngle - 5) {
                        aimAngle+=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else if(aimAngle > tempAngle + 5) {
                        aimAngle-=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else {
                        throwAttack = false;
                        throwRecharge = false;
                    }
                } else {
                    if (armSwingUp) {
                        aimAngle -= Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                        if (aimAngle <= -targetAngle) {
                            aimAngle = -targetAngle;
                            targetAngle = 0;
                            armSwingUp = false;
                            thrown = false;
                        }
                    } else {
                        aimAngle += Inventory.inventory[Inventory.selectedIndex].throwSpeed;
                        if (aimAngle > targetAngle) {
                            aimAngle = targetAngle;
                            throwRecharge = true;
                        }
                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (width/2);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (width/2);

                        float throwX = body.getPosition().x * PPM + width / 2 + xAim;
                        float throwY = body.getPosition().y * PPM + 46 + 23 + yAim;

                        Vector3 vec = ControlCenter.camera.project(new Vector3(throwX, throwY, 0));

                        float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() + 8 - vec.x),
                                Gdx.input.getY() - 8 - vec.y));
                        if (Gdx.input.getX() > ControlCenter.width/2) {
                            tempAngle = (float) Math.toDegrees(Math.atan2((((ControlCenter.width - Gdx.input.getX()) - vec.x + 8)),
                                    ControlCenter.height - vec.y - 8));
                        }

                        if(aimAngle > tempAngle - Inventory.inventory[Inventory.selectedIndex].throwSpeed &&
                                aimAngle < tempAngle + Inventory.inventory[Inventory.selectedIndex].throwSpeed && !thrown) {
                            thrown = true;
                            int amount = 0;
                            if(glaiveLord) {
                                if(Inventory.inventory[Inventory.selectedIndex].count < 5) {
                                    amount = Inventory.inventory[Inventory.selectedIndex].count;
                                } else {
                                    amount = 5;
                                }
                            } else if(triToss) {
                                if(Inventory.inventory[Inventory.selectedIndex].count < 3) {
                                    amount = Inventory.inventory[Inventory.selectedIndex].count;
                                } else {
                                    amount = 3;
                                }
                            } else if(duoToss) {
                                if(Inventory.inventory[Inventory.selectedIndex].count < 2) {
                                    amount = Inventory.inventory[Inventory.selectedIndex].count;
                                } else {
                                    amount = 2;
                                }
                            } else {
                                amount = 1;
                            }

                            Inventory.inventory[Inventory.selectedIndex].count-=amount;
                            findShootAngle(xAim, yAim);

                            for(int i = 0; i < amount; i++) {
                                Throw temp = null;
                                if(Inventory.inventory[Inventory.selectedIndex].name.equals("Shuriken")) {
                                    Sounds.playSound(Sounds.ninjaStarThrow);
                                    temp = new TriStar(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                                }
                                else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Dagger")) {
                                    Sounds.playSound(Sounds.throwWeapon);
                                    temp = new Dagger(throwX, throwY, direction, shootAngle - (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                                }
                                EntityManager.throwing.add(temp);
                            }
                        }
                    }
                }
            } else {
                aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() > ControlCenter.width / 2) {
                    aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }
            }
            armRotationRight = aimAngle + 45 - 360;
        }

        else {
            if(throwAttack) {
                if(aimAngle < 0) {
                    aimAngle = -aimAngle;
                }
                if(throwRecharge) {
                    float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                    if (Gdx.input.getX() < ControlCenter.width/2) {
                        tempAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                        ControlCenter.width / 2),
                                Gdx.input.getY() - ControlCenter.height / 2));
                    }

                    if(aimAngle < tempAngle - 5) {
                        aimAngle+=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else if(aimAngle > tempAngle + 5) {
                        aimAngle-=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else {
                        throwAttack = false;
                        throwRecharge = false;
                    }
                } else {
                    if (armSwingUp) {
                        aimAngle += Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                        if (aimAngle >= targetAngle) {
                            aimAngle = targetAngle;
                            targetAngle = 0;
                            armSwingUp = false;
                            thrown = false;
                        }
                    } else {
                        aimAngle -= Inventory.inventory[Inventory.selectedIndex].throwSpeed;

                        if (aimAngle < targetAngle) {
                            aimAngle = targetAngle;
                            throwRecharge = true;
                        }

                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (width/2);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (width/2);

                        float throwX = body.getPosition().x * PPM + width / 2 + xAim;
                        float throwY = body.getPosition().y * PPM + 46 + 23 + yAim;

                        Vector3 vec = ControlCenter.camera.project(new Vector3(throwX, throwY, 0));

                        float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() + 8 - vec.x),
                                Gdx.input.getY() - 8 - vec.y));
                        if (Gdx.input.getX() < ControlCenter.width/2) {
                            tempAngle = (float) Math.toDegrees(Math.atan2((((ControlCenter.width - Gdx.input.getX()) - vec.x + 8)),
                                    ControlCenter.height - vec.y - 8));
                        }

                        if(aimAngle > tempAngle - Inventory.inventory[Inventory.selectedIndex].throwSpeed &&
                                aimAngle < tempAngle + Inventory.inventory[Inventory.selectedIndex].throwSpeed && !thrown) {

                            thrown = true;

                            int amount = 0;
                            if(glaiveLord) {
                                if(Inventory.inventory[Inventory.selectedIndex].count < 5) {
                                    amount = Inventory.inventory[Inventory.selectedIndex].count;
                                } else {
                                    amount = 5;
                                }
                            } else if(triToss) {
                                if(Inventory.inventory[Inventory.selectedIndex].count < 3) {
                                    amount = Inventory.inventory[Inventory.selectedIndex].count;
                                } else {
                                    amount = 3;
                                }
                            } else if(duoToss) {
                                if(Inventory.inventory[Inventory.selectedIndex].count < 2) {
                                    amount = Inventory.inventory[Inventory.selectedIndex].count;
                                } else {
                                    amount = 2;
                                }
                            } else {
                                amount = 1;
                            }

                            Inventory.inventory[Inventory.selectedIndex].count-=amount;
                            findShootAngle(xAim, yAim);

                            for(int i = 0; i < amount; i++) {
                                Throw temp = null;
                                if(Inventory.inventory[Inventory.selectedIndex].name.equals("Shuriken")) {
                                    Sounds.playSound(Sounds.ninjaStarThrow);
                                    temp = new TriStar(throwX, throwY, direction, shootAngle + (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                                }
                                else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Dagger")) {
                                    Sounds.playSound(Sounds.throwWeapon);
                                    temp = new Dagger(throwX, throwY, direction, shootAngle + (float)(i*5 * Math.PI/180), Inventory.inventory[Inventory.selectedIndex].throwingDamage*bonusThrowingPercentage);
                                }
                                EntityManager.throwing.add(temp);
                            }
                        }
                    }
                }
            } else {
                aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() < ControlCenter.width/2) {
                    aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }
            }
            armRotationRight = aimAngle + 45 - 90;
        }
    }

    public void checkMeleeCombat() {
        if(direction == 0) {
            if(meleeAttack) {
                if(aimAngle > 0) {
                    aimAngle = -aimAngle;
                }
                if(meleeRecharge) {
                    float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                    if (Gdx.input.getX() > ControlCenter.width/2) {
                        tempAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                        ControlCenter.width / 2),
                                Gdx.input.getY() - ControlCenter.height / 2));
                    }

                    if(aimAngle < tempAngle - 5) {
                        aimAngle+=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else if(aimAngle > tempAngle + 5) {
                        aimAngle-=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else {
                        meleeAttack = false;
                        meleeRecharge = false;
                    }
                } else {
                    if (armSwingUp) {
                        aimAngle -= Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                        if (aimAngle <= -targetAngle) {
                            aimAngle = -targetAngle;
                            targetAngle = 0;
                            armSwingUp = false;
                            if(!Inventory.inventory[Inventory.selectedIndex].heavy)
                                Sounds.playSound(Inventory.inventory[Inventory.selectedIndex].swingSound);
                        }
                    } else {
                        if(Inventory.inventory[Inventory.selectedIndex].heavy) {
                            if ((!fall && !jump) || haltReset) {
                                aimAngle += Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                                if(!heavySoundPlayed) {
                                    Sounds.playSound(Inventory.inventory[Inventory.selectedIndex].swingSound);
                                    heavySoundPlayed = true;
                                }
                            } else {
                                aimAngle += 1;
                                if(haltForce < 45) {
                                    haltForce += 0.75f;
                                }
                                halt = true;
                            }
                        } else {
                            aimAngle += Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                        }
                        if (aimAngle > -targetAngle) {
                            haltReset = false;
                            aimAngle = -targetAngle;
                            meleeRecharge = true;
                            Rectangle attackRectangle = new Rectangle(body.getPosition().x*PPM - Inventory.inventory[Inventory.selectedIndex].width - width, body.getPosition().y*PPM,
                                    Inventory.inventory[Inventory.selectedIndex].width + width, Inventory.inventory[Inventory.selectedIndex].height);

                            heavySoundPlayed = false;
                            // checks target for attack
                            if(Inventory.inventory[Inventory.selectedIndex].heavy) {
                                for(int i = 0; i < EntityManager.entities.size(); i++) {
                                    if(EntityManager.entities.get(i).getBound().overlaps(attackRectangle) && EntityManager.entities.get(i) != this) {
                                        if(EntityManager.entities.get(i) instanceof Creature) {
                                            Creature c = (Creature)EntityManager.entities.get(i);
                                            c.stunned = true;
                                            float force = Inventory.inventory[Inventory.selectedIndex].force + (float)(Math.random()*Inventory.inventory[Inventory.selectedIndex].force/4)
                                                    + haltForce * 30;
                                            EntityManager.entities.get(i).body.applyForceToCenter(
                                                    -force, 250, false);
                                            c.hurt(Inventory.inventory[Inventory.selectedIndex].damage, GameState.difficulty);
                                        }
                                    }
                                }
                                if(Inventory.inventory[Inventory.selectedIndex].isAxe) {
                                    for (int y = GameState.yStart; y < GameState.yEnd; y++) {
                                        for (int x = GameState.xStart; x < GameState.xEnd; x++) {
                                            if(GameState.herbs[x][y] != null && GameState.herbs[x][y].getBound().overlaps(attackRectangle)) {
                                                if(GameState.herbs[x][y] instanceof Tree) {
                                                    Tree temp = (Tree)GameState.herbs[x][y];
                                                    temp.impact(Inventory.inventory[Inventory.selectedIndex].damage, direction);
                                                }
                                            }
                                        }
                                    }
                                }
                                if(Inventory.inventory[Inventory.selectedIndex].isPick) {
                                    attackRectangle = new Rectangle(body.getPosition().x*PPM - Inventory.inventory[Inventory.selectedIndex].width - width + 25, body.getPosition().y*PPM - 10,
                                            Inventory.inventory[Inventory.selectedIndex].width + width - 25, Inventory.inventory[Inventory.selectedIndex].height + 10);
                                    int tileX = (int)((mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    int tileY = (int)((mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    if(tileX >= 0 && tileX < GameState.worldWidth && tileY >= 0 && tileY < GameState.worldHeight
                                            && GameState.tiles[tileX][tileY] != null && GameState.tiles[tileX][tileY].getBound().overlaps(attackRectangle)
                                            && !GameState.occupied[tileX][tileY]) {
                                        GameState.tiles[tileX][tileY].damage(Inventory.inventory[Inventory.selectedIndex].damage);
                                    }
                                }
                            } else {
                                float shortestDistance = -1;
                                Entity targeted = null;
                                for(int i = 0; i < EntityManager.entities.size(); i++) {
                                    if(EntityManager.entities.get(i).getBound().overlaps(attackRectangle) && EntityManager.entities.get(i) != this) {
                                        if((EntityManager.entities.get(i) instanceof Creature || (EntityManager.entities.get(i) instanceof Tree && Inventory.inventory[Inventory.selectedIndex].isAxe)) && EntityManager.entities.get(i).health > 0) {
                                            if(targeted == null) {
                                                targeted = EntityManager.entities.get(i);
                                                shortestDistance = Math.abs(body.getPosition().x*PPM - EntityManager.entities.get(i).body.getPosition().x*PPM);
                                            } else {
                                                float distance = Math.abs(body.getPosition().x*PPM - EntityManager.entities.get(i).body.getPosition().x*PPM);
                                                if(distance < shortestDistance) {
                                                    targeted = EntityManager.entities.get(i);
                                                    shortestDistance = distance;
                                                }
                                            }
                                        }
                                    }
                                }
                                if(Inventory.inventory[Inventory.selectedIndex].isAxe) {
                                    for (int y = GameState.yStart; y < GameState.yEnd; y++) {
                                        for (int x = GameState.xStart; x < GameState.xEnd; x++) {
                                            if(GameState.herbs[x][y] != null && GameState.herbs[x][y].getBound().overlaps(attackRectangle)) {
                                                if(targeted == null) {
                                                    targeted = GameState.herbs[x][y];
                                                    shortestDistance = Math.abs(body.getPosition().x*PPM - GameState.herbs[x][y].getBound().x);
                                                } else {
                                                    float distance = Math.abs(body.getPosition().x*PPM - GameState.herbs[x][y].getBound().x);
                                                    if(distance < shortestDistance) {
                                                        targeted = GameState.herbs[x][y];
                                                        shortestDistance = distance;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                boolean breakTile = false;
                                if(Inventory.inventory[Inventory.selectedIndex].isPick) {
                                    attackRectangle = new Rectangle(body.getPosition().x*PPM - Inventory.inventory[Inventory.selectedIndex].width - width + 25, body.getPosition().y*PPM - 10,
                                            Inventory.inventory[Inventory.selectedIndex].width + width - 25, Inventory.inventory[Inventory.selectedIndex].height + 10);
                                    int tileX = (int)((mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    int tileY = (int)((mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    if(tileX >= 0 && tileX < GameState.worldWidth && tileY >= 0 && tileY < GameState.worldHeight
                                            && GameState.tiles[tileX][tileY] != null && GameState.tiles[tileX][tileY].getBound().overlaps(attackRectangle)
                                            && !GameState.occupied[tileX][tileY]) {
                                        GameState.tiles[tileX][tileY].damage(Inventory.inventory[Inventory.selectedIndex].damage);
                                        breakTile = true;
                                    }
                                }

                                if(targeted != null && !breakTile) {
                                    if(targeted instanceof Tree) {
                                        Tree temp = (Tree)targeted;
                                        temp.impact(Inventory.inventory[Inventory.selectedIndex].damage, direction);
                                    } else {
                                        Creature c = (Creature) targeted;
                                        c.stunned = true;
                                        float force = Inventory.inventory[Inventory.selectedIndex].force + (float) (Math.random() * Inventory.inventory[Inventory.selectedIndex].force / 4);
                                        c.body.applyForceToCenter(
                                                -force, 250, false);
                                        c.hurt(Inventory.inventory[Inventory.selectedIndex].damage, GameState.difficulty);
                                    }
                                }
                            }
                            haltForce = 0;
                            halt = false;
                        }
                    }
                }
            } else {
                aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() > ControlCenter.width / 2) {
                    aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }
            }
            armRotationRight = aimAngle + 45 - 360;
        }

        else {
            if(meleeAttack) {
                if(aimAngle < 0) {
                    aimAngle = -aimAngle;
                }
                if(meleeRecharge) {
                    float tempAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                    if (Gdx.input.getX() < ControlCenter.width/2) {
                        tempAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                        ControlCenter.width / 2),
                                Gdx.input.getY() - ControlCenter.height / 2));
                    }

                    if(aimAngle < tempAngle - 5) {
                        aimAngle+=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else if(aimAngle > tempAngle + 5) {
                        aimAngle-=Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                    } else {
                        meleeAttack = false;
                        meleeRecharge = false;
                    }
                } else {
                    if (armSwingUp) {
                        aimAngle += Inventory.inventory[Inventory.selectedIndex].drawSpeed;
                        if (aimAngle >= targetAngle) {
                            aimAngle = targetAngle;
                            targetAngle = 0;
                            armSwingUp = false;
                            if(!Inventory.inventory[Inventory.selectedIndex].heavy)
                                Sounds.playSound(Inventory.inventory[Inventory.selectedIndex].swingSound);
                        }
                    } else {
                        if(Inventory.inventory[Inventory.selectedIndex].heavy) {
                            if ((!fall && !jump) || haltReset) {
                                if(!heavySoundPlayed) {
                                    Sounds.playSound(Inventory.inventory[Inventory.selectedIndex].swingSound);
                                    heavySoundPlayed = true;
                                }
                                aimAngle -= Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                            } else {
                                aimAngle -= 1;
                                if(haltForce < 45) {
                                    haltForce += 0.75f;
                                }
                                halt = true;
                            }
                        } else {
                            aimAngle -= Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                        }

                        if (aimAngle < targetAngle) {
                            aimAngle = targetAngle;
                            meleeRecharge = true;
                            haltReset = false;
                            heavySoundPlayed = false;

                            Rectangle attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM,
                                    Inventory.inventory[Inventory.selectedIndex].width + width, Inventory.inventory[Inventory.selectedIndex].height);

                            // checks target for attack
                            if(Inventory.inventory[Inventory.selectedIndex].heavy) {
                                for(int i = 0; i < EntityManager.entities.size(); i++) {
                                    if(EntityManager.entities.get(i).getBound().overlaps(attackRectangle) && EntityManager.entities.get(i) != this) {
                                        if(EntityManager.entities.get(i) instanceof Creature || (EntityManager.entities.get(i) instanceof Tree && Inventory.inventory[Inventory.selectedIndex].isAxe)) {
                                            Creature c = (Creature)EntityManager.entities.get(i);
                                            c.stunned = true;
                                            float force = Inventory.inventory[Inventory.selectedIndex].force + (float)(Math.random()*Inventory.inventory[Inventory.selectedIndex].force/4)
                                                    + haltForce * 30;
                                            EntityManager.entities.get(i).body.applyForceToCenter(
                                                    force, 250, false);
                                            c.hurt(Inventory.inventory[Inventory.selectedIndex].damage, GameState.difficulty);
                                        }
                                    }
                                }
                                if(Inventory.inventory[Inventory.selectedIndex].isAxe) {
                                    for (int y = GameState.yStart; y < GameState.yEnd; y++) {
                                        for (int x = GameState.xStart; x < GameState.xEnd; x++) {
                                            if(GameState.herbs[x][y] != null && GameState.herbs[x][y].getBound().overlaps(attackRectangle)) {
                                                if(GameState.herbs[x][y] instanceof Tree) {
                                                    Tree temp = (Tree)GameState.herbs[x][y];
                                                    temp.impact(Inventory.inventory[Inventory.selectedIndex].damage, direction);
                                                }
                                            }
                                        }
                                    }
                                }
                                if(Inventory.inventory[Inventory.selectedIndex].isPick) {
                                    attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM - 10,
                                            Inventory.inventory[Inventory.selectedIndex].width + width - 25, Inventory.inventory[Inventory.selectedIndex].height + 10);
                                    int tileX = (int)((mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    int tileY = (int)((mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    if(tileX >= 0 && tileX < GameState.worldWidth && tileY >= 0 && tileY < GameState.worldHeight
                                            && GameState.tiles[tileX][tileY] != null && GameState.tiles[tileX][tileY].getBound().overlaps(attackRectangle)
                                            && !GameState.occupied[tileX][tileY]) {
                                        GameState.tiles[tileX][tileY].damage(Inventory.inventory[Inventory.selectedIndex].damage);
                                    }
                                }
                            } else {
                                float shortestDistance = -1;
                                Entity targeted = null;
                                for(int i = 0; i < EntityManager.entities.size(); i++) {
                                    if(EntityManager.entities.get(i).getBound().overlaps(attackRectangle) && EntityManager.entities.get(i) != this) {
                                        if((EntityManager.entities.get(i) instanceof Creature || (EntityManager.entities.get(i) instanceof Tree && Inventory.inventory[Inventory.selectedIndex].isAxe)) && EntityManager.entities.get(i).health > 0) {
                                            Creature c = (Creature)EntityManager.entities.get(i);
                                            c.stunned = true;
                                            float force = Inventory.inventory[Inventory.selectedIndex].force + (float)(Math.random()*Inventory.inventory[Inventory.selectedIndex].force/4);
                                            EntityManager.entities.get(i).body.applyForceToCenter(
                                                    force, 250, false);
                                            if(targeted == null) {
                                                targeted = EntityManager.entities.get(i);
                                                shortestDistance = Math.abs(body.getPosition().x*PPM - EntityManager.entities.get(i).body.getPosition().x*PPM);
                                            } else {
                                                float distance = Math.abs(body.getPosition().x*PPM - EntityManager.entities.get(i).body.getPosition().x*PPM);
                                                if(distance < shortestDistance) {
                                                    targeted = EntityManager.entities.get(i);
                                                    shortestDistance = distance;
                                                }
                                            }
                                        }
                                    }
                                }
                                if(Inventory.inventory[Inventory.selectedIndex].isAxe) {
                                    for (int y = GameState.yStart; y < GameState.yEnd; y++) {
                                        for (int x = GameState.xStart; x < GameState.xEnd; x++) {
                                            if(GameState.herbs[x][y] != null && GameState.herbs[x][y].getBound().overlaps(attackRectangle)) {
                                                if(targeted == null) {
                                                    targeted = GameState.herbs[x][y];
                                                    shortestDistance = Math.abs(body.getPosition().x*PPM - GameState.herbs[x][y].getBound().x);
                                                } else {
                                                    float distance = Math.abs(body.getPosition().x*PPM - GameState.herbs[x][y].getBound().x);
                                                    if(distance < shortestDistance) {
                                                        targeted = GameState.herbs[x][y];
                                                        shortestDistance = distance;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                boolean breakTile = false;
                                if(Inventory.inventory[Inventory.selectedIndex].isPick) {
                                    attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM - 10,
                                            Inventory.inventory[Inventory.selectedIndex].width + width - 25, Inventory.inventory[Inventory.selectedIndex].height + 10);
                                    int tileX = (int)((mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    int tileY = (int)((mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
                                    if(tileX >= 0 && tileX < GameState.worldWidth && tileY >= 0 && tileY < GameState.worldHeight
                                            && GameState.tiles[tileX][tileY] != null && GameState.tiles[tileX][tileY].getBound().overlaps(attackRectangle)
                                            && !GameState.occupied[tileX][tileY]) {
                                        GameState.tiles[tileX][tileY].damage(Inventory.inventory[Inventory.selectedIndex].damage);
                                        breakTile = true;
                                    }
                                }

                                if(targeted != null && !breakTile) {
                                    if(targeted instanceof Tree) {
                                        Tree temp = (Tree)targeted;
                                        temp.impact(Inventory.inventory[Inventory.selectedIndex].damage, direction);
                                    } else {
                                        Creature c = (Creature) targeted;
                                        c.stunned = true;
                                        float force = Inventory.inventory[Inventory.selectedIndex].force + (float) (Math.random() * Inventory.inventory[Inventory.selectedIndex].force / 4);
                                        c.body.applyForceToCenter(
                                                -force, 250, false);
                                        c.hurt(Inventory.inventory[Inventory.selectedIndex].damage, GameState.difficulty);
                                    }
                                }
                            }
                            haltForce = 0;
                            halt = false;
                        }
                    }
                }
            } else {
                aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
                if (Gdx.input.getX() < ControlCenter.width/2) {
                    aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                    ControlCenter.width / 2),
                            Gdx.input.getY() - ControlCenter.height / 2));
                }
            }
            armRotationRight = aimAngle + 45 - 90;
        }
    }

    public void checkArcaneCombat() {
        findAimAngle();

        if(Inventory.inventory[Inventory.selectedIndex].name.equals("Shadow Manipulator")) {
            maxLightRadius = 0;
            forceMax = 8;
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                GameState.charging = true;
                if(!previousItem.equals("Shadow Manipulator")) {
                    previousItem = "Shadow Manipulator";
                    forceCharge = 0;
                }
                if(forceCharge < forceMax) {
                    forceCharge += 0.08;
                }
                cdr += ControlCenter.delta;

                particleTimer+=ControlCenter.delta;
                health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption * ControlCenter.delta;
                if(particleTimer > 0.25) {
                    shadowHold = true;
                    shadowCharge.start();
                    particleTimer = 0;
                }

                float xAim, yAim;

                if(direction == 0) {
                    xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180)) * (85);
                    yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180)) * (-85);
                    shadowCharge.getEmitters().first().setPosition(
                            body.getPosition().x * PPM + width / 2 + xAim,
                            body.getPosition().y * PPM + 69 + yAim);
                } else {
                    xAim = (float) Math.cos(Math.toRadians(-armRotationRight)) * (85);
                    yAim = (float) Math.sin(Math.toRadians(-armRotationRight)) * (-85);
                    shadowCharge.getEmitters().first().setPosition(
                            body.getPosition().x * PPM + width / 2 + xAim, 69 + body.getPosition().y * PPM + yAim);
                }
            } else {
                GameState.charging = false;
                if(shadowHold) {
                    if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                        cdr = 0;

                        if (direction == 0) {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180)) * (85);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180)) * (-85);

                            // shoot angle is in radians
                            if (Gdx.input.getX() <= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            Sounds.playSound(Sounds.shadowCast, 2.5f);
                            Shadow shadow = new Shadow(body.getPosition().x * PPM + width / 2 + xAim, 69 + body.getPosition().y * PPM + yAim,
                                    this, direction, (float)Math.sin(shootAngle + Math.PI/20)*forceCharge, -(float)Math.cos(shootAngle + Math.PI/20)*forceCharge, false);

                            EntityManager.entities.add(shadow);
                            shadows.add(shadow);
                        } else {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight)) * (85);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight)) * (-85);

                            // shoot angle is in radians
                            if (Gdx.input.getX() >= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            Sounds.playSound(Sounds.shadowCast, 2.5f);
                            Shadow shadow = new Shadow(body.getPosition().x * PPM + width / 2 + xAim, 69 + body.getPosition().y * PPM + yAim,
                                    this, direction, (float)Math.sin(shootAngle - Math.PI/20)*forceCharge, -(float)Math.cos(shootAngle - Math.PI/20)*forceCharge, false);
                            EntityManager.entities.add(shadow);
                            shadows.add(shadow);
                        }
                    }
                }
                shadowHold = false;
                forceCharge = 0;
                cdr = 0;
                shadowCharge.getEmitters().get(0).setContinuous(false);
            }

            if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                if(!shadows.isEmpty()) {
                    shadowExplosion.getEmitters().get(0).setPosition(body.getPosition().x*PPM, body.getPosition().y*PPM + height/2);
                    shadowExplosion.start();
                    blink = true;

                    Sounds.playSound(Sounds.shadowShift, 0.5f);
                    Shadow shadow = shadows.poll();
                    body.setTransform(shadow.body.getPosition().x, shadow.body.getPosition().y, 0);
                    body.setLinearVelocity(0, 0);
                    shadow.life = Shadow.maxLife;
                }
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Caster")) {
            GameState.charging = false;
            arcaneLight.setColor(ArcaneTrail.color);
            forceCharge = 0;
            maxLightRadius = 450;
            if(direction == 0) {
                float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);
                arcaneLight.setPosition(body.getPosition().x * PPM + width / 2 + xAim,
                        body.getPosition().y * PPM + 46 + 23 + yAim);
            } else {
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);
                arcaneLight.setPosition((body.getPosition().x * PPM + width / 2) + xAim,
                        (body.getPosition().y * PPM + 46 + 23) + yAim);
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                cdr += ControlCenter.delta;

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                    cdr = 0;
                    casted = true;
                    health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption;
                    if (direction == 0) {
                        float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                        float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() <= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        Sounds.playSound(Sounds.arcaneCaster);
                        ArcaneTrail arcaneTrail = new ArcaneTrail(body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle + (float)Math.PI/15, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);

                        EntityManager.entities.add(arcaneTrail);
                    } else {
                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() >= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        Sounds.playSound(Sounds.arcaneCaster);
                        ArcaneTrail arcaneTrail = new ArcaneTrail((body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle - (float)Math.PI/15, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneTrail);
                    }
                }
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Devastation")) {
            arcaneLight.setColor(ArcaneDevastation.color);
            maxLightRadius = 650;
            forceMax = 1f;
            if(direction == 0) {
                float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-100);
                float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (100);
                arcaneLight.setPosition(body.getPosition().x * PPM + width / 2 + xAim,
                        body.getPosition().y * PPM + 46 + 23 + yAim);
            } else {
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (100);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (100);
                arcaneLight.setPosition((body.getPosition().x * PPM + width / 2) + xAim,
                        (body.getPosition().y * PPM + 46 + 23) + yAim);
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                GameState.charging = true;
                cdr += ControlCenter.delta;

                if(!previousItem.equals("Devastation")) {
                    previousItem = "Devastation";
                    forceCharge = 0;
                }
                if(forceCharge < forceMax) {
                    forceCharge += 0.002;
                }

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                    cdr = 0;
                    Sounds.playSound(Sounds.arcaneDevastation, forceCharge/forceMax);
                    casted = true;
                    health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption;
                    if (direction == 0) {
                        float xAim = (float) Math.sin(Math.toRadians(aimAngle + 35 + 90)) * (-100);
                        float yAim = (float) Math.cos(Math.toRadians(aimAngle + 35 + 90)) * (100);

                        // shoot angle is in radians
                        if (Gdx.input.getX() <= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneDevastation arcaneTrail = new ArcaneDevastation(body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle + (float)Math.PI/10f, forceCharge, (forceCharge * 50)*bonusArcanePercentage);

                        EntityManager.entities.add(arcaneTrail);
                    } else {
                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 35)) * (100);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 35)) * (100);

                        // shoot angle is in radians
                        if (Gdx.input.getX() >= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneDevastation arcaneTrail = new ArcaneDevastation((body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle - (float)Math.PI/10f, forceCharge, (forceCharge * 50)*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneTrail);
                    }
                }
            }

            else {
                GameState.charging = false;
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Reflection")) {
            GameState.charging = false;
            arcaneLight.setColor(ArcaneReflection.color);
            forceCharge = 0;
            maxLightRadius = 450;
            if(direction == 0) {
                float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);
                arcaneLight.setPosition(body.getPosition().x * PPM + width / 2 + xAim,
                        body.getPosition().y * PPM + 46 + 23 + yAim);
            } else {
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);
                arcaneLight.setPosition((body.getPosition().x * PPM + width / 2) + xAim,
                        (body.getPosition().y * PPM + 46 + 23) + yAim);
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                cdr += ControlCenter.delta;

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                    health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption;
                    cdr = 0;
                    casted = true;
                    Sounds.playSound(Sounds.arcaneReflection);
                    if (direction == 0) {
                        float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                        float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() <= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneReflection arcaneReflection = new ArcaneReflection(body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle + (float) Math.PI / 15, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneReflection);

                    } else {
                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() >= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneReflection arcaneReflection = new ArcaneReflection((body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle - (float) Math.PI / 15, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneReflection);
                    }
                }
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Eruption")) {
            arcaneLight.setColor(ArcaneEruption.color);
            maxLightRadius = 800;
            forceMax = 100;
            if(direction == 0) {
                float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);
                arcaneLight.setPosition(body.getPosition().x * PPM + width / 2 + xAim,
                        body.getPosition().y * PPM + 46 + 23 + yAim);
            } else {
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);
                arcaneLight.setPosition((body.getPosition().x * PPM + width / 2) + xAim,
                        (body.getPosition().y * PPM + 46 + 23) + yAim);
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                GameState.charging = true;
                if(!previousItem.equals("Eruption")) {
                    previousItem = "Eruption";
                    forceCharge = 0;
                }
                if(forceCharge < forceMax) {
                    forceCharge += 0.5;
                }
                cdr += ControlCenter.delta;
                health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption*ControlCenter.delta;
                particleTimer+=ControlCenter.delta;
                if(particleTimer > 0.25) {
                    eruptionHold = true;
                    eruptionCharge.start();
                    particleTimer = 0;
                }

                float xAim, yAim;

                if(direction == 0) {
                    xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180)) * (76);
                    yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180)) * (-76);
                    eruptionCharge.getEmitters().first().setPosition(
                            body.getPosition().x * PPM + width / 2 + xAim,
                            body.getPosition().y * PPM + 69 + yAim);
                } else {
                    xAim = (float) Math.cos(Math.toRadians(-armRotationRight)) * (76);
                    yAim = (float) Math.sin(Math.toRadians(-armRotationRight)) * (-76);
                    eruptionCharge.getEmitters().first().setPosition(
                            body.getPosition().x * PPM + width / 2 + xAim, 69 + body.getPosition().y * PPM + yAim);
                }

            }

            else {
                GameState.charging = false;
                if(eruptionHold) {
                    if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                        cdr = 0;
                        Sounds.playSound(Sounds.eruptionCast, 0.75f);
                        if (direction == 0) {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180)) * (76);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180)) * (-76);

                            // shoot angle is in radians
                            if (Gdx.input.getX() <= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            ArcaneEruption arcaneTrail = new ArcaneEruption(body.getPosition().x * PPM + width / 2 + xAim,
                                    body.getPosition().y * PPM + 69 + yAim,
                                    10, direction, shootAngle,
                                    (float)Math.sin(shootAngle + Math.PI/20)*forceCharge, -(float)Math.cos(shootAngle + Math.PI/20)*forceCharge,
                                    (Inventory.inventory[Inventory.selectedIndex].burst + forceCharge)*bonusArcanePercentage);
                            EntityManager.entities.add(arcaneTrail);
                        } else {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight)) * (76);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight)) * (-76);

                            // shoot angle is in radians
                            if (Gdx.input.getX() >= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            ArcaneEruption arcaneTrail = new ArcaneEruption(body.getPosition().x * PPM + width / 2 + xAim,
                                    69 + body.getPosition().y * PPM + yAim,
                                    10, direction, shootAngle,
                                    (float)Math.sin(shootAngle - Math.PI/20)*forceCharge, -(float)Math.cos(shootAngle - Math.PI/20)*forceCharge,
                                    (Inventory.inventory[Inventory.selectedIndex].burst + forceCharge)*bonusArcanePercentage);
                            EntityManager.entities.add(arcaneTrail);
                        }
                    }
                }
                eruptionHold = false;
                forceCharge = 0;
                cdr = 0;
                eruptionCharge.getEmitters().get(0).setContinuous(false);
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Rebound")) {
            GameState.charging = false;
            arcaneLight.setColor(ArcaneRebound.color);
            maxLightRadius = 600;
            forceCharge = 0;
            if(direction == 0) {
                float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);
                arcaneLight.setPosition(body.getPosition().x * PPM + width / 2 + xAim,
                        body.getPosition().y * PPM + 46 + 23 + yAim);
            } else {
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);
                arcaneLight.setPosition((body.getPosition().x * PPM + width / 2) + xAim,
                        (body.getPosition().y * PPM + 46 + 23) + yAim);
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                cdr += ControlCenter.delta;

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                    Sounds.playSound(Sounds.arcaneRebound, 1.5f);
                    health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption;
                    cdr = 0;
                    casted = true;
                    if (direction == 0) {
                        float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                        float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() <= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneRebound arcaneRebound = new ArcaneRebound(body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneRebound);
                    } else {
                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() >= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneRebound arcaneRebound = new ArcaneRebound((body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneRebound);
                    }
                }
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Escort") && ArcaneEscort.numEscort <= 15) {
            GameState.charging = false;
            arcaneLight.setColor(ArcaneEscort.color);
            maxLightRadius = 400;
            forceCharge = 0;
            if(direction == 0) {
                float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);
                arcaneLight.setPosition(body.getPosition().x * PPM + width / 2 + xAim,
                        body.getPosition().y * PPM + 46 + 23 + yAim);
            } else {
                float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);
                arcaneLight.setPosition((body.getPosition().x * PPM + width / 2) + xAim,
                        (body.getPosition().y * PPM + 46 + 23) + yAim);
            }
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !GameState.options.active) {
                cdr += ControlCenter.delta;
                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                    Sounds.playSound(Sounds.arcaneEscort);
                    health -= Inventory.inventory[Inventory.selectedIndex].healthConsumption;
                    cdr = 0;
                    casted = true;
                    if (direction == 0) {
                        float xAim = (float) Math.sin(Math.toRadians(aimAngle + 45 + 90)) * (-70);
                        float yAim = (float) Math.cos(Math.toRadians(aimAngle + 45 + 90)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() <= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneEscort arcaneEscort = new ArcaneEscort(body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneEscort);
                    } else {
                        float xAim = (float) Math.cos(Math.toRadians(aimAngle - 45)) * (70);
                        float yAim = (float) Math.sin(Math.toRadians(aimAngle - 45)) * (70);

                        // shoot angle is in radians
                        if (Gdx.input.getX() >= ControlCenter.width / 2) {
                            shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        } else {
                            shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                    (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                        }
                        ArcaneEscort arcaneEscort = new ArcaneEscort((body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle, Inventory.inventory[Inventory.selectedIndex].burst*bonusArcanePercentage);
                        EntityManager.entities.add(arcaneEscort);
                    }
                }
            }
        }

        else {
            GameState.charging = false;
            eruptionHold = false;
            shadowHold = false;
            forceCharge = 0;
            cdr = 0;
        }
    }

    public void findShootAngle(float xAim, float yAim) {
        if(direction == 0) {
            // shoot angle is in radians
            if (Gdx.input.getX() <= ControlCenter.width / 2) {
                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
            } else {
                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
            }
        } else {
            // shoot angle is in radians
            if (Gdx.input.getX() >= ControlCenter.width / 2) {
                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
            } else {
                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
            }
        }
    }

    public void inputUpdate() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            EntityManager.entities.add(new Spiderling(body.getPosition().x * PPM - 300, body.getPosition().y * PPM + 100, (int)(Math.random()*50)+ 75));
        }

        if(dash) {
            if(canDash) {
                dashResetTimer += ControlCenter.delta;
                if (Gdx.input.isKeyJustPressed(Input.Keys.A) && dashResetTimer > 0.05f && dashResetTimer <= 0.25f && dashDirection == 0) {
                    body.setLinearVelocity(-50, -2);
                    dashing = true;
                    canDash = false;
                    dashResetTimer = 0;
                    dashEffect = new ParticleEffect();
                    dashEffect.load(Gdx.files.internal("particles/dashLeft"), Gdx.files.internal(""));
                    dashEffect.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
                    dashEffect.start();
                    //EntityManager.particles.add(dashEffect);
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.D) && dashResetTimer > 0.05f && dashResetTimer <= 0.25f && dashDirection == 1) {
                    body.setLinearVelocity(50, -2);
                    dashing = true;
                    canDash = false;
                    dashResetTimer = 0;
                    dashEffect = new ParticleEffect();
                    dashEffect.load(Gdx.files.internal("particles/dashRight"), Gdx.files.internal(""));
                    dashEffect.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
                    dashEffect.start();
                    //EntityManager.particles.add(dashEffect);
                }
                if(dashResetTimer > 0.25f) {
                    canDash = false;
                    dashResetTimer = 0;
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    canDash = true;
                    dashDirection = 0;
                    dashResetTimer = 0;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    canDash = true;
                    dashDirection = 1;
                    dashResetTimer = 0;
                }
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A) && !dashing) {
            horizontalForce = -1;
            currentSpeed += 0.2f;
            if(currentSpeed >= speed) {
                currentSpeed = speed;
            }
            direction = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) && !dashing) {
            horizontalForce = 1;
            currentSpeed += 0.2f;
            if(currentSpeed >= speed) {
                currentSpeed = speed;
            }
            direction = 1;
        }

        // jump test
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && canJump && !jump && !fall) {
            body.applyForceToCenter(0, 800/(body.getLinearVelocity().y/10 + 1), false);
            jump = true;
            fall = false;
            canJump = false;
            airborn = true;
            Sounds.playSound(Sounds.steps[0]);

            legsJump[0].currentIndex = 0;
            legsJump[1].currentIndex = 0;
            legsRun[0].currentIndex = 0;
            legsRun[1].currentIndex = 0;
        }
    }

    public void findAimAngle() {
        if(direction == 0) {
            aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                    Gdx.input.getY() - ControlCenter.height / 2));
            if (Gdx.input.getX() > ControlCenter.width / 2) {
                aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
            }
            armRotationRight = aimAngle + 45 - 360;
        } else {
            aimAngle = (float) Math.toDegrees(Math.atan2((Gdx.input.getX() - ControlCenter.width / 2),
                    Gdx.input.getY() - ControlCenter.height / 2));
            if (Gdx.input.getX() < ControlCenter.width/2) {
                aimAngle = (float) Math.toDegrees(Math.atan2(((ControlCenter.width - Gdx.input.getX()) -
                                ControlCenter.width / 2),
                        Gdx.input.getY() - ControlCenter.height / 2));
            }

            armRotationRight = aimAngle + 45 - 90;
        }
    }

    @Override
    public Rectangle getBound() {
        if(jump || fall) {
            return new Rectangle(body.getPosition().x*Constants.PPM, body.getPosition().y*Constants.PPM + 25, boundWidth, boundHeight - 25);
        }
        return new Rectangle(body.getPosition().x*Constants.PPM, body.getPosition().y*Constants.PPM, boundWidth, boundHeight);
    }

    public void checkMovement() {
        if(body.getLinearVelocity().x != 0 && !cameraXStopped) {
            movingHorizontal = true;
            BackgroundManager.layers[0].x -= body.getLinearVelocity().x/2f;
            BackgroundManager.layers[1].x -= body.getLinearVelocity().x/2.75f;
            BackgroundManager.layers[2].x -= body.getLinearVelocity().x/3.5f;
        } else {
            movingHorizontal = false;
        }

        if(horizontalForce == 0 && currentSpeed > 0) {
            currentSpeed -= 0.2;
            if(currentSpeed <= 0)
                currentSpeed = 0;
        }

        if(dashing)
            return;

        if(yThrust != 0) {
            body.setLinearVelocity(body.getLinearVelocity().x, velYBeforeThrust);
            velYBeforeThrust = body.getLinearVelocity().y;
        }
        if(direction == 0)
            body.setLinearVelocity(-currentSpeed + xThrust, body.getLinearVelocity().y + yThrust);
        else {
            body.setLinearVelocity(currentSpeed + xThrust, body.getLinearVelocity().y);
        }
    }

    public void cameraUpdate() {
        Vector3 position = ControlCenter.camera.position;
        camX = ControlCenter.camera.position.x + (body.getPosition().x * PPM - ControlCenter.camera.position.x)*.1f;
        camY = ControlCenter.camera.position.y + (body.getPosition().y * PPM - ControlCenter.camera.position.y + Tile.TILESIZE/2*3)*.1f;
        position.x = camX;
        position.y = camY;

        if(position.x - ControlCenter.width/2 < 0) {
            position.x = ControlCenter.width/2;
            cameraXStopped = true;
        } else if(position.x + ControlCenter.width/2 > Tile.TILESIZE*GameState.worldWidth - width/2 - 20) {
            position.x = Tile.TILESIZE*GameState.worldWidth - ControlCenter.width/2 - width/2 - 20;
            cameraXStopped = true;
        } else {
            cameraXStopped = false;
        }

        if(position.y - ControlCenter.height/2 < 0) {
            position.y = ControlCenter.height/2;
        } else if(position.y + ControlCenter.height/2 > Tile.TILESIZE*GameState.worldHeight - height/2 - 10) {
            position.y = Tile.TILESIZE*GameState.worldHeight - ControlCenter.height/2 - height/2 - 10;
        }

        ControlCenter.camera.position.set(position);

        ControlCenter.camera.update();
    }

    public static Vector3 mouseWorldPos() {
        return ControlCenter.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        Rectangle attackRectangle;
//        if(direction == 0) {
//            attackRectangle = new Rectangle(body.getPosition().x*PPM - Tile.TILESIZE*2 + width - Tile.TILESIZE/2, body.getPosition().y*PPM - Tile.TILESIZE,
//                    Tile.TILESIZE*2 + Tile.TILESIZE/2, height + Tile.TILESIZE * 2);
//        } else {
//            attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM - Tile.TILESIZE,
//                    Tile.TILESIZE*2 + Tile.TILESIZE/2, height + Tile.TILESIZE * 2);
//        }
//        batch.draw(Tiles.blackTile, attackRectangle.x, attackRectangle.y, attackRectangle.width, attackRectangle.height);

        if(tilePlacing) {
            int tileX = (int)((Player.mouseWorldPos().x + Tile.TILESIZE/2)/Tile.TILESIZE);
            int tileY = (int)((Player.mouseWorldPos().y + Tile.TILESIZE/2)/Tile.TILESIZE);
            if(!canPlace) {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.35f);
            }
            if(Inventory.inventory[Inventory.selectedIndex] != null && Inventory.inventory[Inventory.selectedIndex].texture != null)
                batch.draw(Inventory.inventory[Inventory.selectedIndex].texture, tileX*Constants.PPM - Tile.TILESIZE/2, tileY*Constants.PPM - Tile.TILESIZE/2, Tile.TILESIZE, Tile.TILESIZE);
            if(!canPlace) {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);
            }
        }

        if(eruptionHold)
            eruptionCharge.draw(batch);
        else {
            if(!eruptionCharge.isComplete()) {
                eruptionCharge.draw(batch);
            }
        }

        if(shadowHold)
            shadowCharge.draw(batch);
        else {
            if(!shadowCharge.isComplete()) {
                shadowCharge.draw(batch);
            }
        }

        if(dashEffect != null && !dashEffect.isComplete()) {
            dashEffect.draw(batch);
        }

        if(blink) {
            shadowExplosion.draw(batch);
        } else {
            if(!shadowExplosion.isComplete()) {
                shadowExplosion.draw(batch);
            }
        }

        if(horizontalForce != 0) {
            if(jump || fall) {
                jumpAnimation = true;
                batch.draw(legsJump[direction].getFrame(), body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
            } else {
                legAnimation = true;
                batch.draw(legsRun[direction].getFrame(), body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
            }
            if(!onhold) {
                armAnimation = true;
                batch.draw(armsRun[direction].getFrame(),
                        body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
            } else {
                if(Inventory.inventory[Inventory.selectedIndex] != null && (Inventory.inventory[Inventory.selectedIndex].heavy || Inventory.inventory[Inventory.selectedIndex] instanceof Arcane)) {
                    batch.draw(Entities.doubleArmsHold[direction], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5,
                            57, 74,
                            114, 114, 1f, 1f, armRotationRight);
                } else {
                    batch.draw(Entities.armsHoldRight[direction], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5,
                            57, 74,
                            114, 114, 1f, 1f, armRotationRight);
                    batch.draw(Entities.armsHoldLeft[direction], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5,
                            57, 74,
                            114, 114, 1f, 1f, -armRotationLeft);
                }

                if(Inventory.inventory[Inventory.selectedIndex] != null && (Inventory.inventory[Inventory.selectedIndex] instanceof Melee ||
                        Inventory.inventory[Inventory.selectedIndex] instanceof Arcane || Inventory.inventory[Inventory.selectedIndex] instanceof Throwing)) {
                    if (direction == 0) {
                        batch.draw(Inventory.inventory[Inventory.selectedIndex].display[direction],
                                body.getPosition().x * PPM + width / 2 - 33 - (Inventory.inventory[Inventory.selectedIndex].width - Inventory.inventory[Inventory.selectedIndex].holdX),
                                body.getPosition().y * PPM + 46 - Inventory.inventory[Inventory.selectedIndex].holdY,
                                Inventory.inventory[Inventory.selectedIndex].holdX + 33 + (Inventory.inventory[Inventory.selectedIndex].width/2 - Inventory.inventory[Inventory.selectedIndex].holdX)*2,
                                Inventory.inventory[Inventory.selectedIndex].holdY + 23,
                                Inventory.inventory[Inventory.selectedIndex].width, Inventory.inventory[Inventory.selectedIndex].height,
                                1f, 1f, armRotationRight);
                    } else {
                        batch.draw(Inventory.inventory[Inventory.selectedIndex].display[direction],
                                body.getPosition().x * PPM + width / 2 + 33 - Inventory.inventory[Inventory.selectedIndex].holdX,
                                body.getPosition().y * PPM + 46 - Inventory.inventory[Inventory.selectedIndex].holdY,
                                Inventory.inventory[Inventory.selectedIndex].holdX - 33,
                                Inventory.inventory[Inventory.selectedIndex].holdY + 23,
                                Inventory.inventory[Inventory.selectedIndex].width, Inventory.inventory[Inventory.selectedIndex].height,
                                1f, 1f, armRotationRight);
                    }
                }
            }
            bodyAnimation = true;
            batch.draw(chestRun[direction].getFrame(),
                    body.getPosition().x * PPM + width / 2 - 57,
                    body.getPosition().y * PPM - 5, 114, 114);

            headAnimation = true;
            batch.draw(headRun[direction].getFrame(),
                    body.getPosition().x * PPM + width / 2 - 57,
                    body.getPosition().y * PPM - 5, 114, 114);

        } else {
            if(headAnimation && headRun[direction].currentIndex > 15) {
                batch.draw(headRun[direction].getFrame(),
                        body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
            } else {
                headAnimation = false;
                batch.draw(Entities.headRun[direction][0], body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
                headRun[0].currentIndex = 0;
                headRun[1].currentIndex = 0;
            }

            if(bodyAnimation && chestRun[direction].currentIndex > 15) {
                batch.draw(chestRun[direction].getFrame(),
                        body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
            } else {
                bodyAnimation = false;
                batch.draw(Entities.chestRun[direction][0], body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
                chestRun[0].currentIndex = 0;
                chestRun[1].currentIndex = 0;
            }

            if (!onhold) {
                if(armAnimation && armsRun[direction].currentIndex > 15) {
                    batch.draw(armsRun[direction].getFrame(),
                            body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5, 114, 114);
                } else {
                    armAnimation = false;
                    batch.draw(Entities.armRun[direction][0], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5, 114, 114);
                    armsRun[0].currentIndex = 0;
                    armsRun[1].currentIndex = 0;
                }
            } else {
                armAnimation = false;
                if(!weaponActive && Inventory.inventory[Inventory.selectedIndex].heavy) {
                    batch.draw(Entities.armsHoldRight[direction], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5, 114, 114);
                    batch.draw(Entities.armsHoldLeft[direction], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5, 114, 114);

                    if (direction == 1) {
                        batch.draw(Inventory.inventory[Inventory.selectedIndex].texture,
                                body.getPosition().x * PPM + width / 2 - 57 + 90 - Inventory.inventory[Inventory.selectedIndex].holdX,
                                body.getPosition().y * PPM - 5 + 51 - Inventory.inventory[Inventory.selectedIndex].holdY,
                                Inventory.inventory[Inventory.selectedIndex].width, Inventory.inventory[Inventory.selectedIndex].height);
                    } else {
                        batch.draw(Inventory.inventory[Inventory.selectedIndex].display[direction],
                                body.getPosition().x * PPM + width / 2 - 57 + 24 - Inventory.inventory[Inventory.selectedIndex].holdX,
                                body.getPosition().y * PPM - 5 + 51 - Inventory.inventory[Inventory.selectedIndex].holdY,
                                Inventory.inventory[Inventory.selectedIndex].width, Inventory.inventory[Inventory.selectedIndex].height);
                    }
                } else {
                    if(Inventory.inventory[Inventory.selectedIndex] != null && (Inventory.inventory[Inventory.selectedIndex].heavy || Inventory.inventory[Inventory.selectedIndex] instanceof Arcane)) {
                        batch.draw(Entities.doubleArmsHold[direction], body.getPosition().x * PPM + width / 2 - 57,
                                body.getPosition().y * PPM - 5,
                                57, 74,
                                114, 114, 1f, 1f, armRotationRight);
                    } else {
                        batch.draw(Entities.armsHoldRight[direction], body.getPosition().x * PPM + width / 2 - 57,
                                body.getPosition().y * PPM - 5,
                                57, 74,
                                114, 114, 1f, 1f, armRotationRight);
                        batch.draw(Entities.armsHoldLeft[direction], body.getPosition().x * PPM + width / 2 - 57,
                                body.getPosition().y * PPM - 5,
                                57, 74,
                                114, 114, 1f, 1f, -armRotationLeft);
                    }
                    if(Inventory.inventory[Inventory.selectedIndex] != null && (Inventory.inventory[Inventory.selectedIndex] instanceof Melee ||
                            Inventory.inventory[Inventory.selectedIndex] instanceof Arcane || Inventory.inventory[Inventory.selectedIndex] instanceof Throwing)) {
                        if (direction == 0) {
                            batch.draw(Inventory.inventory[Inventory.selectedIndex].display[direction],
                                    body.getPosition().x * PPM + width / 2 - 33 - (Inventory.inventory[Inventory.selectedIndex].width - Inventory.inventory[Inventory.selectedIndex].holdX),
                                    body.getPosition().y * PPM - 5 + 51 - Inventory.inventory[Inventory.selectedIndex].holdY,
                                    Inventory.inventory[Inventory.selectedIndex].holdX + 33 + (Inventory.inventory[Inventory.selectedIndex].width/2 - Inventory.inventory[Inventory.selectedIndex].holdX)*2,
                                    Inventory.inventory[Inventory.selectedIndex].holdY + 23,
                                    Inventory.inventory[Inventory.selectedIndex].width, Inventory.inventory[Inventory.selectedIndex].height,
                                    1f, 1f, armRotationRight);
                        } else {
                            batch.draw(Inventory.inventory[Inventory.selectedIndex].display[direction],
                                    body.getPosition().x * PPM + width / 2 + 33 - Inventory.inventory[Inventory.selectedIndex].holdX,
                                    body.getPosition().y * PPM + 46 - Inventory.inventory[Inventory.selectedIndex].holdY,
                                    Inventory.inventory[Inventory.selectedIndex].holdX - 33,
                                    Inventory.inventory[Inventory.selectedIndex].holdY + 23,
                                    Inventory.inventory[Inventory.selectedIndex].width, Inventory.inventory[Inventory.selectedIndex].height,
                                    1f, 1f, armRotationRight);
                        }
                    }
                }
                armsRun[0].currentIndex = 0;
                armsRun[1].currentIndex = 0;
            }

            if (jump || fall) {
                jumpAnimation = true;
                batch.draw(legsJump[direction].getFrame(), body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
            } else {
                if(legAnimation && legsRun[direction].currentIndex > 15) {
                    batch.draw(legsRun[direction].getFrame(), body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5, 114, 114);
                } else {
                    legAnimation = false;
                    jumpAnimation = false;
                    batch.draw(Entities.legRun[direction][0], body.getPosition().x * PPM + width / 2 - 57,
                            body.getPosition().y * PPM - 5, 114, 114);
                    legsRun[0].currentIndex = 0;
                    legsRun[1].currentIndex = 0;
                    legsJump[0].currentIndex = 0;
                    legsJump[1].currentIndex = 0;
                }
            }
        }
        batch.end();
    }

    @Override
    public void finish() {

    }
}
