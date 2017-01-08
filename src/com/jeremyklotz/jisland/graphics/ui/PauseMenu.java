package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.core.Engine;
import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.GraphicEffects;
import com.jeremyklotz.jisland.utils.ColorUtils;

import java.io.IOException;

/**
 * Created by Jeremy Klotz on 5/28/16.
 */
public class PauseMenu implements Scene {
    private static final int BLUR_RADIUS = 10;
    private static final int BLUR_ITERATIONS = 2;

    private int[] background;
    private Engine engine;
    private Bitmap bitmap;
    private Input input;
    private ListChooser options;

    public PauseMenu(int[] screenshot, Bitmap bitmap, Input input, Engine engine) {
        this.bitmap = bitmap;
        this.input = input;
        this.engine = engine;
        background = GraphicEffects.blur(screenshot, BLUR_RADIUS, BLUR_ITERATIONS, bitmap.getWidth());

        ListElement[] listElements = new ListElement[3];

        listElements[0] = new ListElement("Resume") {
            @Override
            public void trigger() {
                SceneManager.showGameScene();
            }
        };
        
        listElements[1] = new ListElement("Save world") {
            @Override
            public void trigger() {
                try {
                    engine.saveWorldToFile(JIsland.WORLD_SAVE_PATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        listElements[2] = new ListElement("Main Menu") {
            @Override
            public void trigger() {
                SceneManager.showMainMenu();
            }
        };

        options = new ListChooser(listElements, bitmap.getWidth() / 2 - Text.textWidth("Resume") / 2, 30,
                ColorUtils.createColor(100, 100, 100), ColorUtils.createColor(255, 255, 255));
    }

    @Override
    public void update() {
        options.update(input);
    }

    @Override
    public void show() {

    }

    @Override
    public void render() {
        bitmap.drawSprite(background, 0, 0, bitmap.getWidth());
        Text.render("Paused", bitmap.getWidth() / 2 - Text.textWidth("Paused") / 2, 10,
                bitmap, ColorUtils.createColor(255, 255, 255));
        options.render(bitmap);
    }

    @Override
    public void dispose() {

    }
}
