package com.web.dashboard.pages;

import com.web.dashboard.pages.abstractPages.AbstractBredPage;
import com.web.util.ConfigurationReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.web.util.Driver.getDriver;

public class DynamicPage extends AbstractBredPage {

    private static Logger LOG = LogManager.getLogger(LoginPage.class);

    @FindBy(id = "checkbox-example")
    private WebElement checkboxForm;

    @FindBy(id = "input-example")
    private WebElement inputForm;

    @FindBy(css = "button[onclick='swapCheckbox()']")
    private WebElement removeCheckBoxBtn;

    @FindBy(css = "button[onclick='swapInput()']")
    private WebElement disableInputBtn;

    @FindBy(id = "loading")
    private WebElement loadingIndicator;

    public void navigate() {
        getDriver().get(ConfigurationReader.get("url") + "/dynamic_controls");
    }

    public void verifyPageLoaded() {
       checkboxForm.isDisplayed();
       inputForm.isDisplayed();
    }


    public void clickRemoveCheckboxBtn() {
        removeCheckBoxBtn.click();
    }

    public void verifyCheckboxRemoved() {
        waitForInvisibilityOf(loadingIndicator);
        getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        Assert.assertTrue(checkboxForm.findElements(By.id("checkbox")).size()==0);
        getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(ConfigurationReader.get("implicit.timeout.seconds")), TimeUnit.SECONDS);
    }

    public void clickDisableInputBtn() {
        disableInputBtn.click();
    }

    public void verifyInputDisabled() {
        waitForInvisibilityOf(loadingIndicator);
        Assert.assertFalse(inputForm.findElement(By.tagName("input")).isEnabled());
    }
}