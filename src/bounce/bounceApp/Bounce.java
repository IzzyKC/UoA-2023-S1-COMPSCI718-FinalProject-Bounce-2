package bounce.bounceApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import bounce.*;
import bounce.forms.FormResolver;
import bounce.forms.util.FormComponent;
import bounce.forms.util.FormHandler;
import bounce.views.AnimationView;
import bounce.views.Task2;
import bounce.views.TableModelAdapter;


/**
 * Main program for Bounce application. A Bounce instance sets up a GUI
 * comprising three views of a ShapeModel: an animation view, a table view and
 * a tree view. In addition the GUI includes buttons and associated event
 * handlers to add new shapes to the animation and to remove existing shapes.
 * A Bounce object uses a Timer to progress the animation; this results in the
 * ShapeModel being sent a clock() message to which it responds by moving its
 * constituent Shape objects and then by notifying the three views
 * (ShapeModelListeners). The application uses a BounceConfig object to read
 * properties from the bounce.properties file, one of which is the name of a
 * ShapeFactory implementation class that is used to create Shapes on request.
 *
 * @author Ian Warren
 */
public class Bounce extends JPanel {
    private static final int DELAY = 25;

    // Underlying model for the application.
    private ShapeModel model;

    private ShapeClassComboBoxModel comboBoxModel;

    // View instances.
    private JTree treeView;
    private AnimationView animationView;
    private JTable tabularView;

    /*
     * Adapter objects (ShapeModelListeners) that transform ShapeModelEvents
     * into Swing TreeModel and TableModel events.
     */
    private Task2 treeModelAdapter;
    private TableModelAdapter tableModelAdapter;

    // Swing components to handle user input.
    private JButton newShape;
    private JButton deleteShape;
    private JComboBox<Class<? extends Shape>> shapeTypes;

    // Button for cut and paste shape
    private JButton cutPasteShape;

    // Shape selected in the JTree view.
    private Shape shapeSelected;

    // Shape selected for "cut and paste"
    private Shape shapeToPaste;

    /**
     * Creates a Bounce object.
     */
    public Bounce() {
        // Instantiate model and populate it with an initial set of shapes.
        BounceConfig config = BounceConfig.instance();
        model = new ShapeModel(config.getAnimationBounds());
        populateModel();

        comboBoxModel = new ShapeClassComboBoxModel();

        // Instantiate GUI objects and construct GUI.
        buildGUI();

        // Register views with models.
        model.addShapeModelListener(animationView);
        model.addShapeModelListener(tableModelAdapter);
        model.addShapeModelListener(treeModelAdapter);

        // Setup event handlers to process user input.
        setUpEventHandlers();

        // Show GUI and ensure the root shape within the JTree view is selected.
        treeView.setSelectionPath(new TreePath(model.root()));

        // Start animation.
        Timer timer = new Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.clock();
            }
        });
        timer.start();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Bounce");
        JComponent newContentPane = new Bounce();
        frame.add(newContentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /*
     * Adds shapes to the model.
     */
    private void populateModel() {
        NestingShape root = model.root();

        model.add(new RectangleShape(440, 0, 10, 10, 4, 2), root);
        model.add(new RectangleShape(0, 0, 5, 7), root);
        model.add(new RectangleShape(0, 0, 2, 2, 10, 10), root);
        model.add(new DynamicRectangleShape(0, 0, 2, 3, 180, 130, "I change color when I bounce", Color.CYAN), root);

        NestingShape child = new NestingShape(10, 10, 2, 2, 100, 100);
        model.add(new DynamicRectangleShape(0, 0, 2, 3, 50, 80, Color.RED), child);
        model.add(child, root);
    }

    /*
     * Registers event handlers with Swing components to process user inputs.
     */
    private void setUpEventHandlers() {
        /*
         * Event handling code to be executed whenever the users presses the
         * "New" button. Based on the Shape type selected in the combo box, a
         * suitable Form/FormHandler pair is acquired from the FormResolver.
         * The Form is used by the user to specify attribute values for the
         * new Shape to be created, and the FormHandler is responsible for
         * instantiating the correct Shape subclass and adding the new instance
         * to the application's ShapeModel.
         */
        newShape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                Class<? extends Shape> cls = (Class<? extends Shape>) comboBoxModel.getSelectedItem();
                FormComponent form = FormResolver.getForm(cls);
                FormHandler handler = FormResolver.getFormHandler(cls, model, (NestingShape) shapeSelected);
                form.setFormHandler(handler);
                form.prepare();

                // Display the form.
                form.setLocationRelativeTo(null);
                form.setVisible(true);

            }
        });

        /*
         * Event handling code to be executed whenever the user presses the
         * "Delete" button. The shape that is currently selected in the JTree
         * view is removed from the model. During removal, the removed shape's
         * former parent is selected in the JTree.
         */
        deleteShape.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Shape selection = shapeSelected;
                NestingShape parent = selection.parent();

                treeView.setSelectionPath(new TreePath(parent.path().toArray()));
                model.remove(selection);

            }
        });

        /*
         * TODO: Event handling code to be executed whenever the user presses
         *  the "Cut" / "Paste" button. If there is no shape to paste, the button
         *  is set to be "Cut". If there is a shape to paste, the button is set
         *  to "Paste". Note that any shape can be cut with the exception of the root
         */
        cutPasteShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Shape selection = shapeSelected;
                if("Cut".equals(e.getActionCommand())) {
                    shapeToPaste = selection;
                    treeView.setSelectionPath(new TreePath(shapeToPaste.parent().path().toArray()));
                    model.cut(shapeToPaste);
                    cutPasteShape.setText("Paste");
                }else if("Paste".equals(e.getActionCommand())){
                    boolean success = model.paste(shapeToPaste, (NestingShape) selection);
                    if(success) {
                        shapeToPaste = null;
                        cutPasteShape.setText("Cut");
                        cutPasteShape.setEnabled(shapeSelected != model.root());
                    }
                }
            }
        });

        /*
         * Event handling code to be executed whenever the user selects a node
         * within the JTree view. The event handler records which shape is
         * selected and in addition enables/disables the "New" and "Delete"
         * buttons appropriately. In addition, the TableModel representing the
         * the shape selected in the JTree component is informed of the newly
         * selected shape.
         */
        treeView.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath selectionPath = treeView.getSelectionPath();
                shapeSelected = (Shape) selectionPath.getLastPathComponent();

                /*
                 * Enable button newShape only if what is selected in the
                 * JTree is a NestingShape. Rationale: new shapes can only be
                 * added to NestingShape instances.
                 */
                newShape.setEnabled(shapeSelected instanceof NestingShape);

                /*
                 * Enable button deleteShape only if what is selected in the
                 * JTree is not the root node. Rationale: any shape can be
                 * removed with the exception of the root.
                 */
                deleteShape.setEnabled(shapeSelected != model.root());


                /*
                 * TODO Enable button cutPasteShape only if what is selected
                 *  in the JTree can be cut or pasted
                 */
                if(cutPasteShape.getText().equals("Cut")) {
                    //allow users to cut any shape (except the root)
                    cutPasteShape.setEnabled(shapeSelected != model.root());
                } if(cutPasteShape.getText().equals("Paste")){
                    //destination is a NestingShape, can fit the shape to be pasted
                    //shape to be pasted not be the ancestor of the destination shape
                    cutPasteShape.setEnabled(shapeSelected instanceof NestingShape
                            && (shapeSelected.parent() == null || !shapeSelected.parent().path().contains(shapeToPaste))
                            && shapeSelected.width() > shapeToPaste.width()
                            && shapeSelected.height() > shapeToPaste.height());
                }

                /*
                 * Tell the table model to represent the shape that is now
                 * selected in the JTree component.
                 */
                tableModelAdapter.setAdaptee(shapeSelected);
            }
        });
    }

    /*
     * Creates and lays out GUI components. Note: there is nothing particularly
     * interesting about this method - it simply builds up a composition of GUI
     * components and makes use of borders, scroll bars and layout managers.
     */
    private void buildGUI() {
        // Create Swing model objects.
        treeModelAdapter = new Task2(model);
        tableModelAdapter = new TableModelAdapter(model.root());

        // Create main Swing components.
        treeView = new JTree(treeModelAdapter);
        treeView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tabularView = new JTable(tableModelAdapter);
        animationView = new AnimationView(BounceConfig.instance().getAnimationBounds());

        /*
         * Create a panel to house the JTree component. The panel includes a
         * titled border and scrollbars that will be activated when necessary.
         */
        JPanel treePanel = new JPanel();
        treePanel.setBorder(BorderFactory.createTitledBorder("Shape composition hierarchy"));
        JScrollPane scrollPaneForTree = new JScrollPane(treeView);
        scrollPaneForTree.setPreferredSize(new Dimension(300, 504));
        treePanel.add(scrollPaneForTree);

        /*
         * Create a panel to house the animation view. This panel includes a
         * titled border and scroll bars if the animation area exceeds the
         * allocated screen space.
         */
        JPanel animationPanel = new JPanel();
        animationPanel.setBorder(BorderFactory.createTitledBorder("Shape animation"));
        JScrollPane scrollPaneForAnimation = new JScrollPane(animationView);
        scrollPaneForAnimation.setPreferredSize(new Dimension(504, 504));
        animationPanel.add(scrollPaneForAnimation);
        animationView.setPreferredSize(BounceConfig.instance().getAnimationBounds());


        /*
         * Create a panel to house the tabular view. Again, decorate the
         * tabular view with a border and enable automatic activation of
         * scroll bars.
         */
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createTitledBorder("Shape state"));
        JScrollPane scrollPaneForTable = new JScrollPane(tabularView);
        scrollPaneForTable.setPreferredSize(new Dimension(810, 150));
        tablePanel.add(scrollPaneForTable);

        /*
         * Create a control panel housing buttons for creating and destroying
         * shapes, plus a combo box for selecting the type of shape to create.
         */
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control panel"));
        newShape = new JButton("New");
        deleteShape = new JButton("Delete");
        shapeTypes = new JComboBox<Class<? extends Shape>>(comboBoxModel);
        cutPasteShape = new JButton("Cut");

        /*
         * Set up a custom renderer for the Combo box. Instead of displaying
         * the fully qualified names (that include packages) of Shape
         * subclasses, display onlt the class names (without the package
         * prefixes).
         */
        shapeTypes.setRenderer(new BasicComboBoxRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String className = value.toString().substring(value.toString().lastIndexOf('.') + 1);
                return super.getListCellRendererComponent(list, className, index, isSelected, cellHasFocus);
            }
        });


        controlPanel.add(newShape);
        controlPanel.add(deleteShape);
        controlPanel.add(shapeTypes);
        controlPanel.add(cutPasteShape);

        JPanel top = new JPanel(new BorderLayout());
        top.add(animationPanel, BorderLayout.CENTER);
        top.add(treePanel, BorderLayout.WEST);
        top.add(tablePanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(top, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }


    /*
     * Helper class to define a custom model for the Combo box. This
     * ComboBoxModel stores Shape subclasses that are acquired from
     * BounceConfig.
     */
    private class ShapeClassComboBoxModel extends
            DefaultComboBoxModel<Class<? extends Shape>> {

        public ShapeClassComboBoxModel() {
            List<Class<? extends Shape>> shapeClasses = BounceConfig.instance()
                    .getShapeClasses();

            for (Class<? extends Shape> cls : shapeClasses) {
                addElement(cls);
            }
        }

    }
}
