package me.zeejfps.tests.pixelsnakegame;

import me.zeejfps.gametoolkit.engine.Bitmap;
import me.zeejfps.gametoolkit.engine.Game;
import me.zeejfps.gametoolkit.math.Vec2i;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Zeejfps on 10/6/2016.
 */
public class PixelSnakeGame extends Game {

    private static final Random rand = new Random(System.nanoTime());

    private enum Direction {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

        public final int x, y;
        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private Vec2i mapSize;
    private Vec2i applePos;
    private Vec2i head;
    private Queue<Vec2i> snake;

    private Direction targetDir;
    private Direction currentDir;

    private Font font;

    public PixelSnakeGame() {
        mapSize = new Vec2i(21, 21);
        applePos = new Vec2i();
        head = new Vec2i();
        snake = new ArrayDeque<>();

        setFixedUpdateInterval(8);
        display.setWindowSize(640, 480);
        display.setFramebufferSize(mapSize.x, mapSize.y);
        display.setWindowTitle("Snake Game");
    }

    @Override
    protected void onInit() {
        spawnSnake();
        spawnApple();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onUpdate() {
        if ((input.getKeyDown(KeyEvent.VK_A) || input.getKeyDown(KeyEvent.VK_LEFT)) && currentDir != Direction.RIGHT) {
            targetDir = Direction.LEFT;
        }
        else
        if ((input.getKeyDown(KeyEvent.VK_D) || input.getKeyDown(KeyEvent.VK_RIGHT)) && currentDir != Direction.LEFT) {
            targetDir = Direction.RIGHT;
        }
        else
        if ((input.getKeyDown(KeyEvent.VK_W) || input.getKeyDown(KeyEvent.VK_UP)) && currentDir != Direction.DOWN) {
            targetDir = Direction.UP;
        }
        else
        if ((input.getKeyDown(KeyEvent.VK_S) || input.getKeyDown(KeyEvent.VK_DOWN)) && currentDir != Direction.UP) {
            targetDir = Direction.DOWN;
        }
    }

    @Override
    protected void onFixedUpdate() {
        moveSnake();
        checkCollisions();
    }

    int fps = 0;
    long startTime;
    @Override
    protected void onRender() {
        display.clear(0);
        drawApple(display.framebuffer());
        drawSnake(display.framebuffer());
        drawMap(display.framebuffer());
        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            display.setWindowTitle("Snake Game | FPS: " + fps);
            fps = 0;
            startTime = System.currentTimeMillis();
        }
    }

    private void checkCollisions() {

        for (Vec2i v : snake) {
            if (head.equals(v)) {
                spawnSnake();
                return;
            }
        }

        if (applePos.x == head.x && applePos.y == head.y) {
            eatApple();
        }

        if (head.x == 0 || head.x == mapSize.x-1 ||
                head.y == 0 || head.y == mapSize.y-1) {
            spawnSnake();
        }
    }

    private void spawnSnake() {
        snake.clear();
        head = new Vec2i(rand.nextInt(mapSize.x-2)+1, rand.nextInt(mapSize.y-2)+1);
        int r = rand.nextInt(3);
        Direction randDir = Direction.values()[r];
        targetDir = randDir;
        currentDir = randDir;
    }

    private void moveSnake() {

        currentDir = targetDir;

        if (!snake.isEmpty()) {
            Vec2i v = snake.remove();
            v.set(head);
            snake.add(v);
        }

        head.x += currentDir.x;
        head.y += currentDir.y;
    }

    private void eatApple() {
        snake.add(new Vec2i(head.x, head.y));
        spawnApple();
    }

    private void spawnApple() {
        do {
            applePos.set(rand.nextInt(mapSize.x-2)+1, rand.nextInt(mapSize.y-2)+1);
        } while (!validSpawnLocation());
    }

    private boolean validSpawnLocation() {
        for (Vec2i v : snake) {
            if (v.equals(applePos)) {
                return false;
            }
        }
        return !applePos.equals(head);
    }

    private void drawApple(Bitmap bitmap) {
        int[] pixels = bitmap.pixels;
        pixels[applePos.y * bitmap.width + applePos.x] = 0xff0000;
    }

    private void drawSnake(Bitmap bitmap) {
        int[] pixels = bitmap.pixels;

        for (Vec2i v : snake) {
            pixels[v.y * bitmap.width + v.x] = 0xB3ff00;
        }

        pixels[head.y * bitmap.width + head.x] = 0x00ff00;
    }

    private void drawMap(Bitmap bitmap) {
        int[] pixels = bitmap.pixels;
        for (int i = 0; i < mapSize.y; i++) {
            for (int j = 0; j < mapSize.x; j++) {
                if (i == 0 || i == mapSize.y-1) {
                    pixels[i*bitmap.width + j] = 0x0000ff;
                }
                if (j == 0 || j == mapSize.x-1) {
                    pixels[i*bitmap.width + j] = 0x0000ff;
                }
            }
        }
    }

    public static void main(String[] args) {
        PixelSnakeGame game = new PixelSnakeGame();
        game.launch();
    }

}
