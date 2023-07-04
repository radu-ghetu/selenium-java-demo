package com.web.dashboard.pages.abstractPages;

import com.web.dashboard.pages.components.abstractComp.AbstractPageObject;
import com.web.util.ConfigurationReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.web.util.Driver.getDriver;
import static org.gradle.internal.impldep.org.testng.Assert.assertTrue;

/**
 * Base Page object to support common elements that should exist on all pages in any application of
 * Onair+
 */
public abstract class AbstractBredPage extends AbstractPageObject {

    public AbstractBredPage() {
        super();
        PageFactory.initElements(getDriver(), this);
    }

    private static Logger LOG = LogManager.getLogger(AbstractBredPage.class);

    @Override
    public AbstractBredPage waitForPageToLoad() {
        waitForJSToComplete();
        super.waitForPageToLoad();
        return this;
    }

    public AbstractBredPage waitToBeStable(int maxWaitMillis, int pollMillis) {
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + maxWaitMillis) {
            String prevState = getDriver().getPageSource();
            try {
                Thread.sleep(pollMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (prevState.equals(getDriver().getPageSource())) {
                break;
            }
        }
        return this;
    }

    public void titleVerification(String expectedTitle) {
        LOG.info("Verifying title");
        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(25));
        wait.until(ExpectedConditions.titleContains(expectedTitle));
        assertTrue(getDriver().getTitle().contains(expectedTitle), "Title is not as expected. Current title is: "+ getDriver().getTitle());
        LOG.debug("Title is presented and contains expected");
    }

}