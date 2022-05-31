package com.automation.core.suite.configuration;

import com.automation.core.suite.configuration.browsers.BrowserSettings;
import com.automation.core.suite.models.Browser;
import com.fasterxml.jackson.xml.XmlMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.map.DeserializationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrowserFactory {

    protected static boolean loaded = false;
    protected static List<Browser> browsers = new ArrayList<Browser>();

    public static void load(String configFile) {
        if (!loaded) {
            loaded = true;
            browsers = new ArrayList<Browser>();

            try {
                XmlMapper mapper = new XmlMapper();
                mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                BrowserSettings settings =
                        mapper.readValue(BrowserFactory.class.getResourceAsStream("/browsers/" + configFile + ".xml"),
                                BrowserSettings.class);

                if (settings != null && settings.getBrowsers() != null) {
                    Collections.addAll(browsers, settings.getBrowsers());
                }
            } catch (IOException ioe) {
                System.err.println("Could not read " + configFile + ".xml file");
                System.err.println(ExceptionUtils.getFullStackTrace(ioe));
            }
        }
    }

    public static List<Browser> getBrowsers() {
        return Collections.unmodifiableList(browsers);
    }

    public static List<Browser> getDesktopBrowsers() {
        List<Browser> filtered = new ArrayList<Browser>();

        for (Browser browser : getBrowsers()) {
            if (!browser.isMobile()) {
                filtered.add(browser);
            }
        }

        return Collections.unmodifiableList(filtered);
    }

    public static List<Browser> getMobileBrowsers() {
        List<Browser> filtered = new ArrayList<Browser>();

        for (Browser browser : getBrowsers()) {
            if (browser.isMobile()) {
                filtered.add(browser);
            }
        }

        return Collections.unmodifiableList(filtered);
    }
}
