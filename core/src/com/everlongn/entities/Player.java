package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.everlongn.assets.Backgrounds;
import com.everlongn.assets.Tiles;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.world.BackgroundManager;

import static com.everlongn.utils.Constants.PPM;

public class Player extends Creature {

    private PointLight light;
    private PointLight sun;
    private int direction = 1;
    private boolean cameraXStopped;
    /*
      0 - left
      1 - right
    */

    private Sprite test = new Sprite(Tiles.temp);

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
    }

    @Override
    public void tick() {
        light.setPosition(body.getPosition().x,
                body.getPosition().y);
        cameraUpdate();
        inputUpdate();
    }

    public void updatePlayer() {
        x = body.getPosition().x*PPM;
        y = body.getPosition().y*PPM;
    }

    public void inputUpdate() {
        int horizontalForce = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalForce = -1;
            if(direction == 1) {
                direction = 0;
                test.flip(true, false);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalForce = 1;
            if(direction == 0) {
                direction = 1;
                test.flip(true, false);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && body.getLinearVelocity().y == 0) {
            body.applyForceToCenter(0, 300, false);
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
            if(direction == 1) {
                BackgroundManager.layers[0].x -= 1.5f;
                BackgroundManager.layers[1].x -= 1f;
                BackgroundManager.layers[2].x -= .5f;
            } else {
                BackgroundManager.layers[0].x += 1.5f;
                BackgroundManager.layers[1].x += 1f;
                BackgroundManager.layers[2].x += .5f;
            }
        }

        body.setLinearVelocity(horizontalForce*speed, body.getLinearVelocity().y);
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
        batch.begin();
        batch.draw(test, body.getPosition().x*PPM + width/2 - 40,
                body.getPosition().y*PPM - 5, 80, 115);
        batch.end();
    }
}
