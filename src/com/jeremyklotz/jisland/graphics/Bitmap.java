package com.jeremyklotz.jisland.graphics;

import com.jeremyklotz.jisland.utils.ColorUtils;

import java.util.Arrays;

public class Bitmap {
    private int width;
    private int height;
    private int scale;
    private int[] pixels;
    private int blending;

    public Bitmap(int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;

        pixels = new int[width * height];

        Arrays.fill(pixels, 0);

        blending = BlendingConstants.NO_BLENDING;
    }

    public void clear(int color) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < pixels.length / width; y++) {
                drawPixel(x, y, color);
            }
        }
    }

    public void shade(int shade) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < pixels.length / width; y++) {
                int color = pixels[x + y * width];
                pixels[x + y * width] = ColorUtils.shade(color, shade);
            }
        }
    }

    public void drawPixel(int x, int y, int color) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;

        switch (blending) {
            case BlendingConstants.NO_BLENDING:
                pixels[x + y * width] = color;
                break;
            case BlendingConstants.ADDITIVE:
                int blendedColorAdditive = ColorUtils.blendColorsAdditive(color, pixels[x + y * width]);
                pixels[x + y * width] = blendedColorAdditive;
                break;
        }
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                if (j == 0 || j == height - 1 || i == 0 || i == width - 1)
                    drawPixel(x + i, y + j, color);
    }

    public void copyToIntArray(int[] destination) {
        int[] newPixels = new int[width * scale * height * scale];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int currentPixel = pixels[x + y * width];

                for (int scaleX = 0; scaleX < scale; scaleX++) {
                    for (int scaleY = 0; scaleY < scale; scaleY++) {
                        newPixels[x * scale + scaleX + (y * scale + scaleY) * width * scale] = currentPixel;
                    }
                }
            }
        }

        for (int i = 0; i < destination.length; i++)
            destination[i] = newPixels[i];
    }

    public void drawSprite(int[] sprite, int x, int y) {
        drawSprite(sprite, x, y, false);
    }

    public void drawSprite(int[] sprite, int x, int y, boolean mirrorX) {
        drawSprite(sprite, x, y, SpriteSheet.SPRITE_SIZE, mirrorX);
    }

    public void drawSprite(int[] sprite, int x, int y, int width) {
        drawSprite(sprite, x, y, width, false);
    }

    public void drawSpriteWithColor(int[] sprite, int x, int y, int width, int color) {
        for (int spriteX = 0; spriteX < width; spriteX++) {
            for (int spriteY = 0; spriteY < sprite.length / width; spriteY++) {

                int pixel = sprite[spriteX + spriteY * width];

                if (pixel != SpriteSheet.TRANSPARENCY_LIGHT && pixel != SpriteSheet.TRANSPARENCY_DARK)
                    drawPixel(x + spriteX, y + spriteY, color);
            }
        }
    }

    public void drawSprite(int[] sprite, int x, int y, int width, boolean mirrorX) {
        for (int spriteX = 0; spriteX < width; spriteX++) {
            for (int spriteY = 0; spriteY < sprite.length / width; spriteY++) {
                int xCor = mirrorX ? width - spriteX - 1 : spriteX;

                int pixel = sprite[xCor + spriteY * width];

                if (pixel != SpriteSheet.TRANSPARENCY_LIGHT && pixel != SpriteSheet.TRANSPARENCY_DARK)
                    drawPixel(x + spriteX, y + spriteY, pixel);
            }
        }
    }

    public void drawSubimage(int[] sprite, int x, int y, int spriteWidth, int startX, int startY, int width, int height) {
        drawSubimage(sprite, x, y, spriteWidth, startX, startY, width, height, false);
    }

    public void drawSubimage(int[] sprite, int x, int y, int spriteWidth, int startX, int startY, int width, int height, boolean mirrorX) {
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++) {
                int spriteX = mirrorX ? width - startX - dx - 1 : startX + dx;
                int spriteY = startY + dy;

                int pixel = sprite[spriteX + spriteY * spriteWidth];

                if (pixel != SpriteSheet.TRANSPARENCY_LIGHT && pixel != SpriteSheet.TRANSPARENCY_DARK)
                    drawPixel(x + dx, y + dy, pixel);
            }
        }
    }

    public void setBlending(int blending) {
        this.blending = blending;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
