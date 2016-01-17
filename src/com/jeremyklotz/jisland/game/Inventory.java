package com.jeremyklotz.jisland.game;

import com.jeremyklotz.jisland.core.Input;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jeremy on 1/17/16.
 */
public class Inventory {
    private ArrayList<Tool> tools;
    private int currentToolIndex;
    private int toolDirection;

    public Inventory() {
        tools = new ArrayList<>();
        currentToolIndex = -1;
    }

    public void update(Input input, World world) {
        if (currentToolIndex != -1)
            tools.get(currentToolIndex).update(toolDirection);

        if (currentToolIndex != -1 && input.isSpacePressed()) {
            tools.get(currentToolIndex).useTool(world, toolDirection);
        }
    }

    public void render(Bitmap bitmap, int playerX, int playerY) {
        if (currentToolIndex != -1) {
            Tool tool = tools.get(currentToolIndex);
            switch (tool.getType()) {
                case Tool.TYPE_AXE:
                    if (toolDirection == Player.LEFT) {
                        tool.render(bitmap, playerX - Tool.TOOL_WIDTH, playerY);
                    } else {
                        tool.render(bitmap, playerX + Player.PLAYER_SIZE, playerY);
                    }
                    break;
            }
        }
    }

    public void pickUpTool(Tool tool) {
        tools.add(tool);
        currentToolIndex = tools.size() - 1;
    }

    public Tool dropCurrentTool(int toolX, int toolY) {
        tools.get(currentToolIndex).setFallenX(toolX);
        tools.get(currentToolIndex).setFallenY(toolY);
        Tool fallenTool = tools.remove(currentToolIndex);
        currentToolIndex = -1;
        return fallenTool;
    }

    public int getCurrentToolIndex() {
        return currentToolIndex;
    }

    public int getToolDirection() {
        return toolDirection;
    }

    public void setToolDirection(int toolDirection) {
        this.toolDirection = toolDirection;
    }
}
