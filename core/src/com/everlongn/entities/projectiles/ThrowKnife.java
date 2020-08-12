package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.everlongn.assets.Items;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.ThrowWeapons;
import com.everlongn.entities.*;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Item;
import com.everlongn.items.Throwing;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ThrowKnife extends Throw {
    public ParticleEffect explosion;

    public ThrowKnife(float x, float y, int direction, float angle, float damage, Entity source) {
        super(x, y, 20, 20, 1);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;
        this.source = source;

        body = Tool.createEntity((int) (x), (int) (y), width, height, false, 1, false,
                (short) Constants.BIT_PROJECTILE, (short) (Constants.BIT_TILE | Constants.BIT_ENEMY), (short) 0, this);

        float forceX = (float) (Math.abs(Math.sin(angle) * 135));
        float forceY = (float) (Math.cos(angle) * 110);

        if (direction == 0) {
            moveByForce(new Vector2(-forceX, -forceY));
        } else {
            moveByForce(new Vector2(forceX, -forceY));
        }

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/throwExplosion"), Gdx.files.internal(""));
        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

        throwBound = new Rectangle(0, 0, Throwing.throwKnife.width, Throwing.throwKnife.height);
    }

    @Override
    public void tick() {
        throwBound.setPosition(body.getPosition().x * Constants.PPM - Throwing.throwKnife.width / 2, body.getPosition().y * Constants.PPM - Throwing.throwKnife.height / 2);

        if (!lifeOut) {
            if (direction == 0)
                rotation += 20;
            else
                rotation -= 20;
        } else {
            if (!exploded) {
                explosionTimer += ControlCenter.delta;
                if (explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
            if(!collected) {
                if(locked != null && locked.body != null)
                    body.setTransform(locked.body.getPosition().x + lockX, locked.body.getPosition().y + lockY, angle);
                else if(attached != null && attached.body != null && attached.health > 0)
                    body.setTransform(attached.body.getPosition().x + lockX, attached.body.getPosition().y + lockY, angle);
                else
                    lifeOut = false;
            }
            checkPickedUp();
        }

        if ((lifeOut && deactivate) || (despawn && explosion.isComplete())) {
            GameState.world.destroyBody(body);
            explosion.dispose();
            active = false;
        }

        if(lifeOut && despawn) {
            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            explosion.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void checkPickedUp() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && throwBound.contains(Player.mouseWorldPos().x, Player.mouseWorldPos().y) && throwBound.overlaps(Player.itemPickBound) && Item.canPick) {
            collected = true;
            Item.canPick = false;
        }

        if (!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Item.canPick = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && throwBound.overlaps(Player.itemPickBound)) {
            collected = true;
        }

        if (collected) {
            float sx = Player.itemCollectBound.x / Constants.PPM;
            float sy = Player.itemCollectBound.y / Constants.PPM;

            if (Math.abs(sx - body.getPosition().x) > 75 / Constants.PPM) {
                collected = false;
                return;
            }

            double angle = Math.atan2(sx - body.getPosition().x,
                    sy - body.getPosition().y);

            body.setLinearVelocity((float) Math.sin(angle) * (10f), (float) Math.cos(angle) * (10f));

            if (throwBound.overlaps(Player.itemCollectBound)) {
                GameState.inventory.addItem(Throwing.throwKnife.createNew(1));
                pickedUp = true;
                deactivate = true;
            }
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x / 1.04f, body.getLinearVelocity().y);
        }
    }

    public void explode() {
        Rectangle explosionRectangle = new Rectangle(body.getPosition().x * Constants.PPM + 2 - Throwing.triStar.width / 2, body.getPosition().y * Constants.PPM + 2 - Throwing.triStar.height / 2,
                Throwing.triStar.width, Throwing.triStar.height);
        for (int i = 0; i < EntityManager.entities.size(); i++) {
            if (EntityManager.entities.get(i).getBound().overlaps(explosionRectangle) && EntityManager.entities.get(i) != this) {
                if (EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature) EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 500 + (float) Math.random() * 100;
                    float angle = (float) (Math.random() * (Math.PI / 4));
                    if (direction == 0) {
                        c.body.applyForceToCenter(
                                -(float) Math.cos(angle) * force, (float) Math.sin(angle) * force, false);
                    } else {
                        c.body.applyForceToCenter(
                                (float) Math.cos(angle) * force, (float) Math.sin(angle) * force, false);
                    }

                    c.hurt(damage, source);
                    c.target = source;
                    break;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if (body != null) {
            if(lifeOut && despawn) {
                alpha -= 0.05;
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
            }
            if (direction == 1)
                batch.draw(ThrowWeapons.throwKnifeR, body.getPosition().x * Constants.PPM - Throwing.throwKnife.width / 2 + width/2, body.getPosition().y * Constants.PPM - Throwing.throwKnife.height / 2 + height/2,
                        Throwing.throwKnife.width / 2, Throwing.throwKnife.height / 2,
                        Throwing.throwKnife.width, Throwing.throwKnife.height, 1f, 1f, rotation);
            else
                batch.draw(ThrowWeapons.throwKnifeL, body.getPosition().x * Constants.PPM - Throwing.throwKnife.width / 2 + width/2, body.getPosition().y * Constants.PPM - Throwing.throwKnife.height / 2 + height/2,
                        Throwing.throwKnife.width / 2, Throwing.throwKnife.height / 2,
                        Throwing.throwKnife.width, Throwing.throwKnife.height, 1f, 1f, rotation);

            if(lifeOut && despawn) {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
            }
        }
        if(lifeOut && despawn) {
            explosion.draw(batch);
        }
        batch.end();
    }

    @Override
    public void finish() {
        lifeOut = true;

        Sounds.playSound(Sounds.throwKnifeLand);
        if ((int) (Math.random() * 100) < 25) {
            despawn = true;
            explosion.start();
        } else {
            despawn = false;
        }
    }
}