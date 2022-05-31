package com.automation.core.suite.configuration.xml;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class XMLProperties {

    protected Map<String, String> map = new HashMap<String, String>();

    @JsonAnyGetter
    public Map<String, String> get() {
        return map;
    }

    public String getProperty(String name) {
        return map.get(name);
    }

    @JsonAnySetter
    public void set(String name, String value) {
        map.put(name, value);
    }
}
