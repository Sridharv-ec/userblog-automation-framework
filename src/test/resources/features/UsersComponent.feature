@regression @users
Feature: json place holder api bdds

# *************************************************************
#      USER BLOG - USERS API Scenarios
# *************************************************************

  @validateUsername @happypath
  Scenario: Get users request to validate user name in users api
    Given fetch user details for user "Delphine"
    And a 200 status code is returned
    Then validate username from the user api response

  @validateID
  Scenario: Get users request to validate id in users api
    Given fetch user details for user "Delphine"
    And a 200 status code is returned
    And get id from the user api response
    Then validate id is not null or blank in the user api response

  @postMethod
  Scenario: Get users api request with POST method returns 405 not found
    And "httpMethod" is amended to "POST"
    Given fetch user details for user "Delphine"
    And a 405 status code is returned

  @invalidHttpMethod
  Scenario Outline: Get users request with invalid method returns 405 not found
    And "httpMethod" is amended to "<method>"
    Given fetch user details for user "Delphine"
    And a 405 status code is returned
    Examples:
      | method |
      | PUT    |
      | PATCH  |
      | DELETE |



