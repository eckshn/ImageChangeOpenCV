import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {
	public static void main(String[] args) {
		//https://docs.opencv.org/master/javadoc/index.html
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String picture = "src/shoe.jpg";
		
		Mat image = loadImage(picture);
		Mat thresh = new Mat();
		Mat grayscale = new Mat();
		//Switches RGB value (Must be RGB2...) to BGR (Similar to cv2.cvtColor from video)
		Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_RGB2GRAY); //sets the value of output of the desired change from image 
		saveImage(grayscale, "src/output/gray.jpg"); //saves output as a file
		showImage(picture);
		showImage("src/output/gray.jpg");
		
		Imgproc.threshold(grayscale, thresh, 100, 255, Imgproc.THRESH_BINARY_INV);
		saveImage(thresh, "src/output/thresh.jpg");
		showImage("src/output/thresh.jpg");
		
		Mat kernel = Mat.ones(new int[] {10, 10}, 0);
		Mat opening = new Mat();
		Mat closing = new Mat();
		Imgproc.morphologyEx(thresh, opening, Imgproc.MORPH_OPEN, kernel);
		Imgproc.morphologyEx(opening, closing, Imgproc.MORPH_CLOSE, kernel);
		saveImage(closing, "src/output/closing.jpg");
		showImage("src/output/closing.jpg");
		
		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchey = new Mat();
		Imgproc.findContours(closing, contours, hierarchey, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE); //adds value to the contours list
		Scalar color = new Scalar(0, 0, 255); //red
		int index = findLargestArea(contours); //index of largest area
		//contours.remove(index); //remove it because largest area was the entire picture
		//index = findLargestArea(contours);
		Imgproc.drawContours(image, contours, index, color, 2); //draws contours only at the index
		saveImage(image, "src/output/lines.jpg");
		showImage("src/output/lines.jpg");
	}
	
	//Loads the given image as a Mat(Matrix Representation)
	public static Mat loadImage(String imagePath) {
	    return Imgcodecs.imread(imagePath);
	}
	
	//Saves the loaded image
	public static void saveImage(Mat imageMatrix, String targetPath) {
		Imgcodecs.imwrite(targetPath, imageMatrix);
	}
	
	public static int findLargestArea(List<MatOfPoint> contours) {
		Mat max = contours.get(0);
		int index = 0;
		for(int i = 0; i < contours.size(); i++) {
			if(Imgproc.contourArea(max) < Imgproc.contourArea(contours.get(i))) {
				max = contours.get(i);
				index = i;
			}
		}
		return index;
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
