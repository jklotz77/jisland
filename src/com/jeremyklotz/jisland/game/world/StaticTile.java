package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.graphics.Bitmap;

/**
 * Created by Jeremy Klotz on 1/4/16
 */
public class StaticTile extends Tile {
    public StaticTile(int type) {
        super(type);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Bitmap bitmap, int x, int y) {
        if (type == TYPE_STONE) {
            bitmap.drawSprite(tiles[1], x, y);
        } else if (type == TYPE_SAND) {
            bitmap.drawSprite(tiles[2], x, y);
        } else {
            bitmap.drawSprite(tiles[0], x, y);
        }
    }
}
