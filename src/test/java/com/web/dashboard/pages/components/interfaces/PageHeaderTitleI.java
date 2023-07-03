package com.web.dashboard.pages.components.interfaces;

import com.web.util.Driver;
import org.openqa.selenium.By;

public interface PageHeaderTitleI extends ComponentI {

    static final By PAGE_HEADER_TITLE = By.id("page-title");

    default String getPageHeaderTitle() {
        return Driver.getDriver().findElement(PAGE_HEADER_TITLE).getText();
    }
}