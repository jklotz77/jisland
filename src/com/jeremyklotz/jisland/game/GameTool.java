package com.jeremyklotz.jisland.game;

import com.jeremyklotz.jisland.game.world.Tree;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import java.awt.*;

/**
 * Created by Jeremy Klotz on 1/14/16
 */
public class GameTool {
    public static final int TOOL_WIDTH = SpriteSheet.SPRITE_SIZE;
    public static final int TOOL_HEIGHT = SpriteSheet.SPRITE_SIZE * 2;
    public static final int TYPE_AXE = 0;
    private static final int TOOL_ROW_ON_SPRITESHEET = 8;

    private static int[][] toolPixels;

    private int type;
    private Player player;

    public GameTool(int type, Player player) {
        this.type = type;
        this.player = player;
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
                        player.getY(), TOOL_WIDTH, TOOL_HEIGHT);

                for (Tree tree : world.getTrees()) {
                    if (tree.bounds().intersects(toolBounds)) {
                        tree.fall();
                        break;
                    }
                }

                break;
        }
    }

    public void render(Bitmap bitmap, int x, int y, boolean mirrorX) {
        switch (type) {
            case TYPE_AXE:
                bitmap.drawSprite(toolPixels[TYPE_AXE], x, y, mirrorX);
                break;
        }
    }

    public int getType() {
        return type;
    }
}
