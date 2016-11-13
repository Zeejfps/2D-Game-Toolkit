package gametoolkit.engine.backend.callback;

import gametoolkit.engine.backend.glfwWindow;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface SizeCallback {
    void onResize(glfwWindow window, int width, int height);
}
