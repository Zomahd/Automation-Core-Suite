# Automation-Core-Suite
Automation Core Suite

Automation Core Suite is a Selenium-based functional testing platform aimed to reduce time to market, simplify test code, provide a reporting framework and integrate with existing continuous integration environments.

Automation Core Suite allows developers and testers to write test cases in Java (no need to learn any new language) using Selenide and TestNG. Selenide reduces the complexity of the tests providing a jQuery-like syntax for DOM queries and manipulation. TestNG provides a reporting framework which has been integrated with Excel and Extent Reports. Out of the box, Automation Core Suite provides an excellent set of reports.

## Testing Features
 - Functional Testing with Selenium
 - jQuery syntax with Selenide
 - Advanced log and screenshots management (generates an event timeline across the test execution)
 - Support for local browsers using ChromeDriver and GeckoDriver
 - Support for remote Selenium Grids (proprietary, BrowserStack and SauceLabs)
 - Advanced support for SauceLabs, manages test cases names, status, tags and builds
 - Support for cross-browser / multi-platform testing
 - Support for custom data providers to repeat test cases based on data iteration

## Reporting Features
 - Extent Reports
 - Excel Reporting with raw data
 - Integrated with SauceLabs test case execution tags and builds
 - Extended reporting framework planned for release 2.0, will allow developers to create custom tests

## Platform Features
 - 100% written in Java
 - Written in JDK 1.8
 - Completely written using open-source frameworks
 - Execution managed by Maven
 - Test cases written in TestNG
 - Advanced support for parallel testing (complete support for TestNG concurrent tests)
 - Runs on any continuous integration environment with Maven support, including Jenkins and Bamboo
 - Tests can be debugged in real-time using IntelliJ or Eclipse


## Project Compilation

Automation Core Suite uses Maven as its build tool. To compile the framework execute the following command:
```
mvn clean install -U -Dmaven.test.skip=true
```

This command will compile the project and install its dependency in the local Maven repository.


## Getting Started

[Automation Center][1] project provides an example project using Automation Core Suite. It provides an overview of the main features of the framework including: test cases creation, session pool usage, screenshots, logs, reports, SauceLabs integrations, etc.

Review the [Automation Center][1] project to start working with Automation Core Suite.

## Automation Core Suite Configuration

By default automation-core-suite gets it's own configuration from the automation-core-suite.properties file, but any client application using automation-core-suite as a dependency can
implement it's own configuration provider following the next steps:

- Create a new Java class implementing com.automation.core.suite.IAutomationCoreConfig. Example (CustomAutomationCoreConfig)
- Override the custom implementation for the methods defined on IAutomationCoreConfig.
- Initialize the AutomationCoreConfigWrapper when the application starts. Example: AutomationCoreConfigWrapper.INSTANCE.init(new CustomAutomationCoreConfig());

[1]: https://github.com/Zomahd/Automation-Center "Automation Center"


