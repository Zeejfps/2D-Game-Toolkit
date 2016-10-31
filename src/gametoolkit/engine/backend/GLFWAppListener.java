package gametoolkit.engine.backend;

import gametoolkit.engine.Renderer;

/**
 * Created by root on 10/31/16.
 */
public interface GLFWAppListener {
    void onCreate(GLFWApp app);
    void update();
    void fixedUpdate();
    void render();
    void onDispose();
}
