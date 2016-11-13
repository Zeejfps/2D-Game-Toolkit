package gametoolkit.engine.backend.callback;

import gametoolkit.engine.backend.GlfwWindow;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface SizeCallback {
    void onResize(GlfwWindow window, int width, int height);
}
