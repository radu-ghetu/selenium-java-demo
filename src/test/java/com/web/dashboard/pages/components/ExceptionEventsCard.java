package com.web.dashboard.pages.components;

import com.web.dashboard.pages.components.abstractComp.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ExceptionEventsCard extends AbstractComponent {
    private EventType eventType;
    private By card;
    private By counter = By.cssSelector("h4");

    public enum EventType{
        CRITICAL,
        WARNING,
        INFORMATION;

        private final String cardSelectorXpathTemplate =
                "//h6[contains(., \"@eventType\")]/ancestor::*[contains(@class, \"MuiCard-root\")]";
        private final String filterInputIdSelectorTemplate =
                "select-multiple-@eventType";

        private String capitalizeOnlyFirstLetter() {
            String eventType = this.toString();
            return eventType.substring(0, 1) + eventType.substring(1).toLowerCase();
        }

        public By getCardSelector(){
            return By.xpath(cardSelectorXpathTemplate.replace(
                    "@eventType",
                    this.capitalizeOnlyFirstLetter()));
        }
    }

    public ExceptionEventsCard( EventType eventType) {
        super();
        this.eventType = eventType;
        card = eventType.getCardSelector();
    }

    @Override
    public ExceptionEventsCard waitForComponentToLoad() {
        waitForAllToExist(card);
        return this;
    }

    public int getCardCounter(){
        return Integer.parseInt(getText(findElement(card), counter));
    }

}