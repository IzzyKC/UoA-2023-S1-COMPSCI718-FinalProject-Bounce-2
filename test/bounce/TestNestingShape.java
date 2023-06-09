package bounce;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test class NestingShape according to its specification.
 *
 * @author Ian Warren
 */
public class TestNestingShape {

    private NestingShape topLevelNest;
    private NestingShape midLevelNest;
    private NestingShape bottomLevelNest;
    private Shape simpleShape;


    /**
     * Creates a Shape composition hierarchy with the following structure:
     * NestingShape (topLevelNest)
     * |
     * --- NestingShape (midLevelNest)
     * |
     * --- NestingShape (bottomLevelNest)
     * |
     * --- RectangleShape (simpleShape)
     */
    @Before
    public void setUpNestedStructure() throws Exception {
        topLevelNest = new NestingShape(0, 0, 2, 2, 100, 100);
        midLevelNest = new NestingShape(0, 0, 2, 2, 50, 50);
        bottomLevelNest = new NestingShape(5, 5, 2, 2, 10, 10);
        simpleShape = new RectangleShape(1, 1, 1, 1, 5, 5);

        midLevelNest.add(bottomLevelNest);
        midLevelNest.add(simpleShape);
        topLevelNest.add(midLevelNest);
    }

    /**
     * Checks that methods move() and paint() correctly move and paint a
     * NestingShape's contents.
     */
    @Test
    public void testBasicMovementAndPainting() {
        Painter painter = new MockPainter();

        topLevelNest.move(500, 500);
        topLevelNest.paint(painter);
        assertEquals("(rectangle 2,2,100,100)(translate 2,2)(rectangle 2,2,50,50)(translate 2,2)(rectangle 7,7,10,10)(translate 7,7)(translate -7,-7)(rectangle 2,2,5,5)(translate -2,-2)(translate -2,-2)", painter.toString());
    }

    /**
     * Checks that method add successfuly adds a valid Shape, supplied as
     * argument, to a NestingShape instance.
     */
    @Test
    public void testAdd() {
        // Check that topLevelNest and midLevelNest mutually reference each other.
        assertSame(topLevelNest, midLevelNest.parent());
        assertTrue(topLevelNest.contains(midLevelNest));

        // Check that midLevelNest and bottomLevelNest mutually reference each other.
        assertSame(midLevelNest, bottomLevelNest.parent());
        assertTrue(midLevelNest.contains(bottomLevelNest));
    }

    /**
     * Check that method add throws an IlegalArgumentException when an attempt
     * is made to add a Shape to a NestingShape instance where the Shape
     * argument is already part of some NestingShape instance.
     */
    @Test
    public void testAddWithArgumentThatIsAChildOfSomeOtherNestingShape() {
        try {
            topLevelNest.add(bottomLevelNest);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected action. Ensure the state of topLevelNest and
            // bottomLevelNest has not been changed.
            assertFalse(topLevelNest.contains(bottomLevelNest));
            assertSame(midLevelNest, bottomLevelNest.parent());
        }
    }

    /**
     * Check that method add throws an IllegalArgumentException when an attempt
     * is made to add a shape that will not fit within the bounds of the
     * proposed NestingShape object.
     */
    @Test
    public void testAddWithOutOfBoundsArgument() {
        Shape rectangle = new RectangleShape(80, 80, 2, 2, 50, 50);

        try {
            topLevelNest.add(rectangle);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected action. Ensure the state of topLevelNest and
            // rectangle has not been changed.
            assertFalse(topLevelNest.contains(rectangle));
            assertNull(rectangle.parent());
        }
    }

    /**
     * Check that method remove breaks the two-way link between the Shape
     * object that has been removed and the NestingShape it was once part of.
     */
    @Test
    public void testRemove() {
        topLevelNest.remove(midLevelNest);
        assertFalse(topLevelNest.contains(midLevelNest));
        assertNull(midLevelNest.parent());
    }

    /**
     * Check that method shapeAt returns the Shape object that is held at a
     * specified position within a NestingShape instance.
     */
    @Test
    public void testShapeAt() {
        assertSame(midLevelNest, topLevelNest.shapeAt(0));
    }

    /**
     * Check that method shapeAt throws a IndexOutOfBoundsException when called
     * with an invalid index argument.
     */
    @Test
    public void testShapeAtWithInvalidIndex() {
        try {
            topLevelNest.shapeAt(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected action.
        }
    }

    /**
     * Check that method shapeCount returns zero when called on a NestingShape
     * object without children.
     */
    @Test
    public void testShapeCountOnEmptyParent() {
        assertEquals(0, bottomLevelNest.shapeCount());
    }

    /**
     * Check that method shapeCount returns the number of children held within
     * a NestingShape instance - where the number of children > 0.
     */
    @Test
    public void testShapeCountOnNonEmptyParent() {
        assertEquals(2, midLevelNest.shapeCount());
    }

    /**
     * Check that method indexOf returns the index position within a
     * NestingShape instance of a Shape held within the NestingShape.
     */
    @Test
    public void testIndexOfWith() {
        assertEquals(0, topLevelNest.indexOf(midLevelNest));
        assertEquals(1, midLevelNest.indexOf(simpleShape));
    }

    /**
     * Check that method indexOf returns -1 when called with an argument that
     * is not part of the NestingShape callee object.
     */
    @Test
    public void testIndexOfWithNonExistingChild() {
        assertEquals(-1, topLevelNest.indexOf(bottomLevelNest));
    }

    /**
     * Check that Shape's path method correctly returns the path from the root
     * NestingShape object through to the Shape object that path is called on.
     */
    @Test
    public void testPath() {
        List<Shape> path = simpleShape.path();

        assertEquals(3, path.size());
        assertSame(topLevelNest, path.get(0));
        assertSame(midLevelNest, path.get(1));
        assertSame(simpleShape, path.get(2));
    }

    /**
     * Check that Shape's path method correctly returns a singleton list
     * containing only the callee object when this Shape object has no parent.
     */
    @Test
    public void testPathOnShapeWithoutParent() {
        List<Shape> path = topLevelNest.path();

        assertEquals(1, path.size());
        assertSame(topLevelNest, path.get(0));
    }
}
