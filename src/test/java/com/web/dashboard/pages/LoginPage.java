package com.web.dashboard.pages;

import com.web.dashboard.pages.abstractPages.AbstractBredPage;
import com.web.util.BrowserUtils;
import com.web.util.ConfigurationReader;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.web.util.ConfigurationReader.get;
import static com.web.util.Driver.getDriver;
import static org.junit.Assert.assertTrue;

public class LoginPage extends AbstractBredPage {

    private static Logger LOG = LogManager.getLogger(LoginPage.class);

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElement submit_btn;

    @FindBy(id = "input-email")
    public WebElement inputEmail;

    @FindBy(id="flash")
    public WebElement loginMessage;

    @FindBy(css=".icon-signout")
    public WebElement logout_btn;

    public void login(String user, String pass) {
        usernameField.clear();
        usernameField.sendKeys(user);
        passwordField.clear();
        passwordField.sendKeys(pass);
        submit_btn.click();
        LOG.info("User logged in using submit bottom");
    }

    public void verifyPageLoaded() throws TimeoutException {
        waitForClickable(submit_btn);
        try {
            Assert.assertTrue(usernameField.isDisplayed());
            Assert.assertTrue(passwordField.isDisplayed());
            Assert.assertTrue(submit_btn.isDisplayed());
        } catch (RuntimeException e) {
            LOG.error("Login Page was not loaded ! Error message : ", e);
            throw new TimeoutException("Login Page was not displayed ! Error message : " + e.getMessage());
        }
    }

     public void verifyLoginSuccess() {
        waitForPageToLoad();
        assertTrue(loginMessage.getText().contains("You logged into a secure area!"));
    }

    public void clickLogout() {
        logout_btn.click();
    }

    public void verifyLoggedOut() {
        waitForPageToLoad();
        assertTrue(submit_btn.isDisplayed());
    }

    public void navigate() {
        getDriver().get(ConfigurationReader.get("url")+"/login");
    }
}