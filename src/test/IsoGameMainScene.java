package test;

import gametoolkit.engine.*;

import java.util.Random;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGameMainScene extends Scene {

    private static Random rand = new Random(System.nanoTime());

    public IsoGameMainScene(Game game) {
        super(game);
    }

    @Override
    protected void onLoad() {

    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate() {
        if (Display.shouldClose()) {
            game.exit();
        }
    }

    @Override
    protected void onFixedUpdate() {

    }

    @Override
    protected void onRender() {

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
