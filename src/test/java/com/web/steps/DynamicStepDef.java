package com.web.steps;

import com.web.dashboard.pages.DynamicPage;
import com.web.util.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import static com.web.util.Driver.getDriver;

public class DynamicStepDef {
    DynamicPage dynamicPage = new DynamicPage();

    @Given("I'm on the dynamic page")
    public void iMOnTheDynamicPage() {
        getDriver().get(ConfigurationReader.get("url"));
        dynamicPage.navigate();
        dynamicPage.verifyPageLoaded();
    }

    @When("I click the remove checkbox button")
    public void iClickTheRemoveCheckboxButton() {
        dynamicPage.clickRemoveCheckboxBtn();
    }

    @And("I verify the checkbox was removed")
    public void iVerifyTheCheckboxWasRemoved() {
        dynamicPage.verifyCheckboxRemoved();
    }

    @When("I click the disable button")
    public void iClickTheDisableButton() {
        dynamicPage.clickDisableInputBtn();
    }

    @And("I verify the input was disabled")
    public void iVerifyTheInputWasDisabled() {
        dynamicPage.verifyInputDisabled();
    }
}
