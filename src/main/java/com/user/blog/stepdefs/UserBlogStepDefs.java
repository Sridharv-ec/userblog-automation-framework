package com.user.blog.stepdefs;


import com.user.blog.commonhelpers.CommentsAPIHelper;
import com.user.blog.commonhelpers.CreateUserApiHelper;
import com.user.blog.commonhelpers.UserAPIHelper;
import com.user.blog.model.RestBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * Step definition class for USER BLOG  api contains the step definitions of the BDD test scenarios of the API
 * @author Sridhar
 */
public class UserBlogStepDefs {

    @Autowired
    private CommentsAPIHelper commentsAPIHelper;

    @Autowired
    private UserAPIHelper userAPIHelper;

    @Autowired
    private CreateUserApiHelper createUserHelper;

    public Scenario scenario;

    @Autowired
    public RestBase restBase;

    public String id = "1";
    public String emailAddress;
    public String username;


    @Before
    public void setup(Scenario scenarioName) {

        System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().getId());

        scenario = scenarioName;

        restBase.setScenario(scenario);

    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {

        }
    }

    @Given("^fetch user details for user \"([^\"]*)\"$")
    public void searchForTheUser(String userName)  {
        username = userName;
        userAPIHelper.sendUsersApiRequest(userName);
        userAPIHelper.embedScenarioEvidenceWithRequestResponse("");
    }


    @When("^a GET call is made to the comments api with the id$")
    public void aGETRequestIsMade()  {
        commentsAPIHelper.sendCommentsApiRequest(id);
        commentsAPIHelper.embedScenarioEvidenceWithRequestResponse("");
    }

    @Then("^a (.*) status code is returned$")
    public void verifyResponseReturnCode(String returnCode) {
        Assert.assertEquals("Expected status code: " + Integer.parseInt(returnCode) + " is not equal Actual Status code: " + this.restBase.getResponse().getStatusCode(), (long)Integer.parseInt(returnCode), (long)this.restBase.getResponse().getStatusCode());
        this.restBase.getResponse().statusCode();
        this.scenario.log(String.valueOf(this.restBase.getResponse().getStatusCode()));
    }


    @And("^get id from the user api response$")
    public void getIdFromUserApi() {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertTrue("Response body is empty.", StringUtils.isNotBlank(this.restBase.getResponse().body().asString()));
        id = restBase.getIdFromUserApiResponse();
    }

    @And("^get email address from the comments api response$")
    public void getEmailAddressFromCommentsApi() {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertTrue("Response body is empty.", StringUtils.isNotBlank(this.restBase.getResponse().body().asString()));
        emailAddress = restBase.getEmailAddressFromApiResponse();
    }

    @And("^validate email address format")
    public void validateEmailAddress() {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertTrue("email address format is incorrect for the email "+emailAddress+"", restBase.isValidEmailAddress(emailAddress));
    }

    @And("^validate username from the user api response")
    public void validateUserName() {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertTrue("Actual User Name "+restBase.getFieldValue("")+" is mismatching with the expected user name "+username+"", restBase.getFieldValue("username").equals(username));
    }

    @And("^validate id is not null or blank in the user api response")
    public void validateId() {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertNotNull("Id is "+restBase.getFieldValue("id")+ " or Blank in User api response ", id);
    }

    @When("^\"([^\"]*)\" is invalid$")
    public void isInvalid(String keyType) {
        RestBase.KeyType type = RestBase.KeyType.valueOf(keyType.toLowerCase());
        this.restBase.overrideHeaderOrBodyValue(type, RestBase.RequestFunctions.invalid, "", "", "", "");
    }

    @When("^\"([^\"]*)\" is amended to \"([^\"]*)\"$")
    public void isAmendedB(String keyType, String value) {
        RestBase.KeyType type = RestBase.KeyType.valueOf(keyType.toLowerCase());
        this.restBase.overrideHeaderOrBodyValue(type, RestBase.RequestFunctions.amended, "", value, "", "");
    }

    @And("^verify mandatory field (.*) in the api response")
    public void validateId(String field) {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertNotNull("Id is "+restBase.getFieldValue(field)+ " or Blank in User api response ", restBase.getFieldValue(field));
    }

    @And("^a POST request is made to create User with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void postRequest(String userid, String id) {
        createUserHelper.sendCreateUserApiRequest(userid,id);
        createUserHelper.embedScenarioEvidenceWithRequestResponse("");
    }

    @And("^validate actual response with the expected one$")
    public void compareActualResponseWithExpected() {
        this.scenario.log(this.restBase.getEvidences(""));
        TestCase.assertTrue(createUserHelper.validateResponse());
    }


    @When("^\"([^\"]*)\" \"([^\"]*)\" is added")
    public void isAdded(String keyType, String key) {
        RestBase.KeyType type = RestBase.KeyType.valueOf(keyType.toLowerCase());
        this.restBase.overrideHeaderOrBodyValue(type, RestBase.RequestFunctions.added, key, "", "", "");
    }

    @When("^\"([^\"]*)\" \"([^\"]*)\" is removed$")
    public void isRemoved(String keyType, String key) {
        RestBase.KeyType type = RestBase.KeyType.valueOf(keyType.toLowerCase());
        this.restBase.overrideHeaderOrBodyValue(type, RestBase.RequestFunctions.removed, key, "", "", "");
    }

    @Given("^set default template for POST request$")
    public void setDefaultRequestBodyInitiate() {
        createUserHelper.setDefaultRequestTemplate("11", "101");
    }

}