package ru.sfedu.opencv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import ru.sfedu.opencv.utils.ConfigUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.sfedu.opencv.constants.Constants.PATH_TO_NATIVE_LIB_LINUX;

public class Lab3 {
    public static void main(String[] args) {
        System.load(ConfigUtils.getConfigProperty(PATH_TO_NATIVE_LIB_LINUX));
    }

    public static Mat transformImagePerspective(String imagePath) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        int x0 = srcImage.cols() / 4;
        int x1 = (srcImage.cols() / 4) * 3;
        int y0 = srcImage.cols() / 4;
        int y1 = (srcImage.cols() / 4) * 3;

        List<Point> listSrcs = List.of(new Point(x0, y0), new Point(x0, y1), new Point(x1, y1), new Point(x1, y0));
        Mat srcPoints = Converters.vector_Point_to_Mat(listSrcs, CvType.CV_32F);

        int xMargin = srcImage.cols() / 10;
        int yMargin = srcImage.rows() / 10;
        List<Point> listDsts = List.of(
                new Point(x0 + xMargin, y0 + yMargin),
                listSrcs.get(1),
                listSrcs.get(2),
                new Point(x1 - xMargin, y0 + yMargin)
        );
        Mat dstPoints = Converters.vector_Point_to_Mat(listDsts, CvType.CV_32F);
        Mat perspectiveMmat = Imgproc.getPerspectiveTransform(srcPoints, dstPoints);
        Mat dstMat = new Mat();

        Imgproc.warpPerspective(srcImage, dstMat, perspectiveMmat, srcImage.size());

        return dstMat;
    }

    public static Mat moveImageFromCenter(String imagePath, int xOffset, int yOffset) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        //https://java.tutorialink.com/shift-image-in-opencv/
        Mat warpMat = new Mat(2, 3, CvType.CV_64FC1);

        warpMat.put(0, 0, 1, 0, xOffset, 0, 1, yOffset);

        Mat dst = new Mat();

        Imgproc.warpAffine(
                srcImage,
                dst,
                warpMat,
                srcImage.size()
        );

        return dst;
    }

    public static Mat repeatImage(String imagePath, int hRepeat, int vRepeat) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);

        if (hRepeat > 0) {
            Core.hconcat(
                    IntStream.range(0, hRepeat)
                            .mapToObj(__ -> srcImage)
                            .collect(Collectors.toList()),
                    srcImage
            );
        }

        if (vRepeat > 0) {
            Core.vconcat(
                    IntStream.range(0, vRepeat)
                            .mapToObj(__ -> srcImage)
                            .collect(Collectors.toList()),
                    srcImage
            );
        }

        return srcImage;
    }

    public static Mat createRotatedImage(String imagePath, int angle, boolean crop) {
        if (crop) {
            return createRotatedImage(imagePath, angle);
        }
        return createRotatedImageWithoutCrop(imagePath, angle);
    }

    public static Mat createChangedSizeImage(String path, int width, int height) {
        Mat srcImage = Imgcodecs.imread(path, Imgcodecs.IMREAD_COLOR);
        Mat dst = new Mat();
        Imgproc.resize(srcImage, dst, new Size(width, height));
        return dst;
    }

    public static Mat createMirrorKernelImage(String imagePath) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Mat dstHV = new Mat();
        Core.flip(srcImage, dstHV, 1);
        return dstHV;
    }

    public static Mat createSobelAndLaplacianKernelImage(String imagePath, int depth, int dx, int dy) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Mat grayImage = new Mat();
        Imgproc.cvtColor(srcImage, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat dstSobel = new Mat();
        Imgproc.Sobel(grayImage, dstSobel, depth, dx, dy);
        Mat dstLaplace = new Mat();
        Imgproc.Laplacian(dstSobel, dstLaplace, depth);
        Mat absLaplasImg = new Mat();
        Core.convertScaleAbs(dstLaplace, absLaplasImg);
        return absLaplasImg;
    }

    private static Mat createRotatedImageWithoutCrop(String imagePath, int angle) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        double width = srcImage.cols();
        double height = srcImage.rows();
        Point centerPoint = new Point(height / 2, width / 2);
        Mat rotatedMat = Imgproc.getRotationMatrix2D(centerPoint, angle, 1);

        double absCos = Math.abs(rotatedMat.get(0, 0)[0]);
        double absSin = Math.abs(rotatedMat.get(0, 1)[0]);
        double boundW = (height * absSin) + (width * absCos);
        double boundH = (height * absCos) + (width * absSin);

        rotatedMat.get(0, 2)[0] = rotatedMat.get(0, 2)[0] + (boundW / 2) - centerPoint.x;
        rotatedMat.get(1, 2)[0] = rotatedMat.get(1, 2)[0] + (boundH / 2) - centerPoint.y;
        Mat dst = new Mat();
        Imgproc.warpAffine(
                srcImage,
                dst,
                rotatedMat,
                new Size(boundW, boundH)
        );
        return dst;
    }

    private static Mat createRotatedImage(String imagePath, int angle) {
        Mat srcImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        Point centerPoint = new Point((double) srcImage.width() / 2, (double) srcImage.height() / 2);
        Mat rotatedMat = Imgproc.getRotationMatrix2D(centerPoint, angle, 1);
        Mat dst = new Mat();

        Imgproc.warpAffine(
                srcImage,
                dst,
                rotatedMat,
                new Size(srcImage.width(), srcImage.height()),
                Imgproc.INTER_LINEAR,
                Core.BORDER_TRANSPARENT,
                new Scalar(0, 0, 0, 255)
        );

        return dst;
    }
}
