# userblog-automation-framework


Start Prequisites
--------
   Git 
   JDK 8 or later 
   Maven 3.0 or later 
   

Clone Git Repository
--------

https://github.com/Sridharv-ec/userblog-automation-framework.git

Run tests locally
--------
   Running tests locally You can run the tests from the command line using:

   mvn test -Dmaven.test.failure.ignore=true -Dcucumber.options="src/test/resources/functionalTests" -Dcucumber.options="-tags @regression"

Framework structure
--------
   •	This framework uses BDD written in Java using cucumber. Cucumber will provide feature to write BDD in gherkin language which business can understand

   •	This framework can be used to run component, end to end and can be be extended with browser testing tools like selenium and appium

   •	This framework is build on spring boot


Framework structure

•	"main/java/com/user/blog/stepdefs" - contains all the step definitions

•	"main/java/com/user/blog/commonhelpers" - this package is used to added api helper java files

•	"main/java/com/user/blog/model" > CommonBase.java" - CommomBase is a super class of all class in the framework. 
   (We can added reusables required for API, Browser etc)
   
•	"main/java/com/user/blog/model" > CommonConstants.java - This is used to add constant values that can be reused in the framework

•	"main/java/com/user/blog/model" > CommonVariables.java - This is used to create instance and normal variables

•	"main/java/com/user/blog/model" > RestBase.java - RestBase is mainly used to add reusables which are required to api automation

•	"main/java/com/user/blog/stepdefs" > CucumberSpringConfiguration.java - This file is used to invoke the cucumber spring boot

•	main/resources/template - This directory is used to create the api request and response body templates

•	main/resources > application.properties - This files is used to provide defaul environment as active spring profile

•	main/resources > application-master.yml - This files used by spring boot to retrieve the data and urls specific to environment. This can act as config file

•	main/resources > logfj.properies - This is used to enable loggin during runtime

•	test/java/resources/com/user/blog > UserBlogTestRunner -- This is the cucumber test runner fields used to run the BDDs with specific tags for ex: regression, happypath etc

•	"test/java/resources/features" - contains feature files written in Gherkin

•	pom.xml - contains information about the project and configuration details used by Maven to build the project.

•	"assertJ" is used for assertion

•	"cucumber-html-reporter" to generate html report from cucumber json report


