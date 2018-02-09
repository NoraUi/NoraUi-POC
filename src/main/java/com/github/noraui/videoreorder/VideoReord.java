/**
 * NoraUi is licensed under the licence GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.videoreorder;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;

import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.noraui.utils.Utilities.OperatingSystem;
import com.github.noraui.utils.Utilities.SystemArchitecture;

public class VideoReord {

    /**
     * Specific logger
     */
    private static final Logger logger = LoggerFactory.getLogger(VideoReord.class);

    public static final String USER_DIR = "user.dir";
    public static final String DOWNLOADED_FILES_FOLDER = "downloadFiles";

    private ScreenRecorder screenRecorder;

    public void main(String[] args) throws Exception {
        VideoReord videoReord = new VideoReord();
        videoReord.startRecording();

        final OperatingSystem currentOperatingSystem = OperatingSystem.getCurrentOperatingSystem();
        String pathWebdriver = String.format("src/test/resources/drivers/%s/googlechrome/%s/chromedriver%s", currentOperatingSystem.getOperatingSystemDir(),
                SystemArchitecture.getCurrentSystemArchitecture().getSystemArchitectureName(), currentOperatingSystem.getSuffixBinary());

        if (!new File(pathWebdriver).setExecutable(true)) {
            logger.error("ERROR when change setExecutable on " + pathWebdriver);
        }

        System.setProperty("webdriver.chrome.driver", pathWebdriver);
        final ChromeOptions chromeOptions = new ChromeOptions();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

        final HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", System.getProperty(USER_DIR) + File.separator + DOWNLOADED_FILES_FOLDER);
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        WebDriver driver = new ChromeDriver(capabilities);

        driver.get("http://www.google.com");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("BreizhCamp 2018");
        element.submit();
        logger.info("Page title is: " + driver.getTitle());
        driver.quit();
        videoReord.stopRecording();
    }

    public void startRecording() throws Exception {
        File file = new File(System.getProperty(USER_DIR) + File.separator + DOWNLOADED_FILES_FOLDER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        this.screenRecorder = new SpecializedScreenRecorder(gc, captureSize, new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)), null, file, "MyVideo");
        this.screenRecorder.start();
    }

    public void stopRecording() throws Exception {
        this.screenRecorder.stop();
    }

}
