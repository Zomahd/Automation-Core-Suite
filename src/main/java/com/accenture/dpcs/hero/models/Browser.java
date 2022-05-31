package com.accenture.dpcs.hero.models;

import java.util.Map;

public interface Browser {

    String getService();
    
    String getPort();

    String getBrowser();

    boolean isMobile();

    String getRemoteUrl();

    String getAlias();

    Map<String, String> getCapabilities();

}
