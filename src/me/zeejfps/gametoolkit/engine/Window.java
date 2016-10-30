package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.engine.callbacks.KeyCallback;
import me.zeejfps.gametoolkit.engine.callbacks.ResizeCallback;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public class Window {

    private List<ResizeCallback> resizeCallbacks = new ArrayList<>();
    private List<KeyCallback> keyCallbacks = new ArrayList<>();

    private long window;

    private int width, height;
    private String title;
    private boolean resizable;

    public Window() {
        width = 640;
        height = 480;
        title = "Untitled";
        resizable = true;
    }

    void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                for (ResizeCallback c : resizeCallbacks) {
                    c.onResize(Window.this, width, height);
                }
            }
        });
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                for (KeyCallback c : keyCallbacks) {
                    c.onKey(Window.this, key, scancode, action, mods);
                }
            }
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        GL.createCapabilities();
    }

    void swapBuffers() {
        glfwSwapBuffers(window);
    }

    void resize() {

    }

    public void show() {
        glfwShowWindow(window);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
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
