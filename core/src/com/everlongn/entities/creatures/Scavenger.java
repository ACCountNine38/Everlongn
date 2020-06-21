package com.everlongn.entities.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.Entities;
import com.everlongn.assets.Tiles;
import com.everlongn.entities.Creature;
import com.everlongn.entities.EntityManager;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.items.Melee;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import static com.everlongn.utils.Constants.PPM;

public class Scavenger extends Creature {
    public Scavenger(ControlCenter c, float x, float y) {
        super(c, x, y, 50, 100, 1, 2.5f);

        body = Tool.createEntity((int)(x), (int)(y), width, height, false, density, true,
                Constants.BIT_ENEMY, (short)(Constants.BIT_PLAYER | Constants.BIT_TILE | Constants.BIT_PROJECTILE), (short)0, this);

        enemyList.add("player");
        setMaxHealth(50);
        setMaxResistance(5);
        knockbackResistance = 1.04f;

        vulnerableToArcane = true;

        destroyed.getEmitters().first().scaleSize(1.5f);
        jumpForce = 750;
    }

    @Override
    public void tick() {
        if(alive) {
            if(getBound().contains(EntityManager.player.mouseWorldPos().x, EntityManager.player.mouseWorldPos().y) &&
                    Inventory.inventory[Inventory.selectedIndex] != null && Inventory.inventory[Inventory.selectedIndex] instanceof Melee) {
                Tool.changeCursor(3);
            }

            if (target == null) {
                findTarget();
            } else {
                chase();
            }

            if (health <= 0) {
                health = 0;
                alive = false;
                body.setActive(false);
                die();
            }
        } else {
            destroyed.update(Gdx.graphics.getDeltaTime());
            destroyed.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
            fadeAlpha-=0.15;
            if(fadeAlpha < 0) {
                fadeAlpha = 0;
            }
            if(destroyed.isComplete()) {
                destroyed.dispose();
                GameState.world.destroyBody(body);
                active = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(alive) {
            batch.draw(Tiles.earthTile, body.getPosition().x * PPM, body.getPosition().y * PPM, width, height);
        } else {
            batch.setColor(0f, 0f, 0f, fadeAlpha);
            batch.draw(Tiles.earthTile, body.getPosition().x * PPM, body.getPosition().y * PPM, width, height);
            batch.setColor(1f, 1f, 1f, 1f);
            destroyed.draw(batch);
        }
        batch.end();
    }

    @Override
    public void die() {
        destroyed.getEmitters().first().setPosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM + 10);
        destroyed.start();
    }
}
