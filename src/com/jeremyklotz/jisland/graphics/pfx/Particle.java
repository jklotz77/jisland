package com.jeremyklotz.jisland.graphics.pfx;

import com.jeremyklotz.jisland.graphics.Bitmap;

import java.util.Random;

public class Particle {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private int color;

    public Particle(int initColor, double range) {
        x = 0;
        y = 0;
        color = initColor;

        Random rand = new Random();

        do {
            dx = rand.nextDouble() * range * 2 - range;
            dy = rand.nextDouble() * range * 2 - range;
        } while (dx == 0 && dy == 0);
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void render(Bitmap bitmap, int originX, int originY) {
        bitmap.drawPixel((int) (originX + x), (int) (originY + y), color);
    }

    public double getX() {
        return x + dy;
    }

    public double getY() {
        return y + dy;
    }
}