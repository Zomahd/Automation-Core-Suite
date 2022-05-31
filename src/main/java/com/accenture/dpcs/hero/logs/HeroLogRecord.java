package com.accenture.dpcs.hero.logs;

import com.accenture.dpcs.hero.models.Timestampable;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.event.Level;

import java.util.Date;

@SuppressWarnings("unused")
public class HeroLogRecord implements Timestampable {

    protected Level level;
    protected String message;
    protected long timestamp;
    protected Throwable throwable;

    public HeroLogRecord(Level level, String message, Throwable throwable) {
        this.level = level;
        this.message = message;
        this.throwable = throwable;
        timestamp = System.currentTimeMillis();
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        String text = String.format(HeroLogConfiguration.getFormat(), new Date(timestamp), level.toString(), message);

        if (throwable != null) {
            text += ExceptionUtils.getFullStackTrace(throwable) + "\n";
        }

        return text;
    }
}
