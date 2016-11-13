package gametoolkit.engine;

/**
 * Created by root on 10/31/16.
 */
public class Config {

    private static final double NS_IN_SC = 1000000000.0;

    double nsPerUpdate = NS_IN_SC / 60.0;
    int windowWidth = 640, windowHeight = 480;
    String windowTitle = "Untitled Game";

    public void setFixedUpdateInterval(float interval) {
        nsPerUpdate = NS_IN_SC / interval;
    }

    public void setWindowSize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
    }

    public void setWindowTitle(String title) {
        this.windowTitle = title;
    }
}
