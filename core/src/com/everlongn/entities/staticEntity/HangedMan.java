package com.everlongn.entities.staticEntity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.everlongn.assets.StaticObjects;
import com.everlongn.assets.ThrowWeapons;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.items.Throwing;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

import static com.everlongn.utils.Constants.PPM;

public class HangedMan extends Creature {
    public ArrayList<Body> bodies;
    public int direction = (int)(Math.random()*2);
    public int containHead = (int)(Math.random()*4);
    public int containLeg = (int)(Math.random()*2);
    public int bodyIndex = (int)(Math.random()*3);
    public int[] stab;
    public int numStabs;

    public Tree source;

    public HangedMan(float x, float y, Tree source) {
        super(x, y, 4, 8, 0f, 0f);

        numStabs = (int)(Math.random()*3);
        this.source = source;
        stab = new int[numStabs];
        for(int i = 0; i < numStabs; i++) {
            stab[i] = (int)(Math.random()*5);
        }

        resetHealth(500);
        resistance = 0;
        baseRegenAmount = 0f;
    }

    public static ArrayList<Body> createBodies(int x, int y, int size, float density,
                                             short cBits, short mBits, short gIndex, Object object, int length) {
        ArrayList<Body> bodies = new ArrayList<>();
        bodies.add(Tool.createBox(x, y, size, size, true, true, density, cBits, mBits, gIndex, object));

        for(int i = 1; i < length-1; i++) {
            bodies.add(Tool.createBox(x, y-i*size, size, size, false, false, density, cBits, mBits, gIndex, object));

            RopeJointDef rDef = new RopeJointDef();
            rDef.bodyA = bodies.get(i-1);
            rDef.bodyB = bodies.get(i);
            rDef.collideConnected = true;
            rDef.maxLength = .05f;
            rDef.localAnchorA.set(0, -.05f);
            rDef.localAnchorB.set(0, .05f);
            GameState.world.createJoint(rDef);
        }

        int i = length-1;
        bodies.add(createBox(x, y-i*size, 20, 50, false, false, 6f,
                Constants.BIT_ENEMY, (short)(Constants.BIT_TILE | Constants.BIT_PROJECTILE), gIndex, object));
        RopeJointDef rDef = new RopeJointDef();
        rDef.bodyA = bodies.get(i-1);
        rDef.bodyB = bodies.get(i);
        rDef.collideConnected = true;
        rDef.maxLength = .1f;
        rDef.localAnchorA.set(0, -.1f);
        rDef.localAnchorB.set(0, .05f);
        GameState.world.createJoint(rDef);

        return bodies;
    }

    public static Body createBox(int x, int y, int width, int height, boolean isStatic, boolean fixRotation, float density,
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
        def.fixedRotation = fixRotation;

        PolygonShape shape = new PolygonShape();

        Vector2[] vertices = new Vector2[]{new Vector2(-width /2/ Constants.PPM, -height /2/ Constants.PPM),
        new Vector2(width /2/ Constants.PPM, -height /2/ Constants.PPM),
        new Vector2(width /2/ Constants.PPM, height /2/ Constants.PPM),
        new Vector2(-width /2/ Constants.PPM, height /2/ Constants.PPM)};

        shape.set(vertices);

        CircleShape circle = new CircleShape();
        circle.setRadius(width/Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = mBits;
        fixtureDef.filter.groupIndex = gIndex;
        // creates the body and puts it into the world
        body = GameState.world.createBody(def);
        body.createFixture(fixtureDef).setUserData(object);
        shape.dispose();
        return body;
    }

    @Override
    public void tick() {
        if(!getBound().overlaps(Player.sightBound())) {
            active = false;
            //GameState.world.destroyBody(body);
            for(Body body: bodies) {
                GameState.world.destroyBody(body);
            }
            bodies.clear();
            return;
        }
        if(bodies.get(bodies.size()-1) != null) {
            if(bodies.get(bodies.size()-1).getAngle() > 0) {
                bodies.get(bodies.size()-1).setAngularVelocity(-1f);
            } else if(bodies.get(bodies.size()-1).getAngle() < 0) {
                bodies.get(bodies.size()-1).setAngularVelocity(1f);
            }
        }
        if(health <= 0 && !bodies.isEmpty()) {
            //GameState.world.destroyBody(body);
            for(Body body: bodies) {
                GameState.world.destroyBody(body);
            }
            bodies.clear();
            //source.corpseList.remove(this);
            active = false;
        }
    }

    @Override
    public Rectangle getBound() {
        return new Rectangle(bodies.get(bodies.size()-1).getPosition().x*Constants.PPM - 10, bodies.get(bodies.size()-1).getPosition().y*Constants.PPM - 25, 20, 50);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        for(Body body: bodies) {
            batch.draw(StaticObjects.chainBit, body.getPosition().x * PPM - 5, body.getPosition().y * PPM - 5, 10, 10);
        }

        if(containHead != 0) {
            batch.draw(StaticObjects.corpseHead[direction],
                    bodies.get(bodies.size()-1).getPosition().x * PPM - 68,
                    bodies.get(bodies.size()-1).getPosition().y * PPM - 88, 68, 80,
                    136, 136, 1f, 1f,
                    (float) Math.toDegrees(bodies.get(bodies.size()-1).getAngle()));
        }

        if(containLeg == 0) {
            batch.draw(StaticObjects.corpseLegs[direction],
                    bodies.get(bodies.size()-1).getPosition().x * PPM - 68,
                    bodies.get(bodies.size()-1).getPosition().y * PPM - 88, 68, 80,
                    136, 136, 1f, 1f,
                    (float) Math.toDegrees(bodies.get(bodies.size()-1).getAngle()));
        }

        if(bodyIndex == 0) {
            batch.draw(StaticObjects.corpseBody[1][direction],
                    bodies.get(bodies.size()-1).getPosition().x * PPM - 68,
                    bodies.get(bodies.size()-1).getPosition().y * PPM - 88, 68, 80,
                    136, 136, 1f, 1f,
                    (float) Math.toDegrees(bodies.get(bodies.size()-1).getAngle()));
        } else {
            batch.draw(StaticObjects.corpseBody[0][direction],
                    bodies.get(bodies.size()-1).getPosition().x * PPM - 68,
                    bodies.get(bodies.size()-1).getPosition().y * PPM - 88, 68, 80,
                    136, 136, 1f, 1f,
                    (float) Math.toDegrees(bodies.get(bodies.size()-1).getAngle()));
        }

        for(int i = 0; i < numStabs; i++) {
            batch.draw(StaticObjects.stickPoll[stab[i]][direction],
                    bodies.get(bodies.size()-1).getPosition().x * PPM - 68,
                    bodies.get(bodies.size()-1).getPosition().y * PPM - 88, 68, 80,
                    136, 136, 1f, 1f,
                    (float) Math.toDegrees(bodies.get(bodies.size()-1).getAngle()));
        }
        batch.end();
    }

    @Override
    public void finish() {

    }
}
