package com.automation.core.suite.configuration;

public interface IAutomationCoreConfig {

    void load();
    String get(String key);
    String get(String key, String def);
    boolean getBoolean(String key);
}
