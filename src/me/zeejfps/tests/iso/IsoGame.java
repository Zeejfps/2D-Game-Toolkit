package me.zeejfps.tests.iso;

import me.zeejfps.gametoolkit.engine.Game;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGame extends Game {

    public IsoGame() {
        super(32);
        display.setTitle("Isometric Test");
        display.setResizable(false);
        camera.setClearColor(0x00ffff);
        camera.setAspect(1f);
        camera.setSize(4);
    }

    long startTime;
    @Override
    protected void onInit() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void onFixedUpdate() {

    }

    int fps=0;
    @Override
    protected void onRender() {
        camera.setPixel(0, 0);
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
