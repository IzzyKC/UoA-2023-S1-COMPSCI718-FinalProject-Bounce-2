package bounce;

import java.awt.Color;

/**
 * Solution class.
 */
public class DynamicRectangleShape extends RectangleShape {

    private static final Color DEFAULT_COLOR = Color.RED;

    protected boolean filled;
    protected Color color;

    public DynamicRectangleShape(int x, int y, int deltaX, int deltaY, int width,
                                 int height) {
        super(x, y, deltaX, deltaY, width, height);
        this.filled = true;
        this.color = DEFAULT_COLOR;
    }

    public DynamicRectangleShape(int x, int y, int deltaX, int deltaY, int width,
                                 int height, Color color) {
        super(x, y, deltaX, deltaY, width, height);
        this.filled = true;
        this.color = color;
    }

    public DynamicRectangleShape(int x, int y, int deltaX, int deltaY, int width,
                                 int height, String text, Color color) {
        super(x, y, deltaX, deltaY, width, height, text);
        this.filled = true;
        this.color = color;
    }

    @Override
    public void move(int width, int height) {
        int preDeltaX = deltaX;
        int preDeltaY = deltaY;
        super.move(width, height);

        if ((preDeltaY < 0 && deltaY > 0) ||
                (preDeltaY > 0 && deltaY < 0)) {
            // Bounced off horizontal wall.
            filled = false;
        }
        if ((preDeltaX < 0 && deltaX > 0) ||
                (preDeltaX > 0 && deltaX < 0)) {
            // Bounced off vertical wall.
            filled = true;
        }
    }

    @Override
    protected void doPaint(Painter painter) {
        if (this.filled) {
            Color defaultColor = painter.getColor();
            painter.setColor(this.color);
            painter.fillRect(this.x, this.y, this.width, this.height);
            painter.setColor(defaultColor);
        } else {
            super.doPaint(painter);
        }
    }
}

