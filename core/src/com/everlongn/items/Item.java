package com.everlongn.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.assets.Items;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

public class Item {
    public static Item[] items = new Item[1000];

    //----------miscellaneous item declarations

    public static Item log = new Item(Items.log, "wood", 0, true, true,
            50, 50, 99,"doesn't look very healthy...", 0, 0, null);
    public static Item stone = new Item(Items.stone, "stone", 1, true, true,
            50, 50, 99, "looks very durable", 0, 0, null);

//new String[]{"miscellaneous"},
    //----------

    public int width, height, id, x, y, count, capacity;
    public long timeDropped;
    public boolean stackable, degeneratable, pickedUp;
    public String name, description;
    public Sprite texture;
    protected String type;
    public Body body;
    public float holdX, holdY;
    public TextureRegion[] display;

    // weapon abstract properties
    public String[] elemental;

    // melee weapon properties
    public int damage, force;
    public float critChance;
    public boolean heavy;
    public float drawSpeed, swingSpeed;

    // magic weapon properties
    public int healthConsumption, burst;
    public float refreshSpeed;

    public Item(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable,
                int width, int height, int capacity, String description, float holdX, float holdY, TextureRegion[] display) {
        this.texture = new Sprite(texture);
        this.name = name;
        this.id = id;
        this.stackable = stackable;
        this.degeneratable = degeneratable;
        this.type = type;
        this.description = description;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
        this.holdX = holdX;
        this.holdY = holdY;
        this.display = display;
        count = 1;

        timeDropped = 0;

        items[id] = this;
    }

    public void tick() {
        if(!pickedUp) {
            timeDropped += Gdx.graphics.getDeltaTime();
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Item createNew(int x, int y) {
        Item i = new Item(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display);
        i.setPosition(x, y);
        body = Tool.createBox(x, y, width, height, false, 1, Constants.BIT_ITEM, Constants.BIT_TILE, (short)0);
        return i;
    }

    public Item createNew(int count) {
        Item i = new Item(texture, name, id, stackable, degeneratable, width, height, capacity, description, holdX, holdY, display);
        i.pickedUp = true;
        i.count = count;
        return i;
    }

    public boolean isFull() {
        if(count == capacity)
            return true;

        return false;
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x, y, width, height);
        batch.end();
    }

    public void onClick() {

    }
}
