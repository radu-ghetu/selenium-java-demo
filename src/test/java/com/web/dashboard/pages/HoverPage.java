package com.web.dashboard.pages;

import com.web.dashboard.pages.abstractPages.AbstractBredPage;
import com.web.util.ConfigurationReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.web.util.Driver.getDriver;

public class HoverPage extends AbstractBredPage {

    private static Logger LOG = LogManager.getLogger(LoginPage.class);

    @FindBy(css = ".figure")
    private List<WebElement> figures;

    @FindBy(css = ".figcaption")
    private List<WebElement> captions;

    public void navigate() {
        getDriver().get(ConfigurationReader.get("url")+"/hovers");
    }

    public void verifyPageLoaded() {
        figures.get(0).isDisplayed();
    }

    public void hoverToElement(int i) {
        Actions a = new Actions(getDriver());
        a.moveToElement(figures.get(i-1)).perform();
    }

    public void verifyCorrectCaptionShown(int i) {
        LOG.info("Display = " + captions.get(i-1).getCssValue("display"));
        Assert.assertTrue(captions.get(i-1).getCssValue("display").equals("block"));
        Assert.assertTrue(captions.get(i-1).getText().contains(String.valueOf(i)));
    }
}