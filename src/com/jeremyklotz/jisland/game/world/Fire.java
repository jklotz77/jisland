package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.game.Animator;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by jeremy on 1/16/16.
 */
public class Fire {
    public static final int FIRE_WIDTH = Tile.TILE_SIZE;
    public static final int FIRE_HEIGHT = Tile.TILE_SIZE;
    public static final int FIRE_LIGHT_COLOR = ColorUtils.createColor(150, 0, 0);
    public static final int FIRE_LIGHT_DISTANCE = 20;

    private static Animator animator;
    private int x;
    private int y;

    public Fire(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static void initArt(SpriteSheet spriteSheet) {
        int[][] frames = new int[4][FIRE_WIDTH * FIRE_HEIGHT];

        frames[0] = spriteSheet.getSprite(7, 1);
        frames[1] = spriteSheet.getSprite(8, 1);
        frames[2] = spriteSheet.getSprite(7, 1);
        frames[3] = spriteSheet.getSprite(9, 1);

        animator = new Animator(20, frames, false);
    }

    public static void update() {
        animator.update();
    }

    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(animator.getCurrentFrame(), x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
