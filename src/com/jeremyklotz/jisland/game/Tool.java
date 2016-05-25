package com.jeremyklotz.jisland.game;

import com.jeremyklotz.jisland.game.world.Tree;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jeremy Klotz on 1/14/16
 */
public class Tool {
    public static final int TOOL_WIDTH = SpriteSheet.SPRITE_SIZE;
    public static final int TOOL_HEIGHT = SpriteSheet.SPRITE_SIZE * 2;
    public static final int TYPE_AXE = 0;
    private static final int TOOL_ROW_ON_SPRITESHEET = 8;

    private static HashMap<Integer, int[]> toolPixels;

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
        if (toolPixels == null)
            throw new IllegalStateException("Tool art must be initialized before creating a tool");

        this.type = type;
        this.player = null;
        direction = Player.RIGHT;
        this.fallenX = fallenX;
        this.fallenY = fallenY;
    }

    public static void initArt(SpriteSheet spriteSheet) {
        toolPixels = new HashMap<>();

        toolPixels.put(TYPE_AXE, spriteSheet.getSprite(TYPE_AXE, TOOL_ROW_ON_SPRITESHEET, TOOL_WIDTH, TOOL_HEIGHT));
    }

    public void useTool(World world, int toolDirection) {
        switch (type) {
            case TYPE_AXE:
                Rectangle toolBounds = new Rectangle(toolDirection == Player.LEFT ? player.getX() - Player.PLAYER_SIZE / 2
                        : player.getX() + Player.PLAYER_SIZE,
                        player.getY(), TOOL_WIDTH, TOOL_HEIGHT / 2);

                for (Iterator<Tree> it = world.getTrees().iterator(); it.hasNext();) {
                    Tree tree = it.next();

                    if (tree.bounds().intersects(toolBounds)) {
                        tree.fall();
                        world.getFallenTrees().add(tree);
                        it.remove();
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
        bitmap.drawSprite(toolPixels.get(type), x, y, direction == Player.LEFT);
    }

    public void pickUp(Player player) {
        this.player = player;
    }

    public int getType() {
        return type;
    }

    public int getFallenX() {
        return fallenX;
    }

    public void setFallenX(int fallenX) {
        this.fallenX = fallenX;
    }

    public int getFallenY() {
        return fallenY;
    }

    public void setFallenY(int fallenY) {
        this.fallenY = fallenY;
    }
}
