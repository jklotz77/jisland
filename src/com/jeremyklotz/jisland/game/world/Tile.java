package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

/**
 * Created by Jeremy Klotz on 1/4/16
 */
public abstract class Tile {
    public static final int TILE_SIZE = SpriteSheet.SPRITE_SIZE;
    public static final int TYPE_GRASS = 0;
    public static final int TYPE_STONE = 1;
    public static final int TYPE_WATER = 100;
    public static final int TYPE_FIRE = 101;
    public static final int TYPE_SAND = 2;
    private static final int TILE_ROW_ON_SPRITESHEET = 0;

    protected static int[][] tiles;
    protected static int[][] animatedTiles;

    protected int type;

    public Tile(int type) {
        this.type = type;
    }

    public static void initTileArt(SpriteSheet spriteSheet) {
        tiles = new int[3][TILE_SIZE * TILE_SIZE];
        animatedTiles = new int[6][TILE_SIZE * TILE_SIZE];

        tiles[TYPE_GRASS] = spriteSheet.getSprite(TYPE_GRASS, TILE_ROW_ON_SPRITESHEET);
        tiles[TYPE_STONE] = spriteSheet.getSprite(TYPE_STONE, TILE_ROW_ON_SPRITESHEET);
        tiles[TYPE_SAND] = spriteSheet.getSprite(TYPE_SAND, TILE_ROW_ON_SPRITESHEET);

        animatedTiles[0] = spriteSheet.getSprite(0, 1);
        animatedTiles[1] = spriteSheet.getSprite(1, 1);
        animatedTiles[2] = spriteSheet.getSprite(2, 1);
        animatedTiles[3] = spriteSheet.getSprite(7, 1);
        animatedTiles[4] = spriteSheet.getSprite(8, 1);
        animatedTiles[5] = spriteSheet.getSprite(9, 1);
    }

    public abstract void update();

    public abstract void render(Bitmap bitmap, int x, int y);

    public int getType() {
        return type;
    }
}
