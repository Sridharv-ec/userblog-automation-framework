@regression @comments
Feature: json place holder comments api bdds

# *************************************************************
#       USER BLOG - COMMENTS API Scenarios
# *************************************************************

  @happyPath
  Scenario: Get comments api request which gives success response
    When a GET call is made to the comments api with the id
    And a 200 status code is returned

  @validateMandatoryFields
  Scenario: Get comments api request to verify mandatory fields in the comments response
    When a GET call is made to the comments api with the id
    And a 200 status code is returned
    Then verify mandatory field id in the api response
    Then verify mandatory field name in the api response
    Then verify mandatory field email in the api response


  @invalidEndPoint
  Scenario: Get comments api request with invalidPath returns 404 not found
    And "requestPath" is invalid
    When a GET call is made to the comments api with the id
    And a 404 status code is returned

  @invalidHttpMethod
  Scenario Outline: Get comments api request with invalid method returns 405 not found
    And "httpMethod" is amended to "<method>"
    When a GET call is made to the comments api with the id
    And a 405 status code is returned
    Examples:
      | method |
      | PUT    |
      | PATCH  |
      | DELETE |

  @postMethod
  Scenario: Get comments api request with POST method returns 405 not found
    And "httpMethod" is amended to "POST"
    When a GET call is made to the comments api with the id
    And a 405 status code is returned

