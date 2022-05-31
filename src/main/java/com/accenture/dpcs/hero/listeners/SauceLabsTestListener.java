package com.accenture.dpcs.hero.listeners;


import com.accenture.dpcs.hero.annotations.SkipSauceLabsListener;
import com.accenture.dpcs.hero.configuration.HeroConfigWrapper;
import com.accenture.dpcs.hero.configuration.HeroPropertiesConfig;
import com.accenture.dpcs.hero.utils.HeroReportUtils;
import com.accenture.dpcs.hero.utils.HeroUtils;
import com.accenture.dpcs.hero.webdriver.SupportedWebDriverService;
import com.saucelabs.saucerest.SauceREST;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.*;

public class SauceLabsTestListener implements ITestListener {

    protected ThreadLocal<Boolean> enabled = new ThreadLocal<Boolean>();
    protected static String testBuildName = null;

    public void onTestStart(ITestResult result) {
        enabled.set(HeroConfigWrapper.INSTANCE.getBoolean("saucelabs.enable.listener") &&
                HeroUtils.hasBrowserService(result, SupportedWebDriverService.SAUCELABS) &&
                HeroUtils.getClassAnnotation(result.getInstance(), SkipSauceLabsListener.class) == null &&
                HeroUtils.getMethodAnnotation(HeroReportUtils.getMethod(result), SkipSauceLabsListener.class) == null);
    }

    protected List<String> getJobTags(ITestResult result) {
        List<String> tags = new ArrayList<String>();

        if (HeroConfigWrapper.INSTANCE.getBoolean("saucelabs.listener.tag.enabled")) {
            tags.add(HeroReportUtils.getExecutionTagName(result));
        }

        Collections.addAll(tags, HeroReportUtils.getFeatures(result));

        return tags;
    }

    protected String getBuildName() {
        String buildName = null;

        if (testBuildName != null) {
            buildName = testBuildName;
        } else if (HeroConfigWrapper.INSTANCE.getBoolean("saucelabs.listener.buildname.enabled")) {
            String buildNamePattern = HeroConfigWrapper.INSTANCE.get("saucelabs.listener.buildname.format", null);

            if (buildNamePattern != null && buildNamePattern.length() > 0) {
                String dateFormat = HeroConfigWrapper.INSTANCE.get("saucelabs.listener.buildname.date.format", "yyyy-MM-dd HH:mm:ss");

                buildName = buildNamePattern.replaceAll("\\$\\{date\\}",
                        new SimpleDateFormat(dateFormat).format(new Date()));

                testBuildName = buildName;
            }
        }

        return buildName;
    }

    protected void updateJobStatus(ITestResult result, boolean passed) {
        SauceREST sauceREST = new SauceREST(HeroConfigWrapper.INSTANCE.get("saucelabs.username", ""),
                HeroConfigWrapper.INSTANCE.get("saucelabs.accessKey", ""));
        String buildName = getBuildName();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("passed", passed);
        options.put("tags", getJobTags(result));
        options.put("name", HeroReportUtils.getTestCaseFullNameNoBrowser(result));

        if (buildName != null && buildName.length() > 0) {
            options.put("build", buildName);
        }

        sauceREST.updateJobInfo(HeroReportUtils.getWebDriverSessionId(result), options);
    }

    public void onTestSuccess(ITestResult result) {
        if (enabled.get()) {
            updateJobStatus(result, true);
        }
    }

    public void onTestFailure(ITestResult result) {
        if (enabled.get()) {
            updateJobStatus(result, false);
        }
    }

    public void onTestSkipped(ITestResult result) {}

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    public void onStart(ITestContext context) {}

    public void onFinish(ITestContext context) {}
}
