package com.jeremyklotz.jisland.utils;

public class ColorUtils {
    public static final int WHITE = createColor(255, 255, 255);
    
    public static int createColor(int r, int g, int b) {
        int color = 0;

        color += (r & 0xFF) << 16;
        color += (g & 0xFF) << 8;
        color += b & 0xFF;

        return color;
    }

    public static int blendColorsAdditive(int color1, int color2) {
        int r1 = color1 / 65536;
        int g1 = color1 / 256 % 256;
        int b1 = color1 % 256;

        int r2 = color2 / 65536;
        int g2 = color2 / 256 % 256;
        int b2 = color2 % 256;

        int r = MathUtils.clamp(r1 + r2, 0, 255);
        int g = MathUtils.clamp(g1 + g2, 0, 255);
        int b = MathUtils.clamp(b1 + b2, 0, 255);

        return createColor(r, g, b);
    }

    public static int blendColorsAverage(int color1, int color2) {
        int r1 = color1 / 65536;
        int g1 = color1 / 256 % 256;
        int b1 = color1 % 256;

        int r2 = color2 / 65536;
        int g2 = color2 / 256 % 256;
        int b2 = color2 % 256;

        int r = (int) ((double) r1 + r2) / 2;
        int g = (int) ((double) g1 + g2) / 2;
        int b = (int) ((double) b1 + b2) / 2;

        return createColor(r, g, b);
    }

    public static int shade(int color, int shade) {
        int r = getR(color);
        int g = getG(color);
        int b = getB(color);

        r = MathUtils.clamp(r - shade, 0, 255);
        g = MathUtils.clamp(g - shade, 0, 255);
        b = MathUtils.clamp(b - shade, 0, 255);

        return createColor(r, g, b);
    }

    public static int getR(int color) {
        return color / 65536;
    }

    public static int getG(int color) {
        return color / 256 % 256;
    }

    public static int getB(int color) {
        return color % 256;
    }
}
