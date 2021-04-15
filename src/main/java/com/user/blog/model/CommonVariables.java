package com.user.blog.model;

import java.io.*;
import java.util.*;
import org.eclipse.jetty.io.WriterOutputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.DocumentContext;


import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Component
@Scope("cucumber-glue")
public class CommonVariables {


    /////////////////////////////////////////
    // Request Details
    /////////////////////////////////////////

    protected RequestSpecification requestSpecification;
    protected Map<String, RequestSpecification> requestSpecificationMap = new HashMap<>();
    protected String requestName = "";
    protected RestBase.HttpMethods overrideHttpMethod = null;

    protected HashMap<String, Object> headersMap = new HashMap<>();
    protected HashMap<String, Object> parametersMap = new HashMap<>();
    protected DocumentContext documentContext;

    protected String overrideContentType = null;


    /////////////////////////////////////////
    // Response Details
    /////////////////////////////////////////

    public Response response;
    protected Map<String, Response> responseMap = new HashMap<>();
    public String endpointUrl = "";

    protected StringWriter requestWriter = new StringWriter();
    protected Map<String, StringWriter> requestWriterMap = new HashMap<>();
    protected StringWriter responseWriter = new StringWriter();
    protected Map<String, StringWriter> responseWriterMap = new HashMap<>();
    protected PrintStream responseCapture = new PrintStream(new WriterOutputStream(responseWriter));

    public Scenario scenario;

    protected PrintStream requestCapture = new PrintStream(new WriterOutputStream(requestWriter));

    protected Boolean invalidUrlFlag = false;

}
