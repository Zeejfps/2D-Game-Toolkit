package gametoolkit.engine;

/**
 * Created by root on 10/31/16.
 */
public class Config {

    private static final double NS_IN_SC = 1000000000.0;

    double nsPerUpdate = NS_IN_SC / 60.0;

    public void setFixedUpdateInterval(float interval) {
        nsPerUpdate = NS_IN_SC / interval;
    }
}
