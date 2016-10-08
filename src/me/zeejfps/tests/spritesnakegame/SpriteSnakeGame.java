package me.zeejfps.tests.spritesnakegame;

import me.zeejfps.gametoolkit.engine.Bitmap;
import me.zeejfps.gametoolkit.engine.Game;
import me.zeejfps.gametoolkit.engine.Input;
import me.zeejfps.gametoolkit.math.Vec2i;
import me.zeejfps.gametoolkit.rendering.BasicRenderer;
import me.zeejfps.gametoolkit.rendering.Transparency;

import java.awt.event.KeyEvent;

/**
 * Created by Zeejfps on 10/6/2016.
 */
public class SpriteSnakeGame extends Game {

    private Vec2i mapSize;
    private BasicRenderer renderer;

    public SpriteSnakeGame() {
        renderer = new BasicRenderer(screen);
        window.setTitle("Sprite Snake Game");
        mapSize = new Vec2i(20, 20);
        screen.setSize(mapSize.x * 10, mapSize.y * 10);
    }

    Sprite snake;
    @Override
    protected void onInit() {
        snake = new Sprite(ResourceLoader.loadBitmap("Snake/head.png"));
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onUpdate() {
        if (input.getKeyDown(KeyEvent.VK_A)) {
            rotation += 15;
        }
        else
        if (input.getKeyDown(KeyEvent.VK_D)) {
            snake.rot90CW();
        }
    }

    @Override
    protected void onFixedUpdate() {

    }

    float rotation = 0;
    int fps = 0;
    long startTime;
    @Override
    protected void onRender() {
        screen.clear(0x00A3A3);
        //renderer.blit(20, 20, snake.getBitmap(), Transparency.FAST_TRANSPARENCY);
        //drawBounds();
        snake.setRotation(rotation, renderer);
        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            window.setTitle("Snake Game | FPS: " + fps);
            fps = 0;
            startTime = System.currentTimeMillis();
        }
    }

    public Vec2i getMapSize() {
        return new Vec2i(mapSize);
    }

    Bitmap wall = new Bitmap(10, 10, 0x0000ff);
    private void drawBounds() {
        for (int i = 0; i < mapSize.y; i++) {
            for (int j = 0; j < mapSize.x; j++) {
                if (i == 0 || i == mapSize.y-1 ||
                        j == 0 || j == mapSize.x-1) {
                    renderer.blit(j*10, i*10, wall, Transparency.NO_TRANSPARENCY);
                }
            }
        }
    }

    public static void main(String[] args) {
        SpriteSnakeGame game = new SpriteSnakeGame();
        game.launch();
    }

}
