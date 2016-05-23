package com.jeremyklotz.jisland.graphics;

/**
 * Created by jeremy on 1/17/16.
 */
public class Text {
    public static final int CHARACTER_SIZE = SpriteSheet.SPRITE_SIZE;
    private static final int ROW_ON_SPRITE_SHEET = 10;
    private static final int NUM_CHARACTERS = 26;
    private static final int CHARACTER_RENDER_SIZE = SpriteSheet.SPRITE_SIZE - 3;
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

            characters[spriteX] = spriteSheet.getSprite(actualSpriteX, spriteY, CHARACTER_SIZE, CHARACTER_SIZE);
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

            if (charIndexInArray < 0 || charIndexInArray >= characters.length) {
                System.err.println("We do not support the character '" + ch + "' yet");

                continue;
            }

            int[] charSprite = characters[charIndexInArray];

            int xCor = x + (i - startWrapIndex) * CHARACTER_RENDER_SIZE;
            int yCor = y + wrapLevel * CHARACTER_RENDER_SIZE;

            if (xCor > bitmap.getWidth() - CHARACTER_RENDER_SIZE) {
                startWrapIndex = i;
                wrapLevel++;

                xCor = x + (i - startWrapIndex) * CHARACTER_RENDER_SIZE;
                yCor = y + wrapLevel * CHARACTER_RENDER_SIZE;
            }

            bitmap.drawSpriteWithColor(charSprite, xCor, yCor, CHARACTER_SIZE, color);
        }
    }
}
