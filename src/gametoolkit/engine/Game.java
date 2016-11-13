package gametoolkit.engine;

import gametoolkit.engine.backend.glfwInputHandler;
import gametoolkit.engine.backend.glfwWindow;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by root on 10/31/16.
 */
public class Game {

    private boolean running;
    private double nsPerUpdate;

    public Game(Config config) {
        nsPerUpdate = config.nsPerUpdate;

        // Initialize backend
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindow window = new glfwWindow(config.windowWidth, config.windowHeight, config.windowTitle);
        glfwInputHandler input = new glfwInputHandler(window);
        Display.init(window);
        Input.init(input);

        glfwSwapInterval(config.vSync ? 1 : 0);
    }

    private void loop(Scene scene) {
        running = true;
        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();
        while(running) {
            glfwPollEvents();
            current = System.nanoTime();
            elapsed = current - previous;

            previous = current;
            lag += elapsed;

            while (lag >= nsPerUpdate && skippedFrames < maxFramesToSkip) {
                scene.fixedUpdate();
                lag -= nsPerUpdate;
                skippedFrames++;
            }
            skippedFrames = 0;

            scene.update();
            scene.render();
        }
    }

    public void launch(Scene scene) {
        try {
            loop(scene);
        }
        finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    public void exit() {
        running = false;
    }
}
