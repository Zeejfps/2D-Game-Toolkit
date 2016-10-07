package me.zeejfps.gametoolkit.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Created by Zeejfps on 8/12/2016.
 */
public class Display {

    protected final JFrame window;
    protected final Canvas canvas;
    protected final GraphicsConfiguration gc;

    private BufferedImage framebufferImg;
    private Bitmap framebuffer;
    private int xPos, yPos, scaledWidth, scaledHeight;
    private double aspect;
    private BufferStrategy bufferStrat;

    public Display(){
        gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

        canvas = new Canvas(gc);
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(true);
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });

        window = new JFrame(gc);
        window.setBackground(Color.BLACK);
        window.setIgnoreRepaint(true);
        window.add(canvas);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setWindowTitle("Untitled Game");
        setWindowSize(640, 480);
        setFramebufferSize(320, 240);
    }

    public void clear(int color) {
        Arrays.fill(framebuffer.pixels, color);
    }

    protected void updateFramebuffer() {

        if (bufferStrat == null) {
            canvas.createBufferStrategy(2);
        }
        bufferStrat = canvas.getBufferStrategy();
        Graphics2D g = (Graphics2D) bufferStrat.getDrawGraphics();

        // Clear the canvas background
        g.setColor(canvas.getBackground());
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Set hints to "speed up" the rendering
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Draw the framebuffer
        g.drawImage(framebufferImg, xPos, yPos, scaledWidth, scaledHeight, null);
        g.dispose();
        bufferStrat.show();
    }

    public void setVisible(boolean visible) {
        window.setVisible(visible);
        canvas.requestFocusInWindow();
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    public Bitmap framebuffer() {
        return framebuffer;
    }

    public void setWindowTitle(String title) {
        window.setTitle(title);
    }

    public void setWindowSize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        window.pack();
        window.setLocationRelativeTo(null);
        resize();
    }

    public void setFramebufferSize(int width, int height) {
        aspect = width / (float)height;
        framebufferImg = gc.createCompatibleImage(
                width,
                height,
                BufferedImage.OPAQUE
        );
        framebuffer = new Bitmap(
                framebufferImg.getWidth(),
                framebufferImg.getHeight(),
                ((DataBufferInt) framebufferImg.getRaster().getDataBuffer()).getData()
        );
        resize();
    }

    private void resize() {
        if (aspect == 0) return;
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        double xScale= w;
        double yScale= w / aspect;
        if (yScale > h) {
            xScale = h * aspect;
            yScale = h;
        }
        xPos = (int)((w - xScale)  * 0.5f);
        yPos = (int)((h - yScale) * 0.5f);
        scaledWidth = (int)Math.round(xScale);
        scaledHeight = (int)Math.round(yScale);
    }

}
