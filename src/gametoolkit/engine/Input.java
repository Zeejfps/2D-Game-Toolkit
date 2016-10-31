package gametoolkit.engine;

import gametoolkit.engine.glfw.GLFWWindow;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Zeejfps on 6/15/2016.
 */
public class Input {

    private static final int MAX_KEYS = 600;

    private boolean[] keysDown = new boolean[MAX_KEYS];
    private boolean[] keysPressed = new boolean[MAX_KEYS];
    private boolean[] keysReleased = new boolean[MAX_KEYS];

    public Input(GLFWWindow window) {
        window.addKeyCallback((w, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW_REPEAT:
                case GLFW_PRESS:
                    keysDown[key] = true;
                    keysPressed[key] = true;
                    keysReleased[key] = false;
                    break;
                case GLFW_RELEASE:
                    keysDown[key] = false;
                    keysPressed[key] = false;
                    keysReleased[key] = true;
                    break;
            }
        });
    }

    public boolean getKeyDown(int keycode) {
        return keysDown[keycode];
    }

    public boolean getKeyPressed(int keycode) {
        boolean keyDown = keysPressed[keycode];
        keysPressed[keycode] = false;
        return keyDown;
    }

    public boolean getKeyReleased(int keycode) {
        boolean keyUp = keysReleased[keycode];
        keysReleased[keycode] = false;
        return keyUp;
    }

}
