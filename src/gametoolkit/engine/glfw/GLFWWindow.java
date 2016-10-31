package gametoolkit.engine.glfw;

import gametoolkit.engine.callbacks.KeyCallback;
import gametoolkit.engine.callbacks.ResizeCallback;
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
public final class GLFWWindow {

    private List<ResizeCallback> resizeCallbacks = new ArrayList<>();
    private List<KeyCallback> keyCallbacks = new ArrayList<>();

    private final long window;
    private final GLCapabilities capabilities;

    private GLFWWindowFocusCallback focusCallback = new GLFWWindowFocusCallback() {
        @Override
        public void invoke(long window, boolean focused) {
            if (focused) {
                glfwMakeContextCurrent(window);
                GL.setCapabilities(capabilities);
            }
        }
    };

    public GLFWWindow(int width, int height, String title, Hint... hints) {
        // Setup window hints
        for (Hint hint : hints) {
            glfwWindowHint(hint.hint, hint.value);
        }

        // Create the glfw window
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

        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                for (ResizeCallback c : resizeCallbacks) {
                    c.onResize(GLFWWindow.this, width, height);
                }
            }
        });
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                for (KeyCallback c : keyCallbacks) {
                    c.onKey(GLFWWindow.this, key, scancode, action, mods);
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

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            glfwShowWindow(window);
        }
        else {
            glfwHideWindow(window);
        }
    }

    public void setVSync(boolean vSync) {
        glfwSwapInterval(vSync ? 1 : 0);
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
