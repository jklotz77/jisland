package com.jeremyklotz.jisland.game;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.game.world.Tile;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.utils.ColorUtils;
import com.jeremyklotz.jisland.utils.MathUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jeremy Klotz on 1/6/16
 */
public class Player {
    public static final int PLAYER_SIZE = SpriteSheet.SPRITE_SIZE * 2;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;
    private static final double SPEED = 50 / (double) JIsland.FPS;
    private static final int ANIMATION_SPEED = 8;
    private static final int INITIAL_HEALTH = 8;

    private double x;
    private double y;
    private Animator downAnimator;
    private Animator sideAnimator;
    private Animator upAninimator;
    private int direction; // 1 = up, 2 = right, 3 = down, 4 = left
    private int toolDirection;
    private boolean moving;
    private int[] heartSprite;

    private World world;

    private ArrayList<Tool> tools;
    private int currentTool;
    private int health;

    public Player(int x, int y, SpriteSheet spriteSheet, World world) {
        this.x = x;
        this.y = y;

        initArt(spriteSheet);

        direction = RIGHT;
        moving = false;

        this.world = world;

        tools = new ArrayList<>();
        currentTool = -1;
        toolDirection = RIGHT;

        health = INITIAL_HEALTH;
    }

    private void initArt(SpriteSheet spriteSheet) {
        int[][] downFrames = new int[4][PLAYER_SIZE * PLAYER_SIZE];
        downFrames[0] = spriteSheet.getSprite(0, 2, PLAYER_SIZE, PLAYER_SIZE);
        downFrames[1] = spriteSheet.getSprite(2, 2, PLAYER_SIZE, PLAYER_SIZE);
        downFrames[2] = spriteSheet.getSprite(4, 2, PLAYER_SIZE, PLAYER_SIZE);
        downFrames[3] = spriteSheet.getSprite(6, 2, PLAYER_SIZE, PLAYER_SIZE);

        int[][] sideFrames = new int[4][PLAYER_SIZE * PLAYER_SIZE];
        sideFrames[0] = spriteSheet.getSprite(0, 4, PLAYER_SIZE, PLAYER_SIZE);
        sideFrames[1] = spriteSheet.getSprite(2, 4, PLAYER_SIZE, PLAYER_SIZE);
        sideFrames[2] = spriteSheet.getSprite(4, 4, PLAYER_SIZE, PLAYER_SIZE);
        sideFrames[3] = spriteSheet.getSprite(6, 4, PLAYER_SIZE, PLAYER_SIZE);

        int[][] upFrames = new int[4][PLAYER_SIZE * PLAYER_SIZE];
        upFrames[0] = spriteSheet.getSprite(0, 6, PLAYER_SIZE, PLAYER_SIZE);
        upFrames[1] = spriteSheet.getSprite(2, 6, PLAYER_SIZE, PLAYER_SIZE);
        upFrames[2] = spriteSheet.getSprite(4, 6, PLAYER_SIZE, PLAYER_SIZE);
        upFrames[3] = spriteSheet.getSprite(6, 6, PLAYER_SIZE, PLAYER_SIZE);

        downAnimator = new Animator(ANIMATION_SPEED, downFrames, false);
        sideAnimator = new Animator(ANIMATION_SPEED, sideFrames, false);
        upAninimator = new Animator(ANIMATION_SPEED, upFrames, false);

        heartSprite = spriteSheet.getSprite(7, 0);
    }

    public void update(Input input) {
        moving = false;

        if (input.isUpPressed()) {
            move(0, -SPEED);
            direction = UP;
        }
        if (input.isDownPressed()) {
            move(0, SPEED);
            direction = DOWN;
        }
        if (input.isRightPressed()) {
            move(SPEED, 0);
            direction = RIGHT;
            toolDirection = RIGHT;
        }
        if (input.isLeftPressed()) {
            move(-SPEED, 0);
            direction = LEFT;
            toolDirection = LEFT;
        }
        if (currentTool == -1 && input.isSpacePressed()) {
            pickUpTool();
        }
        if (currentTool != -1 && input.isEscapePressed()) {
            dropCurrentTool();
        }

        x = MathUtils.clamp(x, 0, world.getTileWidth() * Tile.TILE_SIZE - PLAYER_SIZE);
        y = MathUtils.clamp(y, 0, world.getTileHeight() * Tile.TILE_SIZE - PLAYER_SIZE);

        if (moving) {
            switch (direction) {
                case UP:
                    upAninimator.update();
                    break;
                case RIGHT:
                case LEFT:
                    sideAnimator.update();
                    break;
                case DOWN:
                    downAnimator.update();
            }
        }

        updateTool(input);
    }

    private void pickUpTool() {
        ArrayList<Tool> fallenTools = world.getFallenTools();

        Rectangle playerBounds = bounds();

        for (int i = 0; i < fallenTools.size(); i++) {
            int x = fallenTools.get(i).getFallenX();
            int y = fallenTools.get(i).getFallenY();

            Rectangle toolBounds = new Rectangle(x, y, Tool.TOOL_WIDTH, Tool.TOOL_HEIGHT);

            if (toolBounds.intersects(playerBounds)) {
                fallenTools.get(i).pickUp(this);
                tools.add(fallenTools.get(i));
                currentTool = tools.size() - 1;
                fallenTools.remove(i);
                return;
            }
        }
    }

    private void dropCurrentTool() {
        int toolX;
        int toolY = (int) y;

        if (toolDirection == LEFT) {
            toolX = (int) (x - Tool.TOOL_WIDTH);
        } else {
            toolX = (int) (x + PLAYER_SIZE);
        }

        tools.get(currentTool).setFallenX(toolX);
        tools.get(currentTool).setFallenY(toolY);
        world.getFallenTools().add(tools.get(currentTool));
        tools.remove(currentTool);
        currentTool = -1;
    }

    private void updateTool(Input input) {
        if (currentTool != -1)
            tools.get(currentTool).update(toolDirection);

        if (currentTool != -1 && input.isSpacePressed()) {
            tools.get(currentTool).useTool(world, toolDirection);
        }
    }

    private void move(double dx, double dy) {
        moving = true;

        x += dx;
        y += dy;
    }

    private boolean isInWater() {
        int tileX = (int) (x / Tile.TILE_SIZE);
        int tileY = (int) (y / Tile.TILE_SIZE);

        Rectangle playerBounds = bounds();

        for (int dx = -1; dx <= 2; dx++) {
            for (int dy = -1; dy <= 2; dy++) {
                if (tileX + dx >= 0 && tileX + dx < world.getTileWidth() && tileY + dy >= 0 &&
                        tileY + dy < world.getTileHeight()) {

                    Tile tile = world.getTile(tileX + dx, tileY + dy);

                    if (tile.getType() == Tile.TYPE_WATER) {
                        Rectangle tileBounds = new Rectangle((tileX + dx) * Tile.TILE_SIZE, (tileY + dy) * Tile.TILE_SIZE,
                                Tile.TILE_SIZE, Tile.TILE_SIZE);

                        Rectangle intersection = playerBounds.intersection(tileBounds);

                        if (intersection.getWidth() * intersection.getHeight() > PLAYER_SIZE * PLAYER_SIZE / 8)
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public void render(Bitmap bitmap, int x, int y) {
        if (isInWater()) {
            switch (direction) {
                case UP:
                    bitmap.drawSubimage(upAninimator.getCurrentFrame(), x, y, PLAYER_SIZE, 0, 0, PLAYER_SIZE,
                            PLAYER_SIZE / 2);
                    break;
                case RIGHT:
                    bitmap.drawSubimage(sideAnimator.getCurrentFrame(), x, y, PLAYER_SIZE, 0, 0, PLAYER_SIZE,
                            PLAYER_SIZE / 3 * 2);
                    break;
                case DOWN:
                    bitmap.drawSubimage(downAnimator.getCurrentFrame(), x, y, PLAYER_SIZE, 0, 0, PLAYER_SIZE,
                            PLAYER_SIZE / 5 * 3);
                    break;
                case LEFT:
                    bitmap.drawSubimage(sideAnimator.getCurrentFrame(), x, y, PLAYER_SIZE, 0, 0, PLAYER_SIZE,
                            PLAYER_SIZE / 3 * 2, true);
                    break;
            }
        } else {
            switch (direction) {
                case UP:
                    bitmap.drawSprite(upAninimator.getCurrentFrame(), x, y, PLAYER_SIZE);
                    break;
                case RIGHT:
                    bitmap.drawSprite(sideAnimator.getCurrentFrame(), x, y, PLAYER_SIZE);
                    break;
                case DOWN:
                    bitmap.drawSprite(downAnimator.getCurrentFrame(), x, y, PLAYER_SIZE);
                    break;
                case LEFT:
                    bitmap.drawSprite(sideAnimator.getCurrentFrame(), x, y, PLAYER_SIZE, true);
                    break;
            }
        }

        if (currentTool != -1) {
            Tool tool = tools.get(currentTool);
            switch (tool.getType()) {
                case Tool.TYPE_AXE:
                    if (toolDirection == LEFT) {
                        tool.render(bitmap, x - Tool.TOOL_WIDTH, y);
                    } else {
                        tool.render(bitmap, x + PLAYER_SIZE, y);
                    }
                    break;
            }
        }

        if (JIsland.DEBUG) bitmap.drawRect(x, y, PLAYER_SIZE, PLAYER_SIZE, ColorUtils.createColor(255, 0, 0));
    }

    public void renderHealthBar(Bitmap bitmap) {
        int x = 4;
        int y = 4;

        for (int i = 0; i < health; i++) {
            bitmap.drawSprite(heartSprite, x + i * (SpriteSheet.SPRITE_SIZE + 2), y);
        }
    }

    public Rectangle bounds() {
        return new Rectangle((int) x, (int) y, PLAYER_SIZE, PLAYER_SIZE);
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }
}
