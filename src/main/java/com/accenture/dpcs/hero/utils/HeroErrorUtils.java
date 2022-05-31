package com.accenture.dpcs.hero.utils;


import com.accenture.dpcs.hero.logs.HeroLogger;

public class HeroErrorUtils {

    private static HeroLogger logger = new HeroLogger("framework.log.level", false);

    public static void log(String msg) {
        log(msg, null);
    }

    public static void log(String msg, Throwable throwable) {
        logger.error("[HERO] " + msg, throwable);
    }
}
