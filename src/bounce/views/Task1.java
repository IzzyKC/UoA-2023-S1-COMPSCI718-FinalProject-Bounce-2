package bounce.views;

import bounce.NestingShape;
import bounce.Shape;
import bounce.ShapeModel;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/*
 * TODO Complete this class to display a ShapeModel's shape composition on the JTree
 */
public class Task1 implements TreeModel {
    protected ShapeModel _adaptee;

    protected List<TreeModelListener> _treeModelListenerList;

    public Task1(ShapeModel shapeModel) {
        this._adaptee = shapeModel;
        _treeModelListenerList = new ArrayList<>();

    }

    @Override
    public Object getRoot() {
        return _adaptee.root();
    }

    @Override
    public Object getChild(Object parent, int index) {
        try{
            if(parent instanceof NestingShape){
                return ((NestingShape) parent).shapeAt(index);
            }
        }catch(IndexOutOfBoundsException e){
            System.out.println("Index error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if(parent instanceof NestingShape)
            return ((NestingShape) parent).shapeCount();
        return 0;
    }

    /**
     * NestingShape can have children, return false;
     * Otherwise, return true(is a leaf)
     * @param node  a node in the tree, obtained from this data source
     * @return is a Leaf node
     */
    @Override
    public boolean isLeaf(Object node) {
        return !(node instanceof NestingShape);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        //simply be implemented with an empty method body.
        //This method is an event notification method that can safely be implemented like this
        //in the context of Task 1.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if(parent instanceof NestingShape)
            return ((NestingShape) parent).indexOf((Shape) child);

        return -1;
    }

    /**
     *Adds a listener for the TreeModelEvent posted after the tree changes.
     * @param l       the listener to add
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        _treeModelListenerList.add(l);

    }

    /**
     * Removes a listener previously added with addTreeModelListener
     * @param l       the listener to remove
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        _treeModelListenerList.remove(l);

    }
}


