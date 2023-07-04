@Prod @Int
Feature: DynamicTests

  Scenario: I, as a valid user should be able to remove a checkbox
    Given I'm on the dynamic page
    When I click the remove checkbox button
    And I verify the checkbox was removed


  Scenario: I, as a valid user should be able to disable an input
    Given I'm on the dynamic page
    When I click the disable button
    And I verify the input was disabled