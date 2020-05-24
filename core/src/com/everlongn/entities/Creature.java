package com.everlongn.entities;

import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;

import java.util.ArrayList;

public abstract class Creature extends Entity {
    public float speed, currentSpeed, sightRadius, knockbackResistance;
    public int direction, damage;
    public boolean stunned;

    public Entity target;
    public ArrayList<String> enemyList = new ArrayList<String>();

    public Animation[] chase, attack;

    public Creature(ControlCenter c, float x, float y, int width, int height, float density, float speed) {
        super(c, x, y, width, height, density);

        // default values
        this.speed = speed;
        currentSpeed = speed;
        type.add("creature");
        damage = 50;
        direction = 0; // 0-Left, 1-Right
    }

    public void move() {
        if(!stunned) {
            if (direction == 0)
                body.setLinearVelocity(-speed, body.getLinearVelocity().y);
            else {
                body.setLinearVelocity(speed, body.getLinearVelocity().y);
            }
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x/knockbackResistance, body.getLinearVelocity().y);
            if(Math.abs(body.getLinearVelocity().x) < 1) {
                stunned = false;
            }
        }
    }

    public void findTarget() {
        Entity possibleTarget = null;
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            Entity e = EntityManager.entities.get(i);
            if(e instanceof Creature && e != this) {
                Creature c = (Creature)e;
                for(int j = 0; j < c.type.size(); j++) {
                    if(enemyList.contains(c.type.get(j))) {
                        if(possibleTarget == null) {
                            possibleTarget = c;
                        } else {
                            if(Math.abs(x - possibleTarget.x) > Math.abs(x - c.x)) {
                                possibleTarget = c;
                            }
                        }
                        break;
                    }
                }

            }
        }
        target = possibleTarget;
    }

    public void chase() {
        if(target == null)
            return;
        if(!target.active) {
            target = null;
        }
        if(target.body.getPosition().x < body.getPosition().x) {
            direction = 0;
        } else {
            direction = 1;
        }
        move();
    }
}
