package com.jeremyklotz.jisland.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean rightPressed;
    private boolean leftPressed;
    private boolean spacePressed;
    private boolean wPressed;
    private boolean aPressed;
    private boolean sPressed;
    private boolean dPressed;
    private boolean pPressed;
    private boolean iPressed;
    private boolean cPressed;
    private boolean anyKeyPressed;
    private boolean enterPressed;
    private boolean escapePressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            upPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            downPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            rightPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            leftPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_W)
            wPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_A)
            aPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_S)
            sPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_D)
            dPressed = true;
        if (e.getKeyChar() == KeyEvent.VK_P)
            pPressed = true;
        if (e.getKeyChar() == KeyEvent.VK_I)
            iPressed = true;
        if (e.getKeyChar() == KeyEvent.VK_C)
            cPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            spacePressed = true;
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            enterPressed = true;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            escapePressed = true;

        anyKeyPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            upPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            downPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            rightPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            leftPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_W)
            wPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_A)
            aPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_S)
            sPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_D)
            dPressed = false;
        if (e.getKeyChar() == KeyEvent.VK_P)
            pPressed = false;
        if (e.getKeyChar() == KeyEvent.VK_I)
            iPressed = false;
        if (e.getKeyChar() == KeyEvent.VK_C)
            cPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            spacePressed = false;
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            enterPressed = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            escapePressed = false;

        anyKeyPressed = false;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean iswPressed() {
        return wPressed;
    }

    public boolean isaPressed() {
        return aPressed;
    }

    public boolean issPressed() {
        return sPressed;
    }

    public boolean isdPressed() {
        return dPressed;
    }

    public boolean ispPressed() {
        return pPressed;
    }

    public boolean isiPressed() {
        return iPressed;
    }

    public boolean iscPressed() {
        return cPressed;
    }

    public boolean isSpacePressed() {
        return spacePressed;
    }

    public boolean isAnyKeyPressed() {
        return anyKeyPressed;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public boolean isEscapePressed() {
        return escapePressed;
    }

    public void resetKeys() {
        upPressed = false;
        downPressed = false;
        rightPressed = false;
        leftPressed = false;
        wPressed = false;
        aPressed = false;
        dPressed = false;
        sPressed = false;
        pPressed = false;
        iPressed = false;
        cPressed = false;
        spacePressed = false;
        enterPressed = false;
        escapePressed = false;
    }
}
