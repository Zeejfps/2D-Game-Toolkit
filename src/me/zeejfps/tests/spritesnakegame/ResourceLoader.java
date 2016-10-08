package me.zeejfps.tests.spritesnakegame;

import me.zeejfps.gametoolkit.engine.Bitmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Zeejfps on 10/7/2016.
 */
public class ResourceLoader {

    private ResourceLoader() {}

    public static Bitmap loadBitmap(String bitmap) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(ResourceLoader.class.getClassLoader().getResourceAsStream(bitmap));
        } catch (Exception e) {
            System.err.println("Failed to load bitmap: " + bitmap);
            return new Bitmap(0, 0);
        }
        int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        return new Bitmap(img.getWidth(), img.getHeight(), pixels);
    }

}
