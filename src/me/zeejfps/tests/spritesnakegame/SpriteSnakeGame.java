package me.zeejfps.tests.spritesnakegame;

import me.zeejfps.gametoolkit.engine.Game;
import me.zeejfps.gametoolkit.math.Vec2i;

/**
 * Created by Zeejfps on 10/6/2016.
 */
public class SpriteSnakeGame extends Game {

    private Vec2i mapSize;

    private Snake snake;
    private SnakeRenderer snakeRenderer;

    public SpriteSnakeGame() {

        display.setWindowTitle("Sprite Snake Game");
        display.setFramebufferSize(320, 320);

        mapSize = new Vec2i(21, 21);

        snake = new Snake();
        snakeRenderer = new SnakeRenderer(this);
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected void onUpdate() {

    }

    @Override
    protected void onFixedUpdate() {

    }

    @Override
    protected void onRender() {
        display.clear(0x438bed);
        snakeRenderer.render(snake);
    }

    public Vec2i getMapSize() {
        return new Vec2i(mapSize);
    }

    public static void main(String[] args) {
        SpriteSnakeGame game = new SpriteSnakeGame();
        game.launch();
    }

}
