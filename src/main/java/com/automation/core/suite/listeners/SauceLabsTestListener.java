package com.automation.core.suite.listeners;


import com.automation.core.suite.annotations.SkipSauceLabsListener;
import com.automation.core.suite.configuration.AutomationCoreConfigWrapper;
import com.automation.core.suite.utils.AutomationCoreReportUtils;
import com.automation.core.suite.utils.AutomationCoreUtils;
import com.automation.core.suite.webdriver.SupportedWebDriverService;
import com.saucelabs.saucerest.SauceREST;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.*;

public class SauceLabsTestListener implements ITestListener {

    protected static String testBuildName = null;
    protected ThreadLocal<Boolean> enabled = new ThreadLocal<Boolean>();

    public void onTestStart(ITestResult result) {
        enabled.set(AutomationCoreConfigWrapper.INSTANCE.getBoolean("saucelabs.enable.listener") &&
                AutomationCoreUtils.hasBrowserService(result, SupportedWebDriverService.SAUCELABS) &&
                AutomationCoreUtils.getClassAnnotation(result.getInstance(), SkipSauceLabsListener.class) == null &&
                AutomationCoreUtils.getMethodAnnotation(AutomationCoreReportUtils.getMethod(result), SkipSauceLabsListener.class) == null);
    }

    protected List<String> getJobTags(ITestResult result) {
        List<String> tags = new ArrayList<String>();

        if (AutomationCoreConfigWrapper.INSTANCE.getBoolean("saucelabs.listener.tag.enabled")) {
            tags.add(AutomationCoreReportUtils.getExecutionTagName(result));
        }

        Collections.addAll(tags, AutomationCoreReportUtils.getFeatures(result));

        return tags;
    }

    protected String getBuildName() {
        String buildName = null;

        if (testBuildName != null) {
            buildName = testBuildName;
        } else if (AutomationCoreConfigWrapper.INSTANCE.getBoolean("saucelabs.listener.buildname.enabled")) {
            String buildNamePattern = AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.listener.buildname.format", null);

            if (buildNamePattern != null && buildNamePattern.length() > 0) {
                String dateFormat = AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.listener.buildname.date.format", "yyyy-MM-dd HH:mm:ss");

                buildName = buildNamePattern.replaceAll("\\$\\{date\\}",
                        new SimpleDateFormat(dateFormat).format(new Date()));

                testBuildName = buildName;
            }
        }

        return buildName;
    }

    protected void updateJobStatus(ITestResult result, boolean passed) {
        SauceREST sauceREST = new SauceREST(AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.username", ""),
                AutomationCoreConfigWrapper.INSTANCE.get("saucelabs.accessKey", ""));
        String buildName = getBuildName();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("passed", passed);
        options.put("tags", getJobTags(result));
        options.put("name", AutomationCoreReportUtils.getTestCaseFullNameNoBrowser(result));

        if (buildName != null && buildName.length() > 0) {
            options.put("build", buildName);
        }

        sauceREST.updateJobInfo(AutomationCoreReportUtils.getWebDriverSessionId(result), options);
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

    public void onTestSkipped(ITestResult result) {
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
    }
}
