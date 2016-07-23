package com.jeremyklotz.jisland.graphics.ui;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.GraphicEffects;
import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by Jeremy Klotz on 5/25/16.
 */
public class MainMenuScene implements Scene {
    private static final int BLUR_RADIUS = 20;
    private static final int BLUR_ITERATIONS = 2;

    private Bitmap bitmap;
    private Input input;
    private int[] background;

    private ListChooser options;

    public MainMenuScene(Bitmap bitmap, Input input, int[] background) {
        this.bitmap = bitmap;
        this.input = input;
        this.background = GraphicEffects.blur(background, BLUR_RADIUS, BLUR_ITERATIONS, bitmap.getWidth());

        initOptionsMenu();
    }

    private void initOptionsMenu() {
        ListElement[] elements = new ListElement[2];

        elements[0] = new ListElement("Play") {
            @Override
            public void trigger() {
                SceneManager.showGameScene();
            }
        };

        elements[1] = new ListElement("Exit") {
            @Override
            public void trigger() {
                System.exit(0);
            }
        };

        options = new ListChooser(elements,
                bitmap.getWidth() / 2 - Text.textWidth("Play"), 50, ColorUtils.createColor(100, 100, 100), ColorUtils.createColor(255, 255, 255));
    }

    @Override
    public void update() {
        options.update(input);
    }

    @Override
    public void render() {
        bitmap.drawSprite(background, 0, 0, bitmap.getWidth());

        Text.render(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR,
                bitmap.getWidth() / 2 - Text.textWidth(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR) / 2,
                10, bitmap, ColorUtils.createColor(255, 255, 255));
        options.render(bitmap);
    }

    @Override
    public void dispose() {

    }
}
