package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Items;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.*;
import com.everlongn.items.Item;
import com.everlongn.items.Throwing;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

import static com.everlongn.utils.Constants.PPM;

public class Shuriken extends Projectile {
    //public ParticleEffect explosion;
    public int direction;
    public float life, angle, rotation;
    public boolean despawn, collected;

    public ArrayList<Entity> damaged = new ArrayList<>();

    public Shuriken(float x, float y, float density, int direction, float angle, float damage) {
        super(x, y, 30, 30, density);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                (short) Constants.BIT_PROJECTILE, (short)(Constants.BIT_TILE), (short)0, this);

        float forceX = (float)(Math.abs(Math.sin(angle)*320));
        float forceY = (float)(Math.cos(angle)*250);

        if(direction == 0) {
            moveByForce(new Vector2(-forceX, -forceY));
        } else {
            moveByForce(new Vector2(forceX, -forceY));
        }

        throwBound = new Rectangle(0, 0, Throwing.shuriken.width, Throwing.shuriken.height);
//        explosion = new ParticleEffect();
//        explosion.load(Gdx.files.internal("particles/trailExplosion"), Gdx.files.internal(""));
//        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

    }

    @Override
    public void tick() {
        throwBound.setPosition(body.getPosition().x*Constants.PPM+2 - Throwing.shuriken.width/2, body.getPosition().y*Constants.PPM+2 - Throwing.shuriken.height/2);

        if(!lifeOut) {
            for(Entity e: EntityManager.entities) {
                if(e.getBound().overlaps(throwBound) && !damaged.contains(e) && e.team != EntityManager.player.team) {
                    damaged.add(e);
                    e.stunned = true;

                    float force = 500 + (float)Math.random()*100;
                    float angle = (float)(Math.random()*(Math.PI/4));
                    if(direction == 0) {
                        e.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    } else {
                        e.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    }

                    e.hurt(damage, GameState.difficulty);
                }
            }
            if(direction == 0)
                rotation += 15;
            else
                rotation -= 15;

            if(body.getLinearVelocity().x > 0) {
                direction = 1;
            } else {
                direction = 0;
            }
        } else {
            checkPickedUp();
            if(!collected)
                body.setLinearVelocity(0, 0);
            if(!exploded) {
                explosionTimer += Gdx.graphics.getDeltaTime();
                if(explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
        }

        if(lifeOut && despawn) {
            GameState.world.destroyBody(body);
            active = false;
        }

//        if(lifeOut && explosion.isComplete() && currentRadius <= 0) {
//            GameState.world.destroyBody(body);
//            explosion.dispose();
//            light.remove();
//            active = false;
//        }
//
//        if(lifeOut) {
//            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
//            explosion.update(Gdx.graphics.getDeltaTime());
//        }
    }

    public void checkPickedUp() {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && throwBound.contains(Player.mouseWorldPos().x, Player.mouseWorldPos().y) && throwBound.overlaps(Player.itemPickBound) && Item.canPick) {
            collected = true;
            Item.canPick = false;
        }

        if(!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Item.canPick = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && throwBound.overlaps(Player.itemPickBound)) {
            collected = true;
        }

        if(collected) {
            float sx = Player.itemCollectBound.x/Constants.PPM;
            float sy = Player.itemCollectBound.y/Constants.PPM;

            if(Math.abs(sx - body.getPosition().x) > 75/Constants.PPM) {
                collected = false;
                return;
            }

            double angle = Math.atan2(sx - body.getPosition().x,
                    sy - body.getPosition().y);

            body.setLinearVelocity((float)Math.sin(angle) * (10f), (float) Math.cos(angle) * (10f));

            if(throwBound.overlaps(Player.itemCollectBound)) {
                GameState.inventory.addItem(Throwing.shuriken.createNew(1));
                pickedUp = true;
                despawn = true;
            }
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x/1.04f, body.getLinearVelocity().y);
        }
    }

    public void explode() {
        Rectangle explosionRectangle = new Rectangle(body.getPosition().x*Constants.PPM+2 - Throwing.shuriken.width/2, body.getPosition().y*Constants.PPM+2 - Throwing.shuriken.height/2,
                Throwing.shuriken.width, Throwing.shuriken.height);
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(explosionRectangle) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 500 + (float)Math.random()*100;
                    float angle = (float)(Math.random()*(Math.PI/4));
                    if(direction == 0) {
                        c.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    } else {
                        c.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    }
                    Sounds.playSound(Sounds.shurikenSlice);
                    c.hurt(damage, GameState.difficulty);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        //batch.draw(Tiles.blackTile, throwBound.x, throwBound.y, throwBound.width, throwBound.height);
        if(body != null)
            batch.draw(Items.shuriken, body.getPosition().x*Constants.PPM - Throwing.shuriken.width/2 + width/2, body.getPosition().y*Constants.PPM -Throwing.shuriken.height/2 + height/2,
                    Throwing.shuriken.width/2, Throwing.shuriken.height/2,
                    Throwing.shuriken.width, Throwing.shuriken.height, 1f, 1f, rotation);
//        if(lifeOut) {
//            explosion.draw(batch);
//        }
        batch.end();
    }

    @Override
    public void finish() {
        //explosion.start();
        lifeOut = true;
        despawn = false;
        body.setLinearVelocity(0,0);
        Sounds.playSound(Sounds.shurikenLand);
    }
}
