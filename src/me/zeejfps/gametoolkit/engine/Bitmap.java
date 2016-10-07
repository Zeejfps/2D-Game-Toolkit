package me.zeejfps.gametoolkit.engine;

import java.util.Arrays;

/**
 * Created by Zeejfps on 7/30/2016.
 */
public class Bitmap {

    public final int width;
    public final int height;
    public final int[] pixels;

    public Bitmap(Bitmap copy){
        this(copy.width, copy.height, Arrays.copyOf(copy.pixels, copy.pixels.length));
    }

    public Bitmap(int width, int height) {
        this(width, height, 0xff000000);
    }

    public Bitmap(int width, int height, int color) {
        this(width, height, new int[width*height]);
        Arrays.fill(pixels, color);
    }

    public Bitmap(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] pixels() {
        return pixels;
    }

    @Override
    public String toString() {
        return width + " x " + height + " Bitmap";
    }
}
