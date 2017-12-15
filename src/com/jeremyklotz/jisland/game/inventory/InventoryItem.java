package com.jeremyklotz.jisland.game.inventory;

import com.jeremyklotz.jisland.game.Player;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.SpriteSheet;

/**
 * Created by jeremy on 10/7/16.
 */
public abstract class InventoryItem {
    public static final int ITEM_WIDTH = SpriteSheet.SPRITE_SIZE;
    public static final int ITEM_HEIGHT = SpriteSheet.SPRITE_SIZE;

    protected int direction;
    protected int fallenXOnMap;
    protected int fallenYOnMap;
    protected Player player;
    protected boolean hasLightSource;

    public abstract void use(World world);
    
    public abstract void update();

    public abstract void render(Bitmap bitmap, int x, int y);

    public void pickUp(Player player) {
        this.player = player;
    }

    public void update(int direction) {
        this.direction = direction;
    }

    public int getFallenXOnMap() {
        return fallenXOnMap;
    }

    public int getFallenYOnMap() {
        return fallenYOnMap;
    }

    public void setFallenXOnMap(int fallenXOnMap) {
        this.fallenXOnMap = fallenXOnMap;
    }

    public void setFallenYOnMap(int fallenYOnMap) {
        this.fallenYOnMap = fallenYOnMap;
    }

    public int getItemWidth() {
        return ITEM_WIDTH;
    }

    public int getItemHeight() {
        return ITEM_HEIGHT;
    }
    
    public boolean hasLightSource() {
        return hasLightSource;
    }
}
