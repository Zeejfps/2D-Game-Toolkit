package gametoolkit.engine.glfw;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by root on 10/31/16.
 */
public class GLFWApplication {

    private static boolean GLFW_INITIALIZED;

    private boolean running;
    private final ApplicationListener appListener;

    private double nsPerUpdate;
    public final GLFWWindow window;

    public GLFWApplication(ApplicationListener appListener, ApplicationConfig config) {
        this.appListener = appListener;
        nsPerUpdate = config.nsPerUpdate;

        // Initialize glfw
        if (!GLFW_INITIALIZED) {
            GLFWErrorCallback.createPrint(System.err).set();
            if (!glfwInit()) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }
            GLFW_INITIALIZED = true;
        }

        GLFWWindow.Hint[] hints = {
                new GLFWWindow.Hint(GLFW_VISIBLE, GLFW_FALSE),
                new GLFWWindow.Hint(GLFW_RESIZABLE, config.resizable ? GLFW_TRUE : GLFW_FALSE)
        };
        window = new GLFWWindow(config.width, config.height, config.title, hints);
        glfwSwapInterval(config.vSync ? 1 : 0);
        appListener.onCreate(this);
    }

    public void launch() {
        if (running) return;
        running = true;

        window.setVisible(true);
        appListener.init();
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
            window.swapBuffers();
        }
    }

    public void exit() {
        running = false;
    }
}
