@Prod
Feature: HoverTest

  Background:
    Given I'm on the hover page

  Scenario Outline: I, as a valid user should be able to hover over elements
    When I hover over "<element>"
    And I verify "<object>" is shown
    Examples:
      | element | object|
      | figure1 | user1 |
      | figure2 | user2|
      | figure3 | user3|