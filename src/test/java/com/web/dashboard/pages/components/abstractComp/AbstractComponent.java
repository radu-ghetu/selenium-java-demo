package com.web.dashboard.pages.components.abstractComp;

import com.web.dashboard.pages.components.interfaces.ComponentI;
import com.web.util.Driver;
import com.web.util.MessageException;
import org.assertj.core.api.Assertions;
import org.junit.platform.commons.util.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractComponent implements ComponentI {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private WebDriver driver = Driver.getDriver();
    private SearchContext searchContext;
    private By searchContextLocator;
    public WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    //    public AbstractComponent(WebDriver driver) {
//        this(driver, driver);
//    }
    public AbstractComponent() {
        PageFactory.initElements(driver, this);
    }

    /*
    I remove WebDriver as parameter as we use Singleton DP and sharing driver in application
     */
    public AbstractComponent(SearchContext searchContext) {
//        this.driver = driver;
        this.searchContext = searchContext;
        this.searchContextLocator = null;
    }

    public AbstractComponent(By searchContextLocator) {
        // this.driver = driver;
        this.searchContext = driver;
        this.searchContextLocator = searchContextLocator;
    }

    public AbstractComponent waitForComponentToLoad() {
        return this;
    }

    protected void waitForComponentsToLoad(AbstractComponent... components) {
        Stream.of(components).parallel().forEach(AbstractComponent::waitForComponentToLoad);
    }

    /**
     * The SearchContext defines the search domain
     *
     * @return
     */
    public SearchContext getContext() {

        if (searchContext == null || (searchContext == driver && searchContextLocator != null)) {
            searchContext = driver.findElement(searchContextLocator);
        } else if (searchContextLocator != null) {
            try {
                searchContext.findElement(By.cssSelector("*"));
            } catch (WebDriverException e) {
                searchContext = driver.findElement(searchContextLocator);
            }
        }

        return searchContext;
    }

    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
        this.searchContextLocator = null;
    }

    public void setSearchContextLocator(By searchContextLocator) {
        this.searchContext = driver;
        this.searchContextLocator = searchContextLocator;
    }

//    /*
//    we have getDriver() in Driver class
//     */
//
//    @Override
//    public WebDriver getWebDriver() {
//        return driver;
//    }

    /*
     * //confirmation pop up is true when it is a traditional alert
     */

    protected void confirmationYes() {
        ((JavascriptExecutor) driver).executeScript("window.confirm = function(msg){return true;}");
    }

    /**
     * Performs a pause
     *
     * @param seconds
     */
    public static void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitForMilliSeconds(int miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * waits for backgrounds processes on the browser to complete
//     *
//     * @param timeOutInSeconds
//     */
//    public static void waitForPageToLoad(long timeOutInSeconds) {
//        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver driver) {
//                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
//            }
//        };
//        try {
//            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
//            wait.until(expectation);
//        } catch (Throwable error) {
//            error.printStackTrace();
//        }
//    }

    protected boolean isElementPresent(By locatorKey) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
            driver.findElement(locatorKey);
            driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
            return false;
        }
    }

    protected boolean isElementPresent(List<WebElement> l) {
        boolean result;
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        result = l.size() > 0;
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
        return result;
    }

    protected void waitForAllToExist(By... locators) {

        try {
            ExpectedCondition<Boolean> condition = ExpectedConditions
                    .and(Stream.of(locators).map(ExpectedConditions::presenceOfElementLocated)
                            .toArray(ExpectedCondition[]::new));
            waitFor(Duration.ofSeconds(30), condition);
        } catch (Exception e) {
            fail("Failed to locate all elements in timeframe: " + e.getMessage());
        }
    }

    protected void waitForAllToBeVisible(WebElement... elements) {

        try {
            ExpectedCondition<Boolean> condition = ExpectedConditions
                    .and(Stream.of(elements).map(ExpectedConditions::visibilityOfAllElements)
                            .toArray(ExpectedCondition[]::new));
            waitFor(Duration.ofSeconds(30), condition);
        } catch (Exception e) {
            fail("Failed to locate all elements in timeframe: " + e.getMessage());
        }
    }

    protected void waitForAllToBeVisible(By... locators) {
        List<WebElement> elements = Stream.of(locators).parallel()
                .map(this::findElement)
                .collect(toList());
        waitFor(Duration.ofSeconds(5), ExpectedConditions.visibilityOfAllElements(elements));
    }

    protected void waitForAllToBeVisible(List<WebElement> elements) {
        //TODO decrease waits after performance of Event tracker will be improved
        waitFor(Duration.ofSeconds(120), ExpectedConditions.visibilityOfAllElements(elements));
    }

    protected void waitForAllToBeInvisible(By... locators) {
        List<WebElement> elements = Stream.of(locators).parallel()
                .map(this::findElement)
                .collect(toList());
        waitFor(Duration.ofSeconds(5), ExpectedConditions.invisibilityOfAllElements(elements));
    }

    /**
     * Waits for element matching the locator to be visible on the page
     *
     * @param locator
     * @param timeout
     * @return
     */
    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(),Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Verifies whether the element matching the provided locator is displayed on page
     *
     * @param by
     * @throws AssertionError if the element matching the provided locator is not found or not displayed
     */
    public static void verifyElementDisplayed(By by) {
        try {
            assertTrue(Driver.getDriver().findElement(by).isDisplayed(), "Element not visible: " + by);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Assertions.fail("Element not found: " + by);

        }
    }

    /**
     * Verifies whether the element matching the provided locator is displayed on page
     *
     * @param element
     * @throws AssertionError if the element matching the provided locator is not found or not displayed
     */
    public static void verifyElementDisplayed(WebElement element) {
        try {
            assertTrue(element.isDisplayed(), "Element not visible");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Assertions.fail("Element not found");

        }
    }

    /**
     * Verifies whether the element matching the provided locator is NOT displayed on page
     *
     * @param by
     * @throws AssertionError the element matching the provided locator is displayed
     */
    protected void verifyElementNotDisplayed(By by) {
        try {
            assertFalse(findElement(by).isDisplayed(), "Element should not be visible: " + by);
        } catch (NoSuchElementException e) {
            e.printStackTrace();

        }
    }

    /**
     * Wait the default timeout for an element with the given id to load into
     * the page.
     *
     * @param locator Element ID to wait for.
     */
    protected boolean waitForExists(final By locator) {
        try {
            waitFor(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * Wait the default timeout for an element with the given id to load into
     * the page.
     *
     * @param locator Element ID to wait for.
     */
    protected boolean waitForToNotExist(final By locator) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        List<WebElement> l = driver.findElements(locator);
        if (l.size() == 0) {
            driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
            return true;
        } else {
            try {
                waitForInvisibilityOf(l.get(0));
                driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
                return true;
            } catch (Exception e) {
                driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
                return false;
            }
        }
    }

    protected boolean elementExists(final By locator) {
        try {
            waitFor(Duration.ofMillis(500), input -> !findElements(locator).isEmpty());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    protected boolean elementDoesNotExist(final By locator) {
        try {
            waitFor(Duration.ofMillis(500), input -> findElements(locator).isEmpty());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void waitForComponentToBeStale() {
        if (searchContext instanceof WebElement) {
            waitFor(ExpectedConditions.stalenessOf((WebElement) searchContext));
        } else {
            throw new IllegalStateException(
                    "Component must have been provided a context to be able to wait for it " +
                            "to be stale.");
        }

    }

    /**
     * Waits for element to be not stale
     *
     * @param locator
     */
    public void waitForStaleElement(By locator) {
        WebElement element = findElement(locator);
        int y = 0;
        while (y <= 15) {
            if (y == 1)
                try {
                    element.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    protected void click(By locator) {
        click(getContext(), locator);
    }

    protected void click(SearchContext context, By locator) {
        context.findElement(locator).click();
    }


    protected void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        scrollToObject(element);
    }

    protected void clickAndNavigate(By locator) {
        clickAndNavigate(getContext(), locator);
    }

    protected void clickAndNavigate(SearchContext searchContext, By locator) {
        WebElement originalBody = driver.findElement(HTML_BODY);
        executeJs("arguments[0].tagForRefresh = true", originalBody);
        click(searchContext, locator);
        waitFor(Duration.ofSeconds(30),
                ExpectedConditions.or(ExpectedConditions.stalenessOf(originalBody), driver -> {
                    try {
                        WebElement body = driver.findElement(HTML_BODY);
                        return executeJs("return !arguments[0] || !arguments[0].tagForRefresh",
                                body);
                    } catch (WebDriverException e) {
                        return true;
                    }
                }));
    }

    // click element using javascript
    public void clickJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * Performs double click action on an element
     *
     * @param locator
     */
    protected void doubleClick(By locator) {
        WebElement element = findElement(locator);
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    /**
     * Performs double click action on an element
     *
     * @param element
     */
    public void doubleClick(WebElement element) {
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    /**
     * attempts to click on provided element until given time runs out
     *
     * @param locator
     * @param timeout
     */
    protected void clickWithTimeOut(By locator, int timeout) {
        WebElement element = findElement(locator);
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitForSeconds(1);
            }
        }
    }

    /**
     * Returns true if text is on the page, false if not Newer versions of
     * Selenium no longer have the isTextPresentInTable method so we had to implement
     * our own with the help of Selenium docs
     *
     * @param value - string value you want to verify is on the page
     */
    protected boolean verifyTextPresent(String value) {
        return driver.getPageSource().contains(value);
    }

    protected boolean verifyTextPresent(final By locator, String value) {
        return verifyTextPresent(getContext(), locator, value);
    }

    protected boolean verifyTextPresent(SearchContext context, By locator, String value) {
        waitForExists(locator);
        return locator.findElement(context).getText().contains(value);
    }

    /**
     * Clears value when given a locator - By id, name, xpath, cssSeletor,
     * linkText, etc and then the value
     */
    protected void clear(final By locator) {
        clear(getContext(), locator);
    }

    /**
     * Clears value when given a locator - By id, name, xpath, cssSeletor,
     * linkText, etc and then the value
     */
    protected void clear(SearchContext context, final By locator) {
        waitForExists(locator);
        locator.findElement(context).clear();
    }

    /**
     * wait for the loading icon to appear and then disappear to know a page has
     * loaded
     */
    protected void waitForLoadingIcon() {
        try {

            waitForVisibilityOf(LOADING_ICON);
            waitForInvisibilityOf(LOADING_ICON);
        } catch (Exception e) {
            // Ignore failing to find loading as it may come and go quicker than selenium can find.
        }
    }

    /**
     * wait until element is clickable
     *
     * @param locator
     */
    protected void waitForClickable(final By locator) {
        waitFor(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForClickable(final WebElement element) {
        waitFor(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Briefly pause the test to wait for an operation to complete.
     *
     * @param milliseconds The # of milliseconds to pause
     */
    protected void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Briefly pause the test to wait for an operation to complete.
     *
     * @param seconds The # of seconds to pause
     */
    protected void pauseSeconds(int seconds) {
        pause(seconds * 1000);
    }

    /** Wait for the item to appear */
    protected void waitForVisibilityOf(By locator) {
        waitFor(Duration.ofSeconds(5), ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForVisibilityOf(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForInvisibilityOf(WebElement element) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait.until(invisibilityOf(element));
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
    }

    public static ExpectedCondition<Boolean> invisibilityOf(final WebElement element) {
        return new ExpectedCondition<>() {

            @Override
            public Boolean apply(WebDriver webDriver) {
                try {
                    return !element.isDisplayed();
                } catch (Exception exceptionOnNotExistElement) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "invisibility of " + element;
            }
        };
    }

    protected boolean isVisible(By locator) {
        try {
            waitFor(Duration.ofMillis(200), ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected void zeroImplicitlyWait() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
    }

    protected void recoverImplicitlyWait() {
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.MILLISECONDS);
    }

    protected boolean isVisible(WebElement element) {
        try {
            zeroImplicitlyWait();
            waitFor(Duration.ofMillis(200), ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            recoverImplicitlyWait();
            return false;
        }
        recoverImplicitlyWait();
        return true;
    }

    // wait item to disappear
    protected void waitForInvisibilityOf(By locator) {
        waitFor(Duration.ofSeconds(5), ExpectedConditions.invisibilityOfElementLocated(locator));
    }


    protected boolean isInvisible(By locator) {
        try {
            waitFor(Duration.ofMillis(200), ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    protected boolean isInvisible(WebElement element) {
        try {
            waitFor(Duration.ofMillis(200), ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            return true;
        }
        return false;
    }


    protected boolean isElementVisible(By locator) {
        Driver.getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        if (Driver.getDriver().findElements(locator).size() == 0) {
            Driver.getDriver().manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
            return false;
        } else {
            Driver.getDriver().manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
            return true;
        }
    }

    protected void pressEnter(By locator) {
        pressEnter(driver, locator);
    }

    protected void pressEnter(SearchContext context, By locator) {
        context.findElement(locator).sendKeys(Keys.ENTER);
    }

    protected void pressBackspace(WebElement element) {
        element.sendKeys(Keys.BACK_SPACE);
    }

    protected void pressBackspace(WebElement element, int counter) {
        IntStream.range(0, counter).forEach(i -> pressBackspace(element));
    }

    protected void scrollToObject(By locator) {
        scrollToObject(getContext(), locator);
    }

    protected void scrollToObject(SearchContext context, By locator) {
        scrollToObject(locator.findElement(context));
    }

    protected void type(By locator, String text) {
        type(getContext(), locator, text);
    }

    protected void type(SearchContext context, By locator, String text) {
        waitForExists(locator);
        type(locator.findElement(context), text);
    }

    protected void type(WebElement element, String text) {
        element.sendKeys(text);
    }

    protected void updateTextField(By locator, String text) {
        updateTextField(getContext(), locator, text);
    }

    protected void updateTextField(SearchContext context, By locator, String text) {
        WebElement element = findElement(context, locator);
        updateTextField(element, text);
    }

    protected void updateTextField(WebElement element, String text) {
        clear(element);
        type(element, text);
    }

    /**
     * Extracts text from list of elements matching the provided locator into new List<String>
     *
     * @param locator
     * @return list of strings
     */
    protected List<String> getElementsText(By locator) {

        List<WebElement> elems = findElements(locator);
        List<String> elemTexts = new ArrayList();

        for (WebElement el : elems) {
            elemTexts.add(el.getText());
        }
        return elemTexts;
    }

    protected void clear(WebElement element) {
        element.clear();
        String text = getAttributeValue(element, "value");
        if (!text.isEmpty()) {
            pressBackspace(element, text.length());
            assertThat(element.getText())
                    .as("Element text shoud be cleared.")
                    .isEmpty();
        }
    }

    protected void updateTextFieldWithBlur(By locator, String text) {
        updateTextFieldWithBlur(getContext(), locator, text);
    }

    protected void updateTextFieldWithBlur(SearchContext context, By locator, String text) {
        WebElement element = findElement(context, locator);
        updateTextFieldWithBlur(element, text);
    }

    protected void updateTextFieldWithBlur(WebElement element, String text) {
        element.clear();
        if (StringUtils.isNotBlank(text)) {
            type(element, text);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].focus();.change().blur();", element);
        }
    }

    protected String getText(By locator) {
        return getText(getContext(), locator);
    }

    protected String getText(SearchContext context, By locator) {
        waitForVisibilityOf(locator);
        return locator.findElement(context).getText();
    }

    protected String getUrlByLinkText(By locator) {
        waitForVisibilityOf(locator);
        return locator.findElement(getContext()).getAttribute("href");
    }

    protected String getAttributeValue(By locator, String attributeName) {
        return getAttributeValue(getContext(), locator, attributeName);
    }

    protected String getAttributeValue(SearchContext context, By locator, String attributeName) {
        waitForExists(locator);
        return getAttributeValue(locator.findElement(context), attributeName);
    }

    protected String getAttributeValue(WebElement element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    //Javascript capabilities

    public Object executeJs(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    // click element using javascript
    protected void clickWithJS(By locator) {
        WebElement element = findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param locator
     */
    protected void executeJScommand(By locator, String command) {
        WebElement element = findElement(locator);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript(command, element);

    }

    /**
     * executes the given JavaScript command on given web element
     *
     * @param command
     */
    protected void executeJScommand(String command) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript(command);

    }

    public void waitForJSToComplete() {
        waitFor(Duration.ofSeconds(10),
                driver -> ((JavascriptExecutor) driver)
                        .executeScript("return typeof(jQuery) == 'undefined' || $.active == 0"));
    }

    protected void scrollToObject(WebElement target) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);",
                target);

    }

    protected void scrollToMiddle(WebElement target) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                target);

    }

    protected void waitForValue(By locator) {
        (new WebDriverWait(driver, Duration.ofSeconds(10))).until((ExpectedCondition<Boolean>) d ->
                d.findElement(locator).getText().length() != 0);
    }

    protected boolean elementHasClass(By locator, String className) {
        return elementHasClass(getContext(), locator, className);
    }

    protected boolean elementHasClass(SearchContext context, By locator, String className) {
        return Arrays.asList(getAttributeValue(context, locator, "class").split(" "))
                .contains(className);
    }

    protected boolean elementHasClass(WebElement element, String className) {
        return Arrays.asList(getAttributeValue(element, "class").split(" ")).contains(className);
    }

    public WebElement findElement(By locator) {
        return findElement(getContext(), locator);
    }

    protected WebElement findElement(SearchContext context, By locator) {
        return locator.findElement(context);
    }

    protected List<WebElement> findElements(By locator) {
        return findElements(getContext(), locator);
    }

    protected List<WebElement> findElements(SearchContext context, By locator) {
        return locator.findElements(context);
    }

    protected List<String> getVisibleElementText(SearchContext context, By locator) {
        try {
            waitForExists(locator);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return findElements(context, locator).stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .collect(toList());
    }

    protected List<String> getVisibleElementText(By locator) {
        return getVisibleElementText(getContext(), locator);
    }

    protected boolean allExists(final By... locators) {
        for (By locator : locators) {
            try {
                waitFor(Duration.ofSeconds(5),
                        ExpectedConditions.presenceOfElementLocated(locator));
            } catch (Exception e) {
                return false;
            }

        }
        return true;
    }

    protected boolean isChecked(By checkboxSelector) {
        return isChecked(getContext(), checkboxSelector);
    }

    protected boolean isChecked(SearchContext context, By checkboxSelector) {
        WebElement checkbox = findElement(context, checkboxSelector);
        if (checkbox.getTagName().equals("input")) {
            return checkbox.isSelected();
        } else {
            return elementHasClass(context, checkboxSelector, "checked");
        }
    }

    protected void setCheckbox(By checkboxSelector, boolean value) {
        setCheckbox(getContext(), checkboxSelector, value);
    }

    protected void setCheckbox(SearchContext context, By checkboxSelector, boolean value) {
        if (isChecked(context, checkboxSelector) != value) {
            click(context, checkboxSelector);
        }
    }

    /**
     * Checks or unchecks given checkbox
     *
     * @param element
     * @param check
     */
    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    protected void acceptAlert() {
        waitFor(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();
        waitForComponentToBeStale();
    }

    protected void moveMouseOverElement(By locator) {

        WebElement we = findElement(locator);

        Actions action = new Actions(driver);
        action.moveToElement(we).build().perform();
    }


    protected String getComputedStyleValue(By locator, String cssProperty) {
        String format = "return window.getComputedStyle(arguments[0])['%s']";
        return Objects
                .toString(executeJs(String.format(format, cssProperty), findElement(locator)));
    }

    protected String getComputedStyleValue(WebElement element, String cssProperty) {
        String format = "return window.getComputedStyle(arguments[0])['%s']";
        return Objects
                .toString(executeJs(String.format(format, cssProperty), element));

    }


    protected String translateWindowRgbToHex(String rgb) {
        Pattern rgbRegExp = Pattern.compile("^rgb\\((.*), (.*), (.*)\\)$");
        Matcher matcher = rgbRegExp.matcher(rgb);
        String hex = "#";
        if (matcher.find()) {
            for (int i = 1; i <= 3; i++) {
                Integer val = Integer.parseInt(matcher.group(i));
                System.out.print(val + ":" + Integer.toHexString(val) + " ");
                hex += Integer.toHexString(val);
            }
            System.out.print("");
        }
        return hex;
    }

    /**
     * Changes the HTML attribute of a Web Element to the given value using JavaScript
     *
     * @param element
     * @param attributeName
     * @param attributeValue
     */
    protected static void setAttribute(WebElement element, String attributeName, String attributeValue) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attributeName, attributeValue);
    }

    /**
     * Highlighs an element by changing its background and border color
     *
     * @param locator
     */
    public void highlight(By locator) {
        WebElement element = findElement(locator);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
        waitForSeconds(1);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].removeAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    /**
     * Highlighs an element by changing its background and border color
     *
     * @param element
     */
    public void highlight(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
        waitForSeconds(1);
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].removeAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    /**
     * Moves the mouse to given element
     *
     * @param element on which to hover
     */
    protected void hover(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        actions.moveToElement(element, 5, 5).perform();
    }

    /**
     * switches to new window by the exact title
     */
    protected void switchToWindow(String targetTitle) {
        String origin = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(targetTitle)) {
                return;
            }
        }
        driver.switchTo().window(origin);
    }

    public void msgVerification(By locator, String expectedMsg) {
        WebElement element = findElement(locator);
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(element, expectedMsg));
            assertEquals(expectedMsg, element.getText());
        } catch (Exception e) {
            try {
                if (e.toString().contains("TimeOut")) {
                    throw new MessageException("Message is'n presented");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public WebElement getHeader(String expectedHeader) {
        List<WebElement> headers = driver.findElements(By.xpath("//table//th"));
        int idx = 0;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().toLowerCase().equals(expectedHeader)) {
                idx = i;
                idx++;
            }
        }
        String path = "//table//th" + idx + "]";
        return null;
    }

    protected String getColor(WebElement element, String attribute) {
        String value = element.getAttribute(attribute);
        String color = value.replaceAll("[\"^rgb\\\\((.*), (.*), (.*)\\\\)$\"]", "");

        return color;
    }

    protected int getRowCountWithoutHeaders() {
        List<WebElement> rows = driver.findElements(By.xpath("//table//tr"));
        return rows.size() - 1;
    }

    /**
     * method navigate to page which passed as a
     *
     * @param element
     */
    protected void navigateToPage(WebElement element) {
        waitForJSToComplete();
        waitForClickable(element);
        highlight(element);
        element.click();
    }

    /**
     * method entering the text to input and submitting with "Enter" from keyboard
     *
     * @param element
     * @param key
     */
    protected void sendKeyAndEnter(WebElement element, String key) {
        waitForJSToComplete();
        element.click();
        element.clear();
        element.sendKeys(key + Keys.ENTER);
    }

    /**
     * An expectation for checking number of WebElements with given locator being more than defined number
     *
     * @param elements used to find the element
     * @param number   used to define minimum number of elements
     * @return Boolean true when size of elements list is more than defined
     */
    public ExpectedCondition<List<WebElement>> numberOfElementsToBeMoreThan(final List<WebElement> elements,
                                                                            final Integer number) {
        return new ExpectedCondition<>() {
            private Integer currentNumber = 0;

            @Override
            public List<WebElement> apply(WebDriver webDriver) {
                currentNumber = elements.size();
                return currentNumber > number ? elements : null;
            }

            @Override
            public String toString() {
                return String.format("number of elements found by %s to be more than \"%s\". Current number: \"%s\"",
                        elements, number, currentNumber);
            }
        };
    }

    protected void scrollToElement(WebElement element) {
        scrollToObject(element);
    }

    protected void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight)");
    }

    protected void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, 0)");
    }

}