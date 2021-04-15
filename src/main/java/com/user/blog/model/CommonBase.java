package com.user.blog.model;

import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;


@Component
@Scope("cucumber-glue")
public class CommonBase {

    public CommonBase() {

    }

    @Autowired
    public CommonVariables variables = new CommonVariables();

    public static final Logger LOG = LoggerFactory.getLogger(CommonBase.class);

    protected void setTargetUrl(String url) {
        if (this.variables.endpointUrl.length() == 0) {
            this.variables.endpointUrl = url;

            try {
                URL test = new URL(this.variables.endpointUrl);
                System.setProperty("TARGETURL", "https://" + test.getHost());
            } catch (MalformedURLException var3) {
                LOG.warn(var3.getMessage());
            }
        }
    }

    public void setScenario(Scenario scen) {
        this.variables.scenario = scen;
    }

    public Scenario getScenario() {
        return this.variables.scenario;
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

}
