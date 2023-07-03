package com.web.steps;

import com.web.dashboard.pages.LoginPage;
import com.web.util.ConfigurationReader;
import com.web.util.Driver;
import com.web.util.Screenshots;
import com.typesafe.config.Config;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.web.util.Driver.getDriver;
import static org.junit.Assert.assertTrue;

public class Hooks {
    private static Config env = ConfigurationReader.config;
    String browser = ConfigurationReader.get("browser.type");
    private Screenshots screenshots = new Screenshots();
    private static Logger LOG = LogManager.getLogger(Hooks.class);
    int pageTimeout = env.getInt("page.timeout.seconds");
    int implicitlyWait = env.getInt("implicit.timeout.seconds");
    LoginPage loginPage = new LoginPage();
    String TSuiteID;

    @Before(order = 1, value = "not @supportadmin")
    public void setUp(Scenario scenario) throws IOException, InterruptedException {
        LOG.info(
                "-------------------------- Before hook starts " +
                        "-------------------------");
        LOG.info("Navigate to " + ConfigurationReader.get("url"));
        getDriver().get(ConfigurationReader.get("url"));
        LOG.debug("Setting the implicit timeout to " + implicitlyWait + " seconds");
        getDriver().manage().timeouts().implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
    }


    @After(order = 0)
    public void tearDown(Scenario scenario) throws IOException, InterruptedException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        try {
            LOG.info("-------------------------- After hook starts" +
                    "-------------------------");

            if (scenario.isFailed()) {
                LOG.error("***** Error test is fail *****");
                String fileNameBase = "Screenshot-" + browser + "-" + env.getString("env") + "-" + formatter.format(calendar.getTime()) + "-" + scenario.getName();
                byte[] fileContent = Files.readAllBytes( screenshots.takeScreenshot(fileNameBase).toPath());
                scenario.embed(fileContent, "image/png");
            }

        } catch (Exception e) {
        } finally {
            Driver.closeDriver();
            LOG.info("Driver closed");
            LOG.info("-------------------------- After hook ends" +
                    "-------------------------");
        }
    }
}