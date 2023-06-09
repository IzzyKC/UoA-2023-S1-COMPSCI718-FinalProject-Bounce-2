package bounce.forms.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;


/**
 * FormElementComponent is an implementation of the FormElement interface.
 * FormElementComponent inherits class JPanel, and so is a visual FormElement.
 * Instances of FormElementComponent are added to FormComponents (visual
 * forms).
 *
 * @author Ian Warren
 * @see bounce.forms.util.FormElement
 */
@SuppressWarnings("serial")
public abstract class FormElementComponent extends JPanel implements FormElement {
    /*
     *  Each field within an FormElementComponent is represented by an entry
     *  in _fields and a corresponding entry in _fieldTypes.
     *
     *  For _fields, the key is field-name (a String) and the value is an
     *  Object. For _fieldTypes, the key is field-name (a String) and the value
     *  is a Class.
     */
    private Map<String, Object> fields;
    private Map<String, Class<?>> fieldTypes;

    /**
     * Creates an empty FormElementComponent.
     */
    public FormElementComponent() {
        fields = new HashMap<String, Object>();
        fieldTypes = new HashMap<String, Class<?>>();
    }

    @Override
    public <T> void addField(String name, T defaultValue, Class<T> type) {
        fields.put(name, defaultValue);
        fieldTypes.put(name, type);
    }

    @Override
    public void putFieldValue(String name, Object value) throws IllegalArgumentException {
        Class<?> fieldType = fieldTypes.get(name);

        if (fieldType.isAssignableFrom(value.getClass())) {
            // Update the field's value.
            fields.put(name, value);
        } else {
            // Field name is associated with a type that's incompatible with
            // value's type.
            throw new IllegalArgumentException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFieldValue(Class<? extends T> type, String name) {
        T result = null;

        Object obj = fields.get(name);
        if ((obj != null) && (type.isAssignableFrom(obj.getClass()))) {
            result = (T) obj;
        }

        return result;
    }
}
