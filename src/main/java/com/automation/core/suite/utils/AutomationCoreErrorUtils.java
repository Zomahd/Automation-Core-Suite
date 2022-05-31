package com.automation.core.suite.utils;


import com.automation.core.suite.logs.AutomationCoreLogger;

public class AutomationCoreErrorUtils {

    private static AutomationCoreLogger logger = new AutomationCoreLogger("framework.log.level", false);

    public static void log(String msg) {
        log(msg, null);
    }

    public static void log(String msg, Throwable throwable) {
        logger.error("[AUTOMATION CORE SUITE] " + msg, throwable);
    }
}
