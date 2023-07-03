package com.web.dashboard.pages.components.abstractComp;


import com.web.util.Driver;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AbstractPageObject extends AbstractComponent{

        public AbstractPageObject()
        {
            super();
        }

        protected AbstractPageObject( By searchContextLocator)
        {
            super( searchContextLocator);
        }

        public AbstractPageObject waitForPageToLoad()
        {
            return (AbstractPageObject) super.waitForComponentToLoad();
        }

        public void waitForUrl(Duration duration, String urlRegex){
            super.waitFor(duration, ExpectedConditions.urlMatches(urlRegex));
        }

        /*
         * //confirmation pop up is true when it is a traditional alert
         */
        public void confirmationYes()
        {
            ((JavascriptExecutor) Driver.getDriver()).executeScript("window.confirm = function(msg){return true;}");
        }

        // get id value from supplied dashboardUrl address
        public Map<String, String> getParameterMap(String address)
        {

            String queryString = StringUtils.substringAfter(address, "?");

            String[] paramPlusValues = queryString == null ? new String[0] : queryString.split("&");

            Map<String, String> paramMap = new HashMap<String, String>();
            for (String param : paramPlusValues)
            {
                String[] paramPlusVal = param.split("=");
                paramMap.put(paramPlusVal[0], paramPlusVal[1]);
                System.out.println("Param:" + paramPlusVal[0] + "=" + paramPlusVal[1]);
            }
            return paramMap;
        }

        // go to popin, click to focus after its visible
        public void goToPopin()
        {
            click(By.id("fancybox-content"));
        }


        protected void scrollToObject(By locator)
        {
            WebElement element = Driver.getDriver().findElement(locator);
            scrollToObject(element);
        }

        public void refreshPage()
        {
            executeJs("arguments[0].tagForRefresh = true", Driver.getDriver().findElement(HTML_BODY));
            Driver.getDriver().navigate().refresh();
            waitFor(driver -> executeJs("return !arguments[0] || !arguments[0].tagForRefresh", Driver.getDriver().findElement(HTML_BODY)));
            waitForPageToLoad();
            waitForJSToComplete();
            waitForSeconds(3);
        }

        public String getPageTitle() {
            return Driver.getDriver().getTitle();
        }

        public String getPageUrl() {
            return Driver.getDriver().getCurrentUrl();
        }

        public AbstractPageObject goBack() {
            Driver.getDriver().navigate().back();

            return this;
        }

        public String getUrlByLinkText(String linkText) {
            By linkLocator = By.linkText(linkText);
            waitForVisibilityOf(linkLocator);
            return getAttributeValue(linkLocator, "href");
        }

    public void maximizeWindow()
    {
        Driver.getDriver().manage().window().maximize();
        waitForPageToLoad();
        waitForJSToComplete();
    }


    }
