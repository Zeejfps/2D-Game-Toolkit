package gametoolkit.engine;

import gametoolkit.engine.ApplicationConfig;
import gametoolkit.engine.ApplicationListener;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

/**
 * Created by root on 10/31/16.
 */
public class GLFWApplication {

    private static boolean GLFW_INITIALIZED;

    private boolean running;
    private final ApplicationListener appListener;
    private final ApplicationConfig config;

    public GLFWApplication(ApplicationListener appListener, ApplicationConfig config) {
        this.appListener = appListener;
        this.config = config;

        // Initialize glfw
        if (!GLFW_INITIALIZED) {
            GLFWErrorCallback.createPrint(System.err).set();
            if (!glfwInit()) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }
            GLFW_INITIALIZED = true;
            appListener.onCreate();
        }

        glfwSwapInterval(config.vSync ? 1 : 0);
    }

    public void launch() {
        if (running) return;;
        running = true;

        appListener.init();
        int maxFramesToSkip = 5;
        int skippedFrames = 0;
        double lag = 0, current, elapsed;
        double previous = System.nanoTime();
        while(running) {
            current = System.nanoTime();
            elapsed = current - previous;

            previous = current;
            lag += elapsed;

            while (lag >= config.nsPerUpdate && skippedFrames < maxFramesToSkip) {
                appListener.fixedUpdate();
                lag -= config.nsPerUpdate;
                skippedFrames++;
            }
            skippedFrames = 0;

            appListener.update();
            appListener.render();
        }
    }
}
