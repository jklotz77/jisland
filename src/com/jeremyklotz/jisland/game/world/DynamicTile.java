package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.game.Animator;
import com.jeremyklotz.jisland.graphics.Bitmap;

/**
 * Created by Jeremy Klotz on 1/6/16
 */
public class DynamicTile extends Tile {
    private Animator animator;

    public DynamicTile(int type) {
        super(type);

        int[][] frames = animatedTiles.get(type);

        animator = new Animator(60, frames, true);
    }

    @Override
    public void update() {
        animator.update();
    }

    @Override
    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(animator.getCurrentFrame(), x, y);
    }
    
    public String toString() {
        return "DynamicTile[type=" + type + "]";
    }
}
