package me.zeejfps.gametoolkit.engine;

/**
 * Created by Zeejfps on 8/10/2016.
 */
public class Time {

    public static final double NS_IN_MS = 1000000.0;
    public static final double NS_IN_SC = 1000000000.0;

    private double startTime;
    private double deltaTime;

    Time() {}

    protected void init() {
        startTime = System.nanoTime();
    }

    protected void tick() {
        deltaTime = (System.nanoTime() - startTime) / NS_IN_SC;
        startTime = System.nanoTime();
    }

    public float deltaTime() {
        return (float)deltaTime;
    }

}
