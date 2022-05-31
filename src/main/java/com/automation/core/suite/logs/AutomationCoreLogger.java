package com.automation.core.suite.logs;

import java.io.PrintStream;

public class AutomationCoreLogger extends AutomationCoreBaseLogger {

    protected PrintStream stream;
    protected boolean serial;

    public AutomationCoreLogger() {
        this.setLevel(AutomationCoreLogConfiguration.getLevels());
        this.stream = AutomationCoreLogConfiguration.getConsole();
        this.serial = AutomationCoreLogConfiguration.getSerial();
    }

    public AutomationCoreLogger(String levelConfigKey, boolean serial) {
        this.setLevel(AutomationCoreLogConfiguration.getLevels(levelConfigKey));
        this.stream = AutomationCoreLogConfiguration.getConsole();
        this.serial = serial;
    }

    public void flush() {
        if (serial && stream != null) {
            for (AutomationCoreLogRecord record : records) {
                stream.print(record.toString());
            }
            stream.println();
        }
    }

    @Override
    protected void onRecordAdded(AutomationCoreLogRecord record) {
        if (!serial && stream != null) {
            stream.print(record.toString());
        }
    }
}
