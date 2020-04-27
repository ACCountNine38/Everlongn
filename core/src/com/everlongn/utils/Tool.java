package com.everlongn.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.everlongn.world.WorldGenerator;

public class Tool {
    public static Body createBox(int x, int y, int width, int height, boolean isStatic) {
        Body body;
        // describes the physical properties the body have
        BodyDef def = new BodyDef();

        if(isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }

        def.position.set(x/Constants.PPM, y/Constants.PPM);
        def.fixedRotation = true;
        // creates the body and puts it into the world
        body = WorldGenerator.world.createBody(def);
        PolygonShape shape = new PolygonShape();
        //size is taken from the center, size 50 by 50
        shape.setAsBox(width/2/Constants.PPM, height/2/Constants.PPM); // divide PPM to turn into box2D units
        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }
}
