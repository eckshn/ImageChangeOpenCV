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
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String picture = "src/rbhog.jpg";
		
		Mat image = loadImage(picture);
		Mat output = new Mat();
		
		//Switches RGB value (Must be RGB2...) to BGR (Similar to cv2.cvtColor from video)
		Imgproc.cvtColor(image, output, Imgproc.COLOR_RGB2BGR); //sets the value of output of the desired change from image 
		saveImage(output, "src/output/gray.jpg"); //saves output as a file
		
		showImage(picture);
		showImage("src/output/gray.jpg");
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
