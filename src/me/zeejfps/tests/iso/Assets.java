package me.zeejfps.tests.iso;

import me.zeejfps.gametoolkit.engine.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public class Assets {

    public static Sprite loadSprite(String path) {
        try {
            BufferedImage img = ImageIO.read(Assets.class.getClassLoader().getResourceAsStream(path));
            int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = rgbbgr(pixels[i]);
            }
            return new Sprite(img.getWidth(), img.getHeight(), pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Sprite(10, 10);
    }

    public static Sprite[] loadSpriteSheet(String path, int tileWidth, int tileHeight) {
        try {
            BufferedImage img = ImageIO.read(Assets.class.getClassLoader().getResourceAsStream(path));
            int w = img.getWidth() / tileWidth;
            int h = img.getHeight() / tileHeight;

            Sprite[] sprites = new Sprite[w*h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int[] pixels = img.getRGB(x*tileWidth, y*tileHeight, tileWidth, tileHeight, null, 0, tileWidth);
                    for (int i = 0; i < pixels.length; i++) {
                        pixels[i] = rgbbgr(pixels[i]);
                    }
                    sprites[x+y*w] = new Sprite(tileWidth, tileHeight, pixels);
                }
            }
            return sprites;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Sprite[0];
    }

    private static int rgbbgr(int color) {
        int a = (color & 0xff000000);
        int r = (color & 0xff0000) >> 16;
        int g = (color & 0xff00);
        int b = (color & 0xff) << 16;
        return a | b | g | r;
    }

}
