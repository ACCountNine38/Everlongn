package com.everlongn.entities.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.*;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Warf extends Creature {
    public boolean enraged, canTurn, attacking;
    public float currentRotation, targetRotation, turnTimer;

    public Rectangle attackBound;

    // add where after player attacks a warf, the other warfs also starts to attack the player
    public Warf(float x, float y, int size) {
        super(x, y, size, size, 5f, (0.25f + (float)Math.random()*0.2f)*2);

        // base variables
        body = Tool.createEntity((int)(x), (int)(y), width/12, height/2 - height/8, false, density, true,
                Constants.BIT_ENEMY, (short)(Constants.BIT_TILE | Constants.BIT_PROJECTILE), (short)0, this);

        chase = new Animation[2];
        chase[0] = new Animation(1f/25f, Entities.warfWalk[0], true);
        chase[1] = new Animation(1f/25f, Entities.warfWalk[1], true);

        attack = new Animation[2];
        attack[0] = new Animation(1f/35f, Entities.warfAttack[0], true);
        attack[1] = new Animation(1f/35f, Entities.warfAttack[1], true);

        boundWidth = width/12;
        boundHeight = height/2 - height/8;

        setMaxHealth(height);
        setMaxResistance(15);

        enemyList.add("spider");
        enemyList.add("hydra");
        enemyList.add("player");
        form = "warf";
        type.add("warf");

        damage = size/15;

        knockbackResistance = 1.04f;
        jumpCondition = speed/6;

        vulnerableToArcane = true;

        for(int i = 0; i < destroyed.length; i++)
            destroyed[i].getEmitters().first().scaleSize(1.5f);
        jumpForce = size*7;

        sightWidth = 600;
        sightHeight = 800;
        sightBound = new Rectangle(body.getPosition().x*Constants.PPM - sightWidth/2, body.getPosition().y*Constants.PPM - sightHeight/2, sightWidth, sightHeight);

        attackBound = new Rectangle(0, 0, 150, height/2 - height/8);
    }

    @Override
    public void tick() {
//        if(status.equals("natural") && health > 0 && naturalStatus == 0 && body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0) {
//            body.setLinearVelocity(0, 2);
//        }

        if(alive) {
            if(status.equals("chase") || health < maxHealth) {
                sightWidth = 1600;
                sightHeight = 800;
            } else {
                sightWidth = 600;
                sightHeight = 800;
            }
            sightBound.setPosition(body.getPosition().x*Constants.PPM - sightWidth/2, body.getPosition().y*Constants.PPM - sightHeight/2);
            sightBound.setSize(sightWidth, sightHeight);
            if (target == null) {
                if(attacking) {
                    attack();
                } else {
                    findTarget();
                    precheckNatural();
                }
            } else {
                if(enraged) {
                    if(attacking) {
                        attack();
                    } else {
                        if (currentRotation > 0) {
                            currentRotation -= 0.01f;
                            if (currentRotation < 0)
                                currentRotation = 0;
                        } else if (currentRotation < 0) {
                            currentRotation += 0.01f;
                            if (currentRotation > 0)
                                currentRotation = 0;
                        } else {
                            if(direction == 1)
                                attackBound.setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
                            else
                                attackBound.setPosition(body.getPosition().x * Constants.PPM - 150, body.getPosition().y * Constants.PPM);
                            chase();
                        }
                    }
                } else {
                    if(attacking) {
                        attack();
                    } else {
                        status.equals("idle");
                        if (sightBound.overlaps(target.getBound()) && canSwitchNatural()) {
                            if (!canTurn) {
                                turnTimer += ControlCenter.delta;
                                if (turnTimer > 2.5f) {
                                    turnTimer = 0;
                                    canTurn = true;
                                }
                            } else {
                                if (target.body.getPosition().x < body.getPosition().x && direction == 1 && canTurn) {
                                    canTurn = false;
                                    direction = 0;
                                    currentRotation *= -1;
                                }
                                if (target.body.getPosition().x > body.getPosition().x && direction == 0 && canTurn) {
                                    canTurn = false;
                                    direction = 1;
                                    currentRotation *= -1;
                                }
                            }
                            body.setLinearVelocity(0, body.getLinearVelocity().y);
                            targetRotation = 0;
                            if ((target.body.getPosition().x < body.getPosition().x && direction == 0) || (target.body.getPosition().x > body.getPosition().x && direction == 1)) {
                                if (direction == 1) {
                                    targetRotation = (float) Math.atan2(target.body.getPosition().y * Constants.PPM - body.getPosition().y * Constants.PPM - height / 10,
                                            target.body.getPosition().x * Constants.PPM - body.getPosition().x * Constants.PPM);
                                } else {
                                    targetRotation = (float) Math.atan2(target.body.getPosition().y * Constants.PPM - body.getPosition().y * Constants.PPM - height / 10,
                                            target.body.getPosition().x * Constants.PPM - body.getPosition().x * Constants.PPM);
                                }
                                if (currentRotation < targetRotation - 0.01f) {
                                    currentRotation += 0.01f;
                                    if (currentRotation > (float) Math.PI / 6) {
                                        currentRotation = (float) Math.PI / 6;
                                    }
                                } else if (currentRotation > targetRotation + 0.01f) {
                                    currentRotation -= 0.01f;
                                    if (currentRotation < -(float) Math.PI / 6) {
                                        currentRotation = -(float) Math.PI / 6;
                                    }
                                }
                            }
                        } else {
                            precheckNatural();
                        }
                    }
                }
            }

            if(target != null && (!target.getBound().overlaps(sightBound) || target.health <= 0)) {
                target = null;
                enraged = false;
            }

            if (health <= 0) {
                health = 0;
                alive = false;
                body.setActive(false);
                finish();
            }
        } else {
            destroyed[destroyedDirection].update(Gdx.graphics.getDeltaTime());
            //destroyed[destroyedDirection].getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
            fadeAlpha-=0.15;
            if(fadeAlpha < 0) {
                fadeAlpha = 0;
            }
            if(destroyed[destroyedDirection].isComplete()) {
                for(int i = 0; i < destroyed.length; i++)
                    destroyed[i].dispose();
                GameState.world.destroyBody(body);
                active = false;
            }
        }
    }

    public void attack() {
        if (currentRotation > 0) {
            currentRotation -= 0.01f;
            if (currentRotation < 0)
                currentRotation = 0;
        } else if (currentRotation < 0) {
            currentRotation += 0.01f;
            if (currentRotation > 0)
                currentRotation = 0;
        } else {
            attack[direction].tick(ControlCenter.delta);
            if (attack[direction].currentIndex == 32) {
                Rectangle attackRect;
                if (direction == 1) {
                    attackRect = new Rectangle(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM - (height / 2 - height / 8) / 2, 250, height / 2);
                } else {
                    attackRect = new Rectangle(body.getPosition().x * Constants.PPM - 250, body.getPosition().y * Constants.PPM - (height / 2 - height / 8) / 2, 250, height / 2);
                }
                for (Entity e : EntityManager.entities) {
                    if (e.getBound().overlaps(attackRect)) {
                        for (int i = 0; i < e.type.size(); i++) {
                            if (enemyList.contains(e.type.get(i))) {
                                e.hurt(damage, this);
                                if (e instanceof Player) {
                                    if (direction == 0)
                                        ((Player) e).xThrust -= width / 40;
                                    else
                                        ((Player) e).xThrust += width / 40;
                                } else {
                                    if (direction == 0)
                                        e.body.applyForceToCenter(-width * 2, 0, false);
                                    else
                                        e.body.applyForceToCenter(width * 2, 0, false);
                                }
                            }
                        }
                    }
                }
            } else if (attack[direction].currentIndex == attack[direction].textures.length - 1) {
                attacking = false;
            }
        }
    }

    public void precheckNatural() {
        if(currentRotation > 0) {
            currentRotation -= 0.01f;
            if(currentRotation < 0)
                currentRotation = 0;
        } else if(currentRotation < 0) {
            currentRotation += 0.01f;
            if(currentRotation > 0)
                currentRotation = 0;
        } else {
            natural();
        }
    }

    public void natural() {
        status = "natural";
        transitionTimer = 0;
        canTurn = false;
        naturalTimer+=Gdx.graphics.getDeltaTime();

        if(naturalTimer > naturalRotation && canSwitchNatural()) {
            naturalTimer = 0;
            if(naturalStatus == 1) {
                naturalStatus = (int)(Math.random()*2);
            } else {
                naturalStatus = 1;
            }

            naturalRotation = (int)(Math.random()*8) + 2;
        }

        if(naturalStatus == 0) {
            if(direction == 1)
                body.setLinearVelocity(speed/2, body.getLinearVelocity().y);
            else
                body.setLinearVelocity(-speed/2, body.getLinearVelocity().y);
            chase[0].tick(Gdx.graphics.getDeltaTime());
            chase[1].tick(Gdx.graphics.getDeltaTime());
        } else {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            turnTimer += ControlCenter.delta;
            if(canTurn && turnTimer > naturalRotation/2) {
                turnTimer = 0;
                if((int)(Math.random()*2) == 0)
                if(direction == 0)
                    direction = 1;
                else
                    direction = 0;
                canTurn = false;
            } else {
                canTurn = true;
            }
        }
    }

    public void chase() {
        transitionTimer += Gdx.graphics.getDeltaTime();
        if(transitionTimer < 0.5)
            return;

        if(target == null || target.body == null || !active)
            return;

        if(target.body.getPosition().x*Constants.PPM < body.getPosition().x*Constants.PPM) {
            direction = 0;
        } else {
            direction = 1;
        }
        chase[direction].tick(Gdx.graphics.getDeltaTime());
        status = "chase";
        move();
        if(target.getBound().overlaps(attackBound)) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
            attacking = true;
            for(Throw e: EntityManager.throwing) {
                if(e.attached == this) {
                    e.attached = null;
                }
            }
        }
    }

    public boolean canSwitchNatural() {
        if(chase[direction].currentIndex == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(!alive) {
            batch.setColor(0f, 0f, 0f, fadeAlpha);
        }
        //batch.draw(Tiles.blackTile, attackRect.x, attackRect.y, attackRect.width, attackRect.height);

        if(attacking) {
            if(attack[direction].getFrame() != null)
                batch.draw(attack[direction].getFrame(), body.getPosition().x * PPM - width/2 + width/24, body.getPosition().y * PPM - height/3f, width, height);
        } else if (body.getLinearVelocity().x != 0) {
            if(getCurrentFrame() != null)
                batch.draw(getCurrentFrame(), body.getPosition().x * PPM - width/2 + width/24, body.getPosition().y * PPM - height/3f, width, height);
        } else {
            batch.draw(Entities.warfBody[direction], body.getPosition().x * PPM - width/2 + width/24, body.getPosition().y * PPM - height/3f, width, height);
            batch.draw(Entities.warfHead[direction],
                    body.getPosition().x * PPM - width / 2 + width / 24,
                    body.getPosition().y * PPM - height / 3f,
                    width / 2,
                    height / 2 + height / 8,
                    width, height,
                    1f, 1f, (float)Math.toDegrees(currentRotation));
        }

        if(!alive) {
            batch.setColor(1f, 1f, 1f, 1f);
            destroyed[destroyedDirection].draw(batch);
        }
        batch.end();
    }

    @Override
    public void hurt(float damage, Entity source) {
        enraged = true;
        super.hurt(damage, source);
    }

    @Override
    public void finish() {
        if(body.getLinearVelocity().x < 0) {
            destroyedDirection = 0;
        } else if(body.getLinearVelocity().x > 0) {
            destroyedDirection = 1;
        } else if(body.getLinearVelocity().x > 0) {
            destroyedDirection = 2;
        }
        destroyed[destroyedDirection].getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
        destroyed[destroyedDirection].start();
    }
}
