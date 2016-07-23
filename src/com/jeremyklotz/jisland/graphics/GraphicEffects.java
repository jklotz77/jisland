package com.jeremyklotz.jisland.graphics;

import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by Jeremy Klotz on 5/29/16.
 */
public class GraphicEffects {
    public static int[] blur(int[] pixels, int blurRadius, int iterations, int width) {
        if (iterations == 0)
            return pixels;

        int[] blur = new int[pixels.length];
        int height = pixels.length / width;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rSum = 0;
                int gSum = 0;
                int bSum = 0;
                int count = 0;

                for (int dx = -blurRadius; dx <= blurRadius; dx++) {
                    for (int dy = -blurRadius; dy <= blurRadius; dy++) {
                        if (x + dx >= 0 && x + dx < width &&
                                y + dy >= 0 && y + dy < height) {
                            int color = pixels[(x + dx) + (y + dy) * width];

                            rSum += ColorUtils.getR(color);
                            gSum += ColorUtils.getG(color);
                            bSum += ColorUtils.getB(color);

                            count++;
                        }
                    }
                }

                int avgR = (int) ((double) rSum / count);
                int avgG = (int) ((double) gSum / count);
                int avgB = (int) ((double) bSum / count);
                blur[x + y * width] = ColorUtils.createColor(avgR, avgG, avgB);
            }
        }

        return blur(blur, blurRadius, iterations - 1, width);
    }
}
