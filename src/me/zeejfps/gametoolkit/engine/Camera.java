package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.math.Vec2f;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class Camera {

    public final Vec2f position;

    private final Game game;
    private int size = 3;
    private float aspect = 16f/9f;

    private BufferedImage buffer;
    private int[] pixels;
    private int clearColor;

    public Camera(Game game) {
        this.game = game;
        position = new Vec2f();
        resize();
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
        resize();
    }

    public void setSize(int size) {
        this.size = size;
        resize();
    }

    public float getAspect() {
        return aspect;
    }

    public void clear() {
        Arrays.fill(pixels, clearColor);
    }

    public void setClearColor(int color) {
        clearColor = color;
    }

    public int getClearColor() {
        return clearColor;
    }

    public void setPixel(int x, int y) {
        pixels[y*buffer.getWidth()+x] = 0xff00ff;
    }

    private void resize() {
        int height = 2*size*game.getPixelsPerUnit();
        int width = (int)(height*aspect);
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
    }

    BufferedImage getBuffer() {
        return buffer;
    }
}
