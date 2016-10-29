package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.math.Vec2f;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class Sprite {

    public final int width, height;
    public final int[] pixels;
    public final Vec2f pivot;

    public Sprite(int width, int height) {
        this(width, height, new int[width*height]);
    }

    public Sprite(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.pivot = new Vec2f();
    }

}
