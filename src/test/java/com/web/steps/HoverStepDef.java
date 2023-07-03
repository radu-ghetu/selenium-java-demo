package com.web.steps;

import com.web.dashboard.pages.HoverPage;
import com.web.util.ConfigurationReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.interactions.Actions;

import static com.web.util.Driver.getDriver;

public class HoverStepDef {
    HoverPage hoverPage = new HoverPage();

    @Given("I'm on the hover page")
    public void iMOnTheHoverPage() {
        getDriver().get(ConfigurationReader.get("url"));
        hoverPage.navigate();
        hoverPage.verifyPageLoaded();
    }

    @When("I hover over {string}")
    public void iHoverOver(String arg0) {

        switch (arg0) {
            case "figure1": {
                hoverPage.hoverToElement(1);
                break;
            }
            case "figure2": {
                hoverPage.hoverToElement(2);
                break;
            }
            case "figure3": {
                hoverPage.hoverToElement(3);
                break;
            }
        }
    }

    @And("I verify {string} is shown")
    public void iVerifyIsShown(String arg0) {
       switch(arg0) {
           case "user1": {
               hoverPage.verifyCorrectCaptionShown(1);
               break;
           }
           case "user2": {
               hoverPage.verifyCorrectCaptionShown(2);
               break;
           }
           case "user3": {
               hoverPage.verifyCorrectCaptionShown(3);
               break;
           }
       }
    }
}
