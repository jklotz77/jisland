package com.jeremyklotz.jisland.game.inventory;

import com.jeremyklotz.jisland.game.Player;
import com.jeremyklotz.jisland.game.inventory.InventoryItem;
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
public class Tool extends InventoryItem {
    public static final int TOOL_WIDTH = SpriteSheet.SPRITE_SIZE;
    public static final int TOOL_HEIGHT = SpriteSheet.SPRITE_SIZE * 2;
    public static final int TYPE_AXE = 0;
    private static final int TOOL_ROW_ON_SPRITESHEET = 8;

    private static HashMap<Integer, int[]> toolPixels;

    private int toolType;

    public Tool(int toolType, Player player) {
        this.toolType = toolType;
        this.player = player;
        direction = Player.RIGHT;
    }

    public Tool(int tool_type, int fallenXOnMap, int fallenYOnMap) {
        if (toolPixels == null)
            throw new IllegalStateException("Tool art must be initialized before creating a tool");

        this.toolType = tool_type;
        this.player = null;
        direction = Player.RIGHT;
        this.fallenXOnMap = fallenXOnMap;
        this.fallenYOnMap = fallenYOnMap;
    }

    public static void initArt(SpriteSheet spriteSheet) {
        toolPixels = new HashMap<>();

        toolPixels.put(TYPE_AXE, spriteSheet.getSprite(TYPE_AXE, TOOL_ROW_ON_SPRITESHEET, TOOL_WIDTH, TOOL_HEIGHT));
    }

    @Override
    public void use(World world) {
        switch (toolType) {
            case TYPE_AXE:
                Rectangle toolBounds = new Rectangle(direction == Player.LEFT ? player.getX() - Player.PLAYER_SIZE / 2
                        : player.getX() + Player.PLAYER_SIZE,
                        player.getY(), TOOL_WIDTH, TOOL_HEIGHT / 2);

                for (Iterator<Tree> it = world.getTrees().iterator(); it.hasNext();) {
                    Tree tree = it.next();

                    if (tree.bounds().intersects(toolBounds)) {
                        InventoryItem logs = new RawMaterial(RawMaterial.TYPE_LOGS);

                        logs.setFallenXOnMap(tree.getX());
                        logs.setFallenYOnMap(tree.getY());

                        world.addFallenItem(logs);

                        it.remove();
                        break;
                    }
                }

                break;
        }
    }

    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(toolPixels.get(toolType), x, y, direction == Player.LEFT);
    }

    public int getToolType() {
        return toolType;
    }

    public int getItemWidth() {
        return TOOL_WIDTH;
    }

    public int getItemHeight() {
        return TOOL_HEIGHT;
    }

    @Override
    public String toString() {
        switch (toolType) {
            case TYPE_AXE:
                return "Axe";
            default:
                return "Unknown tool";
        }
    }
}
