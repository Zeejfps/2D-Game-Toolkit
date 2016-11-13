package gametoolkit.engine;

import gametoolkit.engine.backend.Framebuffer;
import gametoolkit.engine.interfaces.Disposable;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public final class Camera implements Disposable {

    public final Vec2f position;

    private float size;
    private float aspect;
    private int pixelsPerUnit;
    private int clearColor;

    private Framebuffer framebuffer;

    public Camera(float size, float aspect, int pixelsPerUnit) {
        this.position = new Vec2f();
        setSize(size);
        setAspect(aspect);
        setPixelsPerUnit(pixelsPerUnit);
        int h = (int)(2*this.size*pixelsPerUnit);
        int w = (int)(h*aspect);
        framebuffer = new Framebuffer(w, h);
    }

    public Vec2i worldToScreenPos(Vec2f pos) {
        int x = (int)(framebuffer.width() * 0.5f + pos.x*pixelsPerUnit - position.x);
        int y = (int)(framebuffer.height() * 0.5f + pos.y*pixelsPerUnit - position.y);
        return new Vec2i(x, y);
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public void setPixelsPerUnit(int pixelsPerUnit) {
        this.pixelsPerUnit = pixelsPerUnit > 0 ? pixelsPerUnit : 1;
    }

    public int getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setClearColor(int color) {
        this.clearColor = color;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return aspect;
    }

    @Override
    public void dispose() {
        framebuffer.dispose();
    }

    void clear() {
        framebuffer.clear(clearColor);
    }
}
