package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.game.Animator;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

/**
 * Created by Jeremy Klotz on 1/6/16
 */
public class DynamicTile extends Tile {
    private Animator animator;

    public DynamicTile(int type) {
        super(type);


        if (type == Tile.TYPE_WATER) {
            int[][] frames = new int[3][Tile.TILE_SIZE * Tile.TILE_SIZE];
            frames[0] = animatedTiles[0];
            frames[1] = animatedTiles[1];
            frames[2] = animatedTiles[2];
            animator = new Animator(60, frames, true);
        } else if (type == Tile.TYPE_FIRE) {
            int[][] frames = new int[4][Tile.TILE_SIZE * Tile.TILE_SIZE];
            frames[0] = animatedTiles[3];
            frames[1] = animatedTiles[4];
            frames[2] = animatedTiles[3];
            frames[3] = animatedTiles[5];
            animator = new Animator(20, frames, false);
        }
    }

    @Override
    public void update() {
        animator.update();
    }

    @Override
    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(animator.getCurrentFrame(), x, y);
    }
}
