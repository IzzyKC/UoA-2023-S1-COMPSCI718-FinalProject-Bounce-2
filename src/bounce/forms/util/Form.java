package bounce.forms.util;

/**
 * Form is an interface that represents a form containing one or more
 * FormElements. FormElements, in turn, contain fields that can store data
 * values.
 * <p>
 * A Form also has a FormHandler that is responsible for processing the data
 * stored in a Form's formElements' fields.
 * <p>
 * Form can have many implementations, visual or non-visual.
 *
 * @author Ian Warren
 */
public interface Form {

    /**
     * Adds a FormElement to a Form.
     *
     * @param element the FormElement to add.
     */
    void addFormElement(FormElement element);

    /**
     * Sets the FormHandler for a Form.
     *
     * @param handler the FormHandler that will be used to process the Form's
     *                data.
     */
    void setFormHandler(FormHandler handler);

    /**
     * Prepares the Form for use. This method should perform any processing
     * (e.g. laying out a visual form's components) before it's used to enter
     * data.
     */
    void prepare();

    /**
     * Returns the value for a specified field within a Form.
     *
     * @param type the type of the field whose value is requested.
     * @param name the name of the field whose value is requested.
     * @return the value of the required field, or null if there is no field
     * that matches the specified name and type.
     */
    <T> T getFieldValue(Class<? extends T> type, String name);
}
