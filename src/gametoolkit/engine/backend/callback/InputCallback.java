package gametoolkit.engine.backend.callback;

import gametoolkit.engine.backend.glfwWindow;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface InputCallback {
    void onCursorMove(glfwWindow window, double x, double y);
    void onKey(glfwWindow window, int key, int scancode, int action, int mods);
}
