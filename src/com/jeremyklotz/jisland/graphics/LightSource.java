package com.jeremyklotz.jisland.graphics;

import com.jeremyklotz.jisland.utils.ColorUtils;

/**
 * Created by Jeremy Klotz on 1/11/16
 */
public class LightSource {
    private static final double PRECISION_PER_DEPTH = 12;
    private int x;
    private int y;
    private int color;
    private int distance;
    private int[] litPixels;

    public LightSource(int x, int y, int color, int distance) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.distance = distance;

        litPixels = new int[distance * 2 * distance * 2];

        calculateLitPixels();
    }

    private void calculateLitPixels() {
        for (int depth = 1; depth < distance; depth++) {
            double percent = 1 - ((double) depth / distance);

            for (double angle = 0; angle < Math.PI * 2; angle += Math.PI * 2 / (depth * PRECISION_PER_DEPTH)) {
                int xCor = (int) (Math.cos(angle) * depth + distance);
                int yCor = (int) (Math.sin(angle) * depth + distance);

                int r = (int) (ColorUtils.getR(color) * percent);
                int g = (int) (ColorUtils.getG(color) * percent);
                int b = (int) (ColorUtils.getB(color) * percent);

                litPixels[xCor + yCor * distance * 2] = ColorUtils.createColor(r, g, b);
            }
        }
    }

    public void render(Bitmap bitmap) {
        bitmap.setBlending(BlendingConstants.ADDITIVE);
        bitmap.drawSprite(litPixels, x - distance, y - distance, distance * 2);
        bitmap.setBlending(BlendingConstants.NO_BLENDING);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
