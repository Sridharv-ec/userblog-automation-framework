package com.user.blog.commonhelpers;

import com.user.blog.model.CommonConstants;
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
public class CreateUserApiHelper extends RestBase {

    ///////////////////////////////////////////////////////
    // Set JSON_PLACE_HOLDER_HOST REPLAY API endpoints
    ///////////////////////////////////////////////////////

    @Value("${mobiquity.JSON_PLACE_HOLDER_HOST}")
    private String jsonPlaceHolderHostName;

    private static final String JSON_PLACE_HOLDER_COMMENTS_ENDPOINT = "/posts";

    private String createUserTemplatePath = CommonConstants.TEMPLATE_PATH + "createUser/";
    private String requestBodyTemplatePath = createUserTemplatePath + "requestbody/";
    private String createUserBody = requestBodyTemplatePath + "createUser.json";
    private boolean defaultTemplateSet = false;

    /**
     * Returns the url of the to use for the comments-api
     *
     * @return endpointUrl String
     */
    private String getEndpoint() {
        return jsonPlaceHolderHostName + JSON_PLACE_HOLDER_COMMENTS_ENDPOINT;
    }

    /**
     * sendCommentsApiRequest() restassured get call to fetch comments
     */
    public void sendCreateUserApiRequest(String userId, String id) {
        if(!defaultTemplateSet) {
            setDefaultRequestTemplate(userId, id);
        }
        post(getEndpoint(), true, true, false, false, false, false);

    }


    /**
     * set the default headers and enable the swagger validation for post authmech api
     */
    public void setDefaultRequestTemplate(String userId, String id) {
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        setHeaders(headers);
        setTemplate(createUserBody);
        setJsonPathValue("userId", userId);
        setJsonPathValue("id", Integer.parseInt(id));
        defaultTemplateSet = true;
    }

    public boolean validateResponse(){
        try {
            return compareResposeWithExpectedResponseBody(getValidatableResponse()
                    , getDocumentContext().jsonString());
        }catch(Exception ex){
            return false;
        }
    }

}