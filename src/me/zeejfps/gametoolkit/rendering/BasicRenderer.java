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
        xe = x + bitmap.getWidth() > colorBuffer.getWidth() ? colorBuffer.getWidth() : x + bitmap.getWidth();

        // Y bounds
        if (y < 0) {
            ys = 0;
            yOffset = -y;
        }
        else {
            ys = y;
            yOffset = 0;
        }
        ye = y + bitmap.getHeight() > colorBuffer.getHeight() ? colorBuffer.getHeight() : y + bitmap.getHeight();

        xStride = xe - xs;
        yStride = ye - ys;
        if (xStride < 1 || yStride < 1) {
            return;
        }

        srcPos = yOffset * bitmap.getWidth() + xOffset;
        destPos = ys * colorBuffer.getWidth() + xs;
        switch (transparency) {
            case NO_TRANSPARENCY:
                noTransparencyBlit(xStride, yStride, srcPos, destPos, bitmap);
                break;
            case FAST_TRANSPARENCY:
                fastTransparencyBlit(xStride, yStride, srcPos, destPos, bitmap);
                break;
        }
    }

    public void setPixel(int x, int y, int color){
        if (x <0 || x >= colorBuffer.getWidth()) return;
        if (y <0 || y >= colorBuffer.getHeight()) return;
        colorBuffer.pixels()[y * colorBuffer.getWidth() + x] = color;
    }

    private void fastTransparencyBlit(int xStride, int yStride, int srcPos, int destPos, Bitmap bitmap) {
        int xd = bitmap.getWidth() - xStride;
        int rd = colorBuffer.getWidth() - xStride;
        for (int i = 0; i < yStride; i++) {
            for (int j = 0; j < xStride; j++, srcPos++, destPos++) {
                int src = bitmap.pixels()[srcPos];
                if ((src & 0xff000000) != 0) {
                    colorBuffer.pixels()[destPos] = src;
                }
            }
            srcPos += xd;
            destPos += rd;
        }
    }

    private void noTransparencyBlit(int xStride, int yStride, int srcPos, int destPos, Bitmap bitmap) {
        for (int i = 0; i < yStride; i++, srcPos += bitmap.getWidth(), destPos += colorBuffer.getWidth()) {
            System.arraycopy(bitmap.pixels(), srcPos, colorBuffer.pixels(), destPos, xStride);
        }
    }

}
