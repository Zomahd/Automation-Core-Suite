package com.accenture.dpcs.hero.webdriver;

import com.accenture.dpcs.hero.configuration.HeroConfigWrapper;
import com.accenture.dpcs.hero.exceptions.UnknownBrowserException;
import com.accenture.dpcs.hero.models.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.List;

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
