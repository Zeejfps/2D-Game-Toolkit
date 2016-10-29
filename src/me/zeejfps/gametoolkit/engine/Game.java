package me.zeejfps.gametoolkit.engine;

/**
 * Created by Zeejfps on 6/16/2016.
 */
public abstract class Game {

    public final Time time;
    public final Input input;
    public final Display display;
    public final Camera camera;

    private double nsPerUpdate;
    private int pixelsPerUnit;
    private volatile boolean running;

    public Game(int pixelsPerUnit) {
        this.pixelsPerUnit = pixelsPerUnit;
        camera = new Camera(this);
        display = new Display(camera);
        input = new Input(this);
        time = new Time();
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

    public int getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    private void init() {
        onInit();
        display.setVisible(true);
    }

    private void loop() {

        init();

        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();
        time.start();
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

    private void update() {
        onUpdate();
    }

    private void fixedUpdate() {
        onFixedUpdate();
    }

    private void render() {
        camera.clear();
        onRender();
        display.updateFramebuffer();
    }

    protected abstract void onInit();

    protected abstract void onUpdate();

    protected abstract void onFixedUpdate();

    protected abstract void onRender();

}
