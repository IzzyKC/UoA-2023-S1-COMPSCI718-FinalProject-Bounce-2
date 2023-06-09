package bounce.bounceApp;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import bounce.Shape;


/**
 * Singleton class that allows easy access to properties specified in the
 * Bounce application's properties file, named bounce.properties. This file
 * should be stored in the user's project directory.
 *
 * @author Ian Warren
 */
public class BounceConfig {
    // Location of properties file.
    private static final String FILE_LOCATION = "user.dir";

    // Name of properties file.
    private static final String FILE_NAME = "bounce.properties";

    // Property default values.
    public static final int DEFAULT_ANIMATION_WIDTH = 500;
    public static final int DEFAULT_ANIMATION_HEIGHT = 500;
    public static final int MAX_ANIMATION_WIDTH = 1000;
    public static final int MAX_ANIMATION_HEIGHT = 1000;

    // Property names (keys).
    private static final String ANIMATION_WIDTH = "animation_width";
    private static final String ANIMATION_HEIGHT = "animation_height";
    private static final String SHAPES = "shape_classes";

    // Property values.
    private Dimension bounds;
    private String[] shapeClassNames;
    private List<Class<? extends Shape>> shapeClasses;

    // Singleton instance.
    private static BounceConfig instance;

    /**
     * Returns a reference to the BounceConfig object.
     */
    public static BounceConfig instance() {
        if (instance == null) {
            instance = new BounceConfig();
        }
        return instance;
    }

    /*
     * Hidden constructor that populates the BounceConfig instance with
     * property values.
     */
    private BounceConfig() {
        //System.setProperty("user.home", "H://");
        String path = System.getProperty(FILE_LOCATION);
        System.out.println("path " + path);
        File file = new File(path, FILE_NAME);

        // Create a new properties object.
        Properties props = new Properties();

        try {
            // Attempt to read property values from file.
            InputStream in = new FileInputStream(file);
            props.load(in);
            in.close();
        } catch (IOException e) {
            // Properties file not found, or an error has occurred reading the file.
            // No action necessary.
        } finally {
            // Read bounds property.
            int width = getBound(ANIMATION_WIDTH, DEFAULT_ANIMATION_WIDTH, props);
            int height = getBound(ANIMATION_HEIGHT, DEFAULT_ANIMATION_HEIGHT, props);

            /*
             * Restore bounds to default values if invalid values have been
             * read from the properties file.
             */
            if (width < DEFAULT_ANIMATION_WIDTH || width > MAX_ANIMATION_WIDTH) {
                width = DEFAULT_ANIMATION_WIDTH;
            }
            if (height < DEFAULT_ANIMATION_HEIGHT || width > MAX_ANIMATION_HEIGHT) {
                height = DEFAULT_ANIMATION_HEIGHT;
            }
            bounds = new Dimension(width, height);

            // Set shapes property.
            String shapeTypes = props.getProperty(SHAPES);
            if (shapeTypes == null) {
                // Create an empty array if no shape names are given.
                shapeClassNames = new String[0];
            } else {
                shapeClassNames = shapeTypes.split("\\s+");
            }
        }
    }

    /**
     * Returns the bounds of the world in which shapes move around. This method
     * returns bounds in the range DEFAULT_ANIMATION_WIDTH/HEIGHT ..
     * MAX_ANIMATION_WIDTH/HEIGHT. In the properties file specifies bounds
     * outside of this range, they are ignored and DEFAULT_ANIMATION_WIDTH/
     * HEIGHT are returned.
     */
    public Dimension getAnimationBounds() {
        return bounds;
    }

    /**
     * Returns an array of strings containing names of shape classes. If no
     * shape classes are named in the properties file, this method returns an
     * empty array.
     */
    public List<String> getShapeClassNames() {
        List<String> result = Arrays.asList(shapeClassNames);
        Collections.sort(result);

        return result;
    }

    public List<Class<? extends Shape>> getShapeClasses() {
        // If the List of Shape classes has already been created, simply return
        // it.
        if (shapeClasses != null) {
            return shapeClasses;
        }

        // Initialise the List of classes.
        shapeClasses = new ArrayList<Class<? extends Shape>>();

        for (int i = 0; i < shapeClassNames.length; i++) {
            String className = shapeClassNames[i];

            try {
                @SuppressWarnings("unchecked")
                Class<? extends Shape> newClass = (Class<? extends Shape>) Class.forName(className);
                shapeClasses.add(newClass);
            } catch (ClassNotFoundException | ClassCastException e) {
                // Ignore the class loading error. The class either doesn't
                // exist or isn't a Shape subclass.
            }
        }

        // Sort the classes based on name.
        Collections.sort(shapeClasses, new Comparator<Class<? extends Shape>>() {
            @Override
            public int compare(Class<? extends Shape> class1,
                               Class<? extends Shape> class2) {
                return class1.getName().compareTo(class2.getName());
            }
        });


        // Return an unmodifiable collection so that clients cannot change the
        // contents of the returned list (which is part of the state of the
        // BounceConfig object).
        return Collections.unmodifiableList(shapeClasses);
    }

    /*
     * Implementation method to read/validate bound properties.
     */
    private int getBound(String propertyName, int defaultValue, Properties props) {
        int property = defaultValue;
        String propertyStr = props.getProperty(propertyName);
        if (propertyStr != null) {
            try {
                property = Integer.parseInt(propertyStr);
            } catch (NumberFormatException e) {
                // No action necessary - fall back on default setting for property.
            }
        }
        return property;
    }

    /**
     * Main method to simply output the properties held by a BounceConfig
     * object.
     */
    public static void main(String[] args) {
        BounceConfig config = BounceConfig.instance();
        List<Class<? extends Shape>> classes = config.getShapeClasses();

        System.out.println("Animation bounds ...");
        System.out.println("  " + config.getAnimationBounds());

        System.out.println("Shape class names ... ");
        for (String className : config.getShapeClassNames()) {
            System.out.println("  " + className);
        }

        System.out.println("Shape subclasses successfully loaded ...");
        for (Class<? extends Shape> cls : classes) {
            System.out.println("  " + cls.getName());
        }
    }
}
