package com.jeremyklotz.jisland.game.inventory;

import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import java.util.HashMap;

/**
 * Created by Jeremy Klotz on 10/7/16.
 */
public class RawMaterial extends InventoryItem {
    public static final int TYPE_LOGS = 0;

    private int type;
    private static HashMap<Integer, int[]> rawMaterialArt;

    public RawMaterial(int type) {
        if (rawMaterialArt == null)
            throw new IllegalStateException("Must initialize Raw Material Art");

        this.type = type;
    }

    public static void initArt(SpriteSheet spriteSheet) {
        rawMaterialArt = new HashMap<>();

        rawMaterialArt.put(TYPE_LOGS, spriteSheet.getSprite(1, 8, SpriteSheet.SPRITE_SIZE, SpriteSheet.SPRITE_SIZE * 2));
    }

    @Override
    public void use(World world) {

    }

    @Override
    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(rawMaterialArt.get(type), x, y, getItemWidth());
    }

    public int getItemWidth() {
        switch (type) {
            case TYPE_LOGS:
                return SpriteSheet.SPRITE_SIZE;
            default:
                return SpriteSheet.SPRITE_SIZE;
        }
    }

    public int getItemHeight() {
        switch (type) {
            case  TYPE_LOGS:
                return SpriteSheet.SPRITE_SIZE * 2;
            default:
                return SpriteSheet.SPRITE_SIZE;
        }
    }
}
