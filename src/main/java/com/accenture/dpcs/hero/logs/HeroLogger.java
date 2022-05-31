package com.accenture.dpcs.hero.logs;

import java.io.PrintStream;

public class HeroLogger extends HeroBaseLogger {

    protected PrintStream stream;
    protected boolean serial;

    public HeroLogger() {
        this.setLevel(HeroLogConfiguration.getLevels());
        this.stream = HeroLogConfiguration.getConsole();
        this.serial = HeroLogConfiguration.getSerial();
    }

    public HeroLogger(String levelConfigKey, boolean serial) {
        this.setLevel(HeroLogConfiguration.getLevels(levelConfigKey));
        this.stream = HeroLogConfiguration.getConsole();
        this.serial = serial;
    }

    public void flush() {
        if (serial && stream != null) {
            for (HeroLogRecord record : records) {
                stream.print(record.toString());
            }
            stream.println();
        }
    }

    @Override
    protected void onRecordAdded(HeroLogRecord record) {
        if (!serial && stream != null) {
            stream.print(record.toString());
        }
    }
}
