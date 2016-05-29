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

    private ListChooser options;

    public MainMenuScene(Bitmap bitmap, Input input) {
        this.bitmap = bitmap;
        this.input = input;

        initOptionsMenu();
    }

    private void initOptionsMenu() {
        ListElement[] elements = new ListElement[2];

        elements[0] = new ListElement("Play") {
            @Override
            public void trigger() {
                SceneManager.nextScene();
            }
        };

        elements[1] = new ListElement("Exit") {
            @Override
            public void trigger() {
                System.exit(0);
            }
        };

        options = new ListChooser(elements,
                bitmap.getWidth() / 2 - Text.textWidth("Play"), 50, 0, ColorUtils.createColor(255, 255, 255));
    }

    @Override
    public void update() {
        options.update(input);
    }

    @Override
    public void render() {
        bitmap.clear(ColorUtils.createColor(100, 255, 0));

        Text.render(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR,
                bitmap.getWidth() / 2 - Text.textWidth(JIsland.TITLE + " v" + JIsland.VERSION + " by " + JIsland.AUTHOR) / 2,
                10, bitmap, 0);
        options.render(bitmap);
    }

    @Override
    public void dispose() {

    }
}
