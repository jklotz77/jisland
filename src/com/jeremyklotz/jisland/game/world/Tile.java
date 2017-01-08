package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

import java.util.HashMap;

/**
 * Created by Jeremy Klotz on 1/4/16
 */
public abstract class Tile {
    public static final int TILE_SIZE = SpriteSheet.SPRITE_SIZE;
    public static final int TYPE_GRASS = 65280; // (0, 255, 0)
    public static final int TYPE_STONE = 9868950; // (150, 150, 150)
    public static final int TYPE_WATER = 255; // (0, 0, 255)
    public static final int TYPE_FIRE = 16737280; // (255, 100, 0)
    public static final int TYPE_SAND =  16776960; // (255, 255, 0)
    private static final int TILE_ROW_ON_SPRITESHEET = 0;

    protected static HashMap<Integer, int[]> tiles;
    protected static HashMap<Integer, int[][]> animatedTiles;

    protected int type;

    public Tile(int type) {
        if (tiles == null)
            throw new IllegalStateException("Tile art must be initialized before creating a tile");

        this.type = type;
    }

    public static void initTileArt(SpriteSheet spriteSheet) {
        tiles = new HashMap<>();
        animatedTiles = new HashMap<>();

        tiles.put(TYPE_GRASS, spriteSheet.getSprite(0, TILE_ROW_ON_SPRITESHEET));
        tiles.put(TYPE_STONE, spriteSheet.getSprite(1, TILE_ROW_ON_SPRITESHEET));
        tiles.put(TYPE_SAND, spriteSheet.getSprite(2, TILE_ROW_ON_SPRITESHEET));

        int[][] waterTiles = new int[][] {
                spriteSheet.getSprite(0, 1),
                spriteSheet.getSprite(1, 1),
                spriteSheet.getSprite(2, 1)
        };

        animatedTiles.put(TYPE_WATER, waterTiles);
    }

    public abstract void update();

    public abstract void render(Bitmap bitmap, int x, int y);

    public int getType() {
        return type;
    }
}
