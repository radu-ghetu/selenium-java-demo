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

    @FindBy(id = "submit")
    private WebElement submit_btn;

    @FindBy(id = "input-email")
    public WebElement inputEmail;

    @FindBy(css="article div h1")
    public WebElement loginMessage;



    public void login(String user, String pass) {
        usernameField.clear();
        usernameField.sendKeys(user);
        passwordField.clear();
        passwordField.sendKeys(pass);
        submit_btn.click();
        LOG.info("User logged in using submit bottom");
    }

    public WebElement getSubmit_btn() {
        return submit_btn;
    }

    public boolean ifLoginPageLoaded() {
        boolean loaded = false;
        try {
            LoginURLCorrect();
            loaded = true;
            LOG.info("Page is loaded");
        } catch (Exception e) {
            LOG.debug("Some elements are not presented");
            e.printStackTrace();
        } finally {
            return loaded;
        }
    }

    public void LoginURLCorrect() {
        LOG.info("Checking auth0 URL login to correct env");

    }

    public void verifyLoginPageLoaded() throws TimeoutException {
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

    public void clickSubmit() {
        submit_btn.click();
    }


    public void verifyLoginSuccess() {
        assertTrue(loginMessage.getText().equals("Logged In Successfully"));
    }
}