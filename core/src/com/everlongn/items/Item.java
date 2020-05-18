package com.everlongn.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.assets.Items;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

public class Item {
    public static Item[] items = new Item[1000];

    //----------miscellaneous item declarations

    public static Item log = new Item(Items.log, "wood", 0, true, true,
            50, 50, 99,"doesn't look very healthy...");
    public static Item stone = new Item(Items.stone, "stone", 1, true, true,
            50, 50, 99, "looks very durable");

//new String[]{"miscellaneous"},
    //----------

    public int width, height, id, x, y, count, capacity;
    public long timeDropped;
    public boolean stackable, degeneratable, pickedUp;
    public String name, description;
    public Texture texture;
    //protected String[] type;
    public Body body;
//,
//    String[] type
    public Item(Texture texture, String name, int id, boolean stackable, boolean degeneratable,
                int width, int height, int capacity, String description) {
        this.texture = texture;
        this.name = name;
        this.id = id;
        this.stackable = stackable;
        this.degeneratable = degeneratable;
        //this.type = type;
        this.description = description;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
        count = 1;

        //body = Tool.createBox(x, y, width, height, false, 1);

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
        Item i = new Item(texture, name, id, stackable, degeneratable, width, height, capacity, description);
        i.setPosition(x, y);
        return i;
    }

    public Item createNew(int count) {
        Item i = new Item(texture, name, id, stackable, degeneratable, width, height, capacity, description);
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
}
