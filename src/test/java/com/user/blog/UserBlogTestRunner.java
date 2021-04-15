package com.user.blog;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(

        plugin = {"pretty", "html:target/cucumber.html",
                "json:target/cucumber-report.json",
                "junit:target/cucumber.xml"},
        glue = {"com/user/blog/stepdefs"},
        features = "src/test/resources/features/",
        tags = "@regression",
        monochrome = true
)
public class UserBlogTestRunner {

}
