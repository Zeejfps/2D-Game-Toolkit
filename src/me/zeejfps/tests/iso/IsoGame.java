package me.zeejfps.tests.iso;

import me.zeejfps.gametoolkit.engine.Bitmap;
import me.zeejfps.gametoolkit.engine.BitmapFont;
import me.zeejfps.gametoolkit.engine.Game;
import me.zeejfps.gametoolkit.engine.Sprite;
import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;
import me.zeejfps.gametoolkit.utils.AssetLoader;

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
    BitmapFont font;

    public IsoGame() {
        setPixeslPerUnit(32);

        window.setSize(640, 480);
        window.setTitle("Test Game");
        window.setResizable(false);

        camera.setClearColor(0x00ffff);
        camera.setAspect(4/3f);
        camera.setSize(4);

        font = AssetLoader.loadBitmapFont("fonts/Roboto.fnt");

        bitmap = AssetLoader.loadBitmap("iso.png");
        s = AssetLoader.loadSprite("iso.png");
        snake = AssetLoader.loadSprite("Snake/head.png");
        s.pivot.set(0.5f, 0.5f);
        sprites = AssetLoader.loadSpriteSheet("swordsman.png", 72, 72);
        for (Sprite s : sprites) {
            s.pivot.set(0.5f, 0.7f);
        }

    }

    long startTime;
    @Override
    protected void onInit() {
        startTime = System.currentTimeMillis();
        window.show();
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
        y -= time.deltaTime() * 0.001f;
    }

    double t;
    boolean ss;
    @Override
    protected void onFixedUpdate() {
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
            camera.setSize(camera.getSize()+1f);
        }


    }

    int fps=0;
    int i = 4;
    float y = 0;
    String fpsStr = "FPS: 666";
    @Override
    protected void onRender() {




        /*camera.renderBitmap(sprites[2], new Vec2f(0, 0));

        Vec2i p = camera.worldToScreenPos(new Vec2f(0, 0));
        camera.setPixel(p.x, p.y, 0xff00ff);
        */

        //camera.renderBitmap(bitmap, new Vec2i(80, 80));
        camera.renderSprite(s, new Vec2f(0, 0));
        camera.renderSprite(s, new Vec2f(0.5f, 0.25f));
        camera.renderSprite(s, new Vec2f(0.5f, -0.25f));
        camera.renderSprite(s, new Vec2f(-0.5f, 0.25f));
        camera.renderSprite(s, new Vec2f(-0.5f, -0.25f));
        camera.renderSprite(s, new Vec2f(-0.5f, -0.75f));
        camera.renderSprite(s, new Vec2f(-1f, 0));
        camera.renderSprite(sprites[3], new Vec2f(0, 0));
        camera.renderSprite(sprites[9], new Vec2f(1, 0));
        camera.renderSprite(sprites[i], new Vec2f(-1, y));
        /*for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                Vec2i p = camera.worldToScreenPos(new Vec2f(j, i));
                camera.renderBitmap(s, new Vec2f(j, i));
                //camera.setPixel(p.x, p.y, 0xff00ffff);
            }
        }*/
        camera.renderString(
                fpsStr,
                new Vec2i(0, camera.getFramebufferHeight()),
                font,
                0
        );

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
