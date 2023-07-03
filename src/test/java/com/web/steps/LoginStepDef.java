package com.web.steps;

import com.web.dashboard.pages.LoginPage;
import com.web.util.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import static com.web.util.Driver.getDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginStepDef {
    LoginPage loginPage = new LoginPage();
    String username=ConfigurationReader.get("username");
    String password=ConfigurationReader.get("password");

    private static Logger LOG = LogManager.getLogger(LoginStepDef.class);

    @io.cucumber.java.en.Given("I'm on the login page")
    public void iMOnTheLoginPage() {
        getDriver().get(ConfigurationReader.get("url"));
        loginPage.verifyLoginPageLoaded();
    }

    @io.cucumber.java.en.When("I login using valid credentials")
    public void iLoginUsingValidCredentials() {
        loginPage.login(username, password);
    }

    @io.cucumber.java.en.And("I verify I am on landing page")
    public void iVerifyIAmOnLandingPage() {
        loginPage.verifyLoginSuccess();
    }

    @And("I click the logout button")
    public void iClickTheLogoutButton() {
        loginPage.clickLogout();
    }

    @Then("I should be logged out")
    public void iShouldBeLoggedOut() {
        loginPage.verifyLoggedOut();
    }
}
