package com.everlongn.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.everlongn.assets.Items;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.states.GameState;
import com.everlongn.utils.Constants;
import com.everlongn.utils.Tool;

import java.util.ArrayList;

import static com.everlongn.utils.Constants.PPM;

public class Item {
    public static Item[] items = new Item[1000];
    public static boolean canPick;
    public boolean collected;

    //----------miscellaneous item declarations

    public static Item log = new Item(Items.log, "Wood", 0, true, true,
            50, 50, 60, 60, 16,"doesn't look very healthy...", 0, 0, null);

    //----------
    public float x, y;
    public int width, height, id, count, capacity, itemWidth, itemHeight, direction;
    public long timeDropped;
    public boolean stackable, degeneratable, pickedUp, discovered;
    public String name, description;
    public Sprite texture;
    public String type;
    public Body body;
    public float holdX, holdY;
    public TextureRegion[] display;
    public Rectangle bounds;

    // weapon abstract properties
    public String[] elemental;

    // melee weapon properties
    public int damage, force;
    public float critChance;
    public boolean heavy;
    public float drawSpeed, swingSpeed;
    public Sound swingSound;

    // global ranged properties
    public float refreshSpeed;

    // magic weapon properties
    public float healthConsumption, burst;

    // ranged weapon properties
    public float throwingDamage, throwSpeed;

    public Item(TextureRegion texture, String name, int id, boolean stackable, boolean degeneratable,
                int width, int height, int itemWidth, int itemHeight, int capacity, String description, float holdX, float holdY, TextureRegion[] display) {
        this.texture = new Sprite(texture);
        this.name = name;
        this.id = id;
        this.stackable = stackable;
        this.degeneratable = degeneratable;
        this.description = description;
        this.width = width;
        this.height = height;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.capacity = capacity;
        this.holdX = holdX;
        this.holdY = holdY;
        this.display = display;
        count = 1;

        this.timeDropped = 0;

        items[id] = this;
        bounds = new Rectangle(0, 0, width, height);
    }

    public void tick() {
        if(!pickedUp) {
            this.timeDropped += Gdx.graphics.getDeltaTime();
            if(this.timeDropped >= 180 && this.degeneratable) {
                GameState.world.destroyBody(this.body);
                this.pickedUp = true;
            }
        }

        bounds.setPosition(body.getPosition().x*PPM - width/2, body.getPosition().y*PPM - height/2);

        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && bounds.contains(Player.mouseWorldPos().x, Player.mouseWorldPos().y) && bounds.overlaps(Player.itemPickBound) && canPick) {
            collected = true;
            canPick = false;
        }

        if(!Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            canPick = true;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && bounds.overlaps(Player.itemPickBound)) {
            collected = true;
        }

        if(collected) {
            float sx = Player.itemCollectBound.x/Constants.PPM;
            float sy = Player.itemCollectBound.y/Constants.PPM;

            if(Math.abs(sx - body.getPosition().x) > 75/Constants.PPM) {
                collected = false;
                return;
            }

            double angle = Math.atan2(sx - body.getPosition().x,
                    sy - body.getPosition().y);

            body.setLinearVelocity((float)Math.sin(angle) * (10f), (float) Math.cos(angle) * (10f));

            if(bounds.overlaps(Player.itemCollectBound)) {
                GameState.inventory.addItem(this);
                GameState.world.destroyBody(body);
                pickedUp = true;
            }
        } else {
            body.setLinearVelocity(body.getLinearVelocity().x/1.04f, body.getLinearVelocity().y);
        }
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Item createNew(float x, float y, int amount, float forceX, float forceY) {
        Item i = new Item(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
        i.setPosition(x, y);
        i.count = amount;
        i.body = Tool.createBox((int)x, (int)y, width, height, false, 1.75f, Constants.BIT_PROJECTILE, Constants.BIT_TILE, (short)0, i);
        if(forceX > 0) {
            i.direction = 1;
        } else {
            i.direction = 0;
        }
        i.body.applyForceToCenter(forceX, forceY, false);
        return i;
    }

    public Item createNew(int count) {
        Item i = new Item(texture, name, id, stackable, degeneratable, width, height, itemWidth, itemHeight, capacity, description, holdX, holdY, display);
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
        if(body != null)
            batch.draw(texture, body.getPosition().x*Constants.PPM - width/2, body.getPosition().y*Constants.PPM - height/2 - height/5, width, height);
        batch.end();
    }

    public void onClick() {

    }
}
