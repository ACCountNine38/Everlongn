package com.everlongn.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.everlongn.assets.UI;
import com.everlongn.entities.EntityManager;
import com.everlongn.entities.Player;
import com.everlongn.game.ControlCenter;
import com.everlongn.utils.TextManager;

import java.util.ArrayList;

public class Inventory {
    public static Item[] inventory = new Item[18];

    public static int maxInventorySize = 18, hotbarSize = 6, slotSize = 64, selectedIndex = 0;

    public static boolean extended, itemPicking, canAct;

    private ControlCenter c;

    public static Item draggedItem, pickedItem, hoveringItem;

    private int dragBoundX, dragBoundY, draggedIndex, hoveringIndex;
    private float dragTimer, rowY[] = new float[2];

    private String itemDescription = "";

    public GlyphLayout layout = new GlyphLayout();

    public Inventory(ControlCenter c) {
        this.c = c;
        addItem(Arcane.arcaneCaster);
        addItem(Arcane.arcaneEruption);
        addItem(Arcane.arcaneRebound);
        addItem(Arcane.arcaneEscort);
        addItem(Melee.broadSword.createNew(1));
        addItem(Arcane.shadowStaff.createNew(1));
        addItem(Melee.dragondance);
        addItem(Item.stone.createNew(1));
        addItem(Item.log.createNew(88));
        addItem(Item.log.createNew(9));
        addItem(Item.log.createNew(9));
    }

    public void tick() {
        if(Player.inventoryHold && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Player.inventoryHold = false;
        }

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
        }

        if(extended && rowY[0] < slotSize + 10) {
            rowY[0]+=10;
        } else if(!extended && rowY[0] > 0) {
            rowY[0]-=10;
        }
        if(extended && rowY[1] < slotSize*2 + 20) {
            rowY[1]+=20;
        } else if(!extended && rowY[1] > 0) {
            rowY[1]-=20;
        }

        generateDescription();
    }

    public void generateDescription() {
        if(extended && !itemPicking && draggedItem == null && hoveringItem != null) {
            itemDescription = hoveringItem.name;
            if(hoveringItem instanceof Melee) {
                Melee item = (Melee)hoveringItem;
                itemDescription += "\nMelee Damage: " + item.damage;
                itemDescription += "\nCritical Chance: " + Math.round(item.critChance*100);
                //itemDescription += "\n" + optimizeText(hoveringItem.description);
            } else if(hoveringItem instanceof Melee) {
                itemDescription = "melee weapon \n very op";
            } else {

            }
        } else {
            itemDescription = "";
        }
    }

    public String optimizeText(String text) {
        String newText = "";

        String currentText = "";
        while(text.length() > 0) {
            if(text.indexOf(" ") == -1) {
                currentText += text;
                text = "";
            } else {
                currentText += text.substring(0, text.indexOf(" "));
                text = text.substring(text.indexOf(" "));
            }
            layout.setText(TextManager.bfont, currentText);
            if(layout.width > 300) {
                newText += currentText + "\n";
                currentText = "";
            }
        }

        if(currentText.length() > 0) {
            newText += currentText;
        }

        return newText;
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
            if(i < hotbarSize)
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

        if(rowY[0] > 0)
            renderExtendedInventory(batch);

        renderHotbar(batch);

        // only display dragged item when the cursor is out of the selected slot
        if(draggedItem != null && !(Gdx.input.getX() > dragBoundX && Gdx.input.getX() < dragBoundX + slotSize &&
                Gdx.input.getY() > dragBoundY && Gdx.input.getY() < dragBoundY + slotSize)) {
            batch.draw(draggedItem.texture, Gdx.input.getX() - draggedItem.itemWidth/2, ControlCenter.height - Gdx.input.getY() - draggedItem.itemHeight/2, draggedItem.itemWidth, draggedItem.itemHeight);

            if(draggedItem.stackable) {
                batch.draw(UI.selectedSlot, Gdx.input.getX() - slotSize/2 + slotSize - 15, ControlCenter.height - Gdx.input.getY() + slotSize/2 - slotSize - 5, 20, 20);
                TextManager.draw("" + draggedItem.count, Gdx.input.getX() - slotSize/2 + slotSize - 5, ControlCenter.height - Gdx.input.getY() + slotSize/2 - slotSize + 10, Color.WHITE, 1, true);
            }
        }
        if(itemPicking) {
            batch.draw(pickedItem.texture, Gdx.input.getX() - pickedItem.itemWidth/2, ControlCenter.height - Gdx.input.getY() - pickedItem.itemHeight/2, pickedItem.itemWidth, pickedItem.itemHeight);
            if(pickedItem.stackable) {
                batch.draw(UI.selectedSlot, Gdx.input.getX() - slotSize/2 + slotSize - 15, ControlCenter.height - Gdx.input.getY() + slotSize/2 - slotSize - 5, 20, 20);
                TextManager.draw("" + pickedItem.count, Gdx.input.getX() - slotSize/2 + slotSize - 5, ControlCenter.height - Gdx.input.getY() + slotSize/2 - slotSize + 10, Color.WHITE, 1, true);
            }
        }

        if(!itemDescription.equals("")) {
            TextManager.draw(itemDescription, Gdx.input.getX() + 25, ControlCenter.height - Gdx.input.getY(), Color.WHITE, 1f, false);
        }
        batch.end();
    }

    public void renderHotbar(SpriteBatch batch) {
        for(int i = 0; i < hotbarSize; i++) {
            if(Gdx.input.getX() > (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) && Gdx.input.getX() < (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) + slotSize &&
                    Gdx.input.getY() < 20 + slotSize && Gdx.input.getY() > 20) {
                batch.draw(UI.selectedSlot, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10), ControlCenter.height - slotSize - 20, slotSize, slotSize);

                // checks if an item is being dragged in the inventory
                if(draggedItem != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    checkDragItem(i);
                }

                // checks if an item is being picked out in the inventory
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    checkPickItem(i);
                }

                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    selectedIndex = i;
                    Player.inventoryHold = true;
                    Player.inCombat = false;
                }

                if(inventory[i] != null) {
                    hoveringItem = inventory[i];
                    hoveringIndex = i;
                }
            } else {
                if(i == hoveringIndex) {
                    hoveringItem = null;
                }
                batch.draw(UI.hotbarSlot, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10), ControlCenter.height - slotSize - 20, slotSize, slotSize);
            }

            if(i == selectedIndex) {
                batch.draw(UI.selectedSlot, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10), ControlCenter.height - slotSize - 20, slotSize, slotSize);
            }

            if(inventory[i] != null) {
                drawItem(batch, i, 0);
            }
        }
    }

    public void renderExtendedInventory(SpriteBatch batch) {
        for(int r = 2; r >= 1; r--) {
            for(int i = 0; i < hotbarSize; i++) {
                if(Gdx.input.getX() > (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) && Gdx.input.getX() < (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) + slotSize &&
                        Gdx.input.getY() < 20 + slotSize + (r * (slotSize+10)) && Gdx.input.getY() > 20 + (r * (slotSize+10))) {
                    batch.draw(UI.selectedSlot, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10), ControlCenter.height - slotSize - 20 - rowY[r-1], slotSize, slotSize);

                    // checks if an item is being dragged in the inventory
                    if(draggedItem != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        checkDragItem(i + hotbarSize*r);
                    }

                    // checks if an item is being picked out in the inventory
                    if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        //System.out.println(selectedIndex + " " + (i + 8*r));
                        Player.inventoryHold = true;
                        checkPickItem(i + hotbarSize*r);
                    }

                    if(inventory[i + r*hotbarSize] != null) {
                        hoveringItem = inventory[i + r*hotbarSize];
                        hoveringIndex = i + r*hotbarSize;
                    }
                } else {
                    if(i + r*hotbarSize == hoveringIndex) {
                        hoveringItem = null;
                    }
                    batch.draw(UI.inventorySlot, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10), ControlCenter.height - slotSize - 20 - rowY[r-1], slotSize, slotSize);
                }
                if(inventory[i + r*hotbarSize] != null) {
                    drawItem(batch, i, r);
                }
            }
        }
    }

    public void drawItem(SpriteBatch batch, int i, int row) {
        float yDiff = 0;
        if(row > 0) {
            yDiff = rowY[row-1];
        }
        batch.draw(inventory[i + row*hotbarSize].texture,
                (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) + slotSize/2 - inventory[i + row*hotbarSize].itemWidth/2,
                ControlCenter.height - slotSize - 20 - yDiff + slotSize/2 - inventory[i + row*hotbarSize].itemHeight/2,
                inventory[i + row*hotbarSize].itemWidth, inventory[i + row*hotbarSize].itemHeight);

        if(inventory[i + row*hotbarSize].stackable) {
            batch.draw(UI.selectedSlot, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize+10) + slotSize - 15, ControlCenter.height - 20 - yDiff - slotSize - 5, 20, 20);
            TextManager.draw("" + inventory[i + row*hotbarSize].count, (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize+10) + slotSize - 5, ControlCenter.height - 20 - (int)yDiff - slotSize + 10, Color.WHITE, 1, true);
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !itemPicking && !Player.inCombat) {
            if(draggedItem == null && Gdx.input.getX() > (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) && Gdx.input.getX() < (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) + slotSize &&
                    Gdx.input.getY() < 20 + slotSize + (row * (slotSize+10)) && Gdx.input.getY() > 20 + (row * (slotSize+10))) {
                draggedItem = inventory[i + row*hotbarSize];
                dragBoundX = (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10);
                dragBoundY = 20 + (row * (slotSize+10));
                draggedIndex = i + row*hotbarSize;
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

        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                Gdx.input.getX() > (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) && Gdx.input.getX() < (ControlCenter.width/2 - ((slotSize+5)*3)) + i * (slotSize + 10) + slotSize &&
                Gdx.input.getY() < 20 + slotSize + (row * (slotSize+10)) && Gdx.input.getY() > 20 + (row * (slotSize+10)) && draggedItem == null && !Player.inCombat) {
            if(pickedItem != null && inventory[i + row*hotbarSize].id != pickedItem.id) {
                return;
            }
            itemPicking = true;
            if(inventory[i + row*hotbarSize].stackable) {
                if(pickedItem == null) {
                    pickedItem = inventory[i + row*hotbarSize].createNew(1);
                } else {
                    pickedItem.count++;
                }
                inventory[i + row*hotbarSize].count -= 1;
                if(inventory[i + row*hotbarSize].count <= 0) {
                    inventory[i + row*hotbarSize] = null;
                }
            } else {
                pickedItem = inventory[i + row*hotbarSize];
                inventory[i + row*hotbarSize] = null;
            }
        }
    }
}
