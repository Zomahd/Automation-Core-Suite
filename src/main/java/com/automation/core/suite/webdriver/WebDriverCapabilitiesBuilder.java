package com.automation.core.suite.webdriver;

import com.automation.core.suite.models.Browser;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class WebDriverCapabilitiesBuilder {

    public static DesiredCapabilities build(Browser browser) {
        String browserName = browser.getBrowser();
        DesiredCapabilities capabilities;

        if ("firefox".equals(browserName)) {
            capabilities = DesiredCapabilities.firefox();
        } else if ("chrome".equals(browserName)) {
            capabilities = DesiredCapabilities.chrome();
        } else if ("safari".equals(browserName)) {
            capabilities = DesiredCapabilities.safari();
        } else if ("edge".equals(browserName)) {
            capabilities = DesiredCapabilities.edge();
        } else if ("internetExplorer".equals(browserName)) {
            capabilities = DesiredCapabilities.internetExplorer();
        } else if ("operaBlink".equals(browserName)) {
            capabilities = DesiredCapabilities.operaBlink();
        } else if ("htmlUnit".equals(browserName)) {
            capabilities = DesiredCapabilities.htmlUnit();
        } else if ("phantomjs".equals(browserName)) {
            capabilities = DesiredCapabilities.phantomjs();
        } else if ("android".equals(browserName)) {
            capabilities = new DesiredCapabilities();
        } else if ("iphone".equals(browserName)) {
            capabilities = new DesiredCapabilities();
        } else if ("ipad".equals(browserName)) {
            capabilities = new DesiredCapabilities();
        } else {
            capabilities = new DesiredCapabilities();
        }

        if (browser.getCapabilities() != null) {
            for (Map.Entry<String, String> entry : browser.getCapabilities().entrySet()) {
                capabilities.setCapability(entry.getKey(), entry.getValue());
            }
        }

        return capabilities;
    }
}
