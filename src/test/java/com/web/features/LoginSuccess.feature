@Prod
Feature: LoginPass

  Scenario: I, as a valid user should be able to login with valid credentials
    Given I'm on the login page
    When I login using valid credentials
    And I verify I am on landing page