package com.jeremyklotz.jisland;

import com.jeremyklotz.jisland.core.Engine;
import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.game.Tool;
import com.jeremyklotz.jisland.game.world.Tile;
import com.jeremyklotz.jisland.game.world.Tree;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.graphics.Window;

/**
 * Created by Jeremy Klotz on 1/3/16
 */
public class JIsland implements Runnable {
    public static final int WIDTH = 240;
    public static final int HEIGHT = WIDTH * 3 / 4;
    public static final int SCALE = 4;
    public static final int FPS = 60;
    private static final String TITLE = "JIsland";
    private static final String VERSION = "0.1 Pre-Alpha";
    private static final String AUTHOR = "Jeremy Klotz";
    public static final boolean DEBUG = false;

    private Thread thread;
    private Engine engine;
    private Window window;


    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        init();

        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / FPS;

        double delta = 0;

        while (!engine.isGameOver()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                update();
                delta -= 1;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            render();
        }
    }

    private void init() {
        Input input = new Input();
        window = new Window(WIDTH, HEIGHT, SCALE, TITLE + " v" + VERSION + " by " + AUTHOR, input);
        Bitmap bitmap = window.getBitmap();
        SpriteSheet spriteSheet = new SpriteSheet("/spritesheet.png");
        initStaticArt(spriteSheet);
        engine = new Engine(bitmap, input, spriteSheet);
    }

    private void initStaticArt(SpriteSheet spriteSheet) {
        Tile.initTileArt(spriteSheet);
        Tool.initArt(spriteSheet);
        Tree.initArt(spriteSheet);
    }

    private void update() {
        engine.update();
    }

    private void render() {
        engine.render();
        window.swapBuffers();
    }
}
