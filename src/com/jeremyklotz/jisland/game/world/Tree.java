package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import java.awt.*;

/**
 * Created by Jeremy Klotz on 1/14/16
 */
public class Tree {
    public static final int TREE_WIDTH = SpriteSheet.SPRITE_SIZE * 2;
    public static final int TREE_HEIGHT = SpriteSheet.SPRITE_SIZE * 2;
    public static final int STATE_GROWN = 0;
    public static final int STATE_FALLEN = 1;
    protected static int[][] statePixels;
    private int state;
    private int x;
    private int y;

    public Tree(int x, int y) {
        if (statePixels == null)
            throw new IllegalStateException("Tree art must be initialized before creating a tree");

        this.state = STATE_GROWN;
        this.x = x;
        this.y = y;
    }

    public static void initArt(SpriteSheet spriteSheet) {
        statePixels = new int[2][TREE_WIDTH * TREE_HEIGHT];

        statePixels[0] = spriteSheet.getSprite(3, 0, TREE_WIDTH, TREE_HEIGHT);
        statePixels[1] = spriteSheet.getSprite(5, 0, TREE_WIDTH, TREE_HEIGHT);
    }

    public void fall() {
        state = STATE_FALLEN;
    }

    public Rectangle bounds() {
        return new Rectangle(x, y, TREE_WIDTH, TREE_HEIGHT);
    }

    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(statePixels[state], x, y, TREE_WIDTH);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object o) {
        Tree otherTree = (Tree) o;

        if (otherTree.x == x && otherTree.y == y) return true;

        return false;
    }
}
