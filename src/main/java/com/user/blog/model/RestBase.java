package com.user.blog.model;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import io.restassured.config.*;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONArray;
import org.eclipse.jetty.io.WriterOutputStream;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static junit.framework.TestCase.assertTrue;

@Component
@Scope("cucumber-glue")
public class RestBase extends CommonBase {

    public RestBase() {

    }

    public static enum HttpMethods {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE;

        private HttpMethods() {
        }
    }


    protected void get(String url, String nameOfRequest, boolean headers, boolean parameters, boolean cookies, boolean followRedirects) {
        this.setRequestName(nameOfRequest);
        this.get(url, headers, parameters, cookies, followRedirects);
    }

    protected void get(String url, boolean headers, boolean parameters, boolean cookies, boolean followRedirects) {
        if (getRequestName().equals("")) {
            setRequestName(url);
        }

        setTargetUrl(url);
        setupNewCapture();
        setRequestSpecification(given().filter(new RequestLoggingFilter(getRequestCapture())).filter(new ResponseLoggingFilter(getResponseCapture())).redirects().follow(followRedirects));
        RestAssuredConfig myConfig = setRequestConfig(url);
        setRequestSpecification(getRequestSpecification().config(myConfig));

        if (headers) {
            setRequestSpecification(getRequestSpecification().headers(getHeadersFromMap()));
        }

        if (parameters) {
            setRequestSpecification(getRequestSpecification().queryParams(getParameters()));
        }

        if (this.getInvalidUrlFlag()) {
            URL urlModified = null;

            try {
                urlModified = new URL(url);
            } catch (MalformedURLException var9) {
                var9.printStackTrace();
            }

            if (url.contains(":")) {
                url = urlModified.getProtocol() + "://" + urlModified.getHost() + ":" + urlModified.getPort() + "/Invalid" + urlModified.getPath();
            } else {
                url = urlModified.getProtocol() + "://" + urlModified.getHost() + "/Invalid" + urlModified.getPath();
            }

            LOG.info("Modified URL: " + url);
            this.writeScenarioEvidence("Modified URL: " + url);
        }

        if (this.getOverrideContentType() != null) {
            this.setRequestSpecification(this.getRequestSpecification().contentType(this.getOverrideContentType()));
        }

        if (this.getOverrideHttpMethod() == null) {
            this.get(url, this.getRequestName());
        } else if (this.getOverrideHttpMethod() == HttpMethods.POST) {
            this.setRequestSpecification(this.getRequestSpecification().body("{\"Channel\": \"RC\"}"));
            this.post(url, this.getRequestName());
        } else if (this.getOverrideHttpMethod() == HttpMethods.PUT) {
            this.setRequestSpecification(this.getRequestSpecification().body("{\"Channel\": \"RC\"}"));
            this.put(url, this.getRequestName());
        } else if (this.getOverrideHttpMethod() == HttpMethods.PATCH) {
            this.setRequestSpecification(this.getRequestSpecification().body("{\"Channel\": \"RC\"}"));
            this.patch(url, this.getRequestName());
        } else if (this.getOverrideHttpMethod() == HttpMethods.DELETE) {
            this.setRequestSpecification(this.getRequestSpecification().body("{\"Channel\": \"RC\"}"));
            this.delete(url, this.getRequestName());
        }

    }

    private void setupNewCapture() {
        this.variables.requestWriter = new StringWriter();
        this.variables.requestCapture = new PrintStream(new WriterOutputStream(this.variables.requestWriter));
        this.variables.responseWriter = new StringWriter();
        this.variables.responseCapture = new PrintStream(new WriterOutputStream(this.variables.responseWriter));
    }

    protected void setRequestSpecification(RequestSpecification spec) {
        this.variables.requestSpecification = spec;
    }

    private RestAssuredConfig setRequestConfig(String url) {

        HttpClientConfig clientConfig = httpClientConfig()
                .setParam("http.connection.timeout", 60000)
                .setParam("http.socket.timeout", 60000)
                .setParam("http.maxConnections", 100);
        config = config()
                .httpClient(clientConfig)
                .sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());

        RestAssuredConfig restAssuredConfig = config();

        restAssuredConfig = restAssuredConfig.sslConfig(new SSLConfig().relaxedHTTPSValidation());

        return restAssuredConfig;
    }

    public void writeScenarioEvidence(String evidence) {
        try {
            this.getScenario().log(evidence);
        } catch (NullPointerException var3) {
            LOG.error(var3.getMessage());
            var3.printStackTrace();
            LOG.error("Unable to write scenario evidence");
        }

    }

    protected RequestSpecification getRequestSpecification() {
        return this.variables.requestSpecification;
    }

    protected PrintStream getRequestCapture() {
        return this.variables.requestCapture;
    }


    protected void setRequestName(String requestName) {
        this.variables.requestName = requestName;
    }

    protected String getRequestName() {
        return this.variables.requestName;
    }

    protected void get(String url, String nameOfRequest) {
        this.setRequestName(nameOfRequest);
        this.setTargetUrl(url);

        try {
            this.setResponse((Response) this.getRequestSpecification().when().get(url, new Object[0]));
        } catch (Exception var4) {
            LOG.error("Unable to execute post method for url={}", url);
            throw var4;
        }

        this.captureRequestResponse();
    }

    protected void setResponse(Response res) {
        this.variables.response = res;
    }

    private void captureRequestResponse() {
        this.variables.responseMap.put(this.getRequestName(), this.getResponse());
        this.variables.requestSpecificationMap.put(this.getRequestName(), this.getRequestSpecification());
        this.variables.requestWriterMap.put(this.getRequestName(), this.variables.requestWriter);
        this.variables.responseWriterMap.put(this.getRequestName(), this.variables.responseWriter);
        this.setRequestName("");
    }

    protected PrintStream getResponseCapture() {
        return this.variables.responseCapture;
    }

    public Response getResponse() {
        return this.variables.response;
    }

    public String getEvidences(String evidence) {
        return evidence + "\n\n REQUEST: \n\n" + this.variables.requestWriter.toString() + "\n\n RESPONSE: \n\n" + this.variables.responseWriter.toString() + "\n\n RESPONSE TIME: \n\n" + this.getResponse().getTimeIn(TimeUnit.MILLISECONDS) + " ms";
    }

    public void embedScenarioEvidenceWithRequestResponse(String evidence) {
        this.writeScenarioEvidence(this.getEvidences(evidence));
    }

    protected String getOverrideContentType() {
        return this.variables.overrideContentType;
    }

    protected void setOverrideContentType(String type) {
        this.variables.overrideContentType = type;
    }

    protected HashMap<String, Object> getParameters() {
        return variables.parametersMap;
    }

    protected void setParameters(HashMap<String, Object> parameters) {
        variables.parametersMap = parameters;
    }

    protected Headers getHeadersFromMap() {
        List<Header> list = new ArrayList<Header>();
        for (String key : getHeaders().keySet()) {
            String value;
            try {
                value = getHeaders().get(key).toString();
                if (value == null || value.equalsIgnoreCase("null")) {
                    list.add(new Header(key, null));
                } else {
                    list.add(new Header(key, getHeaders().get(key).toString()));
                }
            } catch (Exception e) {
                list.add(new Header(key, null));
            }
        }
        return new Headers(list);
    }

    protected HashMap<String, Object> getHeaders() {
        return variables.headersMap;
    }

    protected void setHeaders(HashMap<String, Object> headers) {
        variables.headersMap.clear();
        variables.headersMap = headers;
    }

    protected HttpMethods getOverrideHttpMethod() {
        return this.variables.overrideHttpMethod;
    }

    public String getJsonPathValue(String key) {
        this.writeScenarioEvidence(key + ": " + ((ValidatableResponse) this.getResponse().then()).extract().jsonPath().getString(key));
        return ((ValidatableResponse) this.getResponse().then()).extract().jsonPath().getString(key);
    }

    public String getIdFromUserApiResponse() {
        return getJsonPathValue("id[0]");
    }

    public String getEmailAddressFromApiResponse() {
        return getJsonPathValue("email[0]");
    }

    public static boolean isValidEmailAddress(String email) {
        boolean stricterFilter = true;
        String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String emailRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public String getFieldValue(String fieldName) {
        return getJsonPathValue(fieldName + "[0]");
    }

    protected boolean compareResposeWithExpectedResponseBody(ValidatableResponse validatableResponse, String expectedResponseBody) {

        String actualResponse = validatableResponse.extract().asString();

        try {

            assertTrue(actualResponse.replaceAll("[\\t\\n\\r\\s+]","").equalsIgnoreCase(expectedResponseBody.replaceAll("[\\t\\n\\r\\s+]","")));
            return true;
        } catch (AssertionError e) {

            LOG.error("Actual Response actualResponse={} doesn't match with expectedResponseBody={}", actualResponse.replaceAll("[\\t\\n\\r\\s+]",""), expectedResponseBody.replaceAll("[\\t\\n\\r\\s+]",""));

            return false;
        }
    }

    public static enum KeyType {
        httpmethod,
        requestpath,
        contenttype,
        body,;

        private KeyType() {
        }
    }

    public static enum RequestFunctions {
        amended,
        invalid,
        added,
        removed,;

        private RequestFunctions() {
        }
    }

    public void overrideHeaderOrBodyValue(KeyType keyType, RequestFunctions function, final String key, final String optionalValue, String optionalJsonPath, String dataType) {
        if (optionalJsonPath.equals("")) {
            optionalJsonPath = "$";
        }

        switch (keyType) {
            case contenttype:
                switch (function) {
                    case amended:
                        this.setOverrideContentType(optionalValue);
                        return;
                    default:
                        return;
                }
            case requestpath:
                switch (function) {
                    case invalid:
                        this.setInvalidUrlFlag(true);
                        return;
                    default:
                        return;
                }
            case httpmethod:
                switch (function) {
                    case amended:
                        this.setOverrideHttpMethod(HttpMethods.valueOf(optionalValue.toUpperCase()));
                        return;
                    default:
                        return;
                }

            case body:
                switch(function) {
                    case amended:
                        if (dataType.toLowerCase().contains("int")) {
                            this.setJsonPathValue(optionalJsonPath + "." + key, Integer.parseInt(optionalValue));
                            return;
                        } else {
                            if (dataType.toLowerCase().contains("double")) {
                                this.setJsonPathValue(optionalJsonPath + "." + key, Double.parseDouble(optionalValue));
                            } else {
                                this.setJsonPathValue(optionalJsonPath + "." + key, optionalValue);
                            }

                            return;
                        }
                    case added:
                        this.addItemAtJsonPath(optionalJsonPath, key, optionalValue);
                        return;
                    case removed:
                        if (key != null && !key.isEmpty()) {
                            this.removeJsonPath(optionalJsonPath + "." + key);
                        } else {
                            this.removeJsonPath(optionalJsonPath + ".*");
                        }

                        return;
                }


        }


    }

    public void setInvalidUrlFlag(Boolean inValidUrlFlag) {
        this.variables.invalidUrlFlag = inValidUrlFlag;
    }

    public Boolean getInvalidUrlFlag() {
        return this.variables.invalidUrlFlag;
    }

    protected void setOverrideHttpMethod(HttpMethods method) {
        this.variables.overrideHttpMethod = method;
    }

    protected void post(String url, String nameOfRequest) {
        this.setRequestName(nameOfRequest);
        this.setTargetUrl(url);

        try {
            this.setResponse((Response) this.getRequestSpecification().when().post(url, new Object[0]));
        } catch (Exception var4) {
            LOG.error(var4.getMessage());
            var4.printStackTrace();
            LOG.error("Unable to execute post method for url={}", url);
            this.writeScenarioEvidence(this.variables.requestWriter.toString());
            this.writeScenarioEvidence(this.variables.responseWriter.toString());
            this.captureRequestResponse();
        }

        this.captureRequestResponse();
    }

    protected void put(String url, String nameOfRequest) {
        this.setRequestName(nameOfRequest);
        this.setTargetUrl(url);

        try {
            this.setResponse((Response) this.getRequestSpecification().when().put(url, new Object[0]));
        } catch (Exception var4) {
            LOG.error("Unable to execute put method for url={}", url);
            throw var4;
        }

        this.captureRequestResponse();
    }

    protected void patch(String url, String nameOfRequest) {
        this.setRequestName(nameOfRequest);
        this.setTargetUrl(url);

        try {
            this.setResponse((Response) this.getRequestSpecification().when().patch(url, new Object[0]));
        } catch (Exception var4) {
            LOG.error("Unable to execute patch method for url={}", url);
            throw var4;
        }

        this.captureRequestResponse();
    }

    protected void delete(String url, String nameOfRequest) {
        this.setRequestName(nameOfRequest);
        this.setTargetUrl(url);

        try {
            this.setResponse((Response) this.getRequestSpecification().when().delete(url, new Object[0]));
        } catch (Exception var4) {
            LOG.error("Unable to execute post delete for url={}", url);
            throw var4;
        }

        this.captureRequestResponse();
    }

    protected void post(String url, String nameOfRequest, boolean body, boolean headers, boolean parameters, boolean cookies, boolean followRedirects, boolean formParameters) {
        setRequestName(nameOfRequest);
        post(url, body, headers, parameters, cookies, followRedirects, formParameters);
    }

    protected void post(String url, boolean body, boolean headers, boolean parameters, boolean cookies, boolean followRedirects, boolean formParameters) {

        if (getRequestName().equals("")) {
            setRequestName(url);
        }

        setTargetUrl(url);
        setupNewCapture();
        setRequestSpecification(given());
        setRequestSpecification(getRequestSpecification().filter(new RequestLoggingFilter(getRequestCapture())).filter(new ResponseLoggingFilter(getResponseCapture())));
        setRequestSpecification(getRequestSpecification().redirects().follow(followRedirects));
        RestAssuredConfig myConfig = setRequestConfig(url);
        setRequestSpecification(getRequestSpecification().config(myConfig));

        if (body) {
            setRequestSpecification(getRequestSpecification().body(getDocumentContext().jsonString()));
        }

        if (headers) {
            setRequestSpecification(getRequestSpecification().headers(getHeadersFromMap()));
        }

        if (parameters) {
            setRequestSpecification(getRequestSpecification().queryParams(getParameters()));
        }

        if (getInvalidUrlFlag()) {

            URL urlModified = null;

            try {
                urlModified = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (url.contains(":")) {
                url = urlModified.getProtocol() + "://" + urlModified.getHost() + ":" + urlModified.getPort() + "/Invalid" + urlModified.getPath();
            } else {
                url = urlModified.getProtocol() + "://" + urlModified.getHost() + "/Invalid" + urlModified.getPath();
            }
            LOG.info("Modified URL: " + url);
            writeScenarioEvidence(("Modified URL: " + url));
        }

        if (getOverrideContentType() != null) {
            setRequestSpecification(getRequestSpecification().contentType(getOverrideContentType()));
        }

        if (getOverrideHttpMethod() == null) {
            post(url, getRequestName());
        } else if (getOverrideHttpMethod() == HttpMethods.GET) {
            setRequestSpecification(getRequestSpecification().body(""));
            get(url, getRequestName());
        } else if (getOverrideHttpMethod() == HttpMethods.PUT) {
            put(url, getRequestName());
        } else if (getOverrideHttpMethod() == HttpMethods.PATCH) {
            patch(url, getRequestName());
        } else if (getOverrideHttpMethod() == HttpMethods.DELETE) {
            delete(url, getRequestName());
        }
    }

    protected void setTemplate(String templatePath) {
        try {
            this.setDocumentContext(JsonPath.parse(this.getClass().getResource(templatePath)));
        } catch (Exception var3) {
            LOG.error("Error reading json {}", var3);
        }

    }

    protected void setDocumentContext(DocumentContext context) {
        this.variables.documentContext = context;
    }

    public DocumentContext getDocumentContext() {
        return this.variables.documentContext;
    }

    protected void setJsonPathValue(String path, Object value) {
        this.getDocumentContext().set(path, value, new Predicate[0]);
    }

    public ValidatableResponse getValidatableResponse() {
        return getResponse().then();
    }

    protected void addItemAtJsonPath(String path, String key, Object value) {
        this.getDocumentContext().put(path, key, value, new Predicate[0]);
    }

    protected void removeJsonPath(String path) {
        this.getDocumentContext().delete(path, new Predicate[0]);
    }


}
