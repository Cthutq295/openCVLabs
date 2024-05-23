package ru.sfedu;

import org.junit.jupiter.api.BeforeAll;
import ru.sfedu.opencv.utils.ConfigUtils;

import static ru.sfedu.opencv.constants.Constants.PATH_TO_NATIVE_LIB_LINUX;

public class BaseTest {
    @BeforeAll
    public static void beforeAll() {
        System.load(ConfigUtils.getConfigProperty(PATH_TO_NATIVE_LIB_LINUX));
    }
}
