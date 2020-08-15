package com.everlongn.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;

public class Tool {
    public static int cursorID;

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

        PolygonShape shape = new PolygonShape();

        Vector2[] vertices = {new Vector2(width / 2 / Constants.PPM, height / Constants.PPM),
                new Vector2(width / Constants.PPM, height/2/Constants.PPM),
                new Vector2(width / 2 / Constants.PPM, 0), new Vector2(0, height/2/Constants.PPM)};
        shape.set(vertices);

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

    public static Body createTile(int x, int y,
                                  int numAdjacent, boolean left, boolean right, boolean up, boolean down,
                                  short cBits, short mBits, short gIndex, Object object) {
        Body body;
        // describes the physical properties the body have
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;

        def.position.set(x/Constants.PPM, y/Constants.PPM);
        def.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        EdgeShape edge = new EdgeShape();

//        Vector2[] vertices = new Vector2[]{new Vector2(0, 0),
//                new Vector2(Tile.TILESIZE / Constants.PPM, 0),
//                new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM),
//                new Vector2(0, Tile.TILESIZE / Constants.PPM)};

        FixtureDef fixtureDef = new FixtureDef();
        Vector2[] vertices = new Vector2[]{};

        if(numAdjacent == 2) {
            if(right && down) {
                edge.set(0, 0, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM);
                fixtureDef.shape = edge;
            } else if(left && down) {
                edge.set(0, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM, 0);
                fixtureDef.shape = edge;
            } else if(right && up) {
                edge.set(0, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM, 0);
                fixtureDef.shape = edge;
            } else if(left && up) {
                edge.set(0, 0, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM);
                fixtureDef.shape = edge;
            } else {
                vertices = new Vector2[]{new Vector2(0, 0),
                    new Vector2(Tile.TILESIZE / Constants.PPM, 0),
                    new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM),
                    new Vector2(0, Tile.TILESIZE / Constants.PPM)};
                shape.set(vertices);
                fixtureDef.shape = shape;
            }
        } else if(numAdjacent == 1) {
            if(left) {
                vertices = new Vector2[]{new Vector2(0, 0),
                        new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / 2 / Constants.PPM),
                        new Vector2(0, Tile.TILESIZE / Constants.PPM)};
            } else if(right) {
                vertices = new Vector2[]{new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM),
                        new Vector2(0, Tile.TILESIZE / 2 / Constants.PPM),
                        new Vector2(Tile.TILESIZE / Constants.PPM, 0)};
            } else if(up) {
                vertices = new Vector2[]{new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM),
                        new Vector2(Tile.TILESIZE / 2 / Constants.PPM, Tile.TILESIZE / 2 / Constants.PPM),
                        new Vector2(0, Tile.TILESIZE / Constants.PPM)};
            } else if(down) {
                vertices = new Vector2[]{new Vector2(0, 0),
                        new Vector2(Tile.TILESIZE / 2 / Constants.PPM, Tile.TILESIZE / 2 / Constants.PPM),
                        new Vector2(Tile.TILESIZE / Constants.PPM, 0)};
            }
            shape.set(vertices);
            fixtureDef.shape = shape;
        } else if(numAdjacent == 0) {
            vertices = new Vector2[]{new Vector2(Tile.TILESIZE / 2 / Constants.PPM, 0),
                    new Vector2(Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / 2 / Constants.PPM),
                    new Vector2(Tile.TILESIZE / 2 / Constants.PPM, Tile.TILESIZE / Constants.PPM),
                    new Vector2(0, Tile.TILESIZE / 2 / Constants.PPM)};
            shape.set(vertices);
            fixtureDef.shape = shape;
        } else {
            if(left && down && right) {
                edge.set(0, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM);
                fixtureDef.shape = edge;
            } else if(left && up && right) {
                edge.set(0, 0, Tile.TILESIZE / Constants.PPM, 0);
                fixtureDef.shape = edge;
            } else if(up && left && down) {
                edge.set(Tile.TILESIZE / Constants.PPM, 0, Tile.TILESIZE / Constants.PPM, Tile.TILESIZE / Constants.PPM);
                fixtureDef.shape = edge;
            } else if(up && right && down) {
                edge.set(0, 0, 0, Tile.TILESIZE / Constants.PPM);
                fixtureDef.shape = edge;
            }
        }

        //fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 0;
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

    public static void changeCursor(int id) {
        if(cursorID != id) {
            cursorID = id;
            if(id == 0) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(ControlCenter.cursor1, 0, 0));
            } else if(id == 1) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(ControlCenter.emptyCursor, 0, 0));
            } else if(id == 2) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(ControlCenter.aimCursor, 0, 0));
            } else if(id == 3) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(ControlCenter.attackCursor, 0, 0));
            } else if(id == 4) {
                Gdx.graphics.setCursor(Gdx.graphics.newCursor(ControlCenter.handCursor, 0, 0));
            }
        }
    }
}
