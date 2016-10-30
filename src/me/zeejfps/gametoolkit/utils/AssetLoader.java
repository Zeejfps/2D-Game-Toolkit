package me.zeejfps.gametoolkit.utils;

import me.zeejfps.gametoolkit.engine.Bitmap;
import me.zeejfps.gametoolkit.engine.BitmapFont;
import me.zeejfps.gametoolkit.engine.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public class AssetLoader {

    public static BitmapFont loadBitmapFont(String path) {
        try {
            return BitmapFont.load(path);
        } catch (IOException e) {
            return new BitmapFont(new HashMap<>(), new HashMap<>(), 0);
        }
    }

    public static Bitmap loadBitmap(String path) {
        try {
            return Bitmap.load(path);
        } catch (IOException e) {
            System.err.println("Failed to load bitmap");
            e.printStackTrace();
            return new Bitmap(0, 0, new int[0]);
        }
    }

    public static Sprite loadSprite(String path) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.load(path);
        } catch (IOException e) {
            System.err.println("Failed to load sprite");
            bitmap = new Bitmap(0, 0, new int[0]);
        }
        return new Sprite(bitmap);
    }

    public static Sprite[] loadSpriteSheet(String path, int tileWidth, int tileHeight) {
        try {
            BufferedImage img = ImageIO.read(AssetLoader.class.getClassLoader().getResourceAsStream(path));
            int w = img.getWidth() / tileWidth;
            int h = img.getHeight() / tileHeight;

            Sprite[] sprites = new Sprite[w*h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int[] pixels = img.getRGB(x*tileWidth, y*tileHeight, tileWidth, tileHeight, null, 0, tileWidth);
                    Bitmap bitmap = new Bitmap(tileWidth, tileHeight, pixels);
                    sprites[x+y*w] = new Sprite(bitmap);
                }
            }
            return sprites;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Sprite[0];
    }
}
