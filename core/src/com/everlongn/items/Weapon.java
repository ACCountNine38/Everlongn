package com.everlongn.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.everlongn.utils.Constants;

public class Weapon extends Item {
    public Weapon(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable, int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display, String[] elemental) {
        super(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        this.elemental = elemental;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        if(body != null) {
            batch.draw(display[direction], body.getPosition().x * Constants.PPM - width / 2, body.getPosition().y * Constants.PPM - height / 2 - height / 5, width, height);
        }
        batch.end();
    }
}
