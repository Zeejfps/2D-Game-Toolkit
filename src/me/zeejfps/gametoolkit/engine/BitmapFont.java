package me.zeejfps.gametoolkit.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zeejfps on 10/30/2016.
 */
public class BitmapFont {

    private final Map<Integer, Glyph> fontChars;
    private final int lineHeight;

    public BitmapFont(Map<Integer, Glyph> fontChars, int lineHeight) {
        this.fontChars = fontChars;
        this.lineHeight = lineHeight;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public Glyph getChar(int id) {
        return fontChars.get(id);
    }

    public static class Glyph {

        public final int xOffset, yOffset, xAdvance;
        public final Bitmap bitmap;

        private Glyph(int xOffset, int yOffset, int xAdvance, Bitmap bitmap) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xAdvance = xAdvance;
            this.bitmap = bitmap;
        }

    }

    public static BitmapFont load(String path) throws IOException {
        Map<Integer, Glyph> fontChars = new HashMap<>();

        Scanner scanner = new Scanner(
                BitmapFont.class.getClassLoader().getResourceAsStream(path)
        );
        Pattern pattern = Pattern.compile("(\\w+)=\"?([^\\s\"]+)");
        Matcher matcher;

        Map<String, String> infoMap = new HashMap<>();
        // Read info line
        String infoLine = scanner.nextLine();
        matcher = pattern.matcher(infoLine);
        addToMap(matcher, infoMap);

        Map<String, String> commonMap = new HashMap<>();
        // Read the common line;
        String commonLine = scanner.nextLine();
        matcher = pattern.matcher(commonLine);
        addToMap(matcher, commonMap);

        int pages = Integer.parseInt(commonMap.get("pages"));
        for (int pageIndex = 0; pageIndex < pages; pageIndex++) {

            Map<String, String> pageMap = new HashMap<>();
            // Read the page
            String pageLine = scanner.nextLine();
            matcher = pattern.matcher(pageLine);
            addToMap(matcher, pageMap);

            String fileName = pageMap.get("file");
            BufferedImage pageImg = ImageIO.read(
                    BitmapFont.class.getClassLoader().getResourceAsStream("fonts/" + fileName)
            );

            Map<String, String> charsMap = new HashMap<>();
            // Read number of chars
            String charsLine = scanner.nextLine();
            matcher = pattern.matcher(charsLine);
            addToMap(matcher, charsMap);

            // Read all of the chars
            int numChars = Integer.parseInt(charsMap.get("count"));
            for (int charIndex = 0; charIndex < numChars; charIndex++) {

                Map<String, String> charMap = new HashMap<>();
                // Read a char
                String charLine = scanner.nextLine();
                matcher = pattern.matcher(charLine);
                addToMap(matcher, charMap);

                int id = Integer.parseInt(charMap.get("id"));
                int x = Integer.parseInt(charMap.get("x"));
                int y = Integer.parseInt(charMap.get("y"));
                int width = Integer.parseInt(charMap.get("width"));
                int height = Integer.parseInt(charMap.get("height"));
                int xOffset = Integer.parseInt(charMap.get("xoffset"));
                int yOffset = Integer.parseInt(charMap.get("yoffset"));
                int xAdvance = Integer.parseInt(charMap.get("xadvance"));
                //int page = Integer.parseInt(charMap.get("page"));
                //int chnl = Integer.parseInt(charMap.get("chnl"));

                int[] pixels = pageImg.getRGB(x, y, width, height, null, 0, width);
                Bitmap bitmap = new Bitmap(width, height, pixels);
                Glyph glyph = new Glyph(xOffset, yOffset, xAdvance, bitmap);
                fontChars.put(id, glyph);
            }

        }
        int lineHeight = Integer.parseInt(commonMap.get("lineHeight"));
        return new BitmapFont(fontChars, lineHeight);
    }

    private static void addToMap(Matcher m, Map<String, String> map) {
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(2);
            map.put(key, value);
        }
    }

}
