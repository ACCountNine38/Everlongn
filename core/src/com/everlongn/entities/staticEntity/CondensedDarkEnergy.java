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
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.ScreenShake;

import static com.everlongn.utils.Constants.PPM;

public class CondensedDarkEnergy extends StaticEntity {
    public boolean activate = false;
    public float chargeRadius = 450f;

    public CondensedDarkEnergy(float x, float y) {
        super(x, y, 80, 80, 50);

        resetHealth(1);
        resistance = 0;
        baseRegenAmount = 0f;
    }

    @Override
    public void tick() {
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
            if(chargeRadius > 1250) {
                GameState.constantUpdateEntities.remove(this);
                destroyed = true;
            }
        }
    }

    private void explode() {
        float xLoc = x*Constants.PPM;
        float yLoc = y*Constants.PPM;
        Ellipse exp = new Ellipse();
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
                        c.hurt((float) 2000, GameState.difficulty);
                        thrust = 70;
                        force = 1800;
                    } else if(dx < 750) {
                        c.hurt((float) 1000, GameState.difficulty);
                        thrust = 50;
                        force = 1500;
                    } else {
                        c.hurt((float) 50, GameState.difficulty);
                        thrust = 35;
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
        int sx = Math.max(0, (int)x - 20);
        int sy = Math.max(0, (int)y - 4);
        int ex = Math.min(GameState.worldWidth, (int)x + 20);
        int ey = Math.min(GameState.worldHeight, (int)y + 20);

        for(int x = sx; x <= ex; x++) {
            for(int y = sy; y <= ey; y++) {
                if(GameState.tiles[x][y] != null &&
                        Intersector.overlaps(explosionCircle, GameState.tiles[x][y].getBound())) {
                    GameState.tiles[x][y].exploded = true;
                    GameState.tiles[x][y].damage(10000);
                }
                if(GameState.walls[x][y] != null &&
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
