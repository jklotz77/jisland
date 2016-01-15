package com.jeremyklotz.jisland.game;

/**
 * Created by Jeremy Klotz on 1/6/16
 */
public class Animator {
    private int ticksPerFrame;
    private int[][] frames;
    private int currentFrame;
    private int ticks;
    private boolean reversing;
    private boolean animatingForward;

    public Animator(int ticksPerFrame, int[][] frames, boolean reversing) {
        this.ticksPerFrame = ticksPerFrame;
        this.frames = frames;
        this.reversing = reversing;
        animatingForward = true;
    }

    public void update() {
        ticks++;

        if (ticks > ticksPerFrame) {
            if (!reversing) {
                ticks = 0;
                currentFrame++;
                currentFrame = currentFrame % frames.length;
            } else {
                if (animatingForward) {
                    ticks = 0;
                    currentFrame++;

                    if (currentFrame == frames.length - 1)
                        animatingForward = false;
                } else {
                    ticks = 0;
                    currentFrame--;

                    if (currentFrame == 0)
                        animatingForward = true;
                }
            }
        }
    }

    public int[] getCurrentFrame() {
        return frames[currentFrame];
    }
}
