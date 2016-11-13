package gametoolkit.engine;

import gametoolkit.engine.backend.GlfwInputHandler;
import gametoolkit.math.Vec2f;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public class Input {

    private static GlfwInputHandler handler;
    private static Vec2f mousePos = new Vec2f();

    static void init(GlfwInputHandler handler) {
        Input.handler = handler;
    }

    public static boolean getKeyDown(int key) {
        return handler.getKeyDown(key);
    }

    public static boolean getKeyPressed(int key) {
        return handler.getKeyPressed(key);
    }

    public static boolean getKeyReleased(int key) {
        return handler.getKeyReleased(key);
    }

    public static Vec2f getMousePos() {
        mousePos.x = (float)handler.mouseX;
        mousePos.y = (float)handler.mouseY;
        return mousePos;
    }

}
