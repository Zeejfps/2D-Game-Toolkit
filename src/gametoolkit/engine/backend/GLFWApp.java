package gametoolkit.engine.backend;

import gametoolkit.engine.Renderer;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by root on 10/31/16.
 */
public class GLFWApp {

    private boolean running;
    private final GLFWAppListener appListener;

    private double nsPerUpdate;

    public GLFWApp(GLFWAppListener appListener, GLFWAppConfig config) {
        this.appListener = appListener;
        nsPerUpdate = config.nsPerUpdate;

        // Initialize backend
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        appListener.onCreate(this);
        try {
            loop();
        }
        finally {
            appListener.onDispose();
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }

    private void loop() {
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
                appListener.fixedUpdate();
                lag -= nsPerUpdate;
                skippedFrames++;
            }
            skippedFrames = 0;

            appListener.update();
            appListener.render();
        }
    }

    public void exit() {
        running = false;
    }
}
