package gametoolkit.engine;

/**
 * Created by Zeejfps on 6/16/2016.
 */
public abstract class Game {

    protected Display display = null;
    protected Camera camera = null;
    protected Input input = null;

    public final Time time;

    private double nsPerUpdate;
    private volatile boolean running;

    public Game() {
        time = new Time();
        setFixedUpdateInterval(60);
    }

    public void launch() {

        if (display == null) {
            throw new IllegalStateException("display was never assigned");
        }
        if (camera == null) {
            throw new IllegalStateException("camera was never assigned");
        }
        if (input == null) {
            throw new IllegalStateException("input was never assigned");
        }

        if (running) return;
        running = true;

        init();
        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();

        time.init();
        while(running) {
            input.pollEvents();

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
        camera.render();
        display.swapBuffers();
    }

    protected abstract void onInit();

    protected abstract void onUpdate();

    protected abstract void onFixedUpdate();

    protected abstract void onRender();

}
