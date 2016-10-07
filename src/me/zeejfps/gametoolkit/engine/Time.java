package me.zeejfps.gametoolkit.engine;

/**
 * Created by Zeejfps on 8/10/2016.
 */
public class Time {

    public static final double NS_IN_MS = 1000000.0;
    public static final double NS_IN_SC = 1000000000.0;

    private double startTime;
    private double deltaTime;

    protected Time() {}

    protected void start() {
        startTime = System.nanoTime();
    }

    protected void tick() {
        deltaTime = (System.nanoTime() - startTime) / NS_IN_MS;
        startTime = System.nanoTime();
    }

    public double deltaTime() {
        return deltaTime;
    }

}
