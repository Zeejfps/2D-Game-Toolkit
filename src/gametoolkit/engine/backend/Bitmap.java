package gametoolkit.engine.backend;

import gametoolkit.utils.IOUtils;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Created by Zeejfps on 10/30/2016.
 */
public class Bitmap {

    private int width, height, ncomps;
    private IntBuffer pixels;

    public Bitmap(int width, int height, int ncomps) {
        this.width = width;
        this.height = height;
        this.ncomps = ncomps;
        this.pixels = BufferUtils.createIntBuffer(width*height);
    }

    private Bitmap(int width, int height, int ncomps, IntBuffer pixels) {
        this.width = width;
        this.height = height;
        this.ncomps = ncomps;
        this.pixels = pixels;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int ncomps() {
        return ncomps;
    }

    public IntBuffer pixels() {
        return pixels;
    }

    public int pixels(int x, int y) {
        return pixels(x+y*width);
    }

    public int pixels(int index) {
        return pixels.get(index);
    }

    public static Bitmap load(String path) throws IOException {
        ByteBuffer data = IOUtils.resourceToByteBuffer(path, 1024);
        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        IntBuffer c = BufferUtils.createIntBuffer(1);

        ByteBuffer img = stbi_load_from_memory(data, w, h, c, 0);
        if (img == null) {
            throw new IOException("Failed to load bitmap: " + stbi_failure_reason());
        }

        IntBuffer pixels = img.asIntBuffer();
        stbi_image_free(img);

        int width = w.get(0);
        int height = h.get(0);
        int ncomps = c.get(0);

        return new Bitmap(width, height, ncomps, pixels);
    }

}
