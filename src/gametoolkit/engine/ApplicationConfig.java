package gametoolkit.engine;

/**
 * Created by root on 10/31/16.
 */
public class ApplicationConfig {

    public static final double NS_IN_SC = 1000000000.0;

    boolean vSync;
    double nsPerUpdate;

    public void enableVSync(boolean enable) {
        vSync = enable;
    }

    public void setFixedUpdateInterval(float interval) {
        nsPerUpdate = NS_IN_SC / interval;
    }
}
