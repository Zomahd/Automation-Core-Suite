package com.accenture.dpcs.hero;

import com.accenture.dpcs.hero.configuration.SelenideConfig;
import com.accenture.dpcs.hero.exceptions.UnknownBrowserException;
import com.accenture.dpcs.hero.models.Browser;
import com.accenture.dpcs.hero.webdriver.WebDriverPool;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;


public class HeroSessionPoolTest extends HeroTest {

    protected WebDriverPool webDriverPool = new WebDriverPool();

    public HeroSessionPoolTest(Browser browser) {
        super(browser);
    }

    @Override
    @AfterMethod(alwaysRun = true)
    protected void afterHeroTestMethod() {
        SelenideConfig.set();
        webdriverInitialized.set(false);
        webDriverPool.release(getWebDriver());
    }

    @AfterClass(alwaysRun = true)
    protected void afterHeroTest() {
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
