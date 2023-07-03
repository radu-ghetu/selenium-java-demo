@Prod
Feature: LoginPass

  Scenario: I, as a valid user should be able to login with valid credentials
    Given I'm on the login page
    When I login using valid credentials
    And I verify I am on landing page


  Scenario: I, as a valid user should be able to logout
    Given I'm on the login page
    When I login using valid credentials
    And I verify I am on landing page
    And I click the logout button
    Then I should be logged out