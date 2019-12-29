package com.jeremyklotz.jisland.graphics.pfx;

import com.jeremyklotz.jisland.graphics.Bitmap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ParticleEffect {
    private List<Particle> particles;
    private double velocityRange;
    private int color;
    private double maxDistance;
    private boolean infinite;

    public ParticleEffect(double velocityRange, int numParticles, int color, double maxDistance) {
        this(velocityRange, numParticles, color, maxDistance, true);
    }

    public ParticleEffect(double velocityRange, int numParticles, int color, double maxDistance, boolean infinite) {
        this.velocityRange = velocityRange;
        this.color = color;
        this.maxDistance = maxDistance;
        this.infinite = infinite;

        if (infinite)
            particles = new ArrayList<>();
        else
            particles = new LinkedList<>();

        for (int i = 0; i < numParticles; i++) {
            particles.add(new Particle(color, velocityRange));
        }
    }

    public void update() {
        for (ListIterator<Particle> it = particles.listIterator(); it.hasNext();) {
            Particle p = it.next();

            p.update();

            double distanceFromOrigin = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());

            if (distanceFromOrigin > maxDistance) {
                if (infinite)
                    it.set(generateParticle());
                else
                    it.remove();
            }
        }
    }

    private Particle generateParticle() {
        return new Particle(color, velocityRange);
    }

    public void render(Bitmap bitmap, int x, int y) {
        for (Particle p : particles)
            p.render(bitmap, x, y);
    }
}
