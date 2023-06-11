package bounce.forms;

/**
 * FormResolver is a utility class that implements methods for returning Form 
 * and FormHandler objects for particular Shape subclasses.
 * 
 * A particular Form/FormHandler combination allows values for Shape attributes
 * to be entered/selected by the user and for those values to be used by a 
 * FormHandler when instantiating a particular Shape subclass. 
 *  
 * @author Ian warren
 * 
 */
import bounce.*;
import bounce.forms.util.FormComponent;
import bounce.forms.util.FormHandler;

public class FormResolver {

	/**
	 * Returns a FormComponent object for entering attribute values for the 
	 * specified Shape subclass.
	 * 
	 * @param shapeClass the subclass of Shape for which a Form is required. 
	 */
	public static FormComponent getForm(Class<? extends Shape> shapeClass) {
		FormComponent form = new FormComponent();
		
		form.addFormElement(new ShapeFormElement());
		
		if(shapeClass == DynamicRectangleShape.class) {
			form.addFormElement(new ColourFormElement());
		} else if(shapeClass == ImageRectangleShape.class) {
			form.addFormElement(new ImageFormElement());
		}
		
		return form;
	}
	
	
	/**
	 * Returns a FormHandler implementation for creating an instance of a 
	 * specified Shape subclass. In response to a process(Form) call, a 
	 * FormHandler extracts form data and uses this to instantiate a Shape 
	 * subclass, to add the new instance to a ShapeModel and as a child of an
	 * existing NestingShape parent.
	 * 
	 * @param shapeClass the subclass of Shape to be instantiated.
	 * @param model the ShapeModel to which the new Shape instance should be 
	 *        added.
	 * @param parent the NestingShape object that will serve as the parent of
	 *        the newly created Shape object.
	 */
	public static FormHandler getFormHandler(Class<? extends Shape> shapeClass, ShapeModel model, NestingShape parent) {
		FormHandler handler = null;
		
		if(shapeClass == DynamicRectangleShape.class) {
			handler = new DynamicRectangleShapeFormHandler(model, parent);
		} else if(shapeClass == ImageRectangleShape.class) {
			handler = new ImageShapeFormHandler(model, parent);
		} else {
			handler = new ShapeFormHandler(shapeClass, model, parent);
		}
		
		return handler;
	}
}
