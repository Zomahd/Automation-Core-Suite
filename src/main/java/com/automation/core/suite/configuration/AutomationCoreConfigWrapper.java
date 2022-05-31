package com.automation.core.suite.configuration;

/**
 * AutomationCoreConfigWrapper is the main entry point to access the AutomationCore Suite configuration.
 * By default it uses the AutomationCorePropertiesConfig class to access the automation-core-suite.properties files and load configuration, but using it's
 * init method ant client can provide it's own IAutomationCoreConfig custom implementation in order to load the configuration.
 */
public enum AutomationCoreConfigWrapper implements IAutomationCoreConfig{

    INSTANCE;

    private IAutomationCoreConfig iAutomationCoreConfig;

    public void init(IAutomationCoreConfig iAutomationCoreConfig) {
        if (iAutomationCoreConfig == null) {
            loadDefaultConfig();
        } else {
            this.iAutomationCoreConfig = iAutomationCoreConfig;
        }
        this.load();
    }

    @Override
    public void load() {
        loadDefaultConfig();
        this.iAutomationCoreConfig.load();
    }

    @Override
    public String get(String key) {
        loadDefaultConfig();
        return this.iAutomationCoreConfig.get(key);
    }

    @Override
    public String get(String key, String def) {
        loadDefaultConfig();
        return this.iAutomationCoreConfig.get(key,def);
    }

    @Override
    public boolean getBoolean(String key) {
        loadDefaultConfig();
        return this.iAutomationCoreConfig.getBoolean(key);
    }

    private void loadDefaultConfig() {
        if (iAutomationCoreConfig == null) {
            this.iAutomationCoreConfig = new AutomationCorePropertiesConfig();
            this.load();
        }
    }

}
