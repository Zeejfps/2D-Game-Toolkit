package gametoolkit.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

/**
 * Created by root on 10/31/16.
 */
public class Framebuffer {

    private int width, height;
    private IntBuffer pixels;

    private int textureHandle;
    private int framebufferHandle;

    Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = BufferUtils.createIntBuffer(width*height);
        framebufferHandle = GL30.glGenFramebuffers();
        resize(width, height);
    }

    public void update() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixels);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void clear(int color) {
        for (int i = 0; i < pixels.limit(); i++) {
            pixels.put(i, color);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        GL11.glDeleteTextures(textureHandle);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferHandle);

        textureHandle = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, 0);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureHandle, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public IntBuffer pixels() {
        return pixels;
    }

    public int id() {
        return framebufferHandle;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

}
