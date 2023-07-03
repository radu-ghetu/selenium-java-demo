package com.web.dashboard.pages.components;

import com.web.dashboard.pages.components.abstractComp.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DateSelector extends AbstractComponent {
    public static final By DATE_SELECT = By.id("selected-date-label");

    public DateSelector() {
        super();
    }

    @Override
    public DateSelector waitForComponentToLoad() {
        waitForAllToExist(DATE_SELECT);
        return this;
    }
}