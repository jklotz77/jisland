package com.jeremyklotz.jisland.core;

import com.jeremyklotz.jisland.game.Player;
import com.jeremyklotz.jisland.game.inventory.Inventory;
import com.jeremyklotz.jisland.game.inventory.Tool;
import com.jeremyklotz.jisland.game.world.Clock;
import com.jeremyklotz.jisland.game.world.Tile;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.game.world.WorldGenerator;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.BlendingConstants;
import com.jeremyklotz.jisland.graphics.LightSource;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.graphics.ui.Scene;
import com.jeremyklotz.jisland.graphics.ui.SceneManager;
import com.jeremyklotz.jisland.utils.ColorUtils;
import com.jeremyklotz.jisland.utils.MathUtils;

import java.util.Random;

/**
 * Created by Jeremy Klotz on 1/3/16
 */
public class Engine implements Scene {
    private static final int WORLD_DARKNESS_MAX = 150;
    private static final int WORLD_DARKNESS_MIN = 80;
    private static final int PLAYER_LIGHT = 120;
    private static final int PLAYER_LIGHT_COLOR = ColorUtils.createColor(PLAYER_LIGHT, PLAYER_LIGHT / 2, 0);
    private static final int PLAYER_LIGHT_DISTANCE = 75;
    private static final int WORLD_WIDTH = 64;
    private static final int WORLD_HEIGHT = 64;
    private static final int NUM_LAKES = 10;
    private static final int NUM_FORESTS = 10;
    private static final double FIRE_PROBABILITY = 0.004;
    private static final int SECONDS_PER_DAY = 120;

    private boolean gameOver;
    private Bitmap bitmap;
    private Input input;
    private World world;
    private Player player;
    private LightSource playerLight;
    private Clock clock;
    private int currentDarkness;

    public Engine(Bitmap bitmap, Input input, SpriteSheet spriteSheet) {
        this.bitmap = bitmap;
        this.input = input;
        this.gameOver = false;

        Random random = new Random();
        world = WorldGenerator.generateWorld(WORLD_WIDTH, WORLD_HEIGHT, NUM_LAKES, NUM_FORESTS, FIRE_PROBABILITY);
        world.addFallenItem(new Tool(Tool.TYPE_AXE, random.nextInt(bitmap.getWidth() / 2), random.nextInt(bitmap.getHeight() / 2)));

        this.player = new Player(10, 10, spriteSheet, world);

        this.playerLight = new LightSource(10, 10, PLAYER_LIGHT_COLOR, PLAYER_LIGHT_DISTANCE);

        clock = new Clock(SECONDS_PER_DAY);
        currentDarkness = WORLD_DARKNESS_MAX;
    }

    @Override
    public void update() {
        if (input.ispPressed()) {
            SceneManager.showPauseMenu();
            return;
        }

        if (input.isiPressed()) {
            SceneManager.showInvetory();
            return;
        }

        world.update();
        player.update(input);

        int viewpointX = player.getX() - bitmap.getWidth() / 2 + Player.PLAYER_SIZE / 2;
        int viewpointY = player.getY() - bitmap.getHeight() / 2 + Player.PLAYER_SIZE / 2;

        viewpointX = MathUtils.clamp(viewpointX, 0, world.getTileWidth() * Tile.TILE_SIZE - bitmap.getWidth() - Tile.TILE_SIZE);
        viewpointY = MathUtils.clamp(viewpointY, 0, world.getTileWidth() * Tile.TILE_SIZE - bitmap.getHeight() - Tile.TILE_SIZE);

        world.setViewpoint(viewpointX, viewpointY);

        playerLight.setX(player.getX() - world.getViewpointX() + Player.PLAYER_SIZE / 2);
        playerLight.setY(player.getY() - world.getViewpointY() + Player.PLAYER_SIZE / 2);

        clock.update();

        currentDarkness = (int) (Math.round((WORLD_DARKNESS_MAX - WORLD_DARKNESS_MIN) *
                clock.percentLight() + WORLD_DARKNESS_MIN));
    }

    @Override
    public void render() {
        bitmap.setBlending(BlendingConstants.NO_BLENDING);
        bitmap.clear(0);

        world.render(bitmap);
        player.render(bitmap, player.getX() - world.getViewpointX(), player.getY() - world.getViewpointY());
        world.renderTrees(bitmap);
        world.renderFire(bitmap);
        bitmap.shade(currentDarkness);
        world.renderLight(bitmap);
        playerLight.render(bitmap);

        renderGui();
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {

    }

    private void renderGui() {
        player.renderHealthBar(bitmap);
        clock.render(bitmap);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Inventory getPlayerInventory() {
        return player.getInventory();
    }
}
