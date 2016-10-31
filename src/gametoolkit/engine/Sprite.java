package gametoolkit.engine;

import gametoolkit.math.Vec2f;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class Sprite {

    public final Vec2f pivot;
    public Bitmap bitmap;

    public Sprite(Bitmap bitmap) {
        this(bitmap, new Vec2f());
    }

    public Sprite(Bitmap bitmap, Vec2f pivot) {
        this.bitmap = bitmap;
        this.pivot = pivot;
    }

}
