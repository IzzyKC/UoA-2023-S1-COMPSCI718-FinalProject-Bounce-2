package bounce.views;

import java.awt.Dimension;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import bounce.NestingShape;
import bounce.RectangleShape;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import bounce.Shape;
import bounce.ShapeModel;

/**
 * Class to test the event notification mechanism of class
 * Task3.
 *
 * @author Ian Warren
 */
public class TestTask2 {

    private ShapeModel model;
    private NestingShape root;
    private NestingShape emptyNest;
    private Shape simpleShape;
    private Shape newShape;
    private Task2 adapter;
    private boolean listenerMethodCalled;


    /**
     * Creates a NestingShape structure as the fixture for each test case.
     */
    @Before
    public void setUpShapeModel() {
        // Create model.
        model = new ShapeModel(new Dimension(500, 500));
        root = model.root();

        // Create shapes.
        emptyNest = new NestingShape(0, 0, 1, 1, 100, 100);
        simpleShape = new RectangleShape(0, 0, 1, 1, 20, 20);
        newShape = new RectangleShape(0, 0, 1, 1, 20, 20);

        // Populate model.
        model.add(emptyNest, root);
        model.add(simpleShape, root);

        // Create the adapter.
        adapter = new Task2(model);

        // Register adapter as a listener of the model.
        model.addShapeModelListener(adapter);

        listenerMethodCalled = false;
    }

    /**
     * Checks that calling Task2's update() method with a ShapeModelEvent that
     * describes a Shape's removal from a ShapeModel results in a correctly
     * constructed TreeModelEvent being sent to a registered TreeModelListener.
     */
    @Test
    public void test_ShapeRemoval() {
        adapter.addTreeModelListener(new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
                // Wrong TreeModelListener method called.
                fail();
            }

            public void treeNodesInserted(TreeModelEvent e) {
                // Wrong TreeModelListener method called.
                fail();
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                listenerMethodCalled = true;

                /* Unpack event. */
                int[] indices = e.getChildIndices();
                Object[] children = e.getChildren();
                Object[] path = e.getPath();

                /*
                 * Check the indices array identifies the index position of the
                 * removed node BEFORE it was removed.
                 */
                assertNotNull(indices);
                assertEquals(1, indices.length);
                assertEquals(1, indices[0]);

                /* Check the children array contains the single removed Shape. */
                assertNotNull(children);
                assertEquals(1, children.length);
                assertSame(simpleShape, children[0]);

                /*
                 * Check the path to the former parent of the changed node is
                 * correct.
                 */
                assertEquals(1, path.length);
                assertSame(root, path[0]);
            }

            public void treeStructureChanged(TreeModelEvent e) {
                fail();
            }
        });

        /*
         * Cause the ShapeModel to fire a ShapeModelEvent describing a shape
         * addition.
         */
        model.remove(simpleShape);
        assertTrue(listenerMethodCalled);
    }

    /**
     * Checks that calling Task2's update() method with a ShapeModelEvent that
     * describes a Shape's addition to a ShapeModel results in a correctly
     * constructed TreeModelEvent being sent to a registered TreeModelListener.
     */
    @Test
    public void test_shapeAdded() {
        adapter.addTreeModelListener(new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
                // Wrong TreeModelListener method called.
                fail();
            }

            public void treeNodesInserted(TreeModelEvent e) {
                listenerMethodCalled = true;

                /* Unpack event. */
                int[] indices = e.getChildIndices();
                Object[] children = e.getChildren();
                Object[] path = e.getPath();

                /*
                 * Check the indices array identifies the index position of the
                 * inserted node (i.e. after insertion).
                 */
                assertNotNull(indices);
                assertEquals(1, indices.length);
                assertEquals(0, indices[0]);

                /* Check the children array contains the single removed . */
                assertNotNull(children);
                assertEquals(1, children.length);
                assertSame(newShape, children[0]);

                /* Check the path to the inserted node's parent is correct. */
                assertEquals(2, path.length);
                assertSame(root, path[0]);
                assertSame(emptyNest, path[1]); // Now not empty!
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                // Wrong TreeModelListener method called.
                fail();
            }

            public void treeStructureChanged(TreeModelEvent e) {
                // Wrong TreeModelListener method called.
                fail();
            }
        });

        /*
         * Cause the ShapeModel to fire a ShapeModelEvent describing a shape
         * addition.
         */
        model.add(newShape, emptyNest);
        assertTrue(listenerMethodCalled);
    }
}
