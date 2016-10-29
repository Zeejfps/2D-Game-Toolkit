package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;

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
    private float aspect = 4f/3f;

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

    public int getSize() {
        return size;
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

    public void setPixel(int x, int y, int color) {
        if (x > 0 && x < buffer.getWidth() && y > 0 && y < buffer.getHeight())
            pixels[y*buffer.getWidth()+x] = color;
    }

    public void render(Sprite sprite, Vec2f position) {
        int xoffset = (int)(sprite.pivot.x * sprite.width);
        int yoffset = (int)(sprite.pivot.y * sprite.height);
        Vec2i pos = worldToScreenPos(position);
        for (int y = 0; y < sprite.height; y++) {
            for (int x = 0; x < sprite.width; x++) {
                int srcPix = sprite.pixels[x+y*sprite.width];
                if ((0xff000000 & srcPix) != 0)
                    pixels[(pos.x+x-xoffset) + (pos.y+y-yoffset) * buffer.getWidth()] = srcPix;
            }
        }
    }

    public Vec2i worldToScreenPos(Vec2f pos) {
        int x = (int)(buffer.getWidth() * 0.5f + pos.x*game.getPixelsPerUnit() + 0.5f - position.x);
        int y = (int)(buffer.getHeight() * 0.5f - pos.y*game.getPixelsPerUnit() + 0.5f + position.y);
        return new Vec2i(x, y);
    }

    private void resize() {
        int height = 2*size*game.getPixelsPerUnit();
        int width = (int)(height*aspect);

        System.out.println(width + "x" + height);

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
    }

    BufferedImage getBuffer() {
        return buffer;
    }
}
