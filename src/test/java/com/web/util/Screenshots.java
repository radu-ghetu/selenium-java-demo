package com.web.util;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static com.web.util.Driver.getDriver;

/**
 *
 */
public class Screenshots {

    private static final Logger LOG = LoggerFactory.getLogger(Screenshots.class);


    private static Map<String, Boolean> screenshotsTaken;

    public Screenshots() {
        screenshotsTaken = Maps.newHashMap();
    }

    public File takeScreenshot(String fileName) {
        final String screenshotsDir = "build/reports/screenshots";
        File screenshot = null;
        if (getDriver() instanceof TakesScreenshot) {
            screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        }

        try {
            if (screenshot != null) {
                String fileNameToUse = fileName;
                File outputFile = new File(screenshotsDir, fileName + ".png");
                File htmlOutputFile = new File(screenshotsDir, fileName + ".html");

                int screenshotNum = 1;
                while (screenshotsTaken.containsKey(fileNameToUse) && outputFile.exists()) {
                    fileNameToUse = fileName + "." + screenshotNum++;
                    outputFile = new File(screenshotsDir, fileNameToUse + ".png");
                    htmlOutputFile = new File(screenshotsDir, fileNameToUse + ".html");
                }

                LOG.warn("Took Screenshot " + outputFile.getName());
                LOG.warn("Screenshot found at path " + outputFile.getAbsolutePath());
                FileUtils.writeStringToFile(htmlOutputFile,
                        getDriver().getPageSource(),
                        Charset.defaultCharset());
                FileUtils.copyFile(screenshot, outputFile);
                screenshot.deleteOnExit();
                screenshotsTaken.put(fileNameToUse, Boolean.TRUE);
            }
        } catch (IOException ioe) {
            LOG.error("Failed to save screenshot for test " + fileName);
        }
        return screenshot;
    }
}
