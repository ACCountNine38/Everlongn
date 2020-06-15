package com.everlongn.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Item;

import java.util.ArrayList;

public class EntityManager {
    private ControlCenter c;

    public static ArrayList<Entity> entities, staticEntities;
    public static ArrayList<Item> items;
    public static Player player;

    public EntityManager(ControlCenter c, Player player) {
        this.c = c;

        entities = new ArrayList<Entity>();
        staticEntities = new ArrayList<Entity>();
        items = new ArrayList<Item>();

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
        for(int i = 0; i < staticEntities.size(); i++) {
            staticEntities.get(i).tick();
            if(!staticEntities.get(i).active) {
                staticEntities.remove(staticEntities.get(i));
                i--;
            }
        }
        for(int i = 0; i < items.size(); i++) {
            items.get(i).tick();
            if(items.get(i).pickedUp) {
                items.remove(items.get(i));
                i--;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).render(batch);
        }
        for(int i = 0; i < staticEntities.size(); i++) {
            staticEntities.get(i).render(batch);
        }
        for(int i = 0; i < items.size(); i++) {
            items.get(i).render(batch);
        }
    }
}
