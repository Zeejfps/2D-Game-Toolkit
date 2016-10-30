package me.zeejfps.gametoolkit.engine;

import me.zeejfps.gametoolkit.math.Vec2f;
import me.zeejfps.gametoolkit.math.Vec2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;

/**
 * Created by Zeejfps on 10/28/2016.
 */
public class Camera {

    public final Vec2f position;
    private final Game game;

    private float size;
    private float aspect;
    private int clearColor;

    private int framebuffer;
    private int texture;
    private IntBuffer pixels;

    private int fWidth, fHeight;
    private int fPosXS, fPosYS;
    private int fPosXE, fPosYE;

    Camera(Game game) {
        this.game = game;
        position = new Vec2f();
        setSize(3);
        setAspect(4/3f);
        setClearColor(0);
    }

    void init() {
        fHeight = Math.round(2 * size * game.getPixelsPerUnit());
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

        game.window.addResizeCallback(this::resize);
    }

    void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < pixels.limit(); i++) {
            pixels.put(i, clearColor);
        }
    }

    void render() {
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

    private void resize(Window window, int width, int height) {
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

    public void renderSprite(Sprite sprite, Vec2f worldPos) {
        Vec2i screenPos = worldToScreenPos(worldPos);
        screenPos.x -= sprite.pivot.x * sprite.bitmap.width();
        screenPos.y += sprite.pivot.y * sprite.bitmap.height();
        renderBitmap(sprite.bitmap, screenPos);
    }

    public void renderBitmap(Bitmap bitmap, Vec2i screenPos) {
        int xs = screenPos.x < 0 ? 0 : screenPos.x;
        int ys = screenPos.y > fHeight ? fHeight : screenPos.y;
        int xe = screenPos.x + bitmap.width();
        xe = xe > fWidth  ? fWidth : xe;
        int ye = screenPos.y - bitmap.height();
        ye = ye < 0 ? 0 : ye;

        int x, y, srcx, srcy;
        for (srcy = -(ys-screenPos.y), y = ys-1; y >= ye; y--, srcy++) {
            for (srcx = xs-screenPos.x, x = xs; x < xe; x++, srcx++) {
                int srcPix = bitmap.pixel(srcx+srcy*bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    pixels.put(x+y*fWidth, srcPix);
            }
        }
    }

    public void renderString(String str, Vec2i screenPos, BitmapFont font) {
        int xCursor = screenPos.x;
        int yCursor = screenPos.y;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\n') {
                yCursor -= font.getLineHeight();
                xCursor = screenPos.x;
            }
            else {
                BitmapFont.Glyph glyph = font.getChar((int)c);
                if (glyph == null) return;

                int xRender = xCursor + glyph.xOffset;
                int yRender = yCursor - glyph.yOffset;
                renderBitmap(glyph.bitmap, new Vec2i(xRender, yRender));

                xCursor += glyph.xAdvance;
            }
        }
    }

    public Vec2i worldToScreenPos(Vec2f pos) {
        int x = (int)(fWidth * 0.5f + pos.x*game.getPixelsPerUnit() - position.x);
        int y = (int)(fHeight * 0.5f + pos.y*game.getPixelsPerUnit() - position.y);
        return new Vec2i(x, y);
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

    public void setClearColor(int color) {
        this.clearColor = color;
    }

    public int getClearColor() {
        return clearColor;
    }

}
