package com.jeremyklotz.jisland.game;

import com.jeremyklotz.jisland.game.world.Tree;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import java.awt.*;

/**
 * Created by Jeremy Klotz on 1/14/16
 */
public class Tool {
    public static final int TOOL_WIDTH = SpriteSheet.SPRITE_SIZE;
    public static final int TOOL_HEIGHT = SpriteSheet.SPRITE_SIZE * 2;
    public static final int TYPE_AXE = 0;
    private static final int TOOL_ROW_ON_SPRITESHEET = 8;

    private static int[][] toolPixels;

    private int type;
    private Player player;
    private int direction;
    private int fallenX;
    private int fallenY;

    public Tool(int type, Player player) {
        this.type = type;
        this.player = player;
        direction = Player.RIGHT;
    }

    public Tool(int type, int fallenX, int fallenY) {
        this.type = type;
        this.player = null;
        direction = Player.RIGHT;
        this.fallenX = fallenX;
        this.fallenY = fallenY;
    }

    public static void initArt(SpriteSheet spriteSheet) {
        toolPixels = new int[1][TOOL_WIDTH * TOOL_HEIGHT];

        toolPixels[TYPE_AXE] = spriteSheet.getSprite(TYPE_AXE, TOOL_ROW_ON_SPRITESHEET, TOOL_WIDTH, TOOL_HEIGHT);
    }

    public void useTool(World world, int toolDirection) {
        switch (type) {
            case TYPE_AXE:
                Rectangle toolBounds = new Rectangle(toolDirection == Player.LEFT ? player.getX() - Player.PLAYER_SIZE / 2
                        : player.getX() + Player.PLAYER_SIZE,
                        player.getY(), TOOL_WIDTH, TOOL_HEIGHT / 2);

                for (Tree tree : world.getTrees()) {
                    if (tree.bounds().intersects(toolBounds)) {
                        tree.fall();
                        break;
                    }
                }

                break;
        }
    }

    public void update(int direction) {
        this.direction = direction;
    }

    public void render(Bitmap bitmap, int x, int y) {
        switch (type) {
            case TYPE_AXE:
                bitmap.drawSprite(toolPixels[TYPE_AXE], x, y, direction == Player.LEFT);
                break;
        }
    }

    public void pickUp(Player player) {
        this.player = player;
    }

    public int getType() {
        return type;
    }

    public void setFallenX(int fallenX) {
        this.fallenX = fallenX;
    }

    public void setFallenY(int fallenY) {
        this.fallenY = fallenY;
    }

    public int getFallenX() {
        return fallenX;
    }

    public int getFallenY() {
        return fallenY;
    }
}
