package com.accenture.dpcs.hero.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HeroPropertiesConfig implements IHeroConfig{

    private static Properties properties = null;

    public void load() {
        if (properties == null) {
            properties = new Properties();

            try {
                InputStream is = HeroPropertiesConfig.class.getResourceAsStream("/hero.properties");
                if (is != null) {
                    properties.load(is);
                }
            } catch (IOException e) {
                System.err.println("Could not load hero.properties.");
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
