package bounce.forms;

import bounce.ImageRectangleShape;
import bounce.NestingShape;
import bounce.ShapeModel;
import bounce.forms.util.Form;
import bounce.forms.util.FormHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/*
 * TODO Complete this class to more appropriately load and scale the image
 */
public class ImageShapeFormHandler implements FormHandler {

    private ShapeModel model;
    private NestingShape parentOfNewShape;

    private SwingWorker<Long,Void> worker;


    public ImageShapeFormHandler(ShapeModel model, NestingShape parent) {
        this.model = model;
        parentOfNewShape = parent;
    }


    @Override
    public void processForm(Form form) {
        worker = new ImageShapeFormWorker(form);
        worker.execute();
    }

    private class ImageShapeFormWorker extends SwingWorker<Long, Void>{
        private Form form;

        public ImageShapeFormWorker(Form form){
            this.form = form;
        }

        @Override
        protected Long doInBackground() throws Exception {
            long startTime = System.currentTimeMillis();
            // Read field values from the form.
            File imageFile = (File) form.getFieldValue(File.class, ImageFormElement.IMAGE);
            int width = form.getFieldValue(Integer.class, ShapeFormElement.WIDTH);
            int deltaX = form.getFieldValue(Integer.class, ShapeFormElement.DELTA_X);
            int deltaY = form.getFieldValue(Integer.class, ShapeFormElement.DELTA_Y);


            // Load the original image (ImageIO.read() is a blocking call).
            BufferedImage fullImage = null;
            try {
                fullImage = ImageIO.read(imageFile);
            } catch (IOException e) {
                System.out.println("Error loading image.");
            }

            int fullImageWidth = fullImage.getWidth();
            int fullImageHeight = fullImage.getHeight();

            BufferedImage scaledImage = fullImage;

            // Scale the image if necessary.
            if (fullImageWidth > width) {
                double scaleFactor = (double) width / (double) fullImageWidth;
                int height = (int) ((double) fullImageHeight * scaleFactor);

                scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = scaledImage.createGraphics();

                // Method drawImage() scales an already loaded image. The
                // ImageObserver argument is null because we don't need to monitor
                // the scaling operation.
                g.drawImage(fullImage, 0, 0, width, height, null);

                // Create the new Shape and add it to the model.
                ImageRectangleShape imageShape = new ImageRectangleShape(deltaX, deltaY, scaledImage);
                model.add(imageShape, parentOfNewShape);
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            return elapsedTime;
        }

        @Override
        public void done() {
            try {
                System.out.println("Image loading ans scaling took " + get().longValue() + "ms.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
