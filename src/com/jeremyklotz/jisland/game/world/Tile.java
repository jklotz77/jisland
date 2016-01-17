package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by Jeremy Klotz on 1/4/16
 */
public abstract class Tile {
    public static final int TILE_SIZE = SpriteSheet.SPRITE_SIZE;
    public static final int TYPE_GRASS = ColorUtils.createColor(0, 255, 0);
    public static final int TYPE_STONE = ColorUtils.createColor(150, 150, 150);
    public static final int TYPE_WATER = ColorUtils.createColor(0, 0, 255);
    public static final int TYPE_FIRE = ColorUtils.createColor(255, 100, 0);
    public static final int TYPE_SAND = ColorUtils.createColor(255, 255, 0);
    private static final int TILE_ROW_ON_SPRITESHEET = 0;

    protected static int[][] tiles;
    protected static int[][] animatedTiles;

    protected int type;

    public Tile(int type) {
        this.type = type;
    }

    public static void initTileArt(SpriteSheet spriteSheet) {
        tiles = new int[3][TILE_SIZE * TILE_SIZE];
        animatedTiles = new int[3][TILE_SIZE * TILE_SIZE];

        tiles[0] = spriteSheet.getSprite(0, TILE_ROW_ON_SPRITESHEET); // Grass
        tiles[1] = spriteSheet.getSprite(1, TILE_ROW_ON_SPRITESHEET); // Stone
        tiles[2] = spriteSheet.getSprite(2, TILE_ROW_ON_SPRITESHEET); // Sand

        animatedTiles[0] = spriteSheet.getSprite(0, 1); // Water
        animatedTiles[1] = spriteSheet.getSprite(1, 1);
        animatedTiles[2] = spriteSheet.getSprite(2, 1);
    }

    public abstract void update();

    public abstract void render(Bitmap bitmap, int x, int y);

    public int getType() {
        return type;
    }
}
