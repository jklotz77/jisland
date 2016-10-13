package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.graphics.Bitmap;

/**
 * Created by Jeremy Klotz on 5/28/16.
 */
public class ListChooser {
    private static final int INPUT_DELAY = 7;
    private static final int LIST_ELEMENT_PADDING = 4;

    private ListElement[] elements;
    private int selectedElement;
    private int timer;
    private boolean timerGoing;
    private int selectedColor;
    private int unselectedColor;

    private int x;
    private int y;

    public ListChooser(ListElement[] elements, int x, int y, int unselectedColor, int selectedColor) {
        this.elements = elements;
        this.x = x;
        this.y = y;

        this.unselectedColor = unselectedColor;
        this.selectedColor = selectedColor;

        selectedElement = 0;
        timer = 0;
        timerGoing = false;
    }

    public void update(Input input) {
        if (input.isEnterPressed())
            elements[selectedElement].trigger();

        if (timerGoing) {
            timer++;

            if (timer == INPUT_DELAY) {
                timer = 0;
                timerGoing = false;
            }

            return;
        }

        if (input.isUpPressed()) {
            selectedElement--;
            timerGoing = true;
    
            if (selectedElement == -1)
                selectedElement = elements.length - 1;
        } else if (input.isDownPressed()) {
            selectedElement++;
            timerGoing = true;
            
            if (selectedElement == elements.length)
                selectedElement = 0;
        }
    }

    public void render(Bitmap bitmap) {
        for (int i = 0; i < elements.length; i++) {
            ListElement element = elements[i];
            int yCor = y + LIST_ELEMENT_PADDING * i + Text.CHARACTER_SIZE * i;

            element.render(bitmap, x, yCor, i == selectedElement ? selectedColor : unselectedColor);
        }
    }
}
