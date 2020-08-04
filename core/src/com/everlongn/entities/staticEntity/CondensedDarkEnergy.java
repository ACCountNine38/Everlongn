package com.everlongn.entities.staticEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.everlongn.assets.Herbs;
import com.everlongn.assets.Sounds;
import com.everlongn.assets.StaticObjects;
import com.everlongn.assets.UI;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.StaticEntity;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.ScreenShake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static com.everlongn.utils.Constants.PPM;

public class CondensedDarkEnergy extends StaticEntity {
    public boolean activate = false;
    public float chargeRadius = 450f, explosionTimer;
    public int xOffset = 0;

    private Queue<Integer> heights;

    public CondensedDarkEnergy(float x, float y) {
        super(x, y, 80, 80, 50);

        resetHealth(1);
        resistance = 0;
        baseRegenAmount = 0f;

        heights = new LinkedList<>();

        for(int i = 0; i < 9; i++) {
            heights.add(5);
        }
        for(int i = 0; i < 6; i++) {
            heights.add(4);
        }
        for(int i = 0; i < 5; i++) {
            heights.add(3);
        }
        for(int i = 0; i < 4; i++) {
            heights.add(2);
        }
        for(int i = 0; i < 3; i++) {
            heights.add(1);
        }
    }

    @Override
    public void tick() {
        if(GameState.tiles[(int)x][(int)y-1] == null) {
            exploded = true;
            health = 0;
        }

        if(!activate) {
            if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && getBound().contains(new Vector2(Player.mouseWorldPos().x, Player.mouseWorldPos().y))) {
                activate = true;
                Sounds.playSound(Sounds.darkEnergyCharge);
                GameState.constantUpdateEntities.add(this);
            }
        }
        if(activate) {
            if(chargeRadius > 0) {
                chargeRadius -= 1.5f;
                if(chargeRadius < 0) {
                    chargeRadius = 0;
                }
            } else {
                activate = false;
                Sounds.playSound(Sounds.darkEnergyExplode);
                Sounds.playSound(Sounds.ring);
                explode();
                GameState.shakeForce.add(new ScreenShake(30, 2.5f, true));
                exploded = true;
            }
        }

        if(exploded) {
            chargeRadius += 15f;
            explosionTimer += ControlCenter.delta;
            if(explosionTimer >= 0.1f && !heights.isEmpty()) {
                explosionTimer = 0;
                int height = heights.poll();
                for(int i = 0; i <= height; i++) {
                    if((int)x-xOffset >= 0 && (int)y-i >= 0) {
                        if(GameState.tiles[(int)x-xOffset][(int)y-i] != null) {
                            GameState.tiles[(int) x - xOffset][(int) y - i].exploded = true;
                            GameState.tiles[(int) x - xOffset][(int) y - i].damage(10000);
                        }

                        if(GameState.walls[(int)x-xOffset][(int)y-i] != null) {
                            GameState.walls[(int) x - xOffset][(int) y - i].exploded = true;
                            GameState.walls[(int) x - xOffset][(int) y - i].damage(10000);
                        }

                        if(GameState.herbs[(int)x-xOffset][(int)y-i] != null) {
                            GameState.herbs[(int) x - xOffset][(int) y - i].exploded = true;
                            GameState.herbs[(int) x - xOffset][(int) y - i].hurt(10000);
                        }
                    }
                    if((int)x-xOffset >= 0 && (int)y+i < GameState.worldHeight) {
                        if(GameState.tiles[(int)x-xOffset][(int)y+i] != null) {
                            GameState.tiles[(int) x - xOffset][(int) y + i].exploded = true;
                            GameState.tiles[(int) x - xOffset][(int) y + i].damage(10000);
                        }

                        if(GameState.walls[(int)x-xOffset][(int)y+i] != null) {
                            GameState.walls[(int) x - xOffset][(int) y + i].exploded = true;
                            GameState.walls[(int) x - xOffset][(int) y + i].damage(10000);
                        }

                        if(GameState.herbs[(int)x-xOffset][(int)y+i] != null) {
                            GameState.herbs[(int)x-xOffset][(int)y+i].exploded = true;
                            GameState.herbs[(int)x-xOffset][(int)y+i].hurt(10000);
                        }
                    }
                    if((int)x+xOffset < GameState.worldWidth && (int)y-i >= 0) {
                        if(GameState.tiles[(int)x+xOffset][(int)y-i] != null) {
                            GameState.tiles[(int) x + xOffset][(int) y - i].exploded = true;
                            GameState.tiles[(int) x + xOffset][(int) y - i].damage(10000);
                        }

                        if(GameState.walls[(int)x+xOffset][(int)y-i] != null) {
                            GameState.walls[(int) x + xOffset][(int) y - i].exploded = true;
                            GameState.walls[(int) x + xOffset][(int) y - i].damage(10000);
                        }

                        if(GameState.herbs[(int)x+xOffset][(int)y-i] != null) {
                            GameState.herbs[(int)x+xOffset][(int)y-i].exploded = true;
                            GameState.herbs[(int)x+xOffset][(int)y-i].hurt(10000);
                        }
                    }
                    if((int)x+xOffset < GameState.worldWidth && (int)y+i < GameState.worldHeight && GameState.tiles[(int)x+xOffset][(int)y+i] != null) {
                        if(GameState.tiles[(int)x+xOffset][(int)y+i] != null) {
                            GameState.tiles[(int) x + xOffset][(int) y + i].exploded = true;
                            GameState.tiles[(int) x + xOffset][(int) y + i].damage(10000);
                        }

                        if(GameState.walls[(int)x+xOffset][(int)y+i] != null) {
                            GameState.walls[(int) x + xOffset][(int) y + i].exploded = true;
                            GameState.walls[(int) x + xOffset][(int) y + i].damage(10000);
                        }

                        if(GameState.herbs[(int)x+xOffset][(int)y+i] != null) {
                            GameState.herbs[(int)x+xOffset][(int)y+i].exploded = true;
                            GameState.herbs[(int)x+xOffset][(int)y+i].hurt(10000);
                        }
                    }
                }
                xOffset++;
            }
            if(chargeRadius > 1250 && heights.isEmpty()) {
                GameState.constantUpdateEntities.remove(this);
                destroyed = true;
            }
        }
    }

    private void explode() {
        float xLoc = x*Constants.PPM;
        float yLoc = y*Constants.PPM;
        Circle explosionCircle = new Circle(xLoc, yLoc, 1250);
        for(int i = 0; i < EntityManager.entities.size(); i++) {
            if(Intersector.overlaps(explosionCircle, EntityManager.entities.get(i).getBound())
                    && EntityManager.entities.get(i) != this) {
                if(EntityManager.entities.get(i) instanceof Creature) {
                    Creature c = (Creature)EntityManager.entities.get(i);

                    c.stunned = true;

                    float angle = (float)(Math.random()*(Math.PI/6) + Math.PI/8);
                    float dx = Math.abs(c.getBody().getPosition().x*Constants.PPM - xLoc);

                    int thrust = 0;
                    int force = 0;
                    if(dx < 250) {
                        c.hurt((float) 2000);
                        thrust = 30;
                        force = 1800;
                    } else if(dx < 750) {
                        c.hurt((float) 1000);
                        thrust = 20;
                        force = 1500;
                    } else {
                        c.hurt((float) 50);
                        thrust = 10;
                        force = 1200;
                    }

                    if(c instanceof Player) {
                        if (xLoc < c.body.getPosition().x*Constants.PPM) {
                            c.xThrust = (float) Math.cos(angle) * ((float) thrust);
                        } else {
                            c.xThrust = -(float) Math.cos(angle) * ((float) thrust);
                        }
                    } else {
                        if (xLoc < c.body.getPosition().x*Constants.PPM) {
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
        ParticleEffect bombExplosion = new ParticleEffect();
        bombExplosion.load(Gdx.files.internal("particles/bombParticles"), Gdx.files.internal(""));
        bombExplosion.getEmitters().first().setPosition(xLoc, yLoc);
        bombExplosion.getEmitters().first().scaleSize(2);
        bombExplosion.start();
        EntityManager.particles.add(bombExplosion);
    }

    public Rectangle getBound() {
        return new Rectangle(x*Constants.PPM + Tile.TILESIZE/2 - width/2, y*Constants.PPM + Tile.TILESIZE/2 - height/2, width, height);
    }

    public Rectangle getExplosionBound() {
        return new Rectangle(x*PPM + Tile.TILESIZE/2 - chargeRadius, y*Constants.PPM + Tile.TILESIZE - chargeRadius, chargeRadius*2, chargeRadius*2);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(exploded) {
            alpha -= 0.05;
            if(alpha <= 0)
                alpha = 0;
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        }

        batch.draw(StaticObjects.condensedDarkEnergy, x*Constants.PPM - width/2, y*Constants.PPM - height/2, width, height);

        if(activate && chargeRadius > 0 && !exploded) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1 - chargeRadius / 450f);
            batch.draw(UI.shockwave1, x * Constants.PPM - chargeRadius, y * Constants.PPM - chargeRadius, chargeRadius * 2, chargeRadius * 2);
            batch.draw(UI.shockwave2, x * Constants.PPM - chargeRadius, y * Constants.PPM - chargeRadius, chargeRadius * 2, chargeRadius * 2);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);
        }

        if(exploded) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1 - chargeRadius/1250);
            batch.draw(UI.shockwave3, x * Constants.PPM - chargeRadius, y * Constants.PPM - chargeRadius, chargeRadius * 2, chargeRadius * 2);
            batch.draw(UI.shockwave2, x * Constants.PPM - chargeRadius, y * Constants.PPM - chargeRadius, chargeRadius * 2, chargeRadius * 2);
            batch.draw(UI.shockwave1, x * Constants.PPM - chargeRadius, y * Constants.PPM - chargeRadius, chargeRadius * 2, chargeRadius * 2);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);
        }

        batch.end();
    }

    @Override
    public void finish() {

    }
}
