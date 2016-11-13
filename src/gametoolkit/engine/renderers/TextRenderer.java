package gametoolkit.engine.renderers;

import gametoolkit.engine.Font;
import gametoolkit.engine.backend.Framebuffer;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public class TextRenderer {

    private Framebuffer fb;

    public TextRenderer(Framebuffer framebuffer) {
        this.fb = framebuffer;
    }

    public void renderString(String str, int x, int y, Font font, int color) {
        int xCursor = x;
        int yCursor = y;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int kerning = 0;
            if (i > 0) {
                char prev = chars[i-1];
                kerning += font.getKerning((int)prev, (int)c);
            }
            if (c == '\n') {
                yCursor -= font.getLineHeight();
                xCursor = x;
            }
            else {
                Font.Glyph glyph = font.getChar((int)c);
                if (glyph == null) return;

                int xRender = xCursor + glyph.xOffset + kerning;
                int yRender = yCursor - glyph.yOffset;
                renderGlyph(glyph, xRender, yRender, color);

                xCursor += glyph.xAdvance;
            }
        }
    }

    private void renderGlyph(Font.Glyph glyph, int xPos, int yPos, int color){
        int xs = xPos < 0 ? 0 : xPos;
        int ys = yPos > fb.height() ? fb.height() : yPos;
        int xe = xPos + glyph.bitmap.width();
        xe = xe > fb.width()  ? fb.width() : xe;
        int ye = yPos - glyph.bitmap.height();
        ye = ye < 0 ? 0 : ye;

        int x, y, srcx, srcy;
        for (srcy = -(ys-yPos), y = ys-1; y >= ye; y--, srcy++) {
            for (srcx = xs-xPos, x = xs; x < xe; x++, srcx++) {
                int srcPix = glyph.bitmap.pixel(srcx+srcy*glyph.bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    fb.pixels().put(x+y*fb.width(), color);
            }
        }
    }
}
