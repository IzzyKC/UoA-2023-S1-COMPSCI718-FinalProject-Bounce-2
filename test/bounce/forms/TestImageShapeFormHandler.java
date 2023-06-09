package bounce.forms;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.io.File;

import bounce.NestingShape;
import org.junit.Before;
import org.junit.Test;

import bounce.ShapeModel;
import bounce.forms.util.Form;
import bounce.forms.util.FormElement;
import bounce.forms.util.FormHandler;

/**
 * Test case to determine whether class ImageShapeFormHandler has a
 * satisfactory response time.
 *
 * @author Ian Warren
 */
public class TestImageShapeFormHandler {

    private FormHandler handler;

    /**
     * Creates an ImageShapeFormHandler object for testing.
     */
    @Before
    public void setUpHandler() {
        // To create an ImageShapeFormHandler, a ShapeModel and a NestingShape
        // are required.
        ShapeModel model = new ShapeModel(new Dimension(500, 500));
        NestingShape nest = new NestingShape(0, 0, 1, 1, 100, 100);

        handler = new ImageShapeFormHandler(model, nest);
    }

    /**
     * Tests that ImageShapeFormHandler's processForm() method executes
     * sufficiently quickly to run on the Event Dispatch thread. The
     * processForm() method should delegate image loading and scaling to a
     * background thread, and so processForm() should return control to the
     * caller within milliseconds.
     */
    @Test
    public void testHandlerExecutionTime() {
        long startTime = System.currentTimeMillis();
        handler.processForm(new MockForm());
        long elapsedTime = System.currentTimeMillis() - startTime;

        assertTrue(elapsedTime <= 50);
        System.out.println("Elapsed time: " + elapsedTime);
    }

    /*
     * Helper class that simulates a GUI form for collecting ImageRectangleShape
     * attribute values. The ImageShapeFormHandler under test uses the form to
     * acquire sufficient data to create an ImageRectangleShape. Part of the
     * data returned by the form is a large image file that takes some time for
     * the ImageShapeFormHandler to load and scale.
     */
    private class MockForm implements Form {
        // Location of image file - the user's home directory. For Windows
        // machines this is C:\Users\<user-id>.
        private static final String FILE_LOCATION = "user.home";

        // Name of image file.
        private static final String IMAGE_FILE_PATH = "mount-shuksan.jpg";

        @Override
        public void addFormElement(FormElement element) {
            // No implementation necessary
        }

        @Override
        public void setFormHandler(FormHandler handler) {
            // No implementation necessary.
        }

        @Override
        public void prepare() {
            // No implementation necessary.
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getFieldValue(Class<? extends T> type, String name) {
            Object result = null;

            if (type.isAssignableFrom(Integer.class)) {
                if (name.equals(ShapeFormElement.WIDTH)) {
                    result = new Integer(80);
                } else if (name.equals(ShapeFormElement.HEIGHT)) {
                    result = new Integer(25);
                } else if (name.equals(ShapeFormElement.DELTA_X)) {
                    result = new Integer(2);
                } else if (name.equals(ShapeFormElement.DELTA_Y)) {
                    result = new Integer(2);
                }
            } else if (type.isAssignableFrom(String.class) && (name.equals(ShapeFormElement.TEXT))) {
                result = null;
            } else if (type.isAssignableFrom(java.io.File.class) && (name.equals(ImageFormElement.IMAGE))) {
                String path = System.getProperty(FILE_LOCATION);
                result = new File(path, IMAGE_FILE_PATH);
            }

            return (T) result;
        }
    }
}
