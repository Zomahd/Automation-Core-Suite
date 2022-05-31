package com.accenture.dpcs.hero.configuration;

import com.codeborne.selenide.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SelenideConfig {

    public static void set() {
        HeroConfigWrapper.INSTANCE.load();

        Level logLevel = Level.parse(HeroConfigWrapper.INSTANCE.get("selenide.log.level"));

        if (logLevel != null) {
            Logger.getLogger("com.codeborne.selenide").setLevel(logLevel);
        }

        String screenshotsDirectory = HeroConfigWrapper.INSTANCE.get("selenide.reportDirectory");

        if (screenshotsDirectory != null) {
            Configuration.reportsFolder = screenshotsDirectory;
        }

        Configuration.holdBrowserOpen = true;
    }

    public static void setReportDirectory(String reportDirectory) {

        if(reportDirectory != null && !reportDirectory.isEmpty()) {
            Configuration.reportsFolder = reportDirectory;
        }

    }

}
