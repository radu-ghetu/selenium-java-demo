package com.web.util;

import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import static org.junit.platform.commons.util.StringUtils.isNotBlank;


public class Driver {
    private Driver() {
    }

    private static final Logger LOG = LogManager.getLogger(ConfigurationReader.class);
    private static final ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private static Config conf = ConfigurationReader.config;
    private static int remainingTries = 10;
    private static String languagePrefix = "en-US";
    private static String browser;

    public static RemoteWebDriver getDriver() {
            if (driver.get() == null) {
                browser = ConfigurationReader.get("browser.type");
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.BROWSER, Level.SEVERE);

                String remoteDriverUrl = conf.getString("remote.driver.url");
                LOG.debug("Browser = " + browser);
                if (isNotBlank(remoteDriverUrl)) {
                    MutableCapabilities browserOptions = setRemoteDriver(browser);

                    LOG.debug("Connecting to a remote webDriver at " + remoteDriverUrl);
                    try {
                        RemoteWebDriver newdriver = new RemoteWebDriver(new URL(remoteDriverUrl), browserOptions);
                        driver.set(newdriver);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    
                } else {
                    switch (browser) {
                        case "chrome":
                            LOG.debug("Configuring a Chrome Browser Driver");
                            WebDriverManager.chromedriver().setup();
                            ChromeOptions optc = new ChromeOptions();
                            optc.addArguments("--disable-notifications");
                            optc.addArguments("--start-maximized");
                            optc.addArguments("use-fake-device-for-media-stream");
                            optc.addArguments("use-fake-ui-for-media-stream");
                            optc.addArguments("--remote-allow-origins=*");
                            driver.set(new ChromeDriver(optc));
                            break;
                        case "chromeHeadless":
                            LOG.debug("Configuring a ChromeHeadless Browser Driver");
                            WebDriverManager.chromedriver().setup();
                            optc = new ChromeOptions();
                            optc.addArguments("--remote-allow-origins=*");
                            optc.addArguments("--headless=true");
                            driver.set(new ChromeDriver(optc));
                            break;

                        case "firefox":
                            LOG.debug("Configuring a Firefox Browser Driver");
                            FirefoxOptions optf = new FirefoxOptions();
                            optf.addPreference("dom.webnotifications.enabled",false);
                            optf.addPreference("dom.push.enabled",false);
                            optf.addPreference("media.navigator.streams.fake", true);
                            optf.addArguments("--window-size=1920,1080");
                            driver.set(new FirefoxDriver(optf));
                            break;
                        case "firefoxHeadless":
                            LOG.debug("Configuring a FirefoxHeadless Browser Driver");
                            optf = new FirefoxOptions();
                            optf.addArguments("--headless=true");
                            driver.set(new FirefoxDriver(optf));
                            break;

                        case "edge":
                            LOG.debug("Configuring a EDGE Browser Driver");
                            WebDriverManager.edgedriver().setup();
                            driver.set(new EdgeDriver());
                            break;

                        case "safari":
                            LOG.debug("Configuring a Safari Browser Driver");
                            if (System.getProperty("os.name").toLowerCase().contains("windows"))
                                throw new WebDriverException("You are operating Windows OS which doesn't support Safari");
                            WebDriverManager.getInstance(SafariDriver.class).setup();
                            driver.set(new SafariDriver());
                            break;
                    }
                }

            }

            if (driver.get() == null) {
                LOG.error("Encountered an error loading the WebDriver, trying again ");
                --remainingTries;
                getDriver();
            }
            if (remainingTries <= 0){
                Assertions.fail("Failed to open browser");
            }
            driver.get().manage().window().maximize();
        return driver.get();
    }

    public static void closeDriver() {
        try {
            if (driver.get() != null) {
                Thread.sleep(1000);
                driver.get().quit();
                driver.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeDriverSoft() {
        try {
            if (driver.get() != null) {
                Thread.sleep(1000);
                driver.get().close();
                driver.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MutableCapabilities setRemoteDriver(String browser) {

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions optc = new ChromeOptions();
                optc.addArguments("window-size=1920,1080");
                optc.addArguments("disable-notifications");
                optc.addArguments("start-maximized");
                optc.addArguments("use-fake-device-for-media-stream");
                optc.addArguments("use-fake-ui-for-media-stream");
                return optc;
            case "safari":
                return new SafariOptions();
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions opte = new EdgeOptions();
                opte.addArguments("--window-size=1920,1080");
                return new InternetExplorerOptions(opte);
            case "firefox":
                FirefoxOptions optf = new FirefoxOptions();
                optf.addPreference("dom.webnotifications.enabled",false);
                optf.addPreference("dom.push.enabled",false);
                optf.addPreference("media.navigator.permission.disabled",true);
                optf.addPreference("media.navigator.streams.fake", true);
                optf.addArguments("--window-size=1920,1080");
                return optf;
            default:
                FirefoxOptions optff = new FirefoxOptions();
                optff.addPreference("dom.webnotifications.enabled",false);
                optff.addPreference("dom.push.enabled",false);
                optff.addPreference("media.navigator.permission.disabled",true);
                optff.addPreference("media.navigator.streams.fake", true);
                optff.addArguments("--window-size=1920,1080");
                return optff;
        }
    }
}