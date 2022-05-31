package com.automation.core.suite.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AutomationCorePropertiesConfig implements IAutomationCoreConfig{

    private static Properties properties = null;

    public void load() {
        if (properties == null) {
            properties = new Properties();

            try {
                InputStream is = AutomationCorePropertiesConfig.class.getResourceAsStream("/automation-core-suite.properties");
                if (is != null) {
                    properties.load(is);
                }
            } catch (IOException e) {
                System.err.println("Could not load automation-core-suite.properties.");
            }
        }
    }

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, String def) {
        if (properties == null) {
            load();
        }

        String value = properties.getProperty(key);
        return value != null ? value : def;
    }

    public  boolean getBoolean(String key) {
        return "true".equalsIgnoreCase(get(key));
    }

}
