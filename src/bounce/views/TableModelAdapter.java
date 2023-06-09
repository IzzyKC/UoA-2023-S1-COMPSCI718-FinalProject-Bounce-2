package bounce.views;

import javax.swing.table.AbstractTableModel;

import bounce.NestingShape;
import bounce.Shape;
import bounce.ShapeModelEvent;
import bounce.ShapeModelListener;

/**
 * Adapter class that adapts Shape/NestingShape to the TableModel target
 * interface. An instance of TableModelAdapter thus allows a Shape/NestingShape
 * object to be displayed by a JTable Swing component. To save implementing the
 * TableModel interface from scratch this class extends AbstractTableModel and
 * simply overrides selected methods as necessary.
 *
 * @author Ian Warren
 */
@SuppressWarnings("serial")
public class TableModelAdapter extends AbstractTableModel implements ShapeModelListener {
    /*
     *  Adaptee, a reference to a Shape or a NestingShape object that is to be
     *  represented by this TableModel implementation.
     */
    private Shape adaptee;

    // Column names for table.
    private static final String[] COLUMN_NAMES = {"Type", "X-pos", "Y-pos", "X-delta", "Y-delta", "Width", "Height", "Text"};

    /**
     * Creates a TableModelAdapter and sets Shape/NestingShape that is to be
     * represented by this TableModelAdapter.
     */
    public TableModelAdapter(Shape shape) {
        adaptee = shape;
    }

    /**
     * Returns the number of columns in this TableModel implementation. A
     * JTable component will call this method when configured with an instance
     * of this class.
     */
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    /**
     * Returns the name of a specified column.
     */
    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    /**
     * Returns the number of rows in this TableModel implementation. If this
     * TableModelAdapter object represents a simple Shape, this method will
     * return 1 as a single row is sufficient to display one simple Shape's
     * attributes. If this TableModelAdapter is set with a NestingShape, this
     * method returns its number of children; one row is then used for each
     * child Shape.
     */
    @Override
    public int getRowCount() {
        int rowCount = 1;

        if (adaptee instanceof NestingShape) {
            NestingShape nestingShape = (NestingShape) adaptee;
            rowCount = nestingShape.shapeCount();
        }
        return rowCount;
    }

    /**
     * Returns the value at a particular cell within this TableModel
     * implementation.
     */
    @Override
    public Object getValueAt(int row, int col) {
        Shape targetShape = adaptee;
        Object result = null;

        if (adaptee instanceof NestingShape) {
            NestingShape nestingShape = (NestingShape) adaptee;
            targetShape = nestingShape.shapeAt(row);
        }

        switch (col) {
            case 0: // Type
                result = targetShape.toString();
                break;
            case 1: // X-Pos
                result = targetShape.x();
                break;
            case 2: // Y-Pos.
                result = targetShape.y();
                break;
            case 3: // X-delta.
                result = targetShape.deltaX();
                break;
            case 4: // Y-delta.
                result = targetShape.deltaY();
                break;
            case 5: // Width.
                result = targetShape.width();
                break;
            case 6: // Height.
                result = targetShape.height();
                break;
            case 7: // Text.
                result = targetShape.text();
                break;
        }
        return result;
    }

    /**
     * Sets the adaptee Shape/NestingShape object that should be represented by
     * this TableModelAdapter instance.
     */
    public void setAdaptee(Shape shape) {
        adaptee = shape;

        /*
         * Cause any TableModelListeners (e.g. a JTable component) to be
         * notified that the data stored in this TableModelAdapter has changed.
         * A JTable component would respond by rebuilding its view of the model.
         */
        fireTableDataChanged();
    }

    /**
     * Called by a ShapeModel object when it has changed in some way. In
     * response to an update() call this TableModelAdapter updates itself
     * if necessary and notifies its TableModelListeners.
     */
    @Override
    public void update(ShapeModelEvent event) {
        // Unpack event.
        ShapeModelEvent.EventType eventType = event.eventType();
        Shape shape = event.operand();

        if (eventType == ShapeModelEvent.EventType.ShapeAdded) {
            NestingShape parent = shape.parent();
            if (parent == adaptee) {
                // The new shapes's parent is represented by this TableModel,
                // so the view will need to be updated to show the new shape.
                fireTableRowsInserted(parent.shapeCount() - 1, parent.shapeCount() - 1);
            }
        } else if (eventType == ShapeModelEvent.EventType.ShapeRemoved) {
            NestingShape parent = event.parent();
            if (parent == adaptee) {
                // The removed shape's former parent is represented by this
                // TableModel. Notify the view so that it will no longer show
                // removed shape.
                fireTableDataChanged();
            }
        } else {
            // Processing a ShapeMoved event.
            fireTableDataChanged();
        }
    }

}
