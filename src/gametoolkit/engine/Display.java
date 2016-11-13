package gametoolkit.engine;

import gametoolkit.engine.backend.Framebuffer;
import gametoolkit.engine.backend.GlfwWindow;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public class Display {

    private static GlfwWindow window;

    private static int fPosXS, fPosYS, fPosXE, fPosYE;
    private static boolean needResize = true;

    static void init(GlfwWindow window) {
        Display.window = window;
        window.addSizeCallback((window1, width, height) -> {
            needResize = true;
        });
    }

    public static void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    public static int getWidth() {
        return window.width();
    }

    public static int getHeight() {
        return window.height();
    }

    public static boolean shouldClose() {
        return window.shouldClose();
    }

    public static void swapBuffers(Framebuffer fb) {

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        if (needResize) {
            int width = window.width();
            int height = window.height();
            double aspect = fb.width() / (float)fb.height();
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
            needResize = false;
        }

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

        window.swapBuffers();
    }

}
