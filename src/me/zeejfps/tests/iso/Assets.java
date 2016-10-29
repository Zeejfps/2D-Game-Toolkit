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
            return new Sprite(img.getWidth(), img.getHeight(), pixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Sprite(10, 10);
    }

}
