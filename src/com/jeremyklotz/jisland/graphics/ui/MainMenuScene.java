package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by Jeremy Klotz on 5/25/16.
 */
public class MainMenuScene implements Scene {
    private Bitmap bitmap;
    private Input input;

    public MainMenuScene(Bitmap bitmap, Input input) {
        this.bitmap = bitmap;
        this.input = input;
    }

    @Override
    public void update() {
        if (input.isSpacePressed())
            SceneManager.nextScene();
    }

    @Override
    public void render() {
        bitmap.clear(ColorUtils.createColor(100, 255, 0));

        Text.render(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR, 10, 10, bitmap, 0);
        Text.render("Press space to play", 50, 50, bitmap, 0);
    }

    @Override
    public void dispose() {

    }
}
