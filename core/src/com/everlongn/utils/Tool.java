package com.everlongn.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;

public class Tool {
    public static Body createEntity(int x, int y, int width, int height, boolean isStatic, float density, boolean friction,
                                    short cBits, short mBits, short gIndex, Object object) {
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

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, width, height);
//        PolygonShape shape = new PolygonShape();
//
//        Vector2[] vertices = {new Vector2(0, 0),
//                new Vector2(0, height/Constants.PPM),
//                new Vector2(width/Constants.PPM, height/Constants.PPM), new Vector2(width/ Constants.PPM, 0)};

//        Vector2[] vertices = {new Vector2(width / 2 / Constants.PPM, height / Constants.PPM),
//                new Vector2(width / Constants.PPM, height/2/Constants.PPM),
//                new Vector2(width / 2 / Constants.PPM, 0), new Vector2(0, height/2/Constants.PPM)};
        //shape.set(vertices);

        //size is taken from the center, size 50 by 50
        //shape.setAsBox(width/2/Constants.PPM, height/2/Constants.PPM); // divide PPM to turn into box2D units
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        if(!friction) {
            fixtureDef.friction = 0;
        }
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        // creates the body and puts it into the world
        body = GameState.world.createBody(def);
        body.createFixture(fixtureDef).setUserData(object);
        shape.dispose();
        return body;
    }

    public static Body createBox(int x, int y, int width, int height, boolean isStatic, float density,
                                 short cBits, short mBits, short gIndex, Object object) {
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

        PolygonShape shape = new PolygonShape();
        //size is taken from the center, size 50 by 50
//        Vector2[] vertices = {new Vector2(0, 0),
//                new Vector2(0, height/Constants.PPM),
//                new Vector2(width/Constants.PPM, height/Constants.PPM), new Vector2(width/ Constants.PPM, 0)};
//        shape.set(vertices);
        shape.setAsBox(width/2/Constants.PPM, height/2/Constants.PPM); // divide PPM to turn into box2D units

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        //fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        // creates the body and puts it into the world
        body = GameState.world.createBody(def);
        body.createFixture(fixtureDef).setUserData(object);
        shape.dispose();
        return body;
    }

    public static Body createDecayTile(int x, int y, int direction,
                                       short cBits, short mBits, short gIndex, Object object) {
        Body body;
        // describes the physical properties the body have
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(x/Constants.PPM, y/Constants.PPM);
        def.fixedRotation = true;

        PolygonShape shape = new PolygonShape();

        Vector2[] verticies;

        if(direction == 1) {
            verticies = new Vector2[]{new Vector2(0, 0),
                    new Vector2(0, Tile.TILESIZE / Constants.PPM),
                    new Vector2(Tile.TILESIZE / Constants.PPM, 0)};
        } else {
            verticies = new Vector2[]{new Vector2(0, 0),
                    new Vector2(Tile.TILESIZE / Constants.PPM, 0),
                    new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM)};
        }
        shape.set(verticies);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        //fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        // creates the body and puts it into the world
        body = GameState.world.createBody(def);
        body.createFixture(fixtureDef).setUserData(object);
        shape.dispose();
        return body;
    }

    public static Body createBall(int x, int y, float radius, float density,
                                  short cBits, short mBits, short gIndex, Object object) {
        Body body;
        // describes the physical properties the body have
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x/Constants.PPM, y/Constants.PPM);
        //def.fixedRotation = true;
        //def.linearDamping = 10f;
        //def.angularDamping = 0.5f;

        // generate the ball's box2d body
        CircleShape circle = new CircleShape();
        circle.setRadius(radius/Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = density;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.75f;
        //fixtureDef.friction = 0;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        // creates the body and puts it into the world
        body = GameState.world.createBody(def);
        body.createFixture(fixtureDef).setUserData(object);
        circle.dispose();
        return body;
    }

}
