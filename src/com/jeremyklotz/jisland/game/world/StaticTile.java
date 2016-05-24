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
        bitmap.drawSprite(tiles.get(type), x, y);
    }
}
