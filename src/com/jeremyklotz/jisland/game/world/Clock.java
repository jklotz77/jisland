package com.jeremyklotz.jisland.game.world;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.graphics.Bitmap;

/**
 * Created by Jeremy Klotz on 4/8/16
 */
public class Clock {
    private double ticksPerDay;

    private int time;

    public Clock(int secondsPerDay) {
        time = 0;
        ticksPerDay = secondsPerDay * JIsland.FPS;
    }

    public void update() {
        time++;

        if (time > ticksPerDay)
            time = 0;
    }

    public void render(Bitmap bitmap) {

    }

    public int time() {
        return time;
    }

    public double percentLight() {
        if (time < ticksPerDay / 2)
            return time / (ticksPerDay / 2);
        else
            return (ticksPerDay - time) / (ticksPerDay / 2);
    }
}
