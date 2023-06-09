package bounce.forms.util;

/**
 * FormElement represents part of a Form.
 * <p>
 * A FormElement is essentially a collection of named fields, where each field
 * has a type and a value. A Field's type is a Java class, and its value is an
 * instance of the type (class).
 * <p>
 * This interface provides operations allowing clients to add fields, to
 * update field values, and to query field values.
 *
 * @author Ian Warren
 */
public interface FormElement {

    /**
     * Adds a field, with a specified name and type and with an optional
     * default value, to this FormElement.
     *
     * @param name         the name of the field to add.
     * @param defaultValue the default value for the field, or null.
     * @param type         the type of the field to add.
     */
    <T> void addField(String name, T defaultValue, Class<T> type);

    /**
     * Updates the value of a specified field.
     *
     * @param name  the name of the field whose value is to be updated.
     * @param value the new value for the field.
     * @throws IllegalArgumentException if the field's type is incompatible
     *                                  with the type of the value parameter. This happens, for example,
     *                                  when value's class is not a subclass of the field's class.
     */
    void putFieldValue(String name, Object value) throws IllegalArgumentException;

    /**
     * Returns the current value for a specified field.
     *
     * @param type the type of the field whose value is required.
     * @param name the name of the field who value is required.
     * @return the field's value, or null if this FormElement does not contain
     * a field of the specified name and type.
     */
    <T> T getFieldValue(Class<? extends T> type, String name);
}
