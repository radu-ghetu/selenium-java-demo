@login
Feature: LoginPass

  @Prod @Int
  Scenario: I, as a valid user should be able to login with valid credentials
    Given I'm on the login page
    When I login using valid credentials
    And I verify I am on landing page

  @Prod @Int
  Scenario: I, as a valid user should be able to logout
    Given I'm on the login page
    When I login using valid credentials
    And I verify I am on landing page
    And I click the logout button
    Then I should be logged out

  @Int
  Scenario: I, as a invalid user should not be able to login with invalid username
    Given I'm on the login page
    When I login using invalid username
    And I verify error message contains "username is invalid"


  @Int
  Scenario: I, as a invalid user should not be able to login with invalid password
    Given I'm on the login page
    When I login using invalid password
    And I verify error message contains "password is invalid"