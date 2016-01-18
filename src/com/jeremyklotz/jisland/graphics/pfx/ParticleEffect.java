package com.jeremyklotz.jisland.graphics.pfx;

import com.jeremyklotz.jisland.JIsland;
import com.jeremyklotz.jisland.graphics.Bitmap;

import java.util.ArrayList;

public class ParticleEffect {
    private Particle[] particles;
    private double velocityRange;
    private int color;
    private double maxDistance;

    public ParticleEffect(double velocityRange, int numParticles, int color, double maxDistance) {
        this.velocityRange = velocityRange;
        this.color = color;
        this.maxDistance = maxDistance;

        particles = new Particle[numParticles];

        for (int i = 0; i < numParticles; i++) {
            particles[i] = new Particle(color, velocityRange);
        }
    }

    public void update() {
        ArrayList<Integer> particleIndicies = new ArrayList<>();

        for (int i = 0; i < particles.length; i++) {
            Particle p = particles[i];
            p.update();

            double distanceFromOrigin = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());

            if (distanceFromOrigin > maxDistance) {
                particleIndicies.add(i);
            }
        }


        for (int i = 0; i < particleIndicies.size(); i++)
            generateParticle(particleIndicies.get(i));
    }

    private void generateParticle(int index) {
        particles[index] = new Particle(color, velocityRange);
    }

    public void render(Bitmap bitmap, int x, int y) {
        for (Particle p : particles)
            p.render(bitmap, x, y);
    }
}