package gametoolkit.engine;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public abstract class Scene {

    protected Camera mainCamera;

    public final Game game;

    public Scene(Game game) {
        this.game = game;
        mainCamera = new Camera(3, 16f/9, 32);
    }

    void load() {
        onLoad();
    }

    void update() {
        onUpdate();
    }

    void render() {
        mainCamera.clear();
        onRender();
        Display.swapBuffers(mainCamera.getFramebuffer());
    }

    void fixedUpdate() {
        onFixedUpdate();
    }

    void unload() {
        onUnload();
    }

    public void setCamera(Camera camera) {
        this.mainCamera = camera;
    }

    public Camera getCamera(){
        return mainCamera;
    }

    protected abstract void onLoad();
    protected abstract void onStart();
    protected abstract void onUpdate();
    protected abstract void onFixedUpdate();
    protected abstract void onRender();
    protected abstract void onUnload();
}
