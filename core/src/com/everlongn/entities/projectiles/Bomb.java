package com.everlongn.entities.projectiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.ThrowWeapons;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.Throw;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Throwing;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.ScreenShake;
import com.everlongn.utils.Tool;

public class Bomb extends Throw {
    public ParticleEffect explosion;
    public float tickTimer;
    public boolean explode;

    public Bomb(float x, float y, int direction, float angle, float damage) {
        super(x, y, 5, 5, 10);
        this.direction = direction;
        this.angle = angle;
        this.damage = damage;

        body = Tool.createBall((int) (x), (int) (y), Throwing.bomb.width/2 - 10, 10f,
                (short) Constants.BIT_PROJECTILE, (short) (Constants.BIT_TILE | Constants.BIT_ENEMY), (short)0, this);

        body.getFixtureList().first().setRestitution(0.25f);

        float forceX = (float)(Math.abs(Math.sin(angle)*1500));
        float forceY = (float)(Math.cos(angle)*1500);

        if(direction == 0) {
            moveByForce(new Vector2(-forceX, -forceY));
        } else {
            moveByForce(new Vector2(forceX, -forceY));
        }

        throwBound = new Rectangle(0, 0, Throwing.triStar.width, Throwing.triStar.height);
    }

    @Override
    public void tick() {
        if(!lifeOut) {
            if(direction == 0)
                rotation += 3;
            else
                rotation -= 3;
        } else {
            if(direction == 0)
                rotation += 10;
            else
                rotation -= 10;
            body.setLinearVelocity(body.getLinearVelocity().x/1.06f, body.getLinearVelocity().y);
            tickTimer += ControlCenter.delta;
            if(tickTimer > 0.45f) {
                explode = true;
            }
        }
        if(explode) {
            if(!exploded) {
                Sounds.playSound(Sounds.bomb);
                explosionTimer += Gdx.graphics.getDeltaTime();
                if(explosionTimer > 0.01) {
                    explode();
                    exploded = true;
                }
            }

            if (lifeOut) {
                GameState.world.destroyBody(body);
                active = false;
            }
        }
    }

    public void explode() {
        Rectangle explosionRectangle = new Rectangle(body.getPosition().x*Constants.PPM - 300, body.getPosition().y*Constants.PPM - 300,
                600, 600);

        Circle explosionCircle = new Circle(body.getPosition().x*Constants.PPM, body.getPosition().y*Constants.PPM, 150);

        GameState.shakeForce.add(new ScreenShake(15, 0.5f));
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(EntityManager.entities.get(i).getBound().overlaps(explosionRectangle) && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float angle = (float)(Math.random()*(Math.PI/6) + Math.PI/8);
                    float dx = Math.abs(c.getBody().getPosition().x*Constants.PPM - body.getPosition().x*Constants.PPM);

                    int thrust = 0;
                    int force = 0;
                    if(dx < 100) {
                        c.hurt((float) 500, GameState.difficulty);
                        thrust = 40;
                        force = 1500;
                    } else if(dx < 150) {
                        c.hurt((float) 200, GameState.difficulty);
                        thrust = 30;
                        force = 1250;
                    } else if(dx < 225) {
                        c.hurt((float) 100, GameState.difficulty);
                        thrust = 20;
                        force = 1050;
                    } else if(dx < 275) {
                        c.hurt((float) 50, GameState.difficulty);
                        thrust = 15;
                        force = 950;
                    } else {
                        c.hurt((float) 20, GameState.difficulty);
                        thrust = 10;
                        force = 850;
                    }

                    if(c instanceof Player) {
                        if (body.getPosition().x < c.body.getPosition().x) {
                            c.xThrust = (float) Math.cos(angle) * ((float) thrust);
                        } else {
                            c.xThrust = -(float) Math.cos(angle) * ((float) thrust);
                        }
                    } else {
                        if (body.getPosition().x < c.body.getPosition().x) {
                            c.body.applyForceToCenter(
                                    (float)Math.cos(angle)*force, (float)Math.sin(angle)*(force), false);
                        } else {
                            c.body.applyForceToCenter(
                                    -(float)Math.cos(angle)*force, (float)Math.sin(angle)*(force), false);
                        }
                    }
                }
            }
        }
        int currentX = (int)body.getPosition().x;
        int currentY = (int)body.getPosition().y;

        int sx = Math.max(0, currentX - 4);
        int sy = Math.max(0, currentY - 4);
        int ex = Math.min(GameState.worldWidth, currentX + 4);
        int ey = Math.min(GameState.worldHeight, currentY + 4);

        for(int x = sx; x <= ex; x++) {
            for(int y = sy; y <= ey; y++) {
                if(GameState.tiles[x][y] != null &&
                        Intersector.overlaps(explosionCircle, GameState.tiles[x][y].getBound())) {
                    GameState.tiles[x][y].exploded = true;
                    GameState.tiles[x][y].damage(10000);
                }
                if(GameState.walls[x][y] != null && GameState.walls[x][y].numAdjacent < 4 &&
                        Intersector.overlaps(explosionCircle, GameState.walls[x][y].getBound())) {
                    GameState.walls[x][y].exploded = true;
                    GameState.walls[x][y].damage(10000);
                }
                if(GameState.herbs[x][y] != null &&
                        Intersector.overlaps(explosionCircle, GameState.herbs[x][y].getBound())) {
                    GameState.herbs[x][y].exploded = true;
                    GameState.herbs[x][y].hurt(10000, GameState.difficulty);
                }
            }
        }

        ParticleEffect bombExplosion = new ParticleEffect();
        bombExplosion.load(Gdx.files.internal("particles/bombParticles"), Gdx.files.internal(""));
        bombExplosion.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        bombExplosion.start();
        EntityManager.particles.add(bombExplosion);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(body != null) {
            if (exploded) {
                alpha -= 0.1;
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
            }
            if(direction == 1)
                batch.draw(ThrowWeapons.bombR, body.getPosition().x * Constants.PPM - Throwing.bomb.width / 2 + width/2, body.getPosition().y * Constants.PPM - Throwing.bomb.height / 2 + height/2,
                        Throwing.bomb.width / 2, Throwing.bomb.height / 2,
                        Throwing.bomb.width, Throwing.dagger.height, 1f, 1f, rotation);
            else
                batch.draw(ThrowWeapons.bombL, body.getPosition().x * Constants.PPM - Throwing.bomb.width / 2 + width/2, body.getPosition().y * Constants.PPM - Throwing.bomb.height / 2 + height/2,
                        Throwing.bomb.width / 2, Throwing.bomb.height / 2,
                        Throwing.bomb.width, Throwing.bomb.height, 1f, 1f, rotation);
            if(exploded) {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
            }
        }

        batch.end();
    }

    @Override
    public void finish() {
        lifeOut = true;
    }
}
