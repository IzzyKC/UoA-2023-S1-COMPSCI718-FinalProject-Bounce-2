package bounce;

import java.awt.Color;
import java.awt.Image;


/**
 * Implementation of the Painter interface that does not actually perform any painting,
 * but instead logs all painting requests. This class is useful for testing purposes.
 *
 * @author Ian Warren
 */
public class MockPainter implements Painter {
    private StringBuffer log = new StringBuffer();

    public String toString() {
        return log.toString();
    }

    /**
     * see bounce.Painter.drawRect
     */
    @Override
    public void drawRect(int x, int y, int width, int height) {
        log.append("(rectangle " + x + "," + y + "," + width + "," + height + ")");
    }

    /**
     * see bounce.Painter.fillRect
     */
    @Override
    public void fillRect(int x, int y, int width, int height) {
        log.append("(filled-rectangle " + x + "," + y + "," + width + "," + height + ")");
    }

    /**
     * see bounce.Painter.drawOval
     */
    @Override
    public void drawOval(int x, int y, int width, int height) {
        log.append("(oval " + x + "," + y + "," + width + "," + height + ")");
    }

    /**
     * see bounce.Painter.drawLine
     */
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        log.append("(line " + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    /**
     * see bounce.Painter.drawRect
     * Always returns null.
     */
    @Override
    public Color getColor() {
        return null;
    }

    /**
     * see bounce.Painter.setColor
     */
    @Override
    public void setColor(Color color) {
        log.append("(color " + color.toString() + ")");
    }

    /**
     * see bounce.Painter.drawCenteredText
     */
    @Override
    public void drawCenteredText(String text, int x, int y) {
        log.append("(centered-text " + text + "," + x + "," + y + ")");
    }

    /**
     * see bounce.Painter.translate
     */
    @Override
    public void translate(int x, int y) {
        log.append("(translate " + x + "," + y + ")");
    }

    /**
     * see bounce.Painter.drawImage
     */
    @Override
    public void drawImage(Image img, int x, int y, int width, int height) {
    }

    @Override
    public void drawGemShape(int x, int y, int width, int height) {

    }
}