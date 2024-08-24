import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.SIFT;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BlobDetection {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        String filePath = "microbes.jpg";
        Mat image = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_GRAYSCALE);

        if (image.empty()) {
            System.out.println("Could not open or find the image!");
            return;
        }

        MatOfKeyPoint keyPoints = new MatOfKeyPoint();

        // Using SIFT (Scale-Invariant Feature Transform) as an alternative for blob detection
        SIFT detector = SIFT.create();
        detector.detect(image, keyPoints);

        System.out.println("Number of objects: " + keyPoints.toArray().length);

        // Draw detected blobs as red circles
        Mat outputImage = new Mat();
        Features2d.drawKeypoints(image, keyPoints, outputImage, new Scalar(0, 255, 0), Features2d.DrawRichKeypoints);

        // Save the output image
        Imgcodecs.imwrite("output.jpg", outputImage);

        // Display the original image
        displayImage("Original Image", image);

        // Display the output image with detected blobs
        displayImage("Detected Blobs", outputImage);
    }

    public static void displayImage(String title, Mat img) {
        ImageIcon icon = new ImageIcon(Mat2BufferedImage(img));
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img.width(), img.height());
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Image Mat2BufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] b = new byte[bufferSize];
        matrix.get(0, 0, b);
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}
