package com.everlongn.entities.projectiles;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ArcaneEscort extends Projectile {
    public ParticleEffect movingParticle, explosion;
    public int direction;
    public static float maxLife = 8;
    public static int numEscort = 0;

    public float life, angle, currentAngle, angleUpdate;
    public boolean reached, canRotate = true;
    public int numRotations = 0;
    public static Color color = new Color(0.02f, 0.02f, 0.04f, 1f);

    public ArcaneEscort(float x, float y, float density, int direction, float angle, float damage) {
        super(x, y, 5, 5, density);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                (short) Constants.BIT_PROJECTILE, (short)(Constants.BIT_TILE | Constants.BIT_ENEMY), (short)0, this);

        currentAngle = angle;

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/escortTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.start();

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/escortExplosion"), Gdx.files.internal(""));
        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

        float mouseX = (Player.mouseWorldPos().x)/Constants.PPM;
        float mouseY = (Player.mouseWorldPos().y)/Constants.PPM;

        float aimAngle = (float)Math.atan2(mouseX - body.getPosition().x,
                mouseY - body.getPosition().y);

        currentAngle = aimAngle;

        speedX = (float) Math.sin(aimAngle) * (10f);
        speedY = (float) Math.cos(aimAngle) * (10f);

        maxMovingRadius = 250;
        maxExplodingRadius = 350;

        light = new PointLight(GameState.rayHandler, 300, color, 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM);
        light.setSoft(true);
        numEscort++;
    }

    @Override
    public void tick() {
        if(body.getLinearVelocity().x < -1) {
            direction = 0;
        } else if(body.getLinearVelocity().x > 1) {
            direction = 1;
        }
        if(!lifeOut) {
            currentRadius+=10;
            if(currentRadius > maxMovingRadius)
                currentRadius = maxMovingRadius;
            light.setDistance(currentRadius);
            light.setPosition(body.getPosition().x * Constants.PPM,
                    body.getPosition().y * Constants.PPM);
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
                    Inventory.inventory[Inventory.selectedIndex] != null && Inventory.inventory[Inventory.selectedIndex].name.equals("Escort")) {
                life = 0;

                float mouseX = (Player.mouseWorldPos().x)/Constants.PPM;
                float mouseY = (Player.mouseWorldPos().y)/Constants.PPM;

                double aimAngle = Math.atan2(mouseX - body.getPosition().x,
                        mouseY - body.getPosition().y);

                if(body.getPosition().x > mouseX - 4/Constants.PPM && body.getPosition().x < mouseX + 4/Constants.PPM &&
                        body.getPosition().y > mouseY - 4/Constants.PPM && body.getPosition().y < mouseY + 4/Constants.PPM) {
                    reached = true;
                    if(canRotate) {
                        numRotations++;
                        canRotate = false;
                    }
                }
                if(!reached) {
                    currentAngle = (float) aimAngle;

                    speedX = (float) Math.sin(currentAngle) * (10f);
                    speedY = (float) Math.cos(currentAngle) * (10f);

                    canRotate = true;
                } else {
                    speedX = (float) Math.sin(currentAngle + (numRotations-1)*Math.PI/10) * (10f);
                    speedY = (float) Math.cos(currentAngle + (numRotations-1)*Math.PI/10) * (10f);
                    angleUpdate += Gdx.graphics.getDeltaTime();
                    if(angleUpdate > 0.2) {
                        reached = false;
                        angleUpdate = 0;
                    }
                }
            } else {
                life += Gdx.graphics.getDeltaTime();
                if(life > maxLife && !lifeOut) {
                    finish();
                }
            }

            moveByVelocityX();
            moveByVelocityY();

        } else {
            body.setLinearVelocity(0, 0);

            if(maxReached) {
                currentRadius -= 25;
                if (currentRadius <= 0)
                    currentRadius = 0;
            } else {
                currentRadius += 25;
                if (currentRadius > maxExplodingRadius) {
                    currentRadius = maxMovingRadius;
                    maxReached = true;
                }
            }

            light.setDistance(currentRadius);
            light.setPosition(body.getPosition().x * Constants.PPM,
                    body.getPosition().y * Constants.PPM);

            if(!exploded) {
                explosionTimer += Gdx.graphics.getDeltaTime();
                if(explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
        }

        if(lifeOut && explosion.isComplete() && movingParticle.isComplete()) {
            GameState.world.destroyBody(body);
            movingParticle.dispose();
            explosion.dispose();
            light.remove();
            active = false;
        }

        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());

        if(lifeOut) {
            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            explosion.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void explode() {
        Rectangle explosionRectangle = new Rectangle(body.getPosition().x*Constants.PPM+2 - 10, body.getPosition().y*Constants.PPM+2 - 10,
                20, 20);
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(explosionRectangle) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 120;
                    float angle = (float)(Math.random()*(Math.PI/4));
                    if(direction == 0) {
                        c.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    } else {
                        c.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    }
                    c.hurt(damage);
                    break;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        movingParticle.draw(batch);
        if(lifeOut) {
            explosion.draw(batch);
        }
        batch.end();
    }

    @Override
    public void finish() {
        lifeOut = true;
        movingParticle.getEmitters().get(0).setContinuous(false);
        explosion.start();
        numEscort--;
    }
}
