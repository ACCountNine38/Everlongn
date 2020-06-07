package com.everlongn.world;

import com.badlogic.gdx.physics.box2d.*;
import com.everlongn.entities.*;
import com.everlongn.entities.projectiles.ArcaneEruption;
import com.everlongn.entities.projectiles.ArcaneRebound;
import com.everlongn.entities.projectiles.ArcaneTrail;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a == null || b == null || a.getUserData() == null || b.getUserData() == null)
            return;

        if(a.getUserData() instanceof ArcaneRebound && b.getUserData() instanceof Creature && !(b.getUserData() instanceof Player)) {
            ArcaneRebound temp = (ArcaneRebound)(a.getUserData());
            if(!temp.lifeOut) {
                temp.destroy();
            }
            return;
        }
        if(b.getUserData() instanceof ArcaneRebound && a.getUserData() instanceof Creature && !(a.getUserData() instanceof Player)) {
            ArcaneRebound temp = (ArcaneRebound)(b.getUserData());
            if(!temp.lifeOut) {
                temp.destroy();
            }
            return;
        }

        short cDef = (short)(a.getFilterData().categoryBits | b.getFilterData().categoryBits);

        if(cDef == (short)(Constants.BIT_ENEMY | Constants.BIT_TILE)) {
            if (a.getFilterData().categoryBits == Constants.BIT_ENEMY) {
                Creature temp = (Creature) a.getUserData();
                temp.stunned = false;
            }
            else {
                Creature temp = (Creature) b.getUserData();
                temp.stunned = false;
            }
        } else if(cDef == (short)(Constants.BIT_PROJECTILE | Constants.BIT_TILE) || cDef == (short)(Constants.BIT_PROJECTILE | Constants.BIT_ENEMY)) {
            if (a.getFilterData().categoryBits == Constants.BIT_PROJECTILE) {
                Projectile temp = (Projectile) a.getUserData();
                if(!temp.lifeOut) {
                    temp.finish();
                }
            }
            else {
                Projectile temp = (Projectile) b.getUserData();
                if(!temp.lifeOut) {
                    temp.finish();
                }
            }
        }

//        else if(cDef == (short)(Constants.BIT_PLAYER | Constants.BIT_TILE) || cDef == (short)(Constants.BIT_ENEMY | Constants.BIT_TILE)) {
//            if (a.getFilterData().categoryBits == Constants.BIT_PLAYER || a.getFilterData().categoryBits == Constants.BIT_ENEMY) {
//                Creature temp = (Creature) a.getUserData();
//                if(!temp.airborn) {
//                    temp.canJump = true;
//                    temp.fall = false;
//                    temp.jump = false;
//                }
//            }
//            if (b.getFilterData().categoryBits == Constants.BIT_PLAYER || b.getFilterData().categoryBits == Constants.BIT_ENEMY) {
//                Creature temp = (Creature) b.getUserData();
//                if(!temp.airborn) {
//                    temp.canJump = true;
//                    temp.fall = false;
//                    temp.jump = false;
//                }
//            }
//        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
