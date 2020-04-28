package com.everlongn.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.everlongn.assets.Images;
import com.everlongn.game.ControlCenter;
import com.everlongn.game.Inputs;
import com.everlongn.game.TextManager;
import com.everlongn.tiles.Tile;
import com.everlongn.world.WorldGenerator;

import java.awt.*;

import static com.everlongn.utils.Constants.PPM;

public class Player extends Creature {

    private PointLight light;

    public Player(ControlCenter c, float x, float y, int width, int height) {
        super(c, x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        speed = 5;

        type.add("player");
        light = new PointLight(WorldGenerator.rayHandler, 100, Color.BLACK, 250/PPM,
                body.getPosition().x,
                body.getPosition().y);
    }

    @Override
    public void tick() {
        light.setPosition(body.getPosition().x,
                body.getPosition().y);
        inputUpdate();
        cameraUpdate();
    }

    public void inputUpdate() {
        int horizontalForce = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalForce = -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalForce = 1;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            body.applyForceToCenter(0, 800, false);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            System.exit(1);
        }
        body.setLinearVelocity(horizontalForce*5, body.getLinearVelocity().y);
    }

    public void cameraUpdate() {
        Vector3 position = ControlCenter.camera.position;
        position.x = body.getPosition().x * PPM; //getting back to scale by *PPM
        position.y = body.getPosition().y * PPM;
        ControlCenter.camera.position.set(position);

        ControlCenter.camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        TextManager.render(x + " " + y);

        batch.draw(Images.temp, body.getPosition().x*PPM - width/2,
                body.getPosition().y*PPM - height/2, width, height);

        batch.setProjectionMatrix(c.getCamera().combined);
    }
}
