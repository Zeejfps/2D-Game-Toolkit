package gametoolkit.engine;

import gametoolkit.engine.App;

/**
 * Created by root on 10/31/16.
 */
public abstract class Game {
    protected abstract void onCreate(App app);
    protected abstract void update();
    protected abstract void fixedUpdate();
    protected abstract void render();
    protected abstract void onDispose();
}
