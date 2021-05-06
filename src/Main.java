import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {
	public static void main(String[] args) {
		//https://docs.opencv.org/master/javadoc/index.html
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String picture = "src/drip.jpg";
		
		Mat image = loadImage(picture);
		Mat thresh = new Mat();
		Mat grayscale = new Mat();
		//Switches RGB value (Must be RGB2...) to BGR (Similar to cv2.cvtColor from video)
		Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_RGB2GRAY); //sets the value of output of the desired change from image 
		saveImage(grayscale, "src/output/gray.jpg"); //saves output as a file
		showImage(picture);
		showImage("src/output/gray.jpg");
		
		Imgproc.threshold(grayscale, thresh, 180, 255, Imgproc.THRESH_BINARY_INV);
		saveImage(thresh, "src/output/thresh.jpg");
		showImage("src/output/thresh.jpg");
		
		Mat kernel = Mat.ones(new int[] {5, 5}, 0);
		Mat opening = new Mat();
		Mat closing = new Mat();
		Imgproc.morphologyEx(thresh, opening, Imgproc.MORPH_OPEN, kernel);
		Imgproc.morphologyEx(opening, closing, Imgproc.MORPH_CLOSE, kernel);
		saveImage(closing, "src/output/closing.jpg");
		showImage("src/output/closing.jpg");
		
	}
	
	//Loads the given image as a Mat(Matrix Representation)
	public static Mat loadImage(String imagePath) {
	    return Imgcodecs.imread(imagePath);
	}
	
	//Saves the loaded image
	public static void saveImage(Mat imageMatrix, String targetPath) {
		Imgcodecs.imwrite(targetPath, imageMatrix);
	}
	
	private static void showImage(final String fileName)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.getContentPane().setLayout(new GridLayout(1,1));
                f.getContentPane().add(new JLabel(new ImageIcon(fileName)));
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
}
