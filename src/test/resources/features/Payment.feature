Feature: Retrieve Payment

  Scenario: Retrieve all payments
    Given there are payments in the system
    When I request to retrieve all payments
    Then I should receive a list of all payments