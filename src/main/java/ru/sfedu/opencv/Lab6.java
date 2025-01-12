package ru.sfedu.opencv;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import ru.sfedu.opencv.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static ru.sfedu.opencv.constants.Constants.PATH_TO_NATIVE_LIB_LINUX;

public class Lab6 {
    public static void main(String[] args) {
        System.load(ConfigUtils.getConfigProperty(PATH_TO_NATIVE_LIB_LINUX));
    }

    public static Mat findBorders(Mat srcImage, double thres) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);

        Mat detectedEdgesImage = new Mat();
        Mat thresholdImage = new Mat();

        double threshold = Imgproc.threshold(grayImage, thresholdImage, thres, 255, Imgproc.THRESH_BINARY);

//        Imgcodecs.imwrite(ConfigUtils.getConfigProperty(LAB6_OUTPUT_PATH) + "threshold.jpg", thresholdImage);
        Imgproc.blur(thresholdImage, detectedEdgesImage, new Size(3, 3));

        Imgproc.Canny(detectedEdgesImage, detectedEdgesImage, threshold, threshold * 3);
//        Imgcodecs.imwrite(ConfigUtils.getConfigProperty(LAB6_OUTPUT_PATH) + "edge.jpg", detectedEdgesImage);

        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(detectedEdgesImage, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        contours.sort(Collections.reverseOrder(Comparator.comparing(Imgproc::contourArea)));
        Imgproc.drawContours(
                srcImage,
                contours.subList(1, contours.size()),
                -1,
                new Scalar(0, 255, 0), 1
        );

        return srcImage;
    }
}
