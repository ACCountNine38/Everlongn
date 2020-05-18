package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.everlongn.assets.Entities;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.world.BackgroundManager;

import static com.everlongn.utils.Constants.PPM;

public class Player extends Creature {

    public static boolean jump, fall, canJump = true, movingHorizontal, airborn, onhold;

    private PointLight light;
    private int direction = 1;
    private boolean cameraXStopped, fallUpdate;
    private float armsTimer, headTimer, legsTimer, bodyTimer, airbornTime, fallUpdateTimer, armRotation, currentSpeed;
    /*
      0 - left
      1 - right
    */
    private Animation[] legsRun = new Animation[2];
    private Animation[] legsJump = new Animation[2];
    private Animation[] armsRun = new Animation[2];
    private Animation[] headRun = new Animation[2];
    private Animation[] chestRun = new Animation[2];

    public static int currentChunkX, currentChunkY, horizontalForce;

    private boolean legAnimation, armAnimation, bodyAnimation, headAnimation, jumpAnimation;

    public Player(ControlCenter c, float x, float y, int width, int height, float density) {
        super(c, x, y, width, height, density);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        speed = 5;

        type.add("player");
        light = new PointLight(GameState.rayHandler, 100, Color.WHITE, 250/PPM,
                body.getPosition().x,
                body.getPosition().y);

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
    }

    @Override
    public void tick() {
        currentChunkX = (int)(body.getPosition().x/GameState.chunkSize);
        currentChunkY = (int)(body.getPosition().y/GameState.chunkSize);

        light.setPosition(body.getPosition().x,
                body.getPosition().y);
        cameraUpdate();
        inputUpdate();
        animationUpdate();

        armsTimer += Gdx.graphics.getDeltaTime();
        headTimer += Gdx.graphics.getDeltaTime();
        legsTimer += Gdx.graphics.getDeltaTime();
        bodyTimer += Gdx.graphics.getDeltaTime();

        if(body.getLinearVelocity().y <= 0 && airbornTime > 0.01 && !fall && jump) {
            fall = true;
            jump = false;
            fallUpdateTimer = 0;
            fallUpdate = true;
        }

        if(fallUpdate) {
            fallUpdateTimer += Gdx.graphics.getDeltaTime();
        }

        if(fall && body.getLinearVelocity().y > -0.5f && fallUpdateTimer > 0.3f) {
            fall = false;
            fallUpdate = false;
        }

        if(jump && !Gdx.input.isKeyPressed(Input.Keys.W)) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y/1.15f);
        }

        if(!canJump) {
            airbornTime += Gdx.graphics.getDeltaTime();
            if(airbornTime >= 0.5) {
                canJump = true;
            }
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

        armRotation = (float)Math.atan2(((body.getPosition().y * PPM - 5) - Gdx.input.getY()), ((body.getPosition().x * PPM + width / 2 - 57) - Gdx.input.getX()));
    }

    public void inputUpdate() {
        horizontalForce = 0;
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

        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && canJump && (Math.abs(body.getLinearVelocity().y) < 0.334 ||
                (body.getLinearVelocity().y > speed - 0.44 && body.getLinearVelocity().y < speed - 0.3))) {
            body.applyForceToCenter(0, 300/(body.getLinearVelocity().y/8 + 1), false);
            jump = true;
            canJump = false;
            airbornTime = 0;
            airborn = true;

            legsJump[0].currentIndex = 0;
            legsJump[1].currentIndex = 0;
            legsRun[0].currentIndex = 0;
            legsRun[1].currentIndex = 0;
//            legsJump[0] = new Animation<>(1f/50f, Entities.legJump[0]);
//            legsJump[1] = new Animation<>(1f/50f, Entities.legJump[1]);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            System.exit(1);
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
        position.x = ControlCenter.camera.position.x + (body.getPosition().x * PPM - ControlCenter.camera.position.x)*.1f;
        position.y = ControlCenter.camera.position.y + (body.getPosition().y * PPM - ControlCenter.camera.position.y + Tile.TILESIZE/2*3)*.1f;

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

    public void render(SpriteBatch batch) {
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();

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
                batch.draw(Entities.armsHold[direction],
                        body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
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
                batch.draw(Entities.armsHold[direction], body.getPosition().x * PPM + width / 2 - 57,
                        body.getPosition().y * PPM - 5, 114, 114);
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
