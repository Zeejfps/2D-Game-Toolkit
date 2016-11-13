package gametoolkit.engine;

import gametoolkit.engine.backend.GlfwInputHandler;
import gametoolkit.engine.backend.GlfwWindow;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by root on 10/31/16.
 */
public class Game {

    private Game() {}

    private static Scene currScene;
    private static double nsPerUpdate;

    public static void init(Config config) {
        nsPerUpdate = config.nsPerUpdate;

        // Initialize backend
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GlfwWindow window = new GlfwWindow(config.windowWidth, config.windowHeight, config.windowTitle);
        GlfwInputHandler input = new GlfwInputHandler(window);
        Display.init(window);
        Input.init(input);

        glfwSwapInterval(config.vSync ? 1 : 0);
    }

    private static void loop(Scene scene) {
        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();
        while(scene.isLoaded()) {
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

    public static void loadScene(Scene scene) {
        if (currScene != null) {
            currScene.unload();
        }
        currScene = scene;
        currScene.load();
        loop(currScene);
    }

    public static void launch(Scene scene) {
        try {
            loadScene(scene);
        }
        finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    public static void exit() {
        if (currScene != null) {
            currScene.unload();
        }
    }
}
