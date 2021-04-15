@regression @createUser
Feature: json place holder create user api bdds

# *************************************************************
#      USER BLOG - CREATE USER API Scenarios
# *************************************************************


  @happypath
  Scenario Outline: POST Request to create User with new userid and id and validate the response
    Given a POST request is made to create User with "<userId>" and "<id>"
    Then a 201 status code is returned
    Then validate actual response with the expected one
    Examples:
      | userId | id  |
      | 11     | 101 |


  @removeFields
  Scenario: POST Request to create user with missing mandatory body fields
    Given set default template for POST request
    And "Body" "id" is removed
    And "Body" "userId" is removed
    And "Body" "title" is removed
    And "Body" "body" is removed
    Given a POST request is made to create User with "" and ""
    Then a 201 status code is returned
    Then validate actual response with the expected one

  @addFields
  Scenario: POST Request to create user with new optional body fields added
    Given set default template for POST request
    And "Body" "email" is added
    And "Body" "phone" is added
    And "Body" "website" is added
    Given a POST request is made to create User with "11" and "101"
    Then a 201 status code is returned
    Then validate actual response with the expected one
