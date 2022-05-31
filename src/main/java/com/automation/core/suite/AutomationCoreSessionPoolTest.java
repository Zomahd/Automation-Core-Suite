package com.automation.core.suite;

import com.automation.core.suite.configuration.SelenideConfig;
import com.automation.core.suite.exceptions.UnknownBrowserException;
import com.automation.core.suite.models.Browser;
import com.automation.core.suite.webdriver.WebDriverPool;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;


public class AutomationCoreSessionPoolTest extends AutomationCoreTest {

    protected WebDriverPool webDriverPool = new WebDriverPool();

    public AutomationCoreSessionPoolTest(Browser browser) {
        super(browser);
    }

    @Override
    @AfterMethod(alwaysRun = true)
    protected void afterAutomationCoreTestMethod() {
        SelenideConfig.set();
        webdriverInitialized.set(false);
        webDriverPool.release(getWebDriver());
    }

    @AfterClass(alwaysRun = true)
    protected void afterAutomationCoreTest() {
        webDriverPool.closeAll();
    }

    @Override
    protected WebDriver initWebDriver(Browser browser) {
        WebDriver wd = null;

        if (webdriverInitialized.get()) {
            wd = getWebDriver();
        } else {
            try {
                wd = webDriverPool.get(browser);
                WebDriverRunner.setWebDriver(wd);
                webdriverInitialized.set(true);
            } catch (UnknownBrowserException ube) {
                System.err.println(ube.getMessage());
            }
        }

        setWebDriverSessionId(wd);

        return wd;
    }

}
