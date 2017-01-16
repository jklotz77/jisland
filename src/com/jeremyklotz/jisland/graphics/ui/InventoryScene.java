package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.game.inventory.Inventory;
import com.jeremyklotz.jisland.game.inventory.InventoryItem;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.GraphicEffects;
import com.jeremyklotz.jisland.utils.ColorUtils;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Jeremy Klotz on 10/9/16.
 */
public class InventoryScene implements Scene {
    private static final int BLUR_RADIUS = 4;
    private static final int BLUR_ITERATIONS = 2;

    private int[] background;
    private Inventory inventory;
    private Bitmap bitmap;
    private Input input;

    private ListChooser inventoryList;

    public InventoryScene(int[] screenshot, Bitmap bitmap, Inventory inventory, Input input) {
        this.bitmap = bitmap;
        this.inventory = inventory;
        this.input = input;

        background = GraphicEffects.blur(screenshot, BLUR_RADIUS, BLUR_ITERATIONS, bitmap.getWidth());
    }

    @Override
    public void update() {
        if (input.isEscapePressed()) {
            SceneManager.showGameScene();
            input.resetKeys();
        }

        inventoryList.update(input);
    }

    @Override
    public void render() {
        bitmap.drawSprite(background, 0, 0, bitmap.getWidth());
        Text.render("Inventory", bitmap.getWidth() / 2 - Text.textWidth("Inventory") / 2, 10,
                bitmap, ColorUtils.WHITE);

        inventoryList.render(bitmap); // inventoryList is only temporary TODO Clean up inventory display
    }

    @Override
    public void show() {
        LinkedList<InventoryItem> items = inventory.getInventoryItems();

        ListElement[] listElements = new ListElement[items.size()];

        int i = 0;
        for (Iterator<InventoryItem> it = items.iterator(); it.hasNext();) {
            final int finalI = i;

            listElements[i] = new ListElement(it.next().toString()) {
                @Override
                public void trigger() {
                    inventory.setCurrentItemIndex(finalI);
                    SceneManager.showGameScene();
                }
            };

            i++;
        }

        inventoryList = new ListChooser(listElements, 10, 10,
                ColorUtils.createColor(100, 100, 100), ColorUtils.WHITE);
    }

    @Override
    public void dispose() {

    }
}
