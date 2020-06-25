package com.everlongn.utils.frameworks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.Inventory;
import com.everlongn.states.GameState;
import com.everlongn.utils.TextManager;

import java.util.ArrayList;

public class Message {
    public String text;
    public static float maxDuration = 10;
    public float currentDuration, x, y, height;
    public boolean isCommand, active = true;

    public float bonusY;

    public Color color;

    public Message(int x, float y, float height, String text, boolean isCommand, Color color) {
        this.text = text;
        this.isCommand = isCommand;
        this.x = x;
        this.y = y;
        this.height = height;
        this.color = color;

        if(isCommand) {
            this.text = text.substring(1);
            checkCommandPrompt();
            active = false;
        } else {
            bonusY += height;
            for(int i = 0; i < Telepathy.messages.size(); i++) {
                Telepathy.messages.get(i).bonusY += height;
            }
        }
    }

    public void checkCommandPrompt() {
        // single word commands
        if(text.equals("menu")) {
            GameState.exiting = true;
            return;
        } else if(text.equals("exit")) {
            System.exit(1);
            return;
        } else if(text.equals("analog")) {
            ControlCenter.DEBUG = !ControlCenter.DEBUG;
            if(ControlCenter.DEBUG)
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Displaying Debug Analog", false, Color.YELLOW));
            else
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Closing Debug Analog", false, Color.YELLOW));

            return;
        } else if(text.equals("show bounds")) {
            ControlCenter.DEBUG_RENDER = !ControlCenter.DEBUG_RENDER;
            if(ControlCenter.DEBUG_RENDER)
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Displaying bounds of all nearby objects", false, Color.YELLOW));
            else
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Removing bound visibility of all nearby objects", false, Color.YELLOW));
            return;
        } else if(text.equals("clear inventory")) {
            Telepathy.messages.add(new Message((int)x, (int)y , height, "Inventory has been wiped", false, Color.YELLOW));
            for(int i = 0; i < Inventory.inventory.length; i++) {
                Inventory.inventory[i] = null;
            }
            return;
        } else if(text.equals("kill all")) {
            Telepathy.messages.add(new Message((int)x, (int)y , height, "All non-player entities has been eliminated", false, Color.YELLOW));
            for(int i = 0; i < EntityManager.entities.size(); i++) {
                if(EntityManager.entities.get(i) instanceof Player)
                    continue;
                EntityManager.entities.get(i).health = -100;
            }
            return;
        } else if(text.equals("set health max")) {
            EntityManager.player.health = EntityManager.player.maxHealth;
            Telepathy.messages.add(new Message((int)x, (int)y , height, "Player's health is set to max", false, Color.YELLOW));
            return;
        } else if(text.equals("tp spawn")) {
            try {
                EntityManager.player.body.setTransform(GameState.spawnX, GameState.spawnY, 0);
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Teleported To Spawn", false, Color.YELLOW));
            } catch (NumberFormatException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        }

        // multi-word commands
        String[] chars = text.split(" ");
        if(chars[0].equals("speed+")) {
            try {
                EntityManager.player.speed += Integer.parseInt(chars[1]);
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Increased speed by: " + Integer.parseInt(chars[1]), false, Color.YELLOW));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("speed-")) {
            try {
                EntityManager.player.speed -= Integer.parseInt(chars[1]);
                if(EntityManager.player.speed < 0)
                    EntityManager.player.speed = 0;
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Decreased speed by: " + Integer.parseInt(chars[1]), false, Color.YELLOW));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("set") && chars[1].equals("health")) {
            try {
                EntityManager.player.health = Integer.parseInt(chars[2]);
                if(EntityManager.player.health < 0)
                    EntityManager.player.health = 0;
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Set Health To: " + Integer.parseInt(chars[2]), false, Color.YELLOW));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("set") && chars[1].equals("speed")) {
            try {
                EntityManager.player.speed = Integer.parseInt(chars[2]);
                if(EntityManager.player.speed < 0)
                    EntityManager.player.speed = 0;
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Set Speed To: " + Integer.parseInt(chars[2]), false, Color.YELLOW));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("tp")) {
            try {
                if(Integer.parseInt(chars[1]) >= 0 && Integer.parseInt(chars[1]) < GameState.worldWidth &&
                        Integer.parseInt(chars[2]) >= 0 && Integer.parseInt(chars[2]) < GameState.worldHeight) {
                    EntityManager.player.body.setTransform(Integer.parseInt(chars[1]), Integer.parseInt(chars[2]), 0);
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Teleported To: " + Integer.parseInt(chars[1]) + ", " + Integer.parseInt(chars[2]), false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Teleportation Out Of Bounds", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        }

        Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
    }

    public void tick() {
        if(bonusY > 0) {
            y += 2;
            bonusY -= 2;
        }
        currentDuration += Gdx.graphics.getDeltaTime();
        if(currentDuration > maxDuration) {
            active = false;
        }

        if(!active) {
            Telepathy.messages.remove(this);
        }
    }

    public void render(SpriteBatch batch) {
        TextManager.draw(text, (int)x + 10, (int)y + 8, color, 1f, false);
    }
}
