package com.everlongn.utils.frameworks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.entities.creatures.Spiderling;
import com.everlongn.game.ControlCenter;
import com.everlongn.items.*;
import com.everlongn.states.GameState;
import com.everlongn.tiles.Tile;
import com.everlongn.utils.Constants;
import com.everlongn.utils.TextManager;
import com.everlongn.utils.Tool;

public class Message {
    public static GlyphLayout layout = new GlyphLayout();
    public String text;
    public static float maxDuration = 15;
    public float currentDuration, x, y, height, alpha = 1;
    public boolean isCommand, active = true, fading;

    public float bonusY;

    public Color color;

    public Message(int x, int y, float height, String text, boolean isCommand, Color color) {
        this.text = text;
        this.isCommand = isCommand;
        this.x = x;
        this.y = y;
        this.height = height;
        this.color = color;

        if(isCommand) {
            if(GameState.mode.equals("Testing")) {
                this.text = text.substring(1);
                checkCommandPrompt();
            } else {
                Telepathy.messages.add(new Message(x, y , height, "Commands are only allowed for Testing Mode", false, Color.YELLOW));
            }
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
            Tool.changeCursor(1);
            GameState.save();
            GameState.exiting = true;
            return;
        } else if(text.equals("exit")) {
            Tool.changeCursor(1);
            GameState.save();
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
                EntityManager.player.body.setTransform(GameState.spawnX/Tile.TILESIZE, GameState.spawnY/Tile.TILESIZE, 0);
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Teleported to spawn", false, Color.YELLOW));
            } catch (NumberFormatException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(text.equals("godmode")) {
            EntityManager.player.godMode = !EntityManager.player.godMode;
            if(EntityManager.player.godMode) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Player is now invulnerable", false, Color.YELLOW));
            } else {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Player is now vulnerable", false, Color.YELLOW));
            }
            return;
        } else if(text.equals("lights out")) {
            GameState.lightsOn = !GameState.lightsOn;
            if(GameState.lightsOn) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Enabling Light Casting", false, Color.YELLOW));
            } else {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Disabling Light Casting", false, Color.YELLOW));
            }
            return;
        } else if(text.equals("get arcane set")) {
            GameState.inventory.addItem(Arcane.arcaneCaster.createNew(1));
            GameState.inventory.addItem(Arcane.arcaneReflection.createNew(1));
            GameState.inventory.addItem(Arcane.arcaneEscort.createNew(1));
            GameState.inventory.addItem(Arcane.arcaneRebound.createNew(1));
            GameState.inventory.addItem(Arcane.arcaneEruption.createNew(1));
            GameState.inventory.addItem(Arcane.arcaneDevastation.createNew(1));
            GameState.inventory.addItem(Arcane.shadowStaff.createNew(1));

            Telepathy.messages.add(new Message((int)x, (int)y , height, "Given 1 of each Arcane Weapon", false, Color.YELLOW));

            return;
        } else if(text.equals("get melee set")) {
            GameState.inventory.addItem(Melee.broadSword.createNew(1));
            GameState.inventory.addItem(Melee.dragondance.createNew(1));
            Telepathy.messages.add(new Message((int)x, (int)y , height, "Given 1 of each Melee Weapon", false, Color.YELLOW));

            return;
        } else if(text.equals("get item set")) {
            GameState.inventory.addItem(Item.log.createNew(Item.log.capacity));
            GameState.inventory.addItem(Throwing.stone.createNew(Throwing.stone.capacity));
            GameState.inventory.addItem(Item.omniEssence.createNew(Item.omniEssence.capacity));
            GameState.inventory.addItem(Item.omniCrystal.createNew(Item.omniCrystal.capacity));
            GameState.inventory.addItem(Item.demiEssence.createNew(Item.demiEssence.capacity));
            GameState.inventory.addItem(Item.demiCrystal.createNew(Item.demiCrystal.capacity));
            GameState.inventory.addItem(Item.spiderLimb.createNew(Item.spiderLimb.capacity));
            GameState.inventory.addItem(Item.mossyFluid.createNew(Item.mossyFluid.capacity));

            Telepathy.messages.add(new Message((int)x, (int)y , height, "Given 1 of each material item", false, Color.YELLOW));

            return;
        } else if(text.equals("get throwing set")) {
            GameState.inventory.addItem(Throwing.stone.createNew(Throwing.stone.capacity));
            GameState.inventory.addItem(Throwing.triStar.createNew(Throwing.triStar.capacity));
            GameState.inventory.addItem(Throwing.shuriken.createNew(Throwing.shuriken.capacity));
            GameState.inventory.addItem(Throwing.dagger.createNew(Throwing.dagger.capacity));
            GameState.inventory.addItem(Throwing.throwKnife.createNew(Throwing.throwKnife.capacity));

            Telepathy.messages.add(new Message((int)x, (int)y , height, "Given 1 of each throwing item", false, Color.YELLOW));

            return;
        } else if(text.equals("clear text")) {
            for(int i = 0; i < Telepathy.messages.size(); i++) {
                Telepathy.messages.get(i).currentDuration = maxDuration;
            }
            return;
        } else if(text.equals("help")) {
            String message = "/help stage : view stage control commands\n" +
                    "/help debug : view debug and testing commands\n" +
                    "/help player : view player customization commands\n" +
                    "/help utility : view other commands\n";
            layout.setText(TextManager.bfont, message);
            Telepathy.messages.add(new Message((int)x, (int)y , height + layout.height, "Command catagories:\n" + message, false, Color.YELLOW));
            return;
        } else if(text.equals("help stage")) {
            String message = "/menu : return to world selection\n" +
                    "/exit : quit the program\n";
            layout.setText(TextManager.bfont, message);
            Telepathy.messages.add(new Message((int)x, (int)y , height + layout.height, "Stage Control Commands:\n" + message, false, Color.YELLOW));
            return;
        } else if(text.equals("help debug")) {
            String message = "/analog : displays/disables the debug analog\n" +
                    "/show bounds : displays/disables all the nearby bounding boxes\n" +
                    "/lights out : disable/enable ray casting and remove all shadows\n" +
                    "/get item set : gets one of each existing miscellaneous item\n" +
                    "/get melee set : gets one of each existing melee weapon\n" +
                    "/get arcane set : gets one of each existing arcane weapon\n";
            layout.setText(TextManager.bfont, message);
            Telepathy.messages.add(new Message((int)x, (int)y , height + layout.height, "Debug and Testing Commands:\n" + message, false, Color.YELLOW));
            return;
        } else if(text.equals("help player")) {
            String message = "/set health amount : sets player’s health to a specific amount\n" +
                    "/set health max : sets player’s health to max\n" +
                    "/set speed amount : sets player’s speed to a specific amount\n" +
                    "/speed+ amount : increase player’s speed by an amount\n" +
                    "/speed- amount : decrease player’s speed by an amount\n" +
                    "/tp x y : teleport the player to block x, y (relative position)\n" +
                    "/tp spawn : teleport the player to spawn point\n" +
                    "/set-spawn x y : sets the spawn location of the player\n" +
                    "/learn skill : learns the specified skill\n" +
                    "/forget skill : forgets the specified skill\n" +
                    "/wipe memory : return to your original form and forget all skills\n";
            layout.setText(TextManager.bfont, message);
            Telepathy.messages.add(new Message((int)x, (int)y , height + layout.height, "Player Customization Commands:\n" + message, false, Color.YELLOW));
            return;
        } else if(text.equals("help utility")) {
            String message = "/godmode : fully heals and making the player invulnerable\n" +
                    "/kill all : exterminate any non-degenerable entities\n" +
                    "/clear inventory : wipes the inventory\n" +
                    "/get name amount : get a specific amount of a requested item\n" +
                    "/spawn name x y : spawns a specific entity at location x, y\n" +
                    "/clear text : removes all the messages\n";
            layout.setText(TextManager.bfont, message);
            Telepathy.messages.add(new Message((int)x, (int)y , height + layout.height, "Utility Commands:\n" + message, false, Color.YELLOW));
            return;
        } else if(text.equals("wipe memory")) {
            Player.dash = false;
            Player.duoToss = false;
            Player.triToss = false;
            Player.glaiveLord = false;

            String message ="You have forgotten all your skills\n" +
                    "You have returned to your original form";
            layout.setText(TextManager.bfont, message);
            Telepathy.messages.add(new Message((int)x, (int)y , height + layout.height, message, false, Color.YELLOW));

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
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Set health to: " + Integer.parseInt(chars[2]), false, Color.YELLOW));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("set") && chars[1].equals("speed")) {
            try {
                EntityManager.player.speed = Integer.parseInt(chars[2]);
                if(EntityManager.player.speed < 0)
                    EntityManager.player.speed = 0;
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Set speed to: " + Integer.parseInt(chars[2]), false, Color.YELLOW));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("tp")) {
            try {
                if(Integer.parseInt(chars[1]) >= 0 && Integer.parseInt(chars[1]) < GameState.worldWidth &&
                        Integer.parseInt(chars[2]) >= 0 && Integer.parseInt(chars[2]) < GameState.worldHeight) {
                    EntityManager.player.body.setTransform(Integer.parseInt(chars[1]), Integer.parseInt(chars[2]), 0);
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Teleported to: " + Integer.parseInt(chars[1]) + ", " + Integer.parseInt(chars[2]), false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Teleportation out of bounds", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("set-spawn")) {
            try {
                if(Integer.parseInt(chars[1]) >= 0 && Integer.parseInt(chars[1]) < GameState.worldWidth &&
                        Integer.parseInt(chars[2]) >= 0 && Integer.parseInt(chars[2]) < GameState.worldHeight) {
                    GameState.spawnX = Integer.parseInt(chars[1]) * Tile.TILESIZE;
                    GameState.spawnY = Integer.parseInt(chars[2]) * Tile.TILESIZE;
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Spawn set to: " + Integer.parseInt(chars[1]) + ", " + Integer.parseInt(chars[2]), false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Spawn point out of bounds", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("get")) {
            try {
                int count = Integer.parseInt(chars[chars.length-1]);
                String name = "";
                for(int i = 1; i < chars.length - 1; i++) {
                    name += chars[i] + " ";
                }
                name = name.substring(0, name.length()-1);
                if(count >= 0) {
                    Item item = null;
                    for(int i = 0; i < Item.items.length; i++) {
                        if(Item.items[i] != null && Item.items[i].name.equals(name)) {
                            item = Item.items[i];
                            GameState.inventory.addItem(Item.items[i].createNew(count));
                            break;
                        }
                    }
                    if(item != null)
                        Telepathy.messages.add(new Message((int)x, (int)y , height, "Added " + count + " " + item.name + "(s) to inventory", false, Color.YELLOW));
                    else
                        Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid item name", false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid item count", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid item data", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("spawn")) {
            try {
                int xLoc = Integer.parseInt(chars[2]);
                int yLoc = Integer.parseInt(chars[3]);
                if(xLoc >= 0 && xLoc < GameState.worldWidth && yLoc >= 0 && yLoc < GameState.worldHeight) {
                    if(chars[1].equals("Spider")) {
                        EntityManager.entities.add(new Spiderling(xLoc*Constants.PPM, yLoc*Constants.PPM, (int)(Math.random()*50)+ 75));
                        Telepathy.messages.add(new Message((int)x, (int)y , height, "Spawned Spider at: " + Integer.parseInt(chars[2]) + ", " + Integer.parseInt(chars[3]), false, Color.YELLOW));
                    } else
                        Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid entity name", false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int)x, (int)y , height, "Spawn range out Of bounds", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid spawn data", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("learn")) {
            try {
                String ability = "";
                for(int i = 1; i < chars.length; i++) {
                    ability += chars[i] + " ";
                }
                ability = ability.substring(0, ability.length()-1);

                if(ability.equals("dash")) {
                    Player.dash = true;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You can now " + chars[1], false, Color.YELLOW));
                } else if(ability.equals("duo-toss")) {
                    Player.duoToss = true;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You can now " + chars[1], false, Color.YELLOW));
                } else if(ability.equals("tri-toss")) {
                    Player.triToss = true;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You can now " + chars[1], false, Color.YELLOW));
                } else if(ability.equals("Glaive Lord")) {
                    Player.glaiveLord = true;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You have evolved to " + chars[1], false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "Unknown ability", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid learn data", false, Color.YELLOW));
            }
            return;
        } else if(chars[0].equals("forget")) {
            try {
                String ability = "";
                for(int i = 1; i < chars.length; i++) {
                    ability += chars[i] + " ";
                }
                ability = ability.substring(0, ability.length()-1);

                if(ability.equals("dash")) {
                    Player.dash = false;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You have forgotten " + chars[1], false, Color.YELLOW));
                } else if(ability.equals("duo-toss")) {
                    Player.duoToss = false;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You have forgotten " + chars[1], false, Color.YELLOW));
                } else if(ability.equals("tri-toss")) {
                    Player.triToss = false;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You have forgotten " + chars[1], false, Color.YELLOW));
                } else if(ability.equals("Glaive Lord")) {
                    Player.glaiveLord = false;
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "You have devolved to " + chars[1], false, Color.YELLOW));
                } else {
                    Telepathy.messages.add(new Message((int) x, (int) y, height, "Unknown ability", false, Color.YELLOW));
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid learn data", false, Color.YELLOW));
            }
            return;
        }

        Telepathy.messages.add(new Message((int)x, (int)y , height, "Invalid Command", false, Color.YELLOW));
    }

    public void tick() {
        if(bonusY > 0) {
            y += 3;
            bonusY -= 3;
        }
        currentDuration += Gdx.graphics.getDeltaTime();
        if(currentDuration > maxDuration) {
            fading = true;
        }

        if(fading) {
            alpha -= 0.01;
            if(alpha <= 0) {
                alpha = 0;
                active = false;
            }
        }

        if(!active) {
            Telepathy.messages.remove(this);
        }
    }

    public void render(SpriteBatch batch) {
        TextManager.draw(text, (int)x + 10, (int)y + 8, new Color(color.r, color.g, color.b, alpha), 1f, false);
    }
}
