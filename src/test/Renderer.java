package test;

import gametoolkit.engine.Bitmap;
import gametoolkit.engine.Camera;
import gametoolkit.engine.Font;
import gametoolkit.engine.Sprite;
import gametoolkit.engine.backend.Framebuffer;
import gametoolkit.engine.backend.GlfwWindow;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import tiled.core.MapLayer;
import tiled.core.TMXMap;
import tiled.core.Tile;
import tiled.core.TileLayer;

/**
 * Created by root on 10/31/16.
 */
public class Renderer {

    private final GlfwWindow window;
    private final Camera camera;
    private final Framebuffer fb;

    private int fPosXS, fPosYS;
    private int fPosXE, fPosYE;
    private boolean drawing;

    public Renderer(GlfwWindow window, Camera camera) {
        this.window = window;
        this.camera = camera;
        this.fb = camera.getFramebuffer();
        resize(window.width(), window.height());
        window.addSizeCallback((window1, width, height) -> {
            resize(width, height);
        });
    }

    private void resize(int width, int height) {
        double xScale= width;
        double yScale= width / camera.getAspect();
        if (yScale > height) {
            xScale = height * camera.getAspect();
            yScale = height;
        }
        fPosXS = (int)((width - xScale)  * 0.5f);
        fPosYS = (int)((height - yScale) * 0.5f);
        fPosXE = (int)Math.round(xScale) + fPosXS;
        fPosYE = (int)Math.round(yScale) + fPosYS;
    }

    private void checkIsDrawing() {
        if (!drawing) throw new IllegalStateException("Must call Renderer.begin() first");
    }

    public void begin()  {
        if (drawing) throw new IllegalStateException("Must call Render.end()");
        drawing = true;
    }

    public void renderSprite(Sprite sprite, Vec2f worldPos) {
        checkIsDrawing();
        Vec2i screenPos = camera.worldToScreenPos(worldPos);
        screenPos.x -= sprite.pivot.x * sprite.bitmap.width();
        screenPos.y += sprite.pivot.y * sprite.bitmap.height();
        renderBitmap(sprite.bitmap, screenPos);
    }

    public void renderBitmap(Bitmap bitmap, int xPos, int yPos) {
        checkIsDrawing();
        int xs = xPos < 0 ? 0 : xPos;
        int ys = yPos > fb.height() ? fb.height() : yPos;
        int xe = xPos + bitmap.width();
        xe = xe > fb.width()  ? fb.width() : xe;
        int ye = yPos - bitmap.height();
        ye = ye < 0 ? 0 : ye;

        int x, y, srcx, srcy;
        for (srcy = -(ys-yPos), y = ys-1; y >= ye; y--, srcy++) {
            for (srcx = xs-xPos, x = xs; x < xe; x++, srcx++) {
                int srcPix = bitmap.pixel(srcx+srcy*bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    fb.pixels().put(x+y*fb.width(), srcPix);
            }
        }
    }

    public void renderBitmap(Bitmap bitmap, Vec2i screenPos) {
        checkIsDrawing();
        renderBitmap(bitmap, screenPos.x, screenPos.y);
    }

    public void renderString(String str, Vec2i screenPos, Font font, int color) {
        checkIsDrawing();
        int xCursor = screenPos.x;
        int yCursor = screenPos.y;
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
                xCursor = screenPos.x;
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

    public void renderGlyph(Font.Glyph glyph, int xPos, int yPos, int color){
        checkIsDrawing();
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

    public void renderMap(TMXMap map) {
        for (MapLayer layer : map.getLayers()) {
            if (layer instanceof TileLayer) {
                TileLayer l = (TileLayer)layer;
                for (int y = 0; y < l.getHeight(); y++) {
                    for (int x = 0; x < l.getWidth(); x++) {
                        Tile t = l.getTileAt(x, y);
                        if (t != null) {
                            Sprite s = new Sprite(t.getBitmap());
                            renderSprite(s, new Vec2f(x, camera.getSize() - y));
                        }
                    }
                }
            }
        }
    }

    public void end() {
        checkIsDrawing();
        fb.update();
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb.id());
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBlitFramebuffer(
                0, 0,
                fb.width(), fb.height(),
                fPosXS, fPosYS,
                fPosXE, fPosYE,
                GL11.GL_COLOR_BUFFER_BIT,
                GL11.GL_NEAREST
        );
        drawing = false;
    }

}
