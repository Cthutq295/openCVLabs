package ru.sfedu.opencv;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import ru.sfedu.opencv.constants.Constants;
import ru.sfedu.opencv.utils.ConfigUtils;

import static ru.sfedu.opencv.constants.Constants.PATH_TO_NATIVE_LIB_LINUX;

@Slf4j
public class Lab1 {
    public static void main(String[] args) {
        System.load(ConfigUtils.getConfigProperty(PATH_TO_NATIVE_LIB_LINUX));
        log.info("Operation system - {}", getOperationSystemType().name());
        log.info("CV version - {}", getOpenCvVersion());
    }

    public static String getOpenCvVersion() {
        return Core.getVersionString();
    }

    public static Constants.OSType getOperationSystemType() {
        String os = System.getProperty("os.name");

        if ((os.contains("mac") || os.contains("darwin")))
            return Constants.OSType.MACOS;

        if (os.contains("win"))
            return Constants.OSType.WINDOWS;

        if (os.contains("nux"))
            return Constants.OSType.LINUX;

        return Constants.OSType.OTHER;
    }
}
