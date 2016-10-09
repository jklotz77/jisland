package com.jeremyklotz.jisland.game.inventory;

import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.game.Player;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;

import java.util.LinkedList;

/**
 * Created by jeremy on 1/17/16.
 */
public class Inventory {
    private LinkedList<InventoryItem> inventoryItems;
    private boolean hasItemInHand;
    private int currentToolIndex;
    private int toolDirection;

    public Inventory() {
        inventoryItems = new LinkedList<>();
        hasItemInHand = false;
        currentToolIndex = 0;
    }

    public void update(Input input, World world) {
        if (hasItemInHand) {
            inventoryItems.get(currentToolIndex).update(toolDirection);

            if (input.isSpacePressed())
                inventoryItems.get(currentToolIndex).use(world);
        }
    }

    public void render(Bitmap bitmap, int playerX, int playerY) {
        if (hasItemInHand) {
            InventoryItem item = inventoryItems.get(currentToolIndex);

            if (toolDirection == Player.LEFT)
                item.render(bitmap, playerX - item.getItemWidth(), playerY);
            else
                item.render(bitmap, playerX + Player.PLAYER_SIZE, playerY);
        }
    }

    public void pickUp(InventoryItem item) {
        inventoryItems.add(item);
        currentToolIndex = inventoryItems.size() - 1;
        hasItemInHand = true;
    }

    public InventoryItem dropCurrentTool(int toolX, int toolY) {
        InventoryItem fallenItem = inventoryItems.remove(currentToolIndex);

        fallenItem.setFallenXOnMap(toolX);
        fallenItem.setFallenYOnMap(toolY);

        currentToolIndex--;
        hasItemInHand = false;

        return fallenItem;
    }

    public int getToolDirection() {
        return toolDirection;
    }

    public void setToolDirection(int toolDirection) {
        this.toolDirection = toolDirection;
    }

    public boolean hasItemInHand() {
        return hasItemInHand;
    }
}
