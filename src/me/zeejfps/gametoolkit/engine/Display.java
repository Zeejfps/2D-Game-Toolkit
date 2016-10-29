package me.zeejfps.gametoolkit.engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;

/**
 * Created by Zeejfps on 8/12/2016.
 */
public class Display {

    protected final JFrame frame;
    protected final Canvas canvas;
    protected final GraphicsConfiguration gc;

    private int xPos, yPos, scaledWidth, scaledHeight;
    private BufferStrategy bufferStrat;
    private final Camera camera;
    private float oldAspect;

    Display(Camera camera){

        this.camera = camera;

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

        frame = new JFrame(gc);
        frame.setBackground(Color.BLACK);
        frame.setIgnoreRepaint(true);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Untitled Game");
        setResizable(true);
        setSize(640, 480);
    }

    protected void updateFramebuffer() {

        if (camera.getAspect() != oldAspect) {
            resize();
            oldAspect = camera.getAspect();
        }

        if (bufferStrat == null) {
            canvas.createBufferStrategy(3);
        }
        bufferStrat = canvas.getBufferStrategy();
        Graphics2D g = (Graphics2D) bufferStrat.getDrawGraphics();

        // Clear the canvas background
        g.setColor(canvas.getBackground());
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Set hints to "speed down" the rendering
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the framebuffer
        g.drawImage(camera.getBuffer(), xPos, yPos, scaledWidth, scaledHeight, null);
        g.dispose();
        bufferStrat.show();
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
        canvas.requestFocusInWindow();
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setSize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setLocationRelativeTo(null);
        resize();
    }

    public void setResizable(boolean resizable) {
        frame.setResizable(resizable);
    }

    public void setIconImage(String image) {
        try {
            frame.setIconImage(ImageIO.read(getClass().getClassLoader().getResourceAsStream(image)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resize() {
        float aspect = camera.getAspect();
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
