package me.zeejfps.gametoolkit.engine.callbacks;

import me.zeejfps.gametoolkit.engine.Window;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface KeyCallback {
    void onKey(Window window, int key, int scancode, int action, int mods);
}
