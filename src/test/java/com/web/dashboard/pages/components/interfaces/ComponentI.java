package com.web.dashboard.pages.components.interfaces;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import static com.web.util.Driver.getDriver;

public interface ComponentI {
    By HTML_BODY = By.tagName("body");
    By LOADING_ICON = By.id("loading-icon");


    /**
     * Given an ExpectedCondition, the default amount of time is waited for that condition to be true.
     *
     * @param test the condition to test
     */
    default <T extends Function<? super WebDriver, ?>> void waitFor(ExpectedCondition<?> test) {
        this.waitForSeconds(30, test);
    }

    default void waitForSeconds(long seconds, ExpectedCondition<?> test) {
        this.waitFor(Duration.of(seconds, ChronoUnit.SECONDS), test);
    }

    /**
     * Given an ExpectedCondition, wait the given amount of time for that condition to be true. This amount can be >=
     * the default wait time.
     *
     * @param duration The long value of the duration
     * @param test     the condition to test
     */
    default void waitFor(Duration duration, ExpectedCondition<?> test) {
        new FluentWait<>(getDriver()).withTimeout(duration).pollingEvery(Duration.ofMillis(50))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                .until(test);
    }
}
