package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;

import java.util.ArrayList;

public class EntityManager {
    private ControlCenter c;

    public static ArrayList<Entity> entities;
    public static Player player;

    public EntityManager(ControlCenter c, Player player) {
        this.c = c;

        entities = new ArrayList<Entity>();

        this.player = player;
        entities.add(this.player);
    }

    public void tick() {
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).tick();
            if(!entities.get(i).active) {
                entities.remove(entities.get(i));
                i--;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).render(batch);
        }
    }
}
