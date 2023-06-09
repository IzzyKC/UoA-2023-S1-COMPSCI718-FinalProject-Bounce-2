package bounce.forms;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import bounce.forms.util.FormElementComponent;
import bounce.forms.util.FormUtility;

/**
 * FormElementComponent subclass that allows a user to select a colour using a
 * Java Swing JColorChooser (colour picker) component. The colour is stored as
 * a field within the ColourFormElement.
 *
 * @author Ian Warren
 * @see bounce.forms.util.FormElementComponent
 */
@SuppressWarnings("serial")
public class ColourFormElement extends FormElementComponent {

    public static final String COLOUR = "color";

    private static final Color DEFAULT_COLOUR = Color.BLACK;

    public ColourFormElement() {
        // Add a field of type Color to the FormElement.
        addField(COLOUR, DEFAULT_COLOUR, java.awt.Color.class);

        final JLabel lblFilledColour = new JLabel("   ");
        lblFilledColour.setBackground(DEFAULT_COLOUR);
        lblFilledColour.setOpaque(true);
        JButton btnSelect = new JButton("Select");

        setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        formUtility.addLabel(btnSelect, this);
        formUtility.addLastField(lblFilledColour, this);

        Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Colour"), border));

        /*
         * Add an ActionListener to the "Select" button that displays a colour
         * picker. When the user selects a colour, it's value is set as the
         * value for the FormElement's colour field.
         */
        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColour = JColorChooser.showDialog(
                        ColourFormElement.this,
                        "Choose shape colour",
                        DEFAULT_COLOUR);
                putFieldValue(COLOUR, newColour);
                lblFilledColour.setBackground(newColour);
            }
        });
    }


}
