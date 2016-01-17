package com.jeremyklotz.jisland.graphics;

/**
 * Created by jeremy on 1/17/16.
 */
public class Text {
    public static final int CHARACTER_SIZE = SpriteSheet.SPRITE_SIZE * 2;
    private static final int ROW_ON_SPRITE_SHEET = 12;
    private static final int NUM_CHARACTERS = 3;
    private static int[][] characters;

    public static void initArt(SpriteSheet spriteSheet) {
        characters = new int[NUM_CHARACTERS][CHARACTER_SIZE * CHARACTER_SIZE];

        for (int spriteX = 0; spriteX < NUM_CHARACTERS * CHARACTER_SIZE / SpriteSheet.SPRITE_SIZE; spriteX += CHARACTER_SIZE / SpriteSheet.SPRITE_SIZE) {
            int spriteY = ROW_ON_SPRITE_SHEET;

            int actualSpriteX = spriteX;

            if (spriteX >= spriteSheet.getWidth() / SpriteSheet.SPRITE_SIZE) {
                actualSpriteX = spriteX - spriteSheet.getWidth() / SpriteSheet.SPRITE_SIZE;
                spriteY += CHARACTER_SIZE / SpriteSheet.SPRITE_SIZE;
            }

            characters[spriteX / 2] = spriteSheet.getSprite(actualSpriteX, spriteY, CHARACTER_SIZE, CHARACTER_SIZE);
        }
    }

    public static void render(String text, int x, int y, Bitmap bitmap, int color) {
        text = text.toUpperCase();

        int startWrapIndex = 0;
        int wrapLevel = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch == ' ')
                continue;

            int charIndexInArray = ((int) ch) - 65;
            int[] charSprite = characters[charIndexInArray];

            int xCor = x + (i - startWrapIndex) * CHARACTER_SIZE;
            int yCor = y + wrapLevel * CHARACTER_SIZE;

            if (xCor > bitmap.getWidth() - CHARACTER_SIZE) {
                startWrapIndex = i;
                wrapLevel++;

                xCor = x + (i - startWrapIndex) * CHARACTER_SIZE;
                yCor = y + wrapLevel * CHARACTER_SIZE;
            }

            bitmap.drawSpriteWithColor(charSprite, xCor, yCor, CHARACTER_SIZE, color);
        }
    }
}
