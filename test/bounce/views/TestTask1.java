package bounce.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;

import javax.swing.tree.TreeModel;

import bounce.NestingShape;
import bounce.RectangleShape;
import org.junit.Before;
import org.junit.Test;

import bounce.Shape;
import bounce.ShapeModel;

/**
 * Class to test the TreeModel implementation of class Task1.
 *
 * @author Ian Warren
 */
public class TestTask1 {

    private NestingShape root;
    private NestingShape emptyNest;
    private Shape simpleShape;
    private TreeModel adapter;

    /**
     * Creates a simple Shape hierarchy as the common fixture for all test
     * case methods.
     */
    @Before
    public void setUpShapeModel() {
        ShapeModel model = new ShapeModel(new Dimension(500, 500));
        adapter = new Task1(model);

        root = model.root();
        emptyNest = new NestingShape(0, 0, 2, 2, 100, 100);
        simpleShape = new RectangleShape(0, 0, 1, 1, 50, 50);

        model.add(emptyNest, root);
        model.add(simpleShape, root);
    }

    /**
     * Checks whether getRoot() returns the root NestingShape of a the
     * ShapeModel as expected.
     */
    @Test
    public void test_getRoot() {
        NestingShape nest = (NestingShape) adapter.getRoot();
        assertSame(root, nest);
    }

    /**
     * Checks whether getChildCount() returns zero for an empty NestingShape.
     */
    @Test
    public void test_getChildCount_OnEmptyNestingShape() {
        int numberOfChildren = adapter.getChildCount(emptyNest);
        assertEquals(numberOfChildren, emptyNest.shapeCount());
    }

    /**
     * Checks whether getChildCount() returns the actual number of children
     * contained in a NestingShape.
     */
    @Test
    public void test_getChildCount_OnNonEmptyNestingShape() {
        int expectedNumberOfChildren = adapter.getChildCount(root);
        int actualNumberOfChidren = root.shapeCount();

        assertEquals(expectedNumberOfChildren, actualNumberOfChidren);
    }

    /**
     * Checks whether getChildCount() returns zero when invoked with an
     * argument that refers to a simple Shape instance.
     */
    @Test
    public void test_getChildCount_OnSimpleShape() {
        int actualNumberOfChildren = adapter.getChildCount(simpleShape);
        assertEquals(0, actualNumberOfChildren);
    }

    /**
     * Checks whether isLeaf() returns false, as required, when supplied with
     * an empty NestingShape as argument.
     */
    @Test
    public void test_isLeaf_OnEmptyNestingShape() {
        assertFalse(adapter.isLeaf(emptyNest));
    }

    /**
     * Checks whether isLeaf() returns false, as required, when supplied with
     * a NestingShape that contains children.
     */
    @Test
    public void test_isLeaf_OnNonEmptyNestingShape() {
        assertFalse(adapter.isLeaf(root));
    }

    /**
     * Checks whether isLeaf() returns true, as required, when supplied with
     * a simple Shape as argument.
     */
    @Test
    public void test_isLeaf_OnSimpleShape() {
        assertTrue(adapter.isLeaf(simpleShape));
    }

    /**
     * Checks whether getChild() correctly returns a reference to a
     * particular child Shape object. The arguments supplied to getChild() are
     * a reference to a NestingShape and the index position within the
     * NestingShape's collection of children that identifies the child Shape
     * sought. This particular test supplies a valid index argument.
     */
    @Test
    public void test_getChild_OnNestingShapeWithInRangeIndex() {
        assertSame(emptyNest, adapter.getChild(root, 0));
    }

    /**
     * Checks whether getChild() returns null, as specified, when an index
     * argument value is supplied that is out of range. The argument is out of
     * range if it is negative or >= the number of children contained in the
     * NestingShape, which is the first argument.
     */
    @Test
    public void test_getChild_OnNestingShapeWithOutOfRangeIndex() {
        assertNull(adapter.getChild(root, 2));
    }

    /**
     * Checks whether getChild() returns null, as it should when supplied
     * with a reference to a simple Shape object as the first argument.
     */
    @Test
    public void test_getChild_OnSimpleShape() {
        assertNull(adapter.getChild(simpleShape, 0));
    }

    /**
     * Checks whether getIndexOfChild() returns -1 as specified when supplied
     * with a reference to a Shape (the second argument) which is not a child of
     * the NestingShape supplied as the first argument.
     */
    @Test
    public void test_getIndexOfChild_OnNestingShapeWithNonChild() {
        Shape newShape = new RectangleShape(0, 0, 1, 1, 10, 10);
        assertEquals(-1, adapter.getIndexOfChild(root, newShape));
    }

    /**
     * Checks whether getIndexOfChild() returns the correct index position of
     * a Shape (second argument) that is a child of a NestingShape (the first
     * argument).
     */
    @Test
    public void test_getIndexOfChild_OnNestingShapeWithChild() {
        assertEquals(1, adapter.getIndexOfChild(root, simpleShape));
    }

    /**
     * Checks whether getIndexOfChild() returns -1 when the first argument
     * refers to a simple Shape.
     */
    @Test
    public void test_getIndexOfChild_OnSimpleShape() {
        assertEquals(-1, adapter.getIndexOfChild(simpleShape, root));
    }

}
