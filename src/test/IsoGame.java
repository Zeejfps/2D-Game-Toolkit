package test;

import gametoolkit.engine.*;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;
import gametoolkit.utils.AssetLoader;

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

    public IsoGame() {
        display = new Display(
                640, 480,       // Display size
                "Hello",        // Display title
                new Display.Hint(GLFW_VISIBLE, GLFW_FALSE)
        );
        camera = new Camera(
                6f, 4/3f,       // Size and Aspect
                16,             // Pixels Per Unit
                display
        );
        input = new Input(display);

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

    long startTime;
    @Override
    protected void onInit() {
        display.show();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onUpdate() {
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
    protected void onRender() {
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
    }

    public static void main(String[] args) {
        new IsoGame().launch();
    }

}
