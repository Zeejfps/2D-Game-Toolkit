package me.zeejfps.tests.iso;

import me.zeejfps.gametoolkit.engine.*;
import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;
import me.zeejfps.gametoolkit.utils.AssetLoader;

import java.awt.event.KeyEvent;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGame extends Game {

    private static Random rand = new Random(System.nanoTime());

    Sprite s;
    Sprite snake;
    Sprite[] sprites;
    Bitmap bitmap;
    BitmapFont font;

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

    private Window window;
    private Camera camera;
    private InputHandler input;

    public IsoGame() {
        window = new Window(
                640, 480,       // Window size
                "Hello",        // Window title
                new Window.Hint(GLFW_VISIBLE, GLFW_FALSE)
        );
        camera = new Camera(
                4f, 4/3f,       // Size and Aspect
                16,             // Pixels Per Unit
                window
        );
        input = new InputHandler(window);

        font = AssetLoader.loadBitmapFont("fonts/Roboto.fnt");
        bitmap = AssetLoader.loadBitmap("iso.png");
        s = AssetLoader.loadSprite("iso.png");
        snake = AssetLoader.loadSprite("Snake/head.png");
        s.pivot.set(0.5f, 0.5f);
        sprites = AssetLoader.loadSpriteSheet("swordsman.png", 72, 72);
        for (Sprite s : sprites) {
            s.pivot.set(0.5f, 0.7f);
        }
        env = AssetLoader.loadSpriteSheet("BoxterEnviroment.png", 16, 16);
    }

    long startTime;
    @Override
    protected void onInit() {
        window.show();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onUpdate() {
        input.pollEvents();
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
    protected void onFixedUpdate() {
        y -= time.deltaTime() * 0.001f;
        if (input.getKeyDown(KeyEvent.VK_A)) {
            camera.position.x -= time.deltaTime() * 0.1f;
        }
        else if (input.getKeyDown(KeyEvent.VK_D)) {
            camera.position.x += time.deltaTime() * 0.1f;
        }

        if (input.getKeyDown(KeyEvent.VK_W)) {
            camera.position.y += time.deltaTime() * 0.1f;
        }
        else if (input.getKeyDown(KeyEvent.VK_S)) {
            camera.position.y -= time.deltaTime()  * 0.1f;
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
    protected void onRender() {
        camera.clear(0x00ffff);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                camera.renderSprite(env[map[i][j]], new Vec2f(j, camera.getSize() - i));
            }
        }

        camera.renderString(fpsStr,new Vec2i(0, camera.getFramebufferHeight()),font,0);
        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            fpsStr = "FPS: " + fps;
            fps = 0;
            startTime = System.currentTimeMillis();
        }

        camera.render();
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new IsoGame().launch();
    }

}
