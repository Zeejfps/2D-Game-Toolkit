package gametoolkit.engine.callbacks;

import gametoolkit.engine.glfw.GLFWWindow;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface ResizeCallback {
    void onResize(GLFWWindow window, int width, int height);
}
