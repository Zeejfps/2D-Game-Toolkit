package gametoolkit.engine.callbacks;

import gametoolkit.engine.Display;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface KeyCallback {
    void onKey(Display display, int key, int scancode, int action, int mods);
}
