package gametoolkit.engine.renderers;

import gametoolkit.engine.backend.Bitmap;
import gametoolkit.engine.Camera;
import gametoolkit.engine.Sprite;
import gametoolkit.engine.backend.Framebuffer;
import gametoolkit.math.Vec2f;
import gametoolkit.math.Vec2i;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public class SpriteRenderer {

    private Camera camera;
    private Framebuffer fb;

    public SpriteRenderer(Camera camera) {
        this.camera = camera;
        this.fb = camera.getFramebuffer();
    }

    public void renderSprite(Sprite sprite, Vec2f worldPos, float angle) {
        Vec2i screenPos = camera.worldToScreenPos(worldPos);
        screenPos.x -= sprite.pivot.x * sprite.bitmap.width();
        screenPos.y += sprite.pivot.y * sprite.bitmap.height();
        renderBitmap(sprite.bitmap, screenPos.x, screenPos.y, angle);
    }

    public void renderSprite(Sprite sprite, Vec2f worldPos) {
        renderSprite(sprite, worldPos, 0);
    }

    public void renderBitmap(Bitmap bitmap, int xPos, int yPos) {
        int xs = xPos < 0 ? 0 : xPos;
        int ys = yPos > fb.height() ? fb.height() : yPos;
        int xe = xPos + bitmap.width();
        xe = xe > fb.width()  ? fb.width() : xe;
        int ye = yPos - bitmap.height();
        ye = ye < 0 ? 0 : ye;

        int x, y, srcx, srcy;
        for (srcy = -(ys-yPos), y = ys-1; y >= ye; y--, srcy++) {
            for (srcx = xs-xPos, x = xs; x < xe; x++, srcx++) {
                int srcPix = bitmap.pixels(srcx+srcy*bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    fb.pixels().put(x+y*fb.width(), srcPix);
            }
        }
    }

    public void renderBitmap(Bitmap bitmap, int xPos, int yPos, float angle) {

       /* if (angle == 0) {
            renderBitmap(bitmap, xPos, yPos);
            return;
        }
        else
        if (angle == -90 || angle == 270) {
            Bitmap result = new Bitmap(bitmap.getHeight(), bitmap.getWidth());
            for (int y = 0; y < bitmap.getHeight(); y++) {
                for (int x = 0; x < bitmap.getWidth(); x++) {
                    int rotx = result.getWidth()-1-y;
                    int roty = x;
                    result.pixels(roty * result.getWidth() + rotx, bitmap.pixels(y*bitmap.getWidth()+x));
                }
            }
            renderBitmap(result, xPos, yPos);
            return;
        }
        else
        if (angle == 90 || angle == -270) {
            Bitmap result = new Bitmap(bitmap.getHeight(), bitmap.getWidth());
            for (int y = 0; y < result.getHeight(); y++) {
                for (int x = 0; x < result.getWidth(); x++) {
                    int rotx = bitmap.getWidth()-1-y;
                    int roty = x;
                    result.pixels(y * result.getWidth() + x, bitmap.pixels(roty*bitmap.getWidth()+rotx));
                }
            }
            renderBitmap(result, xPos, yPos);
            return;
        }*/

        //float a = (float) Math.toRadians(angle);
        float sinA  = (float) Math.sin(-angle);
        float cosA  = (float) Math.cos(-angle);

        int half_width  = (int) (bitmap.width() * 0.5);
        int half_height = (int) (bitmap.height() * 0.5);

        int p1_x = -half_width;
        int p1_y = -half_height;
        int p2_x = p1_x + bitmap.width();
        int p2_y = p1_y;
        int p3_x = p2_x;
        int p3_y = p2_y + bitmap.height();
        int p4_x = p1_x;
        int p4_y = p3_y;

        int x1 = (int) (p1_x * cosA - p1_y * sinA);
        int y1 = (int) (p1_y * cosA + p1_x * sinA);

        int x2 = (int) (p2_x * cosA - p2_y * sinA);
        int y2 = (int) (p2_y * cosA + p2_x * sinA);

        int x3 = (int) (p3_x * cosA - p3_y * sinA);
        int y3 = (int) (p3_y * cosA + p3_x * sinA);

        int x4 = (int) (p4_x * cosA - p4_y * sinA);
        int y4 = (int) (p4_y * cosA + p4_x * sinA);

        int min_x = Math.min(Math.min(x1,x2),Math.min(x3, x4));
        int min_y = Math.min(Math.min(y1, y2), Math.min(y3, y4));
        int max_x = Math.max(Math.max(x1,x2),Math.max(x3, x4));
        int max_y = Math.max(Math.max(y1, y2), Math.max(y3, y4));

        int w = max_x - min_x;
        int h = max_y - min_y;

        int x_start = xPos+min_x;
        if (x_start < 0)
            x_start = 0;

        int y_start = yPos+max_y;
        if (y_start > fb.height())
            y_start = fb.height();

        int x_end = xPos + max_x;
        if (x_end > fb.width())
            x_end = fb.width();

        int y_end = yPos + min_y;
        if (y_end < 0)
            y_end = 0;

        for (int i = y_start-1; i >= y_end; i--) {
            for (int j = x_start; j < x_end; j++) {

                int x = (int) ((j-xPos)*cosA - (i-yPos)*sinA) + half_width;
                int y = (int) ((i-yPos)*cosA + (j-xPos)*sinA) + half_height;

                if (x < 0 || x >= bitmap.width() || y < 0 || y >= bitmap.height())
                    continue;

                int src = bitmap.pixels(x, y);
                if (src == 0xffff00ff) continue;

                fb.pixels().put(j+i*fb.width(), src);
            }
        }
    }

}
