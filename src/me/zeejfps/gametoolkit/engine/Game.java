package me.zeejfps.gametoolkit.engine;

/**
 * Created by Zeejfps on 6/16/2016.
 */
public abstract class Game {

    public final Time time;

    private double nsPerUpdate;
    private volatile boolean running;

    public Game() {
        time = new Time();

        setFixedUpdateInterval(60);
    }

    public void launch() {
        if (running) return;
        running = true;

        init();
        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();

        time.init();
        while(running) {
            current = System.nanoTime();
            elapsed = current - previous;
            time.tick();

            previous = current;
            lag += elapsed;

            while (lag >= nsPerUpdate && skippedFrames < maxFramesToSkip) {
                fixedUpdate();
                lag -= nsPerUpdate;
                skippedFrames++;
            }
            skippedFrames = 0;

            update();
            render();
        }
    }

    public void setFixedUpdateInterval(int ups) {
        nsPerUpdate = Time.NS_IN_SC / ups;
    }

    private void init() {
        onInit();
    }

    private void update() {
        onUpdate();
    }

    private void fixedUpdate() {
        onFixedUpdate();
    }

    private void render() {
        onRender();
    }

    protected abstract void onInit();

    protected abstract void onUpdate();

    protected abstract void onFixedUpdate();

    protected abstract void onRender();

}
