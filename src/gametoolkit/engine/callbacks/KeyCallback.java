package gametoolkit.engine.callbacks;

import gametoolkit.engine.glfw.GLFWWindow;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface KeyCallback {
    void onKey(GLFWWindow window, int key, int scancode, int action, int mods);
}
