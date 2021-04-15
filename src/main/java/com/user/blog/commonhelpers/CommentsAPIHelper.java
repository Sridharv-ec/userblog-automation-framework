package com.user.blog.commonhelpers;

import com.user.blog.model.RestBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * This is a comments Api Helper Class which contains step definitions implementation of the BDD Test Scenarios of the API.
 *
 * @author Sridhar Vuttarkar
 */
@Component
@Scope("cucumber-glue")
public class CommentsAPIHelper extends RestBase {

    ///////////////////////////////////////////////////////
    // Set JSON_PLACE_HOLDER_HOST REPLAY API endpoints
    ///////////////////////////////////////////////////////

    @Value("${mobiquity.JSON_PLACE_HOLDER_HOST}")
    private String jsonPlaceHolderHostName;

    Boolean defaultTemplateSet = false;

    private static final String JSON_PLACE_HOLDER_COMMENTS_ENDPOINT = "/comments";

    /**
     * Returns the url of the to use for the comments-api
     *
     * @return endpointUrl String
     */
    private String getEndpoint(String id) {
        return jsonPlaceHolderHostName + JSON_PLACE_HOLDER_COMMENTS_ENDPOINT;
    }

    /**
     * sendCommentsApiRequest() restassured get call to fetch comments
     */
    public void sendCommentsApiRequest(String id ) {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        setParameters(parameters);

        get(getEndpoint(id), "jsonPlaceHolder", false, true);
        defaultTemplateSet = false;
    }

}