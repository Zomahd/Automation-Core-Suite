package com.accenture.dpcs.hero.webdriver;

public enum SupportedWebDriverService {
    LOCAL("local", "Local"),
    REMOTE("remote", "Remote"),
    SAUCELABS("saucelabs", "SauceLabs");

    private String id;
    private String name;

    SupportedWebDriverService(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static SupportedWebDriverService getById(String id) {
        for(SupportedWebDriverService service : values()) {
            if (service.id.equals(id)) {
                return service;
            }
        }

        return null;
    }
}
