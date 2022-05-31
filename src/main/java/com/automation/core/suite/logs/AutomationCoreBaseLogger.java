package com.automation.core.suite.logs;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.helpers.MarkerIgnoringBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AutomationCoreBaseLogger extends MarkerIgnoringBase implements Logger {

    protected List<AutomationCoreLogRecord> records;
    protected Level level;

    public AutomationCoreBaseLogger() {
        records = new ArrayList<AutomationCoreLogRecord>();
        level = Level.TRACE;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setLevel(String levels) {
        String[] definitions = levels.split(",");
        List<Level> definedLevels = new ArrayList<Level>();

        for (String definition : definitions) {
            Level definedLevel = Level.valueOf(definition.trim().toUpperCase());
            if (definedLevel != null) {
                definedLevels.add(definedLevel);
            }
        }

        if (definedLevels.size() > 0) {
            this.level = Collections.max(definedLevels);
        }
    }

    public boolean isTraceEnabled() {
        return level.compareTo(Level.TRACE) >= 0;
    }

    public void trace(String msg) {
        trace(msg, (Throwable) null);
    }

    public void trace(String msg, Object o) {
        trace(String.format(msg, o), (Throwable) null);
    }

    public void trace(String msg, Object o, Object o1) {
        trace(String.format(msg, o, o1), (Throwable) null);
    }

    public void trace(String msg, Object... objects) {
        trace(String.format(msg, objects), (Throwable) null);
    }

    public void trace(String msg, Throwable throwable) {
        if (isTraceEnabled()) {
            addRecord(new AutomationCoreLogRecord(Level.TRACE, msg, throwable));
        }
    }

    public boolean isDebugEnabled() {
        return level.compareTo(Level.DEBUG) >= 0;
    }

    public void debug(String msg) {
        debug(msg, (Throwable) null);
    }

    public void debug(String msg, Object o) {
        debug(String.format(msg, o), (Throwable) null);
    }

    public void debug(String msg, Object o, Object o1) {
        debug(String.format(msg, o, o1), (Throwable) null);
    }

    public void debug(String msg, Object... objects) {
        debug(String.format(msg, objects), (Throwable) null);
    }

    public void debug(String msg, Throwable throwable) {
        if (isDebugEnabled()) {
            addRecord(new AutomationCoreLogRecord(Level.DEBUG, msg, throwable));
        }
    }

    public boolean isInfoEnabled() {
        return level.compareTo(Level.INFO) >= 0;
    }

    public void info(String msg) {
        info(msg, (Throwable) null);
    }

    public void info(String msg, Object o) {
        info(String.format(msg, o), (Throwable) null);
    }

    public void info(String msg, Object o, Object o1) {
        info(String.format(msg, o, o1), (Throwable) null);
    }

    public void info(String msg, Object... objects) {
        info(String.format(msg, objects), (Throwable) null);
    }

    public void info(String msg, Throwable throwable) {
        if (isInfoEnabled()) {
            addRecord(new AutomationCoreLogRecord(Level.INFO, msg, throwable));
        }
    }

    public boolean isWarnEnabled() {
        return level.compareTo(Level.WARN) >= 0;
    }

    public void warn(String msg) {
        warn(msg, (Throwable) null);
    }

    public void warn(String msg, Object o) {
        warn(String.format(msg, o), (Throwable) null);
    }

    public void warn(String msg, Object... objects) {
        warn(String.format(msg, objects), (Throwable) null);
    }

    public void warn(String msg, Object o, Object o1) {
        warn(String.format(msg, o, o1), (Throwable) null);
    }

    public void warn(String msg, Throwable throwable) {
        if (isWarnEnabled()) {
            addRecord(new AutomationCoreLogRecord(Level.WARN, msg, throwable));
        }
    }

    public boolean isErrorEnabled() {
        return level.compareTo(Level.ERROR) >= 0;
    }

    public void error(String msg) {
        error(msg, (Throwable) null);
    }

    public void error(String msg, Object o) {
        error(String.format(msg, o), (Throwable) null);
    }

    public void error(String msg, Object o, Object o1) {
        error(String.format(msg, o, o1), (Throwable) null);
    }

    public void error(String msg, Object... objects) {
        error(String.format(msg, objects), (Throwable) null);
    }

    public void error(String msg, Throwable throwable) {
        if (isErrorEnabled()) {
            addRecord(new AutomationCoreLogRecord(Level.ERROR, msg, throwable));
        }
    }

    protected void addRecord(AutomationCoreLogRecord record) {
        records.add(record);
        onRecordAdded(record);
    }

    public List<AutomationCoreLogRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(records.size() * 80);
        for (AutomationCoreLogRecord record : records) {
            builder.append(record.toString());
        }
        return builder.toString();
    }

    protected abstract void onRecordAdded(AutomationCoreLogRecord record);
}
