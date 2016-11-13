package gametoolkit.engine;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public abstract class Scene {

    private boolean loaded;

    protected Camera mainCamera;

    public Scene() {
        mainCamera = new Camera(3, 16f/9, 32);
    }

    void load() {
        onLoad();
        loaded = true;
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
        mainCamera.dispose();
        loaded = false;
    }

    public boolean isLoaded() {
        return loaded;
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
