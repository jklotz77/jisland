package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.core.Engine;
import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.GraphicEffects;
import com.jeremyklotz.jisland.utils.ColorUtils;

import java.io.IOException;

/**
 * Created by Jeremy Klotz on 5/25/16.
 */
public class MainMenuScene implements Scene {
    private static final int BLUR_RADIUS = 10;
    private static final int BLUR_ITERATIONS = 2;

    private Bitmap bitmap;
    private Input input;
    private int[] background;
    private Engine engine;

    private ListChooser options;

    public MainMenuScene(Bitmap bitmap, Input input, int[] background, Engine engine) {
        this.bitmap = bitmap;
        this.input = input;
        this.background = GraphicEffects.blur(background, BLUR_RADIUS, BLUR_ITERATIONS, bitmap.getWidth());
        this.engine = engine;

        initOptionsMenu();
    }

    private void initOptionsMenu() {
        ListElement[] elements = new ListElement[3];

        elements[0] = new ListElement("Play") {
            @Override
            public void trigger() {
                SceneManager.showGameScene();
            }
        };
        
        elements[1] = new ListElement("Load") {
            @Override
            public void trigger() {
                try {
                    engine.loadWorldFromFile(JIsland.WORLD_SAVE_PATH);
                    engine.render();
                    background = GraphicEffects.blur(bitmap.screenshot(), BLUR_RADIUS, BLUR_ITERATIONS, bitmap.getWidth());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        elements[2] = new ListElement("Exit") {
            @Override
            public void trigger() {
                System.exit(0);
            }
        };

        options = new ListChooser(elements,
                bitmap.getWidth() / 2 - Text.textWidth("Play"), 50, ColorUtils.createColor(50, 50, 50), ColorUtils.WHITE);
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

        Text.render(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR,
                bitmap.getWidth() / 2 - Text.textWidth(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR) / 2,
                10, bitmap, ColorUtils.WHITE);
        options.render(bitmap);
    }

    @Override
    public void dispose() {

    }
}
