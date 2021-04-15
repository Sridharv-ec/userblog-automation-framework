package com.user.blog.stepdefs;

import com.user.blog.UserBlogFramework;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = UserBlogFramework.class)
public class CucumberSpringConfiguration {

}