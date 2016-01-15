package com.jeremyklotz.jisland.utils;

/**
 * Created by Jeremy Klotz on 1/6/16
 */
public class MathUtils {
    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
