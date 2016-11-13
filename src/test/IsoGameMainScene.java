package test;

import gametoolkit.engine.*;
import gametoolkit.engine.renderers.TextRenderer;
import gametoolkit.math.Vec2i;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGameMainScene extends Scene {

    private static Random rand = new Random(System.nanoTime());

    private TextRenderer textRenderer;
    private Font font;

    public IsoGameMainScene(Game game) {
        super(game);
        try {
            font = Font.load("fonts/Roboto.fnt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainCamera.setClearColor(0xff00ff);
        textRenderer = new TextRenderer(font, mainCamera.getFramebuffer());
    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate() {
        if (Display.shouldClose() || Input.getKeyPressed(KeyEvent.VK_A)) {
            game.exit();
        }
    }

    @Override
    protected void onFixedUpdate() {

    }

    @Override
    protected void onRender() {
        textRenderer.renderString("Test", new Vec2i(100, 100), 0);
    }

    @Override
    protected void onUnload() {

    }

    public static void main(String[] args) {
        Config config = new Config();
        config.setFixedUpdateInterval(60);
        config.setWindowSize(640, 480);
        config.setWindowTitle("Iso Game");

        Game game = new Game(config);
        game.launch(new IsoGameMainScene(game));
    }

}
