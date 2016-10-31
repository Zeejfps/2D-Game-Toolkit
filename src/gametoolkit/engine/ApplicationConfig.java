package gametoolkit.engine;

/**
 * Created by root on 10/31/16.
 */
public class ApplicationConfig {

    private static final double NS_IN_SC = 1000000000.0;

    boolean vSync = false;
    boolean resizable = true;
    double nsPerUpdate = NS_IN_SC / 60.0;
    int width = 640;
    int height = 480;
    String title = "GLFW Application";

    public void setFixedUpdateInterval(float interval) {
        nsPerUpdate = NS_IN_SC / interval;
    }

    public void setApplicationSize(int wdith, int height) {
        this.width = wdith;
        this.height = height;
    }

    public void setApplicationTitle(String title) {
        this.title = title;
    }

    public void enableVSync(boolean enable) {
        vSync = enable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }
}
