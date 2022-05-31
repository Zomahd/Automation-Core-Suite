package com.accenture.dpcs.hero.configuration;

public interface IHeroConfig {

    void load();
    String get(String key);
    String get(String key, String def);
    boolean getBoolean(String key);
}
