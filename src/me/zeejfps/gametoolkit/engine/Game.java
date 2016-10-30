package me.zeejfps.gametoolkit.engine;

/**
 * Created by Zeejfps on 6/16/2016.
 */
public abstract class Game {

    public final Window window;
    public final Camera camera;
    public final Input input;
    public final Time time;

    private int pixelsPerUnit;
    private double nsPerUpdate;
    private volatile boolean running;

    public Game() {
        window = new Window();
        camera = new Camera(this);
        input = new Input(this);
        time = new Time();

        setPixeslPerUnit(32);
        setFixedUpdateInterval(60);
    }

    public void launch() {
        if (running) return;
        running = true;
        new Thread(() -> {
            loop();
        }).start();
    }

    public void setFixedUpdateInterval(int ups) {
        nsPerUpdate = Time.NS_IN_SC / ups;
    }

    public void setPixeslPerUnit(int pixeslPerUnit) {
        this.pixelsPerUnit = pixeslPerUnit;
    }

    public int getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    private void init() {
        window.init();
        camera.init();
        input.init();
        onInit();
    }

    private void loop() {
        init();

        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();

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

    private void update() {
        onUpdate();
    }

    private void fixedUpdate() {
        onFixedUpdate();
    }

    private void render() {
        camera.clear();
        onRender();
        camera.render();
        window.swapBuffers();
    }

    protected abstract void onInit();

    protected abstract void onUpdate();

    protected abstract void onFixedUpdate();

    protected abstract void onRender();

}
