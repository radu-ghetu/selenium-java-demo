package com.web.dashboard.pages.components;


import com.web.dashboard.pages.components.abstractComp.AbstractComponent;
import com.web.util.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
     *
     */
    public class HiddenFilterBar extends AbstractComponent {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));
        @FindBy(id = "filter-toggle-button")
        public WebElement top_filter;

        @FindBy(id = "selected-date-label")
        public WebElement selected_date;

        @FindBy(id = "mui-component-select-selectedSite")
        public WebElement selected_site;

        private static final By FILTER_TOGGLE_BUTTON = By.id("filter-toggle-button");
        private final By[] hiddenElements;

        public HiddenFilterBar(By... hiddenElements) {
            super();
            this.hiddenElements = hiddenElements;
        }

        public void toggleFilters(boolean open) {
            if (isOpen() != open) {
                top_filter.click();
                if (open) {
                    wait.until(ExpectedConditions.elementToBeClickable(selected_date));
                    wait.until(ExpectedConditions.visibilityOf(selected_site));
                } else {
                    waitForInvisibilityOf(selected_date);
                    waitForInvisibilityOf(selected_site);
                }
            }
        }


//    public boolean isOpen() {
//        return Stream.of(hiddenElements).allMatch(this::isVisible);
//    }

        public boolean isOpen() {
            for (By hiddenElement : hiddenElements) {
                if (findElement(hiddenElement).isDisplayed())
                    return true;
            }
            return false;
        }


        public void toggleFilters(By filter) {
            toggleFilters(!isFilterBarOpen());
        }

        public void toggleFilters(WebElement filter) {
            toggleFilters(!isFilterBarOpen());
        }

        public boolean isFilterBarOpen() {
            return isOpen();
        }
    }
