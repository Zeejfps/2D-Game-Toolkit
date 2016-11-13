package gametoolkit.engine.renderers;

import gametoolkit.engine.Bitmap;
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

    public void renderSprite(Sprite sprite, Vec2f worldPos) {
        Vec2i screenPos = camera.worldToScreenPos(worldPos);
        screenPos.x -= sprite.pivot.x * sprite.bitmap.width();
        screenPos.y += sprite.pivot.y * sprite.bitmap.height();
        renderBitmap(sprite.bitmap, screenPos.x, screenPos.y);
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
                int srcPix = bitmap.pixel(srcx+srcy*bitmap.width());
                if ((0xff000000 & srcPix) != 0)
                    fb.pixels().put(x+y*fb.width(), srcPix);
            }
        }
    }

}
