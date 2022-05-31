package com.automation.core.suite.webdriver;

import com.automation.core.suite.exceptions.UnknownBrowserException;
import com.automation.core.suite.models.Browser;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebDriverPool {

    private HashMap<Browser, List<WebDriver>> pool;
    private HashMap<WebDriver, Browser> drivers;

    public WebDriverPool() {
        pool = new HashMap<Browser, List<WebDriver>>();
        drivers = new HashMap<WebDriver, Browser>();
    }

    public WebDriver get(Browser browser) throws UnknownBrowserException {
        synchronized(this) {
            List<WebDriver> available = pool.get(browser);

            if (available != null && available.size() > 0) {
                WebDriver wd = available.remove(0);

                if (isValid(wd)) {
                    return wd;
                } else {
                    close(wd);
                    return get(browser);
                }
            } else {
                WebDriver wd = WebDriverFactory.get(browser);
                drivers.put(wd, browser);
                return wd;
            }
        }
    }

    public void release(WebDriver wd) {
        synchronized(this) {
            Browser browser = drivers.get(wd);

            if (browser != null) {
                List<WebDriver> available = pool.get(browser);

                if (available == null) {
                    available = new ArrayList<WebDriver>();
                    pool.put(browser, available);
                }

                available.add(wd);
            }
        }
    }

    public void closeAll() {
        for (WebDriver wd: drivers.keySet()) {
            close(wd);
        }
    }

    private void close(WebDriver driver) {
        try {
            driver.quit();
        } catch (Exception ignore) {}
    }

    private boolean isValid(WebDriver driver) {
        try {
            driver.getWindowHandle();
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

}
