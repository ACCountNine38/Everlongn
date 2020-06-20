package com.everlongn.entities.projectiles;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.graphics.ParticleEmitterBox2D;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.Projectile;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

public class ArcaneReflection extends Projectile {
    public ParticleEffect movingParticle, explosion;
    public int direction, maxReflection, numReflection = 0, yDirection;

    public float life, angle;
    public static Color color = new Color(0.01f, 0f, 0.01f, 1f);

    public ArcaneReflection(ControlCenter c, float x, float y, float density, int direction, float angle, float damage) {
        super(c, x, y, 5, 5, density);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;

        maxReflection = (int)(Math.random()*7) + 6;

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, 1, false,
                (short) Constants.BIT_PROJECTILE, (short)(Constants.BIT_TILE | Constants.BIT_ENEMY), (short)0, this);

        float newAngle = (float)(angle - Math.PI/20 + Math.random()*(Math.PI/10));
        float xMove = Math.abs((float)Math.sin(newAngle)*(20f));
        if(direction == 0) {
            speedX = -xMove;
        } else {
            speedX = xMove;
        }
        speedY = -(float)Math.cos(newAngle)*(20f);

        movingParticle = new ParticleEffect();
        movingParticle.load(Gdx.files.internal("particles/reflectionTrail"), Gdx.files.internal(""));
        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.getEmitters().add(new ParticleEmitterBox2D(GameState.world,movingParticle.getEmitters().first()));
        movingParticle.getEmitters().removeIndex(0);
        movingParticle.start();

        explosion = new ParticleEffect();
        explosion.load(Gdx.files.internal("particles/reflectionExplosion"), Gdx.files.internal(""));
        explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);

        maxMovingRadius = 200;
        maxExplodingRadius = 300;
        light = new PointLight(GameState.rayHandler, 300, color, 0,
                body.getPosition().x * Constants.PPM,
                body.getPosition().y * Constants.PPM);
        light.setSoft(true);
    }

    @Override
    public void tick() {
        if(!lifeOut) {
            if(body.getLinearVelocity().x > 0) {
                direction = 1;
            } else if(body.getLinearVelocity().x < 0) {
                direction = 0;
            }
            if(body.getLinearVelocity().y > 0) {
                yDirection = 1;
            } else if(body.getLinearVelocity().y < 0) {
                yDirection = 0;
            }
            moveByVelocityX();
            moveByVelocityY();
            currentRadius+=15;
            if(currentRadius > maxMovingRadius)
                currentRadius = maxMovingRadius;
            light.setDistance(currentRadius);
            light.setPosition(body.getPosition().x * Constants.PPM,
                    body.getPosition().y * Constants.PPM);

            life += Gdx.graphics.getDeltaTime();
            if(life > 4) {
                finish();
            }
        } else {
            body.setLinearVelocity(0, 0);
            explosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
            explosion.update(Gdx.graphics.getDeltaTime());
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

            if(explosion.isComplete() && movingParticle.isComplete() && currentRadius <= 0) {
                GameState.world.destroyBody(body);
                explosion.dispose();
                movingParticle.dispose();
                light.remove();
                active = false;
            }

            if(!exploded) {
                explosionTimer += Gdx.graphics.getDeltaTime();
                if(explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }
        }

        movingParticle.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        movingParticle.update(Gdx.graphics.getDeltaTime());
    }

    public void explode() {
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(this.getBound()) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature && !(EntityManager.entities.get(i) instanceof Player)) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float force = 375 + (float)Math.random()*50;
                    float angle = (float)(Math.random()*(Math.PI/4));
                    if(direction == 0) {
                        c.body.applyForceToCenter(
                                -(float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    } else {
                        c.body.applyForceToCenter(
                                (float)Math.cos(angle)*force, (float)Math.sin(angle)*force, false);
                    }

                    c.hurt(damage, GameState.difficulty);

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
    }
    public void finish2(Tile tile, boolean hitEntity) {
        if(hitEntity || numReflection >= maxReflection) {
            lifeOut = true;
            movingParticle.getEmitters().get(0).setContinuous(false);
            explosion.start();
        } else {
            if(tile.numAdjacent == 2) {
                if (tile.left && tile.right && !tile.up && !tile.down) {
                    speedY *= -1;
                    numReflection++;
                }
                if (!tile.left && !tile.right && tile.up && tile.down) {
                    speedX *= -1;
                    numReflection++;
                } else {
                    if (tile.down) {
                        speedY = Math.abs(speedY);
                        speedX *= -1;
                    } else {
                        speedY = -Math.abs(speedY);
                        speedX *= -1;
                    }
                    numReflection++;
                }
            } else {
                if(tile.left && tile.right) {
                    speedY *= -1;
                    numReflection++;
                } else if(tile.up && tile.down) {
                    speedX *= -1;
                    numReflection++;
                }
            }
        }
    }
}
