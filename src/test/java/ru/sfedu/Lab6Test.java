package ru.sfedu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import ru.sfedu.opencv.utils.ConfigUtils;

import static ru.sfedu.opencv.Lab6.findBorders;
import static ru.sfedu.opencv.constants.Constants.*;

public class Lab6Test extends BaseTest {
    @Test
    @DisplayName("Проверка метода по определению границ")
    public void testFindBorders() {
        Mat srcImage = Imgcodecs.imread(ConfigUtils.getConfigProperty(LAB6_IMAGE_TO_DEFINE_BORDER_PATH));
        Mat mat = findBorders(srcImage, 200);
        Imgcodecs.imwrite(ConfigUtils.getConfigProperty(LAB6_OUTPUT_PATH) + "borders.jpg", mat);
        Mat mat2 = findBorders(
                Imgcodecs.imread(ConfigUtils.getConfigProperty(LAB6_IMAGE_TO_DEFINE_BORDER2_PATH)),
                20
        );
        Imgcodecs.imwrite(ConfigUtils.getConfigProperty(LAB6_OUTPUT_PATH) + "borders2.jpg", mat2);
    }
}
