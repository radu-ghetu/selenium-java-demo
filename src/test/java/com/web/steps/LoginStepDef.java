package com.web.steps;

import com.web.dashboard.pages.LoginPage;
import com.web.util.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.web.util.Driver.getDriver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginStepDef {
    LoginPage loginPage = new LoginPage();
    String username = ConfigurationReader.get("username");
    String password = ConfigurationReader.get("password");
    String inv_username = ConfigurationReader.get("inv_username");
    String inv_password = ConfigurationReader.get("inv_password");


    private static Logger LOG = LogManager.getLogger(LoginStepDef.class);

    @io.cucumber.java.en.Given("I'm on the login page")
    public void iMOnTheLoginPage() {
        getDriver().get(ConfigurationReader.get("url"));
        loginPage.navigate();
        loginPage.verifyPageLoaded();
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

    @When("I login using invalid username")
    public void iLoginUsingInvalidUsername() {
        loginPage.login(inv_username, password);
    }

    @When("I login using invalid password")
    public void iLoginUsingInvalidPassword() {
        loginPage.login(username, inv_password);
    }

    @And("I verify error message contains {string}")
    public void iVerifyErrorMessageContains(String arg0) {
        loginPage.verifyLoginError(arg0);
    }
}
