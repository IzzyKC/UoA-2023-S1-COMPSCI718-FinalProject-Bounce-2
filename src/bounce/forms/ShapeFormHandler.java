package bounce.forms;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import bounce.NestingShape;
import bounce.Shape;
import bounce.ShapeModel;
import bounce.forms.util.Form;
import bounce.forms.util.FormHandler;

/**
 * FormHandler implementation for reading form data and using this to
 * instantiate a specified Shape subclass.
 *
 * @author Ian Warren
 */
public class ShapeFormHandler implements FormHandler {
    private Class<? extends Shape> classToInstantiate;
    private ShapeModel model;
    private NestingShape parentOfNewShape;

    /**
     * Creates a ShapeFormHandler.
     *
     * @param cls    the Shape subclass to instantiate.
     * @param model  the ShapeModel to which the handler should add a newly
     *               constructed Shape instance.
     * @param parent the NestingShape object that will serve as the parent for
     *               a new Shape object.
     */
    public ShapeFormHandler(Class<? extends Shape> cls,
                            ShapeModel model,
                            NestingShape parent) {
        classToInstantiate = cls;
        this.model = model;
        parentOfNewShape = parent;
    }

    /**
     * Reads form data that describes a shape. Based on the data, this
     * ShapeFormHandler creates a new instance of a Shape subclass (specified
     * at construction time). The new instance is then added to a ShapeModel
     * and to a NestingShape within the model. The ShapeModel and NestingShape
     * objects are supplied when this ShapeFormHandler is created.
     *
     * @param form the Form that contains Shape data.
     */
    @Override
    public void processForm(Form form) {
        try {
            // Attempt to find a 7-argument constructor for the Shape subclass.
            Constructor<? extends Shape> cons = classToInstantiate.getConstructor(
                    java.lang.Integer.TYPE, java.lang.Integer.TYPE,
                    java.lang.Integer.TYPE, java.lang.Integer.TYPE,
                    java.lang.Integer.TYPE, java.lang.Integer.TYPE,
                    java.lang.String.class);

            int x = 0;
            int y = 0;
            int deltaX = form.getFieldValue(Integer.class, ShapeFormElement.DELTA_X);
            int deltaY = form.getFieldValue(Integer.class, ShapeFormElement.DELTA_Y);
            int width = form.getFieldValue(Integer.class, ShapeFormElement.WIDTH);
            int height = form.getFieldValue(Integer.class, ShapeFormElement.HEIGHT);
            String text = form.getFieldValue(String.class, ShapeFormElement.TEXT);

            // Instantiate shape class, calling the 7-argument constructor.
            Shape newShape = (Shape) cons.newInstance(x, y, deltaX, deltaY, width, height, text);

            model.add(newShape, parentOfNewShape);

        } catch (NoSuchMethodException e) {
            // Thrown if a constructor with the specified arguments is not
            // defined by the class.
            System.err.println(e);
        } catch (SecurityException e) {
            // Thrown if a security manager is set and the running program is
            // not permitted to load classes at run-time.
            System.err.println(e);
        } catch (InstantiationException e) {
            // Thrown if the loaded class cannot be instantiated, e.g. the
            // class might be abstract.
            System.err.println(e);
        } catch (IllegalAccessException e) {
            // Thrown if the class' constructor is hidden, e.g. private.
            System.err.println(e);
        } catch (IllegalArgumentException e) {
            // Thrown if the actual arguments are incompatible with the formal
            // arguments.
            System.err.println(e);
        } catch (InvocationTargetException e) {
            // Thrown if the constructor itself throws an exception.
            System.err.println(e);
        }
    }

}
