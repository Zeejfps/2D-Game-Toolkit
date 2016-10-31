package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public final class Camera {

    public final Vec2f position;
    public final Window window;

    private float size;
    private float aspect;
    private int pixelsPerUnit;

    private int framebuffer;
    private int texture;
    private IntBuffer pixels;

    private int fWidth, fHeight;
    private int fPosXS, fPosYS;
    private int fPosXE, fPosYE;

    public Camera(float size, float aspect, int pixelsPerUnit, Window window) {
        this.window = window;
        this.position = new Vec2f();
        setSize(size);
        setAspect(aspect);
        setPixelsPerUnit(pixelsPerUnit);

        fHeight = Math.round(2 * size * pixelsPerUnit);
        fWidth = Math.round(fHeight * aspect);
        pixels = BufferUtils.createIntBuffer(fWidth*fHeight);

        framebuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);

        texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, fWidth, fHeight, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, 0);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        window.addResizeCallback(this::resize);
    }

    void resize(Window window, int width, int height) {
        double xScale= width;
        double yScale= width / aspect;
        if (yScale > height) {
            xScale = height * aspect;
            yScale = height;
        }
        fPosXS = (int)((width - xScale)  * 0.5f);
        fPosYS = (int)((height - yScale) * 0.5f);
        fPosXE = (int)Math.round(xScale) + fPosXS;
        fPosYE = (int)Math.round(yScale) + fPosYS;
    }

    public void clear(int color) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < pixels.limit(); i++) {
            pixels.put(i, color);
        }
    }

    public void render() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, fWidth, fHeight, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixels);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebuffer);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBlitFramebuffer(
                0, 0,
                fWidth, fHeight,
                fPosXS, fPosYS,
                fPosXE, fPosYE,
                GL11.GL_COLOR_BUFFER_BIT,
                GL11.GL_NEAREST
        );
    }

    public void renderSprite(Sprite sprite, Vec2f worldPos) {
        Vec2i screenPos = worldToScreenPos(worldPos);
        screenPos.x -= sprite.pivot.x * sprite.bitmap.width();
        screenPos.y += sprite.pivot.y * sprite.bitmap.height();
        renderBitmap(sprite.bitmap, screenPos);
    }

    public void renderBitmap(Bitmap bitmap, int xPos, int yPos) {
        int xs = xPos < 0 ? 0 : xPos;
        int ys = yPos > fHeight ? fHeight : yPos;
        int xe = xPos + bitmap.width();
        xe = xe > fWidth  ? fWidth : xe;
        int ye = yPos - bitmap.height();
        ye = ye < 0 ? 0 : ye;

        int x, y, srcx, srcy;
        for (srcy = -(ys-yPos), y = ys-1; y >= ye; y--, srcy++) {
            for (srcx = xs-xPos, x = xs; x < xe; x++, srcx++) {
                int srcPix = bitmap.pixel(srcx+srcy*bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    pixels.put(x+y*fWidth, srcPix);
            }
        }
    }

    public void renderBitmap(Bitmap bitmap, Vec2i screenPos) {
        renderBitmap(bitmap, screenPos.x, screenPos.y);
    }

    public void renderString(String str, Vec2i screenPos, BitmapFont font, int color) {
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
                BitmapFont.Glyph glyph = font.getChar((int)c);
                if (glyph == null) return;

                int xRender = xCursor + glyph.xOffset + kerning;
                int yRender = yCursor - glyph.yOffset;
                renderGlyph(glyph, xRender, yRender, color);

                xCursor += glyph.xAdvance;
            }
        }
    }

    public void renderGlyph(BitmapFont.Glyph glyph, int xPos, int yPos, int color){
        int xs = xPos < 0 ? 0 : xPos;
        int ys = yPos > fHeight ? fHeight : yPos;
        int xe = xPos + glyph.bitmap.width();
        xe = xe > fWidth  ? fWidth : xe;
        int ye = yPos - glyph.bitmap.height();
        ye = ye < 0 ? 0 : ye;

        int x, y, srcx, srcy;
        for (srcy = -(ys-yPos), y = ys-1; y >= ye; y--, srcy++) {
            for (srcx = xs-xPos, x = xs; x < xe; x++, srcx++) {
                int srcPix = glyph.bitmap.pixel(srcx+srcy*glyph.bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    pixels.put(x+y*fWidth, color);
            }
        }
    }

    public Vec2i worldToScreenPos(Vec2f pos) {
        int x = (int)(fWidth * 0.5f + pos.x*pixelsPerUnit - position.x);
        int y = (int)(fHeight * 0.5f + pos.y*pixelsPerUnit - position.y);
        return new Vec2i(x, y);
    }

    public void setPixelsPerUnit(int pixelsPerUnit) {
        this.pixelsPerUnit = pixelsPerUnit > 0 ? pixelsPerUnit : 1;
    }

    public int getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    public int getFramebufferWidth() {
        return fWidth;
    }

    public int getFramebufferHeight() {
        return fHeight;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    public float getAspect() {
        return aspect;
    }
}
