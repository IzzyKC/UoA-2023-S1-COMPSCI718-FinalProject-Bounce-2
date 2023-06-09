package bounce.forms.util;

/**
 * Interface to be implemented by classes that process Form data.
 * <p>
 * Implementing classes typically implement process() by reading data from the
 * supplied form, and performing some action based on the data.
 *
 * @author Ian Warren
 */
public interface FormHandler {
    void processForm(Form form);
}
