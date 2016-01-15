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
        switch (type) {
            case TYPE_GRASS:
                bitmap.drawSprite(tiles[TYPE_GRASS], x, y);
                break;
            case TYPE_STONE:
                bitmap.drawSprite(tiles[TYPE_STONE], x, y);
                break;
            case TYPE_SAND:
                bitmap.drawSprite(tiles[TYPE_SAND], x, y);
        }
    }
}
