package me.zeejfps.tests.spritesnakegame;

import me.zeejfps.gametoolkit.engine.Bitmap;
import me.zeejfps.gametoolkit.engine.Screen;
import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;
import me.zeejfps.gametoolkit.rendering.BasicRenderer;

/**
 * Created by Zeejfps on 10/7/2016.
 */
public class Sprite {

    private Vec2f position;
    private float rotation;

    private Bitmap original;
    private Bitmap bitmap;

    public Sprite(Bitmap bitmap) {
        this.original = bitmap;
        this.bitmap = bitmap;
    }

    public void rot90CW() {
        Bitmap result = new Bitmap(bitmap.getHeight(), bitmap.getWidth());
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int rotx = result.getWidth()-1-y;
                int roty = x;
                result.pixels()[roty * result.getWidth() + rotx] = bitmap.pixels()[y*bitmap.getWidth()+x];
            }
        }
        bitmap = result;
    }

    public void rot90CCW() {
        Bitmap result = new Bitmap(bitmap.getHeight(), bitmap.getWidth());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int rotx = bitmap.getWidth()-1-y;
                int roty = x;
                result.pixels()[y * result.getWidth() + x] = bitmap.pixels()[roty*bitmap.getWidth()+rotx];
            }
        }
        bitmap = result;
    }

    public void rotate(float degrees, BasicRenderer renderer) {
        setRotation(rotation+degrees, renderer);
    }

    public void setRotation(float degrees, BasicRenderer renderer) {
        rotation = degrees;
        double rads = Math.toRadians(rotation);
        double cos = Math.cos(rads);
        double sin = Math.sin(rads);

        int halfWith = original.getWidth()/2;
        int halfHeight = original.getHeight()/2;

        Vec2i topLeft = new Vec2i(-halfWith, -halfHeight);
        Vec2i topRight = new Vec2i(original.getWidth()-halfWith, -halfHeight);
        Vec2i bottomRight = new Vec2i(original.getWidth()-halfWith, original.getHeight()-halfHeight);
        Vec2i bottomLeft = new Vec2i(-halfWith, original.getHeight()-halfHeight);

        //topLeft.x = (int)(cos * topLeft.x - sin * topLeft.y);
        //topLeft.y = (int)(sin * topLeft.x + cos * topLeft.y);

        //topRight.x = (int)(cos * topRight.x - sin * topRight.y);
        //topRight.y = (int)(sin * topRight.x + cos * topRight.y);

        bottomRight.x = (int)(cos * bottomRight.x - sin * bottomRight.y);
        bottomRight.y = (int)(sin * bottomRight.x + cos * bottomRight.y);

        //bottomLeft.x = (int)(cos * bottomLeft.x - sin * bottomLeft.y);
        //bottomLeft.y = (int)(sin * bottomLeft.x + cos * bottomLeft.y);

        int xMin = Math.min(Math.min(topRight.x, topLeft.x), Math.min(bottomRight.x, bottomLeft.x));
        int yMin = Math.min(Math.min(topRight.y, topLeft.y), Math.min(bottomRight.y, bottomLeft.y));
        int xMax = Math.max(Math.max(topRight.x, topLeft.x), Math.max(bottomRight.x, bottomLeft.x));
        int yMax = Math.max(Math.max(topRight.y, topLeft.y), Math.max(bottomRight.y, bottomLeft.y));

        System.out.println(bottomRight);

        renderer.setPixel(topLeft.x + 40, topLeft.y+ 40, 0xffff00ff);
        renderer.setPixel(topRight.x+ 40, topRight.y+ 40, 0xffff00ff);
        renderer.setPixel(bottomRight.x+ 40, bottomRight.y+ 40, 0xffff00ff);
        renderer.setPixel(bottomLeft.x+ 40, bottomLeft.y+ 40, 0xffff00ff);


        /*Bitmap result = new Bitmap(width, height);
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int rotx = (int)(cos * x - sin * y);
                int roty = (int)(sin * x + cos * y);
                int src = roty*original.getWidth()+rotx;
                if (src >= original.pixels().length) {
                    result.pixels()[y*result.getWidth()+x] = 0;
                }
                else {
                    result.pixels()[y*result.getWidth()+x] = original.pixels()[src];
                }
            }
        }
        bitmap = result;*/
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
