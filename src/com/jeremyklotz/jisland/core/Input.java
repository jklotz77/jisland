package com.jeremyklotz.jisland.core;

import com.jeremyklotz.jisland.JIsland;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class Input implements KeyListener {
    private boolean upPressed;
    private boolean downPressed;
    private boolean rightPressed;
    private boolean leftPressed;
    private boolean spacePressed;
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
        if (JIsland.USE_WASD) {
            if (e.getKeyCode() == KeyEvent.VK_A)
                leftPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_S)
                downPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_D)
                rightPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_W)
                upPressed = true;
        } else {
            if (e.getKeyCode() == KeyEvent.VK_UP)
                upPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                downPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                rightPressed = true;
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                leftPressed = true;
        }

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
        if (JIsland.USE_WASD) {
            if (e.getKeyCode() == KeyEvent.VK_A)
                leftPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_S)
                downPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_D)
                rightPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_W)
                upPressed = false;
        } else {
            if (e.getKeyCode() == KeyEvent.VK_UP)
                upPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                downPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                rightPressed = false;
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                leftPressed = false;
        }

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
        pPressed = false;
        iPressed = false;
        cPressed = false;
        spacePressed = false;
        enterPressed = false;
        escapePressed = false;
    }
}
