package gametoolkit.engine;

/**
 * Created by root on 10/31/16.
 */
public interface ApplicationListener {
    void onCreate();
    void init();
    void update();
    void fixedUpdate();
    void render();
}
