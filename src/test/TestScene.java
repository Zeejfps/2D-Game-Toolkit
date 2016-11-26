package test;

import gametoolkit.engine.Font;
import gametoolkit.engine.Scene;
import gametoolkit.engine.renderers.TextRenderer;

import java.io.IOException;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public class TestScene extends Scene {
    TextRenderer r;
    Font f;
    @Override
    protected void onLoad() {
        mainCamera.setClearColor(0xffff00);
        r = new TextRenderer(mainCamera.getFramebuffer());
        try {
            f = Font.load("Roboto.fnt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void onFixedUpdate() {

    }

    @Override
    protected void onRender() {
        r.renderString("Test", 40, 40, null, 0xff00ff);
    }

    @Override
    protected void onUnload() {

    }
}
