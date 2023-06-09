package bounce;

/**
 * Solution class.
 */

import java.util.ArrayList;
import java.util.List;

public class NestingShape extends Shape {

    private List<Shape> shapes;

    public NestingShape(int x, int y, int deltaX, int deltaY, int width,
                        int height) {
        super(x, y, deltaX, deltaY, width, height, null);
        shapes = new ArrayList<>();
    }

    public NestingShape(int x, int y, int deltaX, int deltaY, int width,
                        int height, String text) {
        super(x, y, deltaX, deltaY, width, height, text);
        shapes = new ArrayList<>();
    }

    protected void doPaint(Painter painter) {
        painter.drawRect(x, y, width, height);

        // Cause painting of shapes to be relative to this shape.
        painter.translate(x, y);

        for (Shape shape : shapes) {
            shape.paint(painter);
        }

        // Restore graphics origin.
        painter.translate(-x, -y);
    }

    @Override
    public void move(int width, int height) {
        // Move this nesting shape.
        super.move(width, height);

        // Move contained shapes.
        for (Shape shape : shapes) {
            shape.move(this.width, this.height);
        }
    }

    public void add(Shape shape) throws IllegalArgumentException {
        if (shape.parent() != null || outOfBounds(shape)) {
            throw new IllegalArgumentException();
        }


        shapes.add(shape);
        shape.setParent(this);
    }

    public void remove(Shape shape) {
        shapes.remove(shape);
        shape.setParent(null);
    }

    // Throws IndexOutOfBounds - unchecked exception.
    public Shape shapeAt(int index) throws IndexOutOfBoundsException {
        return shapes.get(index);
    }

    public int shapeCount() {
        return shapes.size();
    }

    // Returns -1 or the index.
    public int indexOf(Shape shape) {
        return shapes.indexOf(shape);
    }

    public boolean contains(Shape shape) {
        return shapes.contains(shape);
    }

    private boolean outOfBounds(Shape s) {
        boolean result = false;

        if ((s.x() + s.width() > this.width)
                || (s.y() + s.height() > this.height)) {
            result = true;
        }
        return result;
    }
}

