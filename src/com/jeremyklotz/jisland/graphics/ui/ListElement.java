package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.graphics.Bitmap;

/**
 * Created by Jeremy Klotz on 5/28/16.
 */
public abstract class ListElement {
    private String text;

    public ListElement(String text) {
        this.text = text;
    }

    public void render(Bitmap bitmap, int x, int y, int color) {
        Text.render(text, x, y, bitmap, color);
    }

    public abstract void trigger();
}
