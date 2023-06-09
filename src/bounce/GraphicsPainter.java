package bounce;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Image;

/**
 * Implementation of the Painter interface that delegates drawing to a
 * java.awt.Graphics object.
 *
 * @author Ian Warren
 */
public class GraphicsPainter implements Painter {
    // Delegate object.
    private Graphics g;

    /**
     * Creates a GraphicsPainter object and sets its Graphics delegate.
     */
    public GraphicsPainter(Graphics g) {
        this.g = g;
    }

    /**
     * @see bounce.Painter.drawRect
     */
    @Override
    public void drawRect(int x, int y, int width, int height) {
        g.drawRect(x, y, width, height);
    }

    /**
     * @see bounce.Painter.drawOval
     */
    @Override
    public void drawOval(int x, int y, int width, int height) {
        g.drawOval(x, y, width, height);
    }

    /**
     * @see bounce.Painter.drawLine
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    /**
     * @see bounce.Painter.fillRect
     */
    @Override
    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    /**
     * @see bounce.Painter.getColor
     */
    @Override
    public Color getColor() {
        return g.getColor();
    }

    /**
     * @see bounce.Painter.setColor
     */
    @Override
    public void setColor(Color color) {
        g.setColor(color);
    }

    /**
     * @see bounce.Painter.drawCenteredText
     */
    @Override
    public void drawCenteredText(String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int ascent = fm.getAscent();
        int descent = fm.getDescent();

        int xPos = x - fm.stringWidth(text) / 2;
        int yPos = y;

        if (ascent > descent)
            yPos += (ascent - descent) / 2;
        else if (ascent < descent)
            yPos -= (descent - ascent) / 2;

        g.drawString(text, xPos, yPos);
    }

    /**
     * @see bounce.Painter.drawCenteredText
     */
    @Override
    public void translate(int x, int y) {
        g.translate(x, y);
    }

    /**
     * @see bounce.Painter.drawImage
     */
    @Override
    public void drawImage(Image img, int x, int y, int width, int height) {
        g.drawImage(img, x, y, width, height, null);
    }
}
