package com.jeremyklotz.jisland.graphics;

import com.jeremyklotz.jisland.core.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Window extends Canvas {
    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private Bitmap bitmap;
    private BufferedImage windowImage;
    private int[] windowComponents;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    public Window(int width, int height, int scale, String title, Input input) {
        Dimension size = new Dimension(width * scale, height * scale);

        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);

        bitmap = new Bitmap(width, height, scale);
        windowImage = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
        windowComponents = ((DataBufferInt) windowImage.getRaster().getDataBuffer()).getData();

        frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(size);
        frame.setVisible(true);

        createBufferStrategy(1);
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();

        addKeyListener(input);
    }

    public void swapBuffers() {
        bitmap.copyToIntArray(windowComponents);
        graphics.drawImage(windowImage, 0, 0, windowImage.getWidth(),
                windowImage.getHeight(), null);
        bufferStrategy.show();
    }

    public JFrame getWindow() {
        return frame;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
