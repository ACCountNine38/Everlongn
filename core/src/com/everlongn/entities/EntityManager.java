package com.everlongn.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Item;
import com.everlongn.states.GameState;

import java.util.ArrayList;

public class EntityManager {
    private ControlCenter c;

    public static ArrayList<Entity> entities;
    public static ArrayList<Item> items;
    public static ArrayList<Throw> throwing;
    public static ArrayList<ParticleEffect> particles;
    public static Player player;

    public EntityManager(ControlCenter c, Player player) {
        this.c = c;

        entities = new ArrayList<>();
        items = new ArrayList<>();
        throwing = new ArrayList<>();
        particles = new ArrayList<>();

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

        for(int i = 0; i < items.size(); i++) {
            items.get(i).tick();
            if(items.get(i).bounds.contains(Player.mouseWorldPos().x, Player.mouseWorldPos().y)) {
                GameState.itemHover = true;
            }
            if(items.get(i).pickedUp) {
                items.remove(items.get(i));
                i--;
            }
        }
        for(int i = 0; i < throwing.size(); i++) {
            throwing.get(i).tick();
            if(throwing.get(i).throwBound.contains(Player.mouseWorldPos().x, Player.mouseWorldPos().y)) {
                GameState.itemHover = true;
            }
            if(throwing.get(i).pickedUp || !throwing.get(i).active) {
                throwing.remove(throwing.get(i));
                i--;
            }
        }
        for(int i = 0; i < particles.size(); i++) {
            particles.get(i).update(ControlCenter.delta);
            if(particles.get(i).isComplete()) {
                particles.get(i).dispose();
                particles.remove(particles.get(i));
                i--;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).render(batch);
        }
        for(int i = 0; i < items.size(); i++) {
            items.get(i).render(batch);
        }
        for(int i = 0; i < throwing.size(); i++) {
            throwing.get(i).render(batch);
        }
        for(int i = 0; i < particles.size(); i++) {
            batch.begin();
            particles.get(i).draw(batch);
            batch.end();
        }
    }
}
