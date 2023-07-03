package com.web.dashboard.pages.components;

import com.web.dashboard.pages.components.abstractComp.AbstractComponent;
import com.web.util.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class SiteSelector extends AbstractComponent {

    public static final By SITE_SELECT = By.id("mui-component-select-selectedSite");
//    private static final String SITE_SELECT_OPTION_XPATH = "//*[@role='listbox']" +
//            "//*[@role='option'][contains(text(), '{siteName}')]";

    HiddenFilterBar hiddenFilterBar = new HiddenFilterBar();

    public SiteSelector() {
        super();
    }

    @Override
    public SiteSelector waitForComponentToLoad() {
        waitForAllToExist(SITE_SELECT);
        return this;
    }

    public void select(String siteName){
        waitForSeconds(2, elementToBeClickable(hiddenFilterBar.selected_site));
        hiddenFilterBar.selected_site.click();

        pause(500);

        WebElement siteToSelect = getSiteOptionLocator(siteName);
        System.out.println("siteToSelect "+siteToSelect);
        waitForSeconds(2, elementToBeClickable(siteToSelect));
        siteToSelect.click();
    }

//    private By getSiteOptionLocator(String siteName){
//        return By.xpath(SITE_SELECT_OPTION_XPATH
//                .replaceAll("\\{siteName\\}", siteName));
//    }

    private WebElement getSiteOptionLocator(String siteName){
        String xpath = "//li[contains(text(),'"+siteName+"')]";

        return Driver.getDriver().findElement(By.xpath(xpath ));
    }

    public String getCurrentSite(){
        return getText(SITE_SELECT);
    }

}