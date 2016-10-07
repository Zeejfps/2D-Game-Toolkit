package me.zeejfps.gametoolkit;

/**
 * Created by Zeejfps on 6/16/2016.
 */
public abstract class Game {

    public final Display display;
    public final InputHandler input;
    public final Time time;

    private GameConfig config;
    private volatile boolean running;

    public Game(GameConfig config) {
        this.config = config;
        display = new Display(config.displayConfig);
        input = new InputHandler(display.canvas);
        time = new Time();
    }

    public void launch() {
        if (running) return;
        running = true;
        new Thread(() -> {
            loop();
        }).start();
    }

    private void loop() {

        double previous = System.nanoTime();
        double lag = 0, current, elapsed;
        double nsPerUpdate = 1000000000.0 / config.getFixedUpdateInterval();
        int maxFramesToSkip = 5;
        int skippedFrames = 0;

        display.setVisible(true);
        init();
        while(running) {

            current = System.nanoTime();
            elapsed = current - previous;
            time.setDeltaTime((float)(elapsed * 0.000000001f));

            previous = current;
            lag += elapsed;

            while (lag >= nsPerUpdate && skippedFrames < maxFramesToSkip) {
                fixedUpdate();
                lag -= nsPerUpdate;
                skippedFrames++;
            }
            skippedFrames = 0;

            update();
            render(display.getColorBuffer());
        }

    }

    protected abstract void init();

    protected abstract void update();

    protected abstract void fixedUpdate();

    protected abstract void render(Bitmap colorBuffer);

}
