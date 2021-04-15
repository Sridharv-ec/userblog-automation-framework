package com.user.blog.commonhelpers;

import com.user.blog.model.RestBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * This is a Users Api Helper Class which contains step definitions implementation of the BDD Test Scenarios of the API.
 *
 * @author Sridhar Vuttarkar
 */
@Component
@Scope("cucumber-glue")
public class UserAPIHelper extends RestBase {

    @Value("${mobiquity.JSON_PLACE_HOLDER_HOST}")
    private String jsonPlaceHolderHostName;


    private static final String JSON_PLACE_HOLDER_COMMENTS_USERS = "/users";


    /**
     * Returns the url of the to use for the users-api
     *
     * @return endpointUrl String
     */
    private String getEndpoint(String userName) {
        return jsonPlaceHolderHostName + JSON_PLACE_HOLDER_COMMENTS_USERS + "?username=" + userName;
    }


    /**
     * sendUsersApiRequest() restassured get call to fetch users
     */
    public void sendUsersApiRequest(String userName) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("username", userName);
        get(getEndpoint(userName), "jsonPlaceHolder", false, false);
    }

}
