package com.everlongn.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.UI;
import com.everlongn.entities.EntityManager;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.TextManager;

public class Inventory {
    public static Item[] inventory = new Item[24];

    public static int maxInventorySize = 24;
    public static int hotbarSize = 8;
    public static int selectedIndex = 0;

    public static boolean extended, itemPicking;

    private ControlCenter c;

    public static Item draggedItem, pickedItem;

    private int dragBoundX, dragBoundY, draggedIndex;
    private float dragTimer;

    public Inventory(ControlCenter c) {
        this.c = c;
        addItem(Item.stone.createNew(1));
        addItem(Item.log.createNew(88));
        addItem(Item.log.createNew(9));
        addItem(Item.log.createNew(9));
        addItem(Item.log.createNew(9));
        addItem(Item.log.createNew(9));
    }

    public void tick() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            extended = !extended;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedIndex = 0;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedIndex = 1;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedIndex = 2;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            selectedIndex = 3;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            selectedIndex = 4;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            selectedIndex = 5;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            selectedIndex = 6;
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            selectedIndex = 7;
        }
    }

    public void addItem(Item item) {
        if (item.stackable) {
            for(int i = 0; i < inventory.length; i++) {
                if(inventory[i] != null) {
                    if (inventory[i].id == item.id && inventory[i].count < inventory[i].capacity) {
                        inventory[i].count += item.count;
                        if(inventory[i].count > inventory[i].capacity) {
                            addItem(item.createNew(inventory[i].count - inventory[i].capacity));
                            inventory[i].count = inventory[i].capacity;
                        }
                        return;
                    }
                }
            }
            if(canAddItem()) {
                setSlot(item);
            }
        } else {
            if(canAddItem()) {
                setSlot(item);
            }
        }
    }

    public boolean canAddItem() {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null) {
                return true;
            }
        }

        return false;
    }

    public void setSlot(Item item) {
        for(int i = 0; i < inventory.length; i++) {
            if(inventory[i] == null) {
                inventory[i] = item;
                return;
            }
        }
    }

    public void checkDragItem(int i) {
        // check if the items can be stacked together
        if(inventory[i] != null && inventory[i].id == draggedItem.id && inventory[i].stackable &&
               !inventory[i].isFull() && !inventory[draggedIndex].isFull()) {

            if(i == draggedIndex) {
                return;
            }
            if(inventory[i].count + draggedItem.count > inventory[i].capacity) {
                draggedItem.count = inventory[i].count + draggedItem.count - inventory[i].capacity;
                inventory[i].count = inventory[i].capacity;

                inventory[draggedIndex] = draggedItem;
            } else {
                inventory[i].count += draggedItem.count;
                inventory[draggedIndex] = null;
            }
            draggedItem = null;
        } else {

            inventory[draggedIndex] = inventory[i];
            inventory[i] = draggedItem;
            if(i < 8)
                selectedIndex = i;
            draggedItem = null;
        }
    }

    public void checkPickItem(int i) {
        if(itemPicking) {
            if(i == selectedIndex) {
                if(inventory[selectedIndex] == null) {
                    inventory[selectedIndex] = pickedItem;
                    pickedItem = null;
                    itemPicking = false;
                } else if(inventory[selectedIndex].id == pickedItem.id) {
                    if(inventory[selectedIndex].count + pickedItem.count <= inventory[selectedIndex].capacity) {
                        inventory[selectedIndex].count += pickedItem.count;
                        pickedItem = null;
                        itemPicking = false;
                    } else {
                        pickedItem.count -= inventory[selectedIndex].capacity - inventory[selectedIndex].count;
                        inventory[selectedIndex].count = inventory[selectedIndex].capacity;
                    }
                } else {
                    Item tempItem = inventory[selectedIndex];
                    inventory[selectedIndex] = pickedItem;
                    pickedItem = tempItem;
                }
            } else {
                if (inventory[i] == null) {
                    inventory[i] = pickedItem;
                    pickedItem = null;
                    itemPicking = false;
                } else if (inventory[i].id == pickedItem.id) {
                    if (inventory[i].count + pickedItem.count > inventory[i].capacity) {
                        pickedItem.count -= inventory[i].capacity - inventory[i].count;
                        inventory[i].count = inventory[i].capacity;
                    } else {
                        inventory[i].count += pickedItem.count;
                        pickedItem = null;
                        itemPicking = false;
                    }
                } else {
                    Item tempItem = inventory[i];
                    inventory[i] = pickedItem;
                    pickedItem = tempItem;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        renderHotbar(batch);

        if(extended)
            renderExtendedInventory(batch);
        if(draggedItem != null && !(Gdx.input.getX() > dragBoundX && Gdx.input.getX() < dragBoundX + 50 &&
                Gdx.input.getY() > dragBoundY && Gdx.input.getY() < dragBoundY + 50)) {
            batch.draw(draggedItem.texture, Gdx.input.getX() - 20, ControlCenter.height - Gdx.input.getY() - 20, 40, 40);

            if(draggedItem.stackable) {
                batch.draw(UI.selectedSlot, Gdx.input.getX() - 20 + 30, ControlCenter.height - Gdx.input.getY() - 30, 20, 20);
                TextManager.draw("" + draggedItem.count, Gdx.input.getX() - 20 + 40, ControlCenter.height - Gdx.input.getY() - 15, Color.WHITE, 1, true);
            }
        }

        if(itemPicking) {
            batch.draw(pickedItem.texture, Gdx.input.getX() - 20, ControlCenter.height - Gdx.input.getY() - 20, 40, 40);

            if(pickedItem.stackable) {
                batch.draw(UI.selectedSlot, Gdx.input.getX() - 20 + 30, ControlCenter.height - Gdx.input.getY() - 30, 20, 20);
                TextManager.draw("" + pickedItem.count, Gdx.input.getX() - 20 + 40, ControlCenter.height - Gdx.input.getY() - 15, Color.WHITE, 1, true);
            }
        }
        batch.end();
    }

    public void renderHotbar(SpriteBatch batch) {
        for(int i = 0; i < 8; i++) {
            if(Gdx.input.getX() > 405 + i * 60 && Gdx.input.getX() < 455 + i * 60 &&
                    Gdx.input.getY() < 70 && Gdx.input.getY() > 20) {
                batch.draw(UI.selectedSlot, 405 + i * 60, ControlCenter.height - 70, 50, 50);

                // checks if an item is being dragged in the inventory
                if(draggedItem != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    checkDragItem(i);
                }

                // checks if an item is being picked out in the inventory
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    checkPickItem(i);
                }

                if(Gdx.input.justTouched()) {
                    selectedIndex = i;
                }
            } else {
                batch.draw(UI.hotbarSlot, 405 + i * 60, ControlCenter.height - 70, 50, 50);
            }

            if(i == selectedIndex) {
                batch.draw(UI.selectedSlot, 405 + i * 60, ControlCenter.height - 70, 50, 50);
            }

            if(inventory[i] != null) {
                drawItem(batch, i, 0);
            }
        }
    }

    public void renderExtendedInventory(SpriteBatch batch) {
        for(int r = 1; r <= 2; r++) {
            for(int i = 0; i < 8; i++) {
                if(Gdx.input.getX() > 405 + i * 60 && Gdx.input.getX() < 455 + i * 60 &&
                        Gdx.input.getY() < 70 + r * 60 && Gdx.input.getY() > 20 + r * 60) {
                    batch.draw(UI.selectedSlot, 405 + i * 60, ControlCenter.height - 70 - r * 60, 50, 50);

                    // checks if an item is being dragged in the inventory
                    if(draggedItem != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        checkDragItem(i + 8*r);
                    }

                    // checks if an item is being picked out in the inventory
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        //System.out.println(selectedIndex + " " + (i + 8*r));
                        checkPickItem(i + 8*r);
                    }
                } else {
                    batch.draw(UI.inventorySlot, 405 + i * 60, ControlCenter.height - 70 - r * 60, 50, 50);
                }
                if(inventory[i + r*8] != null) {
                    drawItem(batch, i, r);
                }
            }
        }
    }

    public void drawItem(SpriteBatch batch, int i, int row) {
        batch.draw(inventory[i + row*8].texture, 405 + i * 60 + 5,
                ControlCenter.height - 65 - row*60, 40, 40);

        if(inventory[i + row*8].stackable) {
            batch.draw(UI.selectedSlot, 440 + i * 60, ControlCenter.height - 75 - row * 60, 20, 20);
            TextManager.draw("" + inventory[i + row*8].count, 450 + i * 60, ControlCenter.height - 59 - row * 60, Color.WHITE, 1, true);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)  && !itemPicking) {
            if(draggedItem == null && Gdx.input.getX() > 405 + i * 60 && Gdx.input.getX() < 455 + i * 60 &&
                    Gdx.input.getY() < 70 + row * 60 && Gdx.input.getY() > 20 + row * 60) {
                draggedItem = inventory[i + row*8];
                dragBoundX = 405 + i * 60;
                dragBoundY = 20 + row * 60;
                draggedIndex = i + row*8;
                dragTimer = 0;
            }
        } else {
            if(draggedItem != null) {
                dragTimer += Gdx.graphics.getDeltaTime();
                if (dragTimer > 0.2) {
                    draggedItem = null;
                    dragTimer = 0;
                }
            }
        }

        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && Gdx.input.getX() > 405 + i * 60 && Gdx.input.getX() < 455 + i * 60 &&
                Gdx.input.getY() < 70 + row * 60 && Gdx.input.getY() > 20 + row * 60 && draggedItem == null) {
            if(pickedItem != null && inventory[i + row*8].id != pickedItem.id) {
                return;
            }
            if(inventory[i + row*8].stackable) {
                if(pickedItem == null) {
                    pickedItem = inventory[i + row*8].createNew(1);
                } else {
                    pickedItem.count++;
                }
                inventory[i + row*8].count -= 1;
                if(inventory[i + row*8].count <= 0) {
                    inventory[i + row*8] = null;
                }
            } else {
                pickedItem = inventory[i + row*8];
                inventory[i + row*8] = null;
            }
            itemPicking = true;
        }
    }
}
