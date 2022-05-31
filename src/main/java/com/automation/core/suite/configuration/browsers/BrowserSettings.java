package com.automation.core.suite.configuration.browsers;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

@SuppressWarnings("unused")
@JacksonXmlRootElement(localName = "settings")
public class BrowserSettings {

    protected BrowserConfiguration[] browsers;

    public BrowserConfiguration[] getBrowsers() {
        return browsers;
    }

    public void setBrowsers(BrowserConfiguration[] browsers) {
        this.browsers = browsers;
    }
}
