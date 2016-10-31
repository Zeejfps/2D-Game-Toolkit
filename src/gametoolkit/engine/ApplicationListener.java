package gametoolkit.engine;

/**
 * Created by root on 10/31/16.
 */
public interface ApplicationListener {
    void onCreate(GLFWApplication app);
    void init();
    void update();
    void fixedUpdate();
    void render();
}
