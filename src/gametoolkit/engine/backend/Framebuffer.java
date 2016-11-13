package gametoolkit.engine.backend;

import gametoolkit.engine.interfaces.Disposable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

/**
 * Created by root on 10/31/16.
 */
public final class Framebuffer implements Disposable {

    private int width, height;
    private IntBuffer pixels;

    public final int textureID;
    public final int framebufferID;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = MemoryUtil.memAllocInt(width*height);

        framebufferID = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebufferID);

        textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, 0);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureID, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public void update() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixels);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void clear(int color) {
        for (int i = 0; i < pixels.limit(); i++) {
            pixels.put(i, color);
        }
    }

    public IntBuffer pixels() {
        return pixels;
    }

    public int id() {
        return framebufferID;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public void dispose() {
        GL11.glDeleteTextures(textureID);
        GL30.glDeleteFramebuffers(framebufferID);
        MemoryUtil.memFree(pixels);
        width = 0;
        height = 0;
    }
}
