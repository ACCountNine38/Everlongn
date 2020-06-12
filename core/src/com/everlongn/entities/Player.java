package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.everlongn.assets.Entities;
import com.everlongn.entities.dream.Scavenger;
import com.everlongn.entities.projectiles.*;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Arcane;
import com.everlongn.items.Inventory;
import com.everlongn.items.Item;
import com.everlongn.items.Melee;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;
import com.everlongn.world.BackgroundManager;

import java.util.LinkedList;
import java.util.Queue;

import static com.everlongn.utils.Constants.PPM;

public class Player extends Creature {

    public static boolean movingHorizontal, onhold,
            weaponActive = true, meleeAttack, meleeRecharge, halt;

    private int direction = 1; // 0 - left, 1- right

    private boolean cameraXStopped, armSwingUp = true;

    // global item related variables
    public static boolean inCombat, inventoryHold, blink, blinkAlphaMax;
    public static Rectangle itemCollectBound;

    // special item related variables
    private boolean eruptionHold, shadowHold;
    public static Queue<Shadow> shadows = new LinkedList<Shadow>();
    public PointLight arcaneLight;
    public boolean casted;
    public float arcaneLightSize, maxLightRadius;

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

    public static int currentChunkX, currentChunkY, horizontalForce;

    private ParticleEffect smoke, eruptionCharge, shadowCharge, shadowExplosion;
    public static float forceCharge, forceMax, airbornTimer, particleTimer;

    // Testing variables
    public PointLight testLight;

    public Player(ControlCenter c, float x, float y, int width, int height, float density) {
        super(c, x, y, width, height, density, 5);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        resetHealth(100);
        baseRegenAmount = 0.1f;
        type.add("player");

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
                Constants.BIT_PLAYER, (short)(Constants.BIT_ENEMY | Constants.BIT_TILE), (short)0, this);

        // particle effects
        eruptionCharge = new ParticleEffect();
        eruptionCharge.load(Gdx.files.internal("particles/eruptionHold"), Gdx.files.internal(""));

        shadowCharge = new ParticleEffect();
        shadowCharge.load(Gdx.files.internal("particles/shadowHold"), Gdx.files.internal(""));

        shadowExplosion = new ParticleEffect();
        shadowExplosion.load(Gdx.files.internal("particles/shadowExplosion"), Gdx.files.internal(""));

        testLight = new PointLight(GameState.rayHandler, 400, Color.BLACK, 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM + 60);
        arcaneLight = new PointLight(GameState.rayHandler, 400, new Color(0.05f, 0.05f, 0.05f, 1), 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM + 80);

        itemCollectBound = new Rectangle(body.getPosition().x*PPM + 10, body.getPosition().y*PPM - height/2 + height/3, 10, 10);
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
            airbornTimer += Gdx.graphics.getDeltaTime();
            if(airbornTimer > 0.25) {
                airborn = false;
            }
        }

        // check fall damage
        if(Math.abs(previousVelY - body.getLinearVelocity().y) > 15) {
            health -= (Math.abs(previousVelY - body.getLinearVelocity().y)-15) * 5;
        }

        // check jump timer
        if(body.getLinearVelocity().y > previousVelY - 0.01 && body.getLinearVelocity().y < previousVelY + 0.01) {
            yChangeTimer += Gdx.graphics.getDeltaTime();
            if (yChangeTimer > 0.1) {
                fall = false;
                canJump = true;
                yChangeTimer = 0;
            }
        } else {
            fall = true;
            canJump = false;
            previousVelY = body.getLinearVelocity().y;
            yChangeTimer = 0;
        }

        itemCollectBound.setPosition(body.getPosition().x*PPM + 10, body.getPosition().y*PPM + 80);
    }

    @Override
    public void tick() {
        checkSpecialCase();

        testLight.setPosition(body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM);

        currentChunkX = (int)(body.getPosition().x/GameState.chunkSize);
        currentChunkY = (int)(body.getPosition().y/GameState.chunkSize);

        cameraUpdate();
        inputUpdate();
        animationUpdate();

        regenerate();

        if(body.getLinearVelocity().y <= 0 && jump && !airborn) {
            fall = true;
            jump = false;
        }

        if(body.getLinearVelocity().y < -50) {
            body.setLinearVelocity(body.getLinearVelocity().x, -50);
        }

        if(jump && !Gdx.input.isKeyPressed(Input.Keys.W) && body.getLinearVelocity().y > 0) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y/1.15f);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            GameState.lightsOn = !GameState.lightsOn;
        }

        checkItemOnHold();
    }

    public void animationUpdate() {
        if(legAnimation) {
            legsRun[0].tick(Gdx.graphics.getDeltaTime());
            legsRun[1].tick(Gdx.graphics.getDeltaTime());
        }

        if(jumpAnimation) {
            legsJump[0].tick(Gdx.graphics.getDeltaTime());
            legsJump[1].tick(Gdx.graphics.getDeltaTime());
        }

        if(armAnimation) {
            armsRun[0].tick(Gdx.graphics.getDeltaTime());
            armsRun[1].tick(Gdx.graphics.getDeltaTime());
        }

        if(bodyAnimation) {
            chestRun[0].tick(Gdx.graphics.getDeltaTime());
            chestRun[1].tick(Gdx.graphics.getDeltaTime());
        }

        if(headAnimation) {
            headRun[0].tick(Gdx.graphics.getDeltaTime());
            headRun[1].tick(Gdx.graphics.getDeltaTime());
        }

        if(eruptionHold)
            eruptionCharge.update(Gdx.graphics.getDeltaTime());
        else {
            if(!eruptionCharge.isComplete()) {
                eruptionCharge.update(Gdx.graphics.getDeltaTime());
            }
        }

        if(shadowHold)
            shadowCharge.update(Gdx.graphics.getDeltaTime());
        else {
            if(!shadowCharge.isComplete()) {
                shadowCharge.update(Gdx.graphics.getDeltaTime());
            }
        }

        if(blink) {
            shadowExplosion.update(Gdx.graphics.getDeltaTime());
        } else {
            if(!shadowExplosion.isComplete()) {
                shadowExplosion.update(Gdx.graphics.getDeltaTime());
            }
        }
    }

    public void checkItemOnHold() {
        if(Inventory.inventory[Inventory.selectedIndex] == null) {
            return;
        }
        if(Inventory.inventory[Inventory.selectedIndex].id >= 200 && Inventory.inventory[Inventory.selectedIndex].id < 300) {
            onhold = true;
        } else {
            onhold = false;
        }

        if((inCombat && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) || inventoryHold) {
            inCombat = false;
        }

        if(inventoryHold) {
            return;
        }

        if(Inventory.inventory[Inventory.selectedIndex] instanceof Melee) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !meleeAttack) {
                meleeAttack = true;
                inCombat = true;
                targetAngle = 155;
                if (aimAngle < 155)
                    armSwingUp = true;
                else {
                    targetAngle = 0;
                }
            }
            checkMeleeCombat();
        } else if(Inventory.inventory[Inventory.selectedIndex] instanceof Arcane) {
            meleeAttack = false;
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                inCombat = true;
            }
            checkArcaneCombat();
        }

        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(eruptionHold)
                eruptionHold = false;
            if(shadowHold)
                shadowHold = false;
            forceCharge = 0;
            cdr = 0;
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
                        aimAngle+=5;
                    } else if(aimAngle > tempAngle + 5) {
                        aimAngle-=5;
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
                        }
                    } else {
                        if(Inventory.inventory[Inventory.selectedIndex].heavy) {
                            if (!fall && !jump) {
                                aimAngle += Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                            } else {
                                aimAngle += 1;
                                if(haltForce < 45) {
                                    haltForce += 1;
                                }
                                halt = true;
                            }
                        } else {
                            aimAngle += Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                        }
                        if (aimAngle > -targetAngle) {
                            aimAngle = -targetAngle;
                            meleeRecharge = true;
                            Rectangle attackRectangle = new Rectangle(body.getPosition().x*PPM - Inventory.inventory[Inventory.selectedIndex].width, body.getPosition().y*PPM,
                                    Inventory.inventory[Inventory.selectedIndex].width + width, Inventory.inventory[Inventory.selectedIndex].height);

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
                                        }
                                    }
                                }
                            } else {
                                for(int i = 0; i < EntityManager.entities.size(); i++) {
                                    if(EntityManager.entities.get(i).getBound().overlaps(attackRectangle) && EntityManager.entities.get(i) != this) {
                                        if(EntityManager.entities.get(i) instanceof Creature) {
                                            Creature c = (Creature)EntityManager.entities.get(i);
                                            c.stunned = true;
                                            float force = Inventory.inventory[Inventory.selectedIndex].force + (float)(Math.random()*Inventory.inventory[Inventory.selectedIndex].force/4);
                                            EntityManager.entities.get(i).body.applyForceToCenter(
                                                    -force, 250, false);
                                        }
                                        break;
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
                        aimAngle+=5;
                    } else if(aimAngle > tempAngle + 5) {
                        aimAngle-=5;
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
                        }
                    } else {
                        if(Inventory.inventory[Inventory.selectedIndex].heavy) {
                            if (!fall && !jump) {
                                aimAngle -= Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                            } else {
                                aimAngle -= 1;
                                if(haltForce < 45) {
                                    haltForce += 1;
                                }
                                halt = true;
                            }
                        } else {
                            aimAngle -= Inventory.inventory[Inventory.selectedIndex].swingSpeed;
                        }

                        if (aimAngle < targetAngle) {
                            aimAngle = targetAngle;
                            meleeRecharge = true;

                            Rectangle attackRectangle = new Rectangle(body.getPosition().x*PPM, body.getPosition().y*PPM,
                                    Inventory.inventory[Inventory.selectedIndex].width + width, Inventory.inventory[Inventory.selectedIndex].height);

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
                                                    force, 250, false);
                                        }
                                    }
                                }
                            } else {
                                for(int i = 0; i < EntityManager.entities.size(); i++) {
                                    if(EntityManager.entities.get(i).getBound().overlaps(attackRectangle) && EntityManager.entities.get(i) != this) {
                                        if(EntityManager.entities.get(i) instanceof Creature) {
                                            Creature c = (Creature)EntityManager.entities.get(i);
                                            c.stunned = true;
                                            float force = Inventory.inventory[Inventory.selectedIndex].force + (float)(Math.random()*Inventory.inventory[Inventory.selectedIndex].force/4);
                                            EntityManager.entities.get(i).body.applyForceToCenter(
                                                    force, 250, false);
                                        }
                                        break;
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

        if(Inventory.inventory[Inventory.selectedIndex].name.equals("Shadow Manipulator")) {
            maxLightRadius = 0;
            forceMax = 1000;
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if(forceCharge < forceMax) {
                    forceCharge += 7.5;
                }
                cdr += Gdx.graphics.getDeltaTime();

                particleTimer+=Gdx.graphics.getDeltaTime();
                if(particleTimer > 0.25) {
                    shadowHold = true;
                    shadowCharge.start();
                    particleTimer = 0;
                }

                float xAim, yAim;

                if(direction == 0) {
                    xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180)) * (80);
                    yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180)) * (-80);
                    shadowCharge.getEmitters().first().setPosition(
                            body.getPosition().x * PPM + width / 2 + xAim,
                            body.getPosition().y * PPM + 69 + yAim);
                } else {
                    xAim = (float) Math.cos(Math.toRadians(-armRotationRight)) * (80);
                    yAim = (float) Math.sin(Math.toRadians(-armRotationRight)) * (-80);
                    shadowCharge.getEmitters().first().setPosition(
                            body.getPosition().x * PPM + width / 2 + xAim, 69 + body.getPosition().y * PPM + yAim);
                }
            } else {
                if(shadowHold) {
                    if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                        cdr = 0;

                        if (direction == 0) {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180 - 30)) * (80);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180 - 30)) * (-80);

                            // shoot angle is in radians
                            if (Gdx.input.getX() <= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            Shadow shadow = new Shadow(c,
                                    body.getPosition().x * PPM + width / 2 + xAim,
                                    body.getPosition().y * PPM + 69 + yAim, width, height,
                                    this, direction, (float)Math.sin(shootAngle + Math.PI/30)*forceCharge, -(float)Math.cos(shootAngle + Math.PI/30)*forceCharge, false);

                            EntityManager.entities.add(shadow);
                            shadows.add(shadow);
                        } else {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 30)) * (80);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 30)) * (-80);

                            // shoot angle is in radians
                            if (Gdx.input.getX() >= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            Shadow shadow = new Shadow(c,
                                    body.getPosition().x * PPM + width / 2 + xAim,
                                    69 + body.getPosition().y * PPM + yAim, width, height,
                                    this, direction, (float)Math.sin(shootAngle - Math.PI/30)*forceCharge, -(float)Math.cos(shootAngle - Math.PI/30)*forceCharge, false);
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

                    Shadow shadow = shadows.poll();
                    body.setTransform(shadow.body.getPosition().x, shadow.body.getPosition().y, 0);
                    body.setLinearVelocity(0, 0);
                    shadow.life = Shadow.maxLife;
                }
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Caster")) {
            arcaneLight.setColor(ArcaneTrail.color);
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
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                cdr += Gdx.graphics.getDeltaTime();

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
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
                        ArcaneTrail arcaneTrail = new ArcaneTrail(c,
                                body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle + (float)Math.PI/15);

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
                        ArcaneTrail arcaneTrail = new ArcaneTrail(c,
                                (body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle - (float)Math.PI/15);
                        EntityManager.entities.add(arcaneTrail);
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
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if(forceCharge < forceMax) {
                    forceCharge += 0.5;
                }
                cdr += Gdx.graphics.getDeltaTime();

                particleTimer+=Gdx.graphics.getDeltaTime();
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

            } else {
                if(eruptionHold) {
                    if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
                        cdr = 0;

                        if (direction == 0) {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 180 - 30)) * (76);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 180 - 30)) * (-76);

                            // shoot angle is in radians
                            if (Gdx.input.getX() <= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 - (Gdx.input.getX() - ControlCenter.width / 2) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            ArcaneEruption arcaneTrail = new ArcaneEruption(c,
                                    body.getPosition().x * PPM + width / 2 + xAim,
                                    body.getPosition().y * PPM + 69 + yAim,
                                    10, direction, shootAngle,
                                    (float)Math.sin(shootAngle + Math.PI/30)*forceCharge, -(float)Math.cos(shootAngle + Math.PI/30)*forceCharge);
                            EntityManager.entities.add(arcaneTrail);
                        } else {
                            float xAim = (float) Math.cos(Math.toRadians(-armRotationRight + 30)) * (76);
                            float yAim = (float) Math.sin(Math.toRadians(-armRotationRight + 30)) * (-76);

                            // shoot angle is in radians
                            if (Gdx.input.getX() >= ControlCenter.width / 2) {
                                shootAngle = (float) Math.atan2(((Gdx.input.getX() + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            } else {
                                shootAngle = (float) Math.atan2(((ControlCenter.width / 2 + (ControlCenter.width / 2 - Gdx.input.getX()) + ControlCenter.camera.position.x - ControlCenter.width / 2) - ((body.getPosition().x * PPM + width / 2) + xAim)),
                                        (Gdx.input.getY() + ControlCenter.camera.position.y - ControlCenter.height / 2) - ((body.getPosition().y * PPM + 46 + 23) + yAim));
                            }
                            ArcaneEruption arcaneTrail = new ArcaneEruption(c,
                                    body.getPosition().x * PPM + width / 2 + xAim,
                                    69 + body.getPosition().y * PPM + yAim,
                                    10, direction, shootAngle,
                                    (float)Math.sin(shootAngle - Math.PI/30)*forceCharge, -(float)Math.cos(shootAngle - Math.PI/30)*forceCharge);
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
            arcaneLight.setColor(ArcaneRebound.color);
            maxLightRadius = 600;
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
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                cdr += Gdx.graphics.getDeltaTime();

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
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
                        ArcaneRebound arcaneRebound = new ArcaneRebound(c,
                                body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle);
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
                        ArcaneRebound arcaneRebound = new ArcaneRebound(c,
                                (body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle);
                        EntityManager.entities.add(arcaneRebound);
                    }
                }
            }
        }

        else if(Inventory.inventory[Inventory.selectedIndex].name.equals("Escort") && ArcaneEscort.numEscort <= 15) {
            arcaneLight.setColor(ArcaneEscort.color);
            maxLightRadius = 400;
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
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                cdr += Gdx.graphics.getDeltaTime();

                if(cdr >= Inventory.inventory[Inventory.selectedIndex].refreshSpeed) {
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
                        ArcaneEscort arcaneEscort = new ArcaneEscort(c,
                                body.getPosition().x * PPM + width / 2 + xAim,
                                body.getPosition().y * PPM + 46 + 23 + yAim,
                                1, direction, shootAngle);
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
                        ArcaneEscort arcaneEscort = new ArcaneEscort(c,
                                (body.getPosition().x * PPM + width / 2) + xAim,
                                (body.getPosition().y * PPM + 46 + 23) + yAim,
                                1, direction, shootAngle);
                        EntityManager.entities.add(arcaneEscort);
                    }
                }
            }
        }

        else {
            eruptionHold = false;
            shadowHold = false;
            forceCharge = 0;
            cdr = 0;
        }
    }

    public void inputUpdate() {
        horizontalForce = 0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            EntityManager.entities.add(new Scavenger(c,  body.getPosition().x * PPM - 300, body.getPosition().y * PPM + 100));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            EntityManager.items.add(Item.stone.createNew(body.getPosition().x * PPM - 300, body.getPosition().y * PPM + 100, 1));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            int xForce = 200;
            if(direction == 0)
                xForce = -200;
            int yForce = 100;
            EntityManager.entities.add(new Shadow(c, body.getPosition().x * PPM + width/2, body.getPosition().y * PPM, width, height, this, direction, xForce, yForce, false));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalForce = -1;
            currentSpeed += 0.2f;
            if(currentSpeed >= speed) {
                currentSpeed = speed;
            }
            direction = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalForce = 1;
            currentSpeed += 0.2f;
            if(currentSpeed >= speed) {
                currentSpeed = speed;
            }
            direction = 1;
        }

        if(horizontalForce == 0 && currentSpeed > 0) {
            currentSpeed -= 0.2;
            if(currentSpeed <= 0)
                currentSpeed = 0;
        }

        // jump
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && canJump && !jump && !fall) {
            body.applyForceToCenter(0, 800/(body.getLinearVelocity().y/10 + 1), false);
            jump = true;
            fall = false;
            canJump = false;
            airborn = true;

            legsJump[0].currentIndex = 0;
            legsJump[1].currentIndex = 0;
            legsRun[0].currentIndex = 0;
            legsRun[1].currentIndex = 0;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            System.exit(1);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            health -= 10;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            health += 10;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
            ControlCenter.DEBUG = !ControlCenter.DEBUG;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F5)) {
            ControlCenter.DEBUG_RENDER = !ControlCenter.DEBUG_RENDER;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
            speed += 5;
        }

        if(body.getLinearVelocity().x != 0 && !cameraXStopped) {
            movingHorizontal = true;
            BackgroundManager.layers[0].x -= body.getLinearVelocity().x/4f;
            BackgroundManager.layers[1].x -= body.getLinearVelocity().x/5.5f;
            BackgroundManager.layers[2].x -= body.getLinearVelocity().x/7f;
        } else {
            movingHorizontal = false;
        }

        if(direction == 0)
            body.setLinearVelocity(-currentSpeed, body.getLinearVelocity().y);
        else {
            body.setLinearVelocity(currentSpeed, body.getLinearVelocity().y);
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

                if(Inventory.inventory[Inventory.selectedIndex] != null) {
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
                    if(Inventory.inventory[Inventory.selectedIndex] != null) {
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
}
