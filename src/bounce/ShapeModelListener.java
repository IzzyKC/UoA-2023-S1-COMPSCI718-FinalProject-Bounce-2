package bounce;

/**
 * Interface to be implemented by classes whose instances need to be notified
 * of changes to a ShapeModel. A ShapeModel calls method upate() on all
 * registered ShapeModelListeners whenever its state changes.
 *
 * @author Ian Warren
 */
public interface ShapeModelListener {
    /**
     * Notifies a ShapeModelListener that a ShapeModel that it has registered
     * interest in has changed.
     *
     * @param event describes the way in which a particular ShapeModel object
     *              has changed.
     */
    void update(ShapeModelEvent event);
}
