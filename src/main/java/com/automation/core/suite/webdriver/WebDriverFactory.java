package com.automation.core.suite.webdriver;

import com.automation.core.suite.exceptions.UnknownBrowserException;
import com.automation.core.suite.models.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory {

    public static WebDriver get(Browser browser) throws UnknownBrowserException {
        DesiredCapabilities capabilities = WebDriverCapabilitiesBuilder.build(browser);
        WebDriver wd;

        if (SupportedWebDriverService.REMOTE.getId().equals(browser.getService())) {
            wd = WebDriverBuilder.remote(browser, capabilities);
        } else if (SupportedWebDriverService.SAUCELABS.getId().equals(browser.getService())) {
            wd = WebDriverBuilder.saucelabs(browser, capabilities);
        } else {
            wd = WebDriverBuilder.local(browser, capabilities);
        }

        return wd;
    }

}
