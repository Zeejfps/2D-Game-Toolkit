package me.zeejfps.tests.iso;

import me.zeejfps.gametoolkit.engine.Game;
import me.zeejfps.gametoolkit.engine.Sprite;
import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGame extends Game {

    private static Random rand = new Random(System.nanoTime());

    Sprite s;

    public IsoGame() {
        super(32);
        display.setTitle("Isometric Test");
        //display.setResizable(false);
        camera.setClearColor(0);
        camera.setAspect(16/10f);
        camera.setSize(4);
        s = Assets.loadSprite("iso.png");
        s.pivot.set(0.5f, 0.5f);
    }

    long startTime;
    @Override
    protected void onInit() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onUpdate() {
        if (input.getKey(KeyEvent.VK_A)) {
            camera.position.x -= time.deltaTime()*50f;
        }
        else if (input.getKey(KeyEvent.VK_D)) {
            camera.position.x += time.deltaTime()*50f;
        }

        if (input.getKey(KeyEvent.VK_W)) {
            camera.position.y += time.deltaTime()*50f;
        }
        else if (input.getKey(KeyEvent.VK_S)) {
            camera.position.y -= time.deltaTime()*50f;
        }
    }

    @Override
    protected void onFixedUpdate() {

    }

    int fps=0;
    @Override
    protected void onRender() {

        float x = (s.width / 2) + (s.width / 2);
        float y = (s.height / 2) - (s.height / 2);

        System.out.println(x + ", " + y);

        camera.render(s, new Vec2f(0, 0));
        camera.render(s, new Vec2f(1, 0));
        camera.render(s, new Vec2f(0.5f, 0.25f));
        camera.render(s, new Vec2f(0.5f, -0.25f));
        camera.render(s, new Vec2f(-0.5f, 0.25f));
        camera.render(s, new Vec2f(-0.5f, -0.25f));
        camera.render(s, new Vec2f(-1f, 0));

        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                Vec2i p = camera.worldToScreenPos(new Vec2f(j, i));
                //camera.render(s, new Vec2f(j, i));
                camera.setPixel(p.x, p.y, 0xff00ff);
            }
        }

        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            System.out.println(fps);
            fps = 0;
            startTime = System.currentTimeMillis();
        }
    }

    public static void main(String[] args) {
        new IsoGame().launch();
    }

}
