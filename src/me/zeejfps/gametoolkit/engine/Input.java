package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.math.Vec2i;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by Zeejfps on 6/15/2016.
 */
public class Input {

    private static final int MAX_KEYS = 600;

    private final KeyListener keyListener;
    private final MouseListener mouseListener;
    private final MouseMotionListener mouseMotionListener;
    private final MouseWheelListener mouseWheelListener;

    private boolean[] keys = new boolean[MAX_KEYS];
    private boolean[] keysDown = new boolean[MAX_KEYS];
    private boolean[] keysUp = new boolean[MAX_KEYS];

    private final Window display;

    public final Vec2i mousePosition;

    public Input(Window display) {
        this.display = display;
        Component component = display.canvas;
        keyListener = new InputKeyListener();
        mouseListener = new InputMouseListener();
        mouseMotionListener = new InputMouseMotionListener();
        mouseWheelListener = new InputMouseWheelListener();
        mousePosition = new Vec2i();

        component.addKeyListener(keyListener);
        component.addMouseListener(mouseListener);
        component.addMouseMotionListener(mouseMotionListener);
        component.addMouseWheelListener(mouseWheelListener);
    }

    public boolean getKey(int keycode) {
        return keys[keycode];
    }

    public boolean getKeyDown(int keycode) {
        boolean keyDown = keysDown[keycode];
        keysDown[keycode] = false;
        return keyDown;
    }

    public boolean getKeyUp(int keycode) {
        boolean keyUp = keysUp[keycode];
        keysUp[keycode] = false;
        return keyUp;
    }

    private class InputKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if (keys[e.getKeyCode()] && !keysDown[e.getKeyCode()]) {
                keys[e.getKeyCode()] = true;
            }
            else {
                keys[e.getKeyCode()] = true;
                keysDown[e.getKeyCode()] = true;
            }
            keysUp[e.getKeyCode()] = false;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;
            keysDown[e.getKeyCode()] = false;
            keysUp[e.getKeyCode()] = true;
        }
    }

    private class InputMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class InputMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            //float renderScale = window.getSettings().getRenderScale();
            //mousePosition.x = (int)(e.getX() * renderScale + 0.5f);
            //mousePosition.y = (int)(e.getY() * renderScale + 0.5f);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //float renderScale = window.getSettings().getRenderScale();
            //mousePosition.x = (int)(e.getX()* renderScale + 0.5f);
            //mousePosition.y = (int)(e.getY()* renderScale + 0.5f);
        }
    }

    private class InputMouseWheelListener implements  MouseWheelListener  {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }
    }
}
