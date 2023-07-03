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

    @FindBy(id = "notistack-snackbar")
    private WebElement snackBar;

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
    public void waitForLoadingTextToDisappear(){
        int waited = 0;
        int maxWait = 40;
        List<WebElement> loadingText;
        getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loadingText = getDriver().findElements(By.cssSelector("h6[data-testid='loadingMsg']"));
        while (loadingText.size()>0 || (waited > maxWait)){
            waitForSeconds(1);
            loadingText = getDriver().findElements(By.cssSelector("h6[data-testid='loadingMsg']"));
            waited = waited + 1;
        }
        getDriver().manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
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

    public void snackBarIsDisplayed()
    {
        LOG.info("Verify snack bar appears");
        waitForVisibilityOf(snackBar);
    }

    public boolean isSnackBarDisplayed()
    {
        LOG.info("Verify snack bar appears");
        return snackBar.isDisplayed();
    }

    public void verifySnackBarText(String text)
    {
        LOG.info("Verify snack bar Text");
        assertTrue(snackBar.getText().contains(text));
    }

    public boolean snackBarContainsText(String text)
    {
        return (snackBar.getText().contains(text));
    }

    public void snackBarIsNotVisible()
    {
       waitForInvisibilityOf(snackBar);
    }

    public void clickButton(WebElement webElement) {
        assertTrue(webElement.isDisplayed());
        webElement.click();
    }

    public void enterKey(WebElement element, String key) {
        waitForJSToComplete();
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        element.clear();
        element.sendKeys(key);
    }

    public String clearAndFillInText(WebElement element, String textValue) {
        LOG.info("Clearing value");

        switch (ConfigurationReader.config.getString("browser.type")) {
            case "firefox": {
                element.clear();
                element.sendKeys(textValue);
                break;
            }
            case "chrome":
            case "edge":
                {
                element.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END,Keys.DELETE));
                waitForSeconds(1);
                element.sendKeys(textValue);
            }

            default: {
                LOG.info("No browser specified !");
                break;
            }
        }
        return textValue;
    }

    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    protected void clearInputfield(WebElement el) {
        while (!el.getAttribute("value").equals("")) {
            el.sendKeys(Keys.BACK_SPACE);
        }
    }

    public void clearMaintenanceFrame() {
        LOG.info("Close the maintenance iFrame if it exists");
        waitForPageToLoad();
        int maintenanceFrameVisibleSize = getDriver().findElements(By.xpath("//iframe")).size();
        if(maintenanceFrameVisibleSize > 1) {
            try {
                getDriver().switchTo().frame(0);
                WebElement maintenanceCloseBtn = getDriver().findElement(By.id("DN70"));
                LOG.info("Clearing maintenance window");
                waitForSeconds(1);
                waitForClickable(maintenanceCloseBtn);
                maintenanceCloseBtn.click();
            } finally {
                getDriver().switchTo().defaultContent();
            }
        }
     }

}