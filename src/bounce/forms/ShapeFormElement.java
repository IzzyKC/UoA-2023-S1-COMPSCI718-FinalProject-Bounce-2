package bounce.forms;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import bounce.bounceApp.BounceConfig;
import bounce.forms.util.FormElementComponent;
import bounce.forms.util.FormUtility;

/**
 * FormElementComponent subclass that allows a user to specify basic/common
 * Shape attribute values, e.g. width, height etc. The value are held in the
 * form and used by the Form's associated FormHandler to construct an instance
 * of a specified Shape subclass.
 * <p>
 * A ShapeFormElement is a GUI component that has sliders for selecting a
 * Shape's width, height, delta-x and delta-y values. A text field is used to
 * enter any text to be associated with a new Shape.
 *
 * @author Ian Warren
 * @see bounce.forms.util.FormElementComponent
 */
@SuppressWarnings("serial")
public class ShapeFormElement extends FormElementComponent {

    // Field identifiers.
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String DELTA_X = "deltax";
    public static final String DELTA_Y = "deltay";
    public static final String TEXT = "text";

    // Default field values.
    private static final int DEFAULT_WIDTH = 25;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int DEFAULT_DELTA_X = 10;
    private static final int DEFAULT_DELTA_Y = 10;
    private static final String DEFAULT_TEXT = "";

    // Min/max field values.
    private static final int MIN_WIDTH = 10;
    private static final int MAX_WIDTH = BounceConfig.instance().getAnimationBounds().width / 2;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_HEIGHT = BounceConfig.instance().getAnimationBounds().height / 2;
    private static final int MIN_DELTA_X = 1;
    private static final int MAX_DELTA_X = 10;
    private static final int MIN_DELTA_Y = 1;
    private static final int MAX_DELTA_Y = 10;

    /**
     * Creates a ShapeFormElement with fields to store Shape attribute values.
     */
    public ShapeFormElement() {
        // Add fields to the form.
        addField(WIDTH, DEFAULT_WIDTH, java.lang.Integer.class);
        addField(HEIGHT, DEFAULT_HEIGHT, java.lang.Integer.class);
        addField(DELTA_X, DEFAULT_DELTA_X, java.lang.Integer.class);
        addField(DELTA_Y, DEFAULT_DELTA_Y, java.lang.Integer.class);
        addField(TEXT, DEFAULT_TEXT, java.lang.String.class);

        setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        // Create sliders abd associated labels.
        final JTextField tfWidth = new JTextField(3);
        tfWidth.setEditable(false);
        final JSlider sldWidth = new JSlider(MIN_WIDTH, MAX_WIDTH);
        formUtility.addLabel("Width: ", this);
        formUtility.addLabel(tfWidth, this);
        formUtility.addLastField(sldWidth, this);

        final JTextField tfHeight = new JTextField(3);
        tfHeight.setEditable(false);
        final JSlider sldHeight = new JSlider(MIN_HEIGHT, MAX_HEIGHT);
        formUtility.addLabel("Height: ", this);
        formUtility.addLabel(tfHeight, this);
        formUtility.addLastField(sldHeight, this);

        final JTextField tfDeltaX = new JTextField(3);
        tfDeltaX.setEditable(false);
        final JSlider sldDeltaX = new JSlider(MIN_DELTA_X, MAX_DELTA_X);
        formUtility.addLabel("Delta X: ", this);
        formUtility.addLabel(tfDeltaX, this);
        formUtility.addLastField(sldDeltaX, this);

        final JTextField tfDeltaY = new JTextField(3);
        tfDeltaY.setEditable(false);
        final JSlider sldDeltaY = new JSlider(MIN_DELTA_Y, MAX_DELTA_Y);
        formUtility.addLabel("Delta Y: ", this);
        formUtility.addLabel(tfDeltaY, this);
        formUtility.addLastField(sldDeltaY, this);

        final JTextField tfText = new JTextField(DEFAULT_TEXT);
        formUtility.addLabel("Text: ", this);
        formUtility.addLastField(tfText, this);

        Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Basic shape properties"), border));

        // Add ChangeListeners to respond to slider changes. The response
        // includes updating the slider's corresponding field value.
        sldWidth.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sliderValue = sldWidth.getValue();
                putFieldValue(WIDTH, sliderValue);
                tfWidth.setText(Integer.toString(sliderValue));
            }
        });

        sldHeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sliderValue = sldHeight.getValue();
                putFieldValue(HEIGHT, sliderValue);
                tfHeight.setText(Integer.toString(sliderValue));
            }
        });

        sldDeltaX.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sliderValue = sldDeltaX.getValue();
                putFieldValue(DELTA_X, sliderValue);
                tfDeltaX.setText(Integer.toString(sliderValue));
            }
        });

        sldDeltaY.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sliderValue = sldDeltaY.getValue();
                putFieldValue(DELTA_Y, sliderValue);
                tfDeltaY.setText(Integer.toString(sliderValue));
            }
        });

        // Add a DocumentListener to the JTextField. Whenever the text is
        // changed, update the form's field value for text.
        tfText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    putFieldValue(TEXT, e.getDocument().getText(0, e.getDocument().getLength() - 1));
                } catch (BadLocationException e1) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    putFieldValue(TEXT, e.getDocument().getText(0, e.getDocument().getLength() - 1));
                } catch (BadLocationException e1) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    putFieldValue(TEXT, e.getDocument().getText(0, e.getDocument().getLength() - 1));
                } catch (BadLocationException e1) {
                }
            }

        });

        // Set the slider values - this causes the registered ChangeListeners
        // to run and initialise the GUI components.
        sldWidth.setValue(DEFAULT_WIDTH);
        sldHeight.setValue(DEFAULT_HEIGHT);
        sldDeltaX.setValue(DEFAULT_DELTA_X);
        sldDeltaY.setValue(DEFAULT_DELTA_Y);
    }
}
