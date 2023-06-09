package bounce.forms;

import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import bounce.forms.util.FormElementComponent;
import bounce.forms.util.FormUtility;

/**
 * FormElementComponent subclass that allows a user to select an image file on
 * disk. The file is stored as a field within the ImageFormElement.
 *
 * @author Ian Warren
 * @see bounce.forms.util.FormElementComponent
 */
@SuppressWarnings("serial")
public class ImageFormElement extends FormElementComponent {

    public static final String IMAGE = "image";

    private static final File DEFAULT_IMAGE_FILE = null;

    public ImageFormElement() {
        addField(IMAGE, DEFAULT_IMAGE_FILE, java.io.File.class);

        final JTextField tfFilename = new JTextField();
        tfFilename.setEditable(false);
        JButton bnBrowse = new JButton("Browse");

        setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        formUtility.addLabel(bnBrowse, this);
        formUtility.addLastField(tfFilename, this);

        Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Image"), border));

        bnBrowse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showDialog(ImageFormElement.this, "Select");

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String filename = file.getAbsolutePath();
                    tfFilename.setText(filename);
                    putFieldValue(IMAGE, file);
                }
            }
        });
    }
}
