package com.jeremyklotz.jisland.graphics;

import com.jeremyklotz.jisland.utils.ColorUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

/**
 * Created by Jeremy Klotz on 1/3/16
 */
public class SpriteSheet {
    public static final int SPRITE_SIZE = 8;
    public static final int TRANSPARENCY_LIGHT = ColorUtils.createColor(187, 19, 207);
    public static final int TRANSPARENCY_DARK = ColorUtils.createColor(136, 16, 150);
    private int[] pixels;
    private BufferedImage image;
    private int width;

    public SpriteSheet(String path) {
        image = null;

        try {
            image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        BufferedImage useableImage;

        if (image.getType() != BufferedImage.TYPE_INT_RGB) {
            useableImage = new BufferedImage(image.getWidth(),
                    image.getHeight(), BufferedImage.TYPE_INT_RGB);
            useableImage.getGraphics().drawImage(image, 0, 0, null);
        } else
            useableImage = image;

        pixels = ((DataBufferInt) useableImage.getRaster().getDataBuffer()).getData();


        this.width = image.getWidth();
    }

    public int[] getSprite(int x, int y) {
        return getSprite(x, y, SPRITE_SIZE, SPRITE_SIZE);
    }

    public int[] getSprite(int x, int y, int spriteWidth, int spriteHeight) {
        int startX = x * SPRITE_SIZE;
        int startY = y * SPRITE_SIZE;

        int[] sprite = new int[spriteWidth * spriteHeight];

        for (int spriteX = 0; spriteX < spriteWidth; spriteX++) {
            for (int spriteY = 0; spriteY < spriteHeight; spriteY++) {
                sprite[spriteX + spriteY * spriteWidth] = pixels[(startX + spriteX) + (startY + spriteY) * width];
            }
        }

        return sprite;
    }

    public int getWidth() {
        return width;
    }
}
