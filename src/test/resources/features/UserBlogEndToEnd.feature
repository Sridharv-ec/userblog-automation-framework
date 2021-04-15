@regression @e2e
Feature: json place holder E2E bdds

# *************************************************************
  #      USER BLOG - E2E Scenarios
# *************************************************************

  @validateEmail @happypath
  Scenario: E2E test - validate users email address format
    Given fetch user details for user "Delphine"
    And a 200 status code is returned
    And get id from the user api response
    When a GET call is made to the comments api with the id
    And a 200 status code is returned
    And get email address from the comments api response
    Then validate email address format

  @multipleUsers
  Scenario Outline: E2E test - validate users email address format
    Given fetch user details for user "<userName>"
    And a 200 status code is returned
    And get id from the user api response
    When a GET call is made to the comments api with the id
    And a 200 status code is returned
    And get email address from the comments api response
    Then validate email address format
    Examples:
      | userName  |
      | Bret      |
      | Antonette |
      | Samantha  |
      | Karianne  |


