package test;

import gametoolkit.engine.*;
import gametoolkit.engine.renderers.SpriteRenderer;
import gametoolkit.engine.renderers.TextRenderer;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;
import gametoolkit.utils.AssetLoader;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class IsoGameMainScene extends Scene {

    private static Random rand = new Random(System.nanoTime());

    private TextRenderer textRenderer;
    private SpriteRenderer spriteRenderer;
    private Font font;
    private Sprite sprite;
    private Bitmap bitmap;

    public IsoGameMainScene() {
        mainCamera.setSize(4);
        mainCamera.setPixelsPerUnit(32);
        mainCamera.setClearColor(0xff00ff);
        textRenderer = new TextRenderer(mainCamera.getFramebuffer());
        spriteRenderer = new SpriteRenderer(mainCamera);
    }

    @Override
    protected void onLoad() {
        font = AssetLoader.loadBitmapFont("fonts/Roboto.fnt");
        sprite = AssetLoader.loadSprite("test.png");
        bitmap = AssetLoader.loadBitmap("test.png");
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onUpdate() {
        if (Input.getKeyPressed(KeyEvent.VK_E)) {
            Game.loadScene(new TestScene());
        }

        if (Display.shouldClose() || Input.getKeyPressed(KeyEvent.VK_A)) {
            Game.exit();
        }
    }

    @Override
    protected void onFixedUpdate() {
        angle += 0.1f;
    }

    float angle = 0f;
    @Override
    protected void onRender() {
        textRenderer.renderString("Test String?", 0, 100, font, 0);
        //spriteRenderer.renderSprite(sprite, new Vec2f(0, 0));
        spriteRenderer.renderBitmap(bitmap, 0, 0, angle);
    }

    @Override
    protected void onUnload() {

    }

    public static void main(String[] args) {
        Config config = new Config();
        config.setFixedUpdateInterval(60);
        config.setWindowSize(640, 480);
        config.setWindowTitle("Iso Game");

        Game.init(config);
        Game.launch(new IsoGameMainScene());
    }

}
