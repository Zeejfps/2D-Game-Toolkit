package test;

import gametoolkit.engine.*;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;
import gametoolkit.utils.AssetLoader;
import tiled.core.TMXMap;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGame extends Game {

    private static Random rand = new Random(System.nanoTime());

    Sprite s;
    Sprite snake;
    Sprite[] sprites;
    Bitmap bitmap;
    Font font;

    Sprite[] env;

    int[][] map = {
            {6,  6,  6,  6,  6,  6,  6,  6,  6},
            {6, 13, 13, 13, 13, 13, 13, 13,  6},
            {6, 13, 13, 13, 13, 13, 13, 13,  6},
            {6, 13, 13, 13, 13, 13, 13, 13,  6},
            {6, 13, 13, 13, 13, 13, 13, 13,  6},
            {6,  3,  0,  0,  0,  0,  0,  0,  6},
            {6,  6,  6,  6,  6,  6,  6,  6,  6},
    };

    private Window window;
    private Renderer renderer;
    private Camera camera;
    private Input input;
    private Time time;
    long startTime;

    private App app;

    private TMXMap tmxMap;

    @Override
    public void onCreate(App app) {
        this.app = app;
        window = new Window(640, 480, "Test");
        window.enableVSync(true);
        camera = new Camera(9f, 4/3f, 16);
        camera.setClearColor(0x00ffff);
        renderer = new Renderer(window, camera);
        input = new Input(window);
        time = new Time();

        font = AssetLoader.loadBitmapFont("fonts/Roboto.fnt");
        bitmap = AssetLoader.loadBitmap("iso.png");
        s = AssetLoader.loadSprite("iso.png");
        s.pivot.set(0.5f, 0.5f);
        snake = AssetLoader.loadSprite("Snake/head.png");
        sprites = AssetLoader.loadSpriteSheet("swordsman.png", 72, 72);
        for (Sprite s : sprites) {
            s.pivot.set(0.5f, 0.7f);
        }
        tmxMap = AssetLoader.loadMap("res/testmap.tmx");
        env = AssetLoader.loadSpriteSheet("BoxterEnviroment.png", 16, 16);
        time.start();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        time.tick();
        t += time.deltaTime();
        if (t >= 125) {
            i += 5;
            if (i > 24) {
                i = 4;
            }
            t = 0;
        }
        if (window.shouldClose()) {
            app.exit();
        }
    }

    double t;
    boolean ss;
    @Override
    public void fixedUpdate() {
        y -= 0.05f;
        if (input.getKeyDown(KeyEvent.VK_A)) {
            camera.position.x -= 1f;
        }
        else if (input.getKeyDown(KeyEvent.VK_D)) {
            camera.position.x += 1f;
        }

        if (input.getKeyDown(KeyEvent.VK_W)) {
            camera.position.y += 1f;
        }
        else if (input.getKeyDown(KeyEvent.VK_S)) {
            camera.position.y -= 1f;
        }

        if (input.getKeyPressed(KeyEvent.VK_SPACE)) {
            camera.setSize(camera.getSize()+0.1f);
        }
    }

    int fps=0;
    int i = 4;
    float y = 0;
    String fpsStr = "FPS: 666";
    @Override
    public void render() {
        renderer.begin();
        renderer.renderMap(tmxMap);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                renderer.renderSprite(env[map[i][j]], new Vec2f(j, camera.getSize() - i));
            }
        }

        renderer.renderSprite(sprites[i], new Vec2f(0, y));
        renderer.renderString(fpsStr, new Vec2i(0, camera.getFramebuffer().height()),font,0);
        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            fpsStr = "FPS: " + fps;
            fps = 0;
            startTime = System.currentTimeMillis();
        }
        renderer.end();
        window.swapBuffers();
    }

    @Override
    public void onDispose() {
        window.dispose();
    }

    public static void main(String[] args) {
        Config config = new Config();
        config.setFixedUpdateInterval(60);
        App app = new App(new IsoGame(), config);
    }

}
