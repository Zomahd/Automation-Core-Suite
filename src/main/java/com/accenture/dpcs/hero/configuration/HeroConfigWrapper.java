package com.accenture.dpcs.hero.configuration;

/**
 * HeroConfigWrapper is the main entry point to access the Hero Suite configuration.
 * By default it uses the HeroPropertiesConfig class to access the hero.properties files and load configuration, but using it's
 * init method ant client can provide it's own IHeroConfig custom implementation in order to load the configuration.
 */
public enum HeroConfigWrapper implements IHeroConfig{

    INSTANCE;

    private IHeroConfig iHeroConfig;

    public void init(IHeroConfig iHeroConfig) {
        if (iHeroConfig == null) {
            loadDefaultConfig();
        } else {
            this.iHeroConfig = iHeroConfig;
        }
        this.load();
    }

    @Override
    public void load() {
        loadDefaultConfig();
        this.iHeroConfig.load();
    }

    @Override
    public String get(String key) {
        loadDefaultConfig();
        return this.iHeroConfig.get(key);
    }

    @Override
    public String get(String key, String def) {
        loadDefaultConfig();
        return this.iHeroConfig.get(key,def);
    }

    @Override
    public boolean getBoolean(String key) {
        loadDefaultConfig();
        return this.iHeroConfig.getBoolean(key);
    }

    private void loadDefaultConfig() {
        if (iHeroConfig == null) {
            this.iHeroConfig = new HeroPropertiesConfig();
            this.load();
        }
    }

}
