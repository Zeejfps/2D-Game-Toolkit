package gametoolkit.engine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zeejfps on 10/30/2016.
 */
public class Font {

    private static final Pattern PATTERN = Pattern.compile("(\\w+)=\"?([^\\s\"]+)");

    private final Map<KeyPair, Integer> kernings;
    private final Map<Integer, Glyph> fontChars;
    private final int lineHeight;

    public Font(Map<Integer, Glyph> fontChars, Map<KeyPair, Integer> kernings, int lineHeight) {
        this.kernings = kernings;
        this.fontChars = fontChars;
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
        public final Bitmap bitmap;

        private Glyph(int xOffset, int yOffset, int xAdvance, Bitmap bitmap) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.xAdvance = xAdvance;
            this.bitmap = bitmap;
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
        BufferedReader br = new BufferedReader(
                new InputStreamReader(Font.class.getClassLoader().getResourceAsStream(path)));

        Map<String, String> infoMap = parseLine(br.readLine());
        Map<String, String> commonMap = parseLine(br.readLine());
        Map<Integer, Glyph> fontChars = new HashMap<>();

        int pages = Integer.parseInt(commonMap.get("pages"));
        for (int pageIndex = 0; pageIndex < pages; pageIndex++) {

            Map<String, String> pageMap = parseLine(br.readLine());

            String fileName = pageMap.get("file");
            BufferedImage pageImg = ImageIO.read(
                    Font.class.getClassLoader().getResourceAsStream("fonts/" + fileName)
            );

            int numChars = Integer.parseInt(parseLine(br.readLine()).get("count"));
            for (int charIndex = 0; charIndex < numChars; charIndex++) {

                Map<String, String> charMap = parseLine(br.readLine());

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

        HashMap<KeyPair, Integer> kernings = new HashMap<>();

        int numKernings = Integer.parseInt(parseLine(br.readLine()).get("count"));
        for (int i = 0; i < numKernings; i++) {

            Map<String, String> k = parseLine(br.readLine());

            int first = Integer.parseInt(k.get("first"));
            int second = Integer.parseInt(k.get("second"));
            int amount = Integer.parseInt(k.get("amount"));

            kernings.put(new KeyPair(first, second), amount);
        }

        int lineHeight = Integer.parseInt(commonMap.get("lineHeight"));
        return new Font(fontChars, kernings, lineHeight);
    }

    private static Map<String, String> parseLine(String line) {
        Matcher matcher = PATTERN.matcher(line);
        Map<String, String> params = new HashMap<>();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            params.put(key, value);
        }
        return params;
    }

}
