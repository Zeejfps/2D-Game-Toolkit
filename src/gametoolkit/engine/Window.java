package gametoolkit.engine;

import gametoolkit.engine.listeners.InputListener;
import gametoolkit.engine.listeners.SizeCallback;
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
public class Window implements Disposable {

    // Callback lists
    private final List<SizeCallback> sizeCallbacks = new ArrayList<>();
    private final List<InputListener> inputListeners = new ArrayList<>();

    private int width;
    private int height;
    private final long window;
    private final GLCapabilities capabilities;

    private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
            for (InputListener c : inputListeners) {
                c.onCursorMove(Window.this, x, y);
            }
        }
    };

    private GLFWWindowFocusCallback focusCallback = new GLFWWindowFocusCallback() {
        @Override
        public void invoke(long window, boolean focused) {
            if (focused) {
                glfwMakeContextCurrent(window);
                GL.setCapabilities(capabilities);
            }
        }
    };

    private GLFWWindowCloseCallback closeCallback = new GLFWWindowCloseCallback() {
        @Override
        public void invoke(long window) {

        }
    };

    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            for (InputListener c : inputListeners) {
                c.onKey(Window.this, key, scancode, action, mods);
            }
        }
    };

    private GLFWWindowSizeCallback sizeCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int width, int height) {
            Window.this.width = width;
            Window.this.height = height;
            for (SizeCallback c : sizeCallbacks) {
                c.onResize(Window.this, width, height);
            }
        }
    };

    public Window(int width, int height, String title, Hint... hints) {
        this.width = width;
        this.height = height;

        // Setup window hints
        for (Hint hint : hints) {
            glfwWindowHint(hint.hint, hint.value);
        }

        // Create the backend window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // Center the window
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Create the capabilities
        glfwMakeContextCurrent(window);
        capabilities = GL.createCapabilities();

        glfwSetWindowFocusCallback(window, focusCallback);
        glfwSetWindowSizeCallback(window, sizeCallback);
        glfwSetKeyCallback(window, keyCallback);
        glfwSetWindowCloseCallback(window, closeCallback);
        glfwSetCursorPosCallback(window, cursorPosCallback);
    }

    public static class Hint {
        private final int hint, value;

        public Hint(int hint, int value) {
            this.hint = hint;
            this.value = value;
        }
    }

    public void enableVSync(boolean enable) {
        glfwSwapInterval(enable ? 1 : 0);
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            glfwShowWindow(window);
        } else {
            glfwHideWindow(window);
        }
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void dispose() {
        glfwDestroyWindow(window);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(width, width, height);
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public void addSizeCallback(SizeCallback callback) {
        sizeCallbacks.add(callback);
    }

    public void removeSizeCallback(SizeCallback callback) {
        sizeCallbacks.remove(callback);
    }

    public void addInputListener(InputListener callback) {
        inputListeners.add(callback);
    }

    public void removeInputListener(InputListener callback) {
        inputListeners.remove(callback);
    }
}
