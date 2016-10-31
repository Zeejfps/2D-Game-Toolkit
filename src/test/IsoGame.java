package test;

import gametoolkit.engine.*;
import gametoolkit.engine.glfw.ApplicationConfig;
import gametoolkit.engine.glfw.ApplicationListener;
import gametoolkit.engine.glfw.GLFWApplication;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;
import gametoolkit.utils.AssetLoader;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGame implements ApplicationListener {

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
            {6,  0,  0,  0,  0,  0,  0,  0,  6},
            {6,  6,  6,  6,  6,  6,  6,  6,  6},
    };

    private Camera camera;
    private Input input;
    private Time time;
    long startTime;

    @Override
    public void onCreate(GLFWApplication app) {
        camera = new Camera(
                6f, 4/3f,       // Size and Aspect
                16,             // Pixels Per Unit
                app.window
        );
        input = new Input(app.window);
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
        env = AssetLoader.loadSpriteSheet("BoxterEnviroment.png", 16, 16);
    }

    @Override
    public void init() {
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
        camera.clear(0x00ffff);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                camera.renderSprite(env[map[i][j]], new Vec2f(j, camera.getSize() - i));
            }
        }

        camera.renderSprite(sprites[i], new Vec2f(0, y));
        camera.renderString(fpsStr,new Vec2i(0, camera.getFramebufferHeight()),font,0);
        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            fpsStr = "FPS: " + fps;
            fps = 0;
            startTime = System.currentTimeMillis();
        }
        camera.render();
    }

    public static void main(String[] args) {
        ApplicationConfig config = new ApplicationConfig();
        config.setFixedUpdateInterval(60);
        config.setApplicationSize(640, 480);
        config.setApplicationTitle("Test Game");
        config.setResizable(true);
        config.enableVSync(false);
        GLFWApplication app = new GLFWApplication(new IsoGame(), config);
        app.launch();
    }

}
