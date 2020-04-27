package com.everlongn.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Inputs {
    public static Vector2 keyforce = new Vector2();
    //a vector has a built in x and y

    public static void tick() {
        keyforce.x = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            keyforce.x -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            keyforce.x += 1;
        }

        keyforce.y = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            keyforce.y -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            keyforce.y += 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            System.exit(1);
        }
    }
}
