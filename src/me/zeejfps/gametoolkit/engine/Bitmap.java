package me.zeejfps.gametoolkit.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Zeejfps on 10/30/2016.
 */
public class Bitmap {

    private final int width;
    private final int height;
    private final int[] pixels;

    public Bitmap(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int pixel(int x, int y) {
        return pixel(x+y*width);
    }

    public int pixel(int index) {
        return pixels[index];
    }

    public static Bitmap load(String path) throws IOException {
        BufferedImage img = ImageIO.read(Bitmap.class.getClassLoader().getResourceAsStream(path));
        int[] pixels = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
        return new Bitmap(img.getWidth(), img.getHeight(), pixels);
    }
}
