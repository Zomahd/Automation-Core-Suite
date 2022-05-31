package com.automation.core.suite.models;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

@SuppressWarnings("unused")
public class Screenshot implements Timestampable {

    protected String name;
    protected File file;
    protected long timestamp;

    public Screenshot(String name, File file) {
        this(name, file, System.currentTimeMillis());
    }

    public Screenshot(String name, File file, long timestamp) {
        setName(name);
        this.file = file;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !"png".equalsIgnoreCase(FilenameUtils.getExtension(name))) {
            name = name + ".png";
        }

        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
