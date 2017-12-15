package com.jeremyklotz.jisland.game.inventory;

import com.jeremyklotz.jisland.game.Animator;
import com.jeremyklotz.jisland.game.world.World;
import com.jeremyklotz.jisland.graphics.Bitmap;
import com.jeremyklotz.jisland.graphics.LightSource;
import com.jeremyklotz.jisland.graphics.SpriteSheet;
import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by Jeremy Klotz on 2/3/17
 */
public class Torch extends InventoryItem {
    private static final int TORCH_WIDTH = SpriteSheet.SPRITE_SIZE;
    private static final int TORCH_HEIGHT = SpriteSheet.SPRITE_SIZE;
    private static final int LIGHT_COLOR = ColorUtils.createColor(100, 50, 0);
    private static final int LIGHT_DISTANCE = 50;
    
    private static LightSource lightSource;
    private static int[][] animationFrames;
    
    private Animator animator;
    
    public Torch() {
        if (lightSource == null)
            throw new IllegalStateException("Torch art and light source not yet initialized.");
        
        animator = new Animator(8, animationFrames, false);
        hasLightSource = true;
    }
    
    public static void init(SpriteSheet spriteSheet) {
        lightSource = new LightSource(0, 0, LIGHT_COLOR, LIGHT_DISTANCE);
        
        animationFrames = new int[5][TORCH_WIDTH * TORCH_HEIGHT];
        animationFrames[0] = spriteSheet.getSprite(8, 0, TORCH_WIDTH, TORCH_HEIGHT);
        animationFrames[1] = spriteSheet.getSprite(9, 0, TORCH_WIDTH, TORCH_HEIGHT);
        animationFrames[2] = spriteSheet.getSprite(10, 0, TORCH_WIDTH, TORCH_HEIGHT);
        animationFrames[3] = spriteSheet.getSprite(11, 0, TORCH_WIDTH, TORCH_HEIGHT);
        animationFrames[4] = spriteSheet.getSprite(12, 0, TORCH_WIDTH, TORCH_HEIGHT);
    }
    
    @Override
    public void use(World world) {
    
    }
    
    @Override
    public void update() {
        animator.update();
    }
    
    @Override
    public void render(Bitmap bitmap, int x, int y) {
        bitmap.drawSprite(animator.getCurrentFrame(), x, y);
        
        lightSource.setX(x);
        lightSource.setY(y);
        lightSource.render(bitmap);
    }
}
