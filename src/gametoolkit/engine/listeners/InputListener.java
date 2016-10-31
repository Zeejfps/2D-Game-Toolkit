package gametoolkit.engine.listeners;

import gametoolkit.engine.Window;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface InputListener {
    void onCursorMove(Window window, double x, double y);
    void onKey(Window window, int key, int scancode, int action, int mods);
}
