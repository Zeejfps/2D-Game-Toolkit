package me.zeejfps.gametoolkit.rendering;

import me.zeejfps.gametoolkit.engine.Bitmap;

/**
 * Created by Zeejfps on 8/12/2016.
 */
public class BasicRenderer {

    private Bitmap colorBuffer;

    public BasicRenderer(Bitmap colorBuffer) {
        this.colorBuffer = colorBuffer;
    }

    public void blit(int x, int y, Bitmap bitmap, Transparency transparency) {
        int xs, xe, ys, ye, yStride, srcPos, destPos, xStride, xOffset, yOffset;

        // X bounds
        if (x < 0) {
            xs = 0;
            xOffset = -x;
        }
        else {
            xs = x;
            xOffset = 0;
        }
        xe = x + bitmap.width > colorBuffer.width ? colorBuffer.width : x + bitmap.width;

        // Y bounds
        if (y < 0) {
            ys = 0;
            yOffset = -y;
        }
        else {
            ys = y;
            yOffset = 0;
        }
        ye = y + bitmap.height > colorBuffer.height ? colorBuffer.height : y + bitmap.height;

        xStride = xe - xs;
        yStride = ye - ys;
        if (xStride < 1 || yStride < 1) {
            return;
        }

        srcPos = yOffset * bitmap.width + xOffset;
        destPos = ys * colorBuffer.width + xs;
        switch (transparency) {
            case NO_TRANSPARENCY:
                noTransparencyBlit(xStride, yStride, srcPos, destPos, bitmap);
                break;
            case FAST_TRANSPARENCY:
                fastTransparencyBlit(xStride, yStride, srcPos, destPos, bitmap);
                break;
        }
    }

    private void fastTransparencyBlit(int xStride, int yStride, int srcPos, int destPos, Bitmap bitmap) {
        int xd = bitmap.width - xStride;
        int rd = colorBuffer.width - xStride;
        for (int i = 0; i < yStride; i++) {
            for (int j = 0; j < xStride; j++, srcPos++, destPos++) {
                int src = bitmap.pixels[srcPos];
                if ((src & 0xff000000) != 0) {
                    colorBuffer.pixels[destPos] = src;
                }
            }
            srcPos += xd;
            destPos += rd;
        }
    }

    private void noTransparencyBlit(int xStride, int yStride, int srcPos, int destPos, Bitmap bitmap) {
        for (int i = 0; i < yStride; i++, srcPos += bitmap.width, destPos += colorBuffer.width) {
            System.arraycopy(bitmap.pixels, srcPos, colorBuffer.pixels, destPos, xStride);
        }
    }

}
