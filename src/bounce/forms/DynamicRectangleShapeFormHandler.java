package bounce.forms;

import java.awt.Color;

import bounce.DynamicRectangleShape;
import bounce.NestingShape;
import bounce.ShapeModel;
import bounce.forms.util.Form;
import bounce.forms.util.FormHandler;

public class DynamicRectangleShapeFormHandler implements FormHandler {
    private ShapeModel model;
    private NestingShape parentOfNewShape;

    public DynamicRectangleShapeFormHandler(ShapeModel model,
                                            NestingShape parent) {
        this.model = model;
        parentOfNewShape = parent;
    }

    @Override
    public void processForm(Form form) {
        int x = 0;
        int y = 0;
        int deltaX = form.getFieldValue(Integer.class, ShapeFormElement.DELTA_X);
        int deltaY = form.getFieldValue(Integer.class, ShapeFormElement.DELTA_Y);
        int width = form.getFieldValue(Integer.class, ShapeFormElement.WIDTH);
        int height = form.getFieldValue(Integer.class, ShapeFormElement.HEIGHT);
        String text = form.getFieldValue(String.class, ShapeFormElement.TEXT);
        Color colour = (Color) form
                .getFieldValue(Color.class, ColourFormElement.COLOUR);

        DynamicRectangleShape newShape = new DynamicRectangleShape(x, y,
                deltaX, deltaY, width, height, text, colour);
        model.add(newShape, parentOfNewShape);
    }

}
