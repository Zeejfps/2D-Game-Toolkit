package gametoolkit.engine.backend.callback;

import gametoolkit.engine.backend.GlfwWindow;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface InputCallback {
    void onCursorMove(GlfwWindow window, double x, double y);
    void onKey(GlfwWindow window, int key, int scancode, int action, int mods);
}
