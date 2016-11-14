package gametoolkit.engine;

import gametoolkit.engine.backend.Bitmap;

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
public class Font {

    private final Map<KeyPair, Integer> kernings;
    private final Map<Integer, Glyph> fontChars;
    private final Map<Integer, Bitmap> pages;
    private final int lineHeight;

    public Font(
            Map<Integer, Glyph> fontChars,
            Map<KeyPair, Integer> kernings,
            Map<Integer, Bitmap> pages,
            int lineHeight
    ) {
        this.kernings = kernings;
        this.fontChars = fontChars;
        this.pages = pages;
        this.lineHeight = lineHeight;
    }

    public int getLineHeight() {
        return lineHeight;
    }

    public Glyph getChar(int id) {
        return fontChars.get(id);
    }

    public int getKerning(int a, int b) {
        Integer k = kernings.get(new KeyPair(a, b));
        if (k == null) {
            return 0;
        }
        return k;
    }

    public static class Glyph {

        public final int xOffset, yOffset, xAdvance;
        public final int x, y, width, height, page;

        private Glyph(int xOffset, int yOffset, int xAdvance, int x, int y, int width, int height, int page) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xAdvance = xAdvance;;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.page = page;
        }
    }

    static class KeyPair {

        public final int first, second;

        public KeyPair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof KeyPair) {
                KeyPair k = (KeyPair)obj;
                return k.first == first && k.second == second;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return first ^ second;
        }
    }

    public static Font load(String path) throws IOException {
        Map<Integer, Glyph> fontChars = new HashMap<>();

        Scanner scanner = new Scanner(
                Font.class.getClassLoader().getResourceAsStream(path)
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

        Map<Integer, Bitmap> pageBitmaps = new HashMap<>();
        int pages = Integer.parseInt(commonMap.get("pages"));
        for (int pageIndex = 0; pageIndex < pages; pageIndex++) {

            Map<String, String> pageMap = new HashMap<>();
            // Read the page
            String pageLine = scanner.nextLine();
            matcher = pattern.matcher(pageLine);
            addToMap(matcher, pageMap);

            String fileName = pageMap.get("file");

            Bitmap pageBitmap = Bitmap.load(fileName);
            pageBitmaps.put(pageIndex, pageBitmap);

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
                int page = Integer.parseInt(charMap.get("page"));
                //int chnl = Integer.parseInt(charMap.get("chnl"));

                Glyph glyph = new Glyph(xOffset, yOffset, xAdvance, x, y, width, height, page);
                fontChars.put(id, glyph);
            }

        }

        Map<KeyPair, Integer> kernings = new HashMap<>();
        Map<String, String> kerningsMap = new HashMap<>();
        // Read in the kernings
        String kerningLine = scanner.nextLine();
        matcher = pattern.matcher(kerningLine);
        addToMap(matcher, kerningsMap);

        // Read each kerning
        int numKernings = Integer.parseInt(kerningsMap.get("count"));
        for (int i = 0; i < numKernings; i++) {

            Map<String, String> k = new HashMap<>();
            String line = scanner.nextLine();
            matcher = pattern.matcher(line);
            addToMap(matcher, k);

            int first = Integer.parseInt(k.get("first"));
            int second = Integer.parseInt(k.get("second"));
            int amount = Integer.parseInt(k.get("amount"));

            kernings.put(new KeyPair(first, second), amount);
        }

        int lineHeight = Integer.parseInt(commonMap.get("lineHeight"));
        return new Font(fontChars, kernings, pageBitmaps, lineHeight);
    }

    private static void addToMap(Matcher m, Map<String, String> map) {
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(2);
            map.put(key, value);
        }
    }

}
