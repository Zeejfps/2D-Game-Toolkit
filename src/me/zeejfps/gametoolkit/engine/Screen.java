package me.zeejfps.gametoolkit.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Created by Zeejfps on 10/7/2016.
 */
public class Screen extends Bitmap {

    private BufferedImage colorBuffer;
    private float aspect;

    public Screen() {
        setSize(320, 240);
    }

    public void setSize(int width, int height) {
        aspect = width / (float)height;
        colorBuffer =  GraphicsEnvironment.getLocalGraphicsEnvironment().
                            getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(
                width,
                height,
                BufferedImage.OPAQUE
        );
        pixels = ((DataBufferInt) colorBuffer.getRaster().getDataBuffer()).getData();
    }

    public void clear(int color) {
        Arrays.fill(pixels, color);
    }

    public float getAspectRatio() {
        return aspect;
    }

    public int getWidth() {
        return colorBuffer.getWidth();
    }

    public int getHeight() {
        return colorBuffer.getHeight();
    }

    public int[] pixels() {
        return pixels;
    }

    protected BufferedImage getColorBuffer() {
        return colorBuffer;
    }

}
