package com.automation.core.suite.configuration.browsers;

import com.automation.core.suite.configuration.xml.XMLProperties;
import com.automation.core.suite.models.Browser;
import com.automation.core.suite.webdriver.SupportedWebDriverService;
import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;

import java.util.Map;

@SuppressWarnings("unused")
public class BrowserConfiguration extends XMLProperties implements Browser {

    @JacksonXmlProperty(isAttribute = true)
    protected String service;

    @JacksonXmlProperty(isAttribute = true)
    protected String port;

    @JacksonXmlProperty(isAttribute = true)
    protected String browser;

    @JacksonXmlProperty(isAttribute = true, localName = "is-mobile")
    protected boolean mobile;

    @JacksonXmlProperty(isAttribute = true, localName = "remote-url")
    protected String remoteUrl;

    @JacksonXmlProperty(isAttribute = true)
    protected String alias;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, String> getCapabilities() {
        return get();
    }

    @Override
    public String toString() {
        String alias = getAlias();
        SupportedWebDriverService serviceType = SupportedWebDriverService.getById(getService());

        if (alias != null && alias.length() > 0) {
            return alias;
        }


        return (serviceType != null ? serviceType.getName() : "Unknown") + ": " + getBrowser() +
                " (" + mapToString(getCapabilities()) + ')';
    }

    protected String mapToString(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();

        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(entry.getKey());
                builder.append(": ");
                builder.append(entry.getValue());
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrowserConfiguration that = (BrowserConfiguration) o;

        if (mobile != that.mobile) return false;
        if (service != null ? !service.equals(that.service) : that.service != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (browser != null ? !browser.equals(that.browser) : that.browser != null) return false;
        if (remoteUrl != null ? !remoteUrl.equals(that.remoteUrl) : that.remoteUrl != null) return false;
        return alias != null ? alias.equals(that.alias) : that.alias == null;
    }

    @Override
    public int hashCode() {
        int result = service != null ? service.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (browser != null ? browser.hashCode() : 0);
        result = 31 * result + (mobile ? 1 : 0);
        result = 31 * result + (remoteUrl != null ? remoteUrl.hashCode() : 0);
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        return result;
    }
}
