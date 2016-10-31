package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.engine.callbacks.KeyCallback;
import me.zeejfps.gametoolkit.engine.callbacks.ResizeCallback;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public final class Display {

    private static boolean GLFW_INITIALIZED;

    private List<ResizeCallback> resizeCallbacks = new ArrayList<>();
    private List<KeyCallback> keyCallbacks = new ArrayList<>();

    private final long window;
    private GLCapabilities capabilities = null;

    public Display(int width, int height, String title, Hint... hints) {
        // Initialize glfw
        if (!GLFW_INITIALIZED) {
            GLFWErrorCallback.createPrint(System.err).set();
            if (!glfwInit()) {
                throw new IllegalStateException("Unable to initialize GLFW");
            }
            GLFW_INITIALIZED = true;
        }

        // Setup display hints
        for (Hint hint : hints) {
            glfwWindowHint(hint.hint, hint.value);
        }

        // Create the glfw display
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW display");
        }

        // Center the display
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Create the capabilities
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        capabilities = GL.createCapabilities();

        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                for (ResizeCallback c : resizeCallbacks) {
                    c.onResize(Display.this, width, height);
                }
            }
        });
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                for (KeyCallback c : keyCallbacks) {
                    c.onKey(Display.this, key, scancode, action, mods);
                }
            }
        });
        glfwSetWindowCloseCallback(window, new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long l) {
                close();
                System.exit(0);
            }
        });
    }

    public static class Hint {
        private final int hint, value;
        public Hint(int hint, int value) {
            this.hint = hint;
            this.value = value;
        }
    }

    public void show() {
        glfwMakeContextCurrent(window);
        GL.setCapabilities(capabilities);
        glfwShowWindow(window);
    }

    public void swapBuffers(Camera camera) {
        camera.render();
        glfwSwapBuffers(window);
    }

    public void close() {
        glfwHideWindow(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        GLFW_INITIALIZED = false;
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(width, width, height);
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public void addResizeCallback(ResizeCallback callback) {
        resizeCallbacks.add(callback);
    }

    public void removeResizeCallback(ResizeCallback callback) {
        resizeCallbacks.remove(callback);
    }

    public void addKeyCallback(KeyCallback callback) {
        keyCallbacks.add(callback);
    }

    public void removeKeyCallback(KeyCallback callback) {
        keyCallbacks.remove(callback);
    }
}
