package com.automation.core.suite.logs;

import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;

import java.io.PrintStream;

public class AutomationCoreLogConfiguration {

    private static PrintStream console = null;

    public static String getLevels() {
        return getLevels("log.level");
    }

    public static String getLevels(String key) {
        return AutomationCoreConfigWrapper.INSTANCE.get(key, "DEBUG");
    }

    public static String getFormat() {
        return AutomationCoreConfigWrapper.INSTANCE.get("log.format", "%1$tF %1$tH:%1$tM:%1$tS,%1$tL [%2$s] %3$s\n");
    }

    public static boolean getSerial() {
        return "true".equalsIgnoreCase(AutomationCoreConfigWrapper.INSTANCE.get("log.serial", "false"));
    }

    public static PrintStream getConsole() {
        if (console == null) {
            String config = AutomationCoreConfigWrapper.INSTANCE.get("log.console", "out");

            if (config == null || "out".equalsIgnoreCase(config)) {
                console = System.out;
            } else if ("err".equalsIgnoreCase(config)) {
                console = System.err;
            }
        }

        return console;
    }
}
